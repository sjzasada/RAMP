/*
 * Created on 16:15:43 11 May 2011
 * Project: RAMP 
 * File: UserAgent.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.user;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MakeOffer;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MakeRequest;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MarketOntology;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl.DefaultMakeRequest;



import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserAgent extends Agent {



	//Hash containing a hash of all of the offers made for a particular RequestID
	private HashMap<String, Offers> offers = new HashMap<String, Offers>();

	private Vector resourceAgents = new Vector();
	UserGui myGui=null;

	private Codec codec = new SLCodec(); 
	private Ontology onto = MarketOntology.getInstance();

	protected void setup () {

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);

		//register the content language 


		// get the args to the prog - should be 1
		Object args[] = getArguments();


		// if the argument is a directory, open a GUI, else open the file;
		if (args.length == 1) {

			String argument=(String)args[0];

			File f = new File(argument);
			if (f.isDirectory()) {

				myGui=new UserGui(this);
				myGui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				myGui.setVisible(true);
				myGui.loadDir(argument);

			} else {

				Request myRequest = new Request();	
				displayMessage("Looking to buy " + myRequest.getCPUCount() + " cores for less than " + myRequest.getCPUCost() + " before " + myRequest.getEnd());

				requestQuote(myRequest);
			}
		} else {
			myGui=new UserGui(this);
			myGui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			myGui.setVisible(true);
		}

		// print out agent details
		displayMessage("My name is " + getAID().getLocalName());
		displayMessage("My GUID is " + getAID().getName());
		displayMessage("My addresses are:");
		Iterator<String> it = getAID().getAllAddresses();
		while (it.hasNext()) {
			displayMessage("- " +it.next());
		}

		//check for resource agents
		addBehaviour(new TickerBehaviour(this, 60000) {
			protected void onTick () {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("resource-trader");
				template.addServices(sd);

				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					resourceAgents.clear();
					for (int i=0; i<result.length; ++i) {
						resourceAgents.addElement(result[i].getName());
					}
				} catch (FIPAException fp) {
					fp.printStackTrace();
				}
			}
		});




	}


	//display a message from the agent
	public void displayMessage (final String s) {
		if (myGui != null) {
			Runnable addIt = new Runnable () {
				public void run() {
					myGui.writeMessage(s);
				}
			};
			SwingUtilities.invokeLater(addIt);
		} else {
			System.out.println(s);
		}
	}


	//this is what happens when the agent finishes
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
		//	this.doDelete();
	}

	//method to begin a negotiation
	public void requestQuote (Request r) {

		Random generator = new Random();
		int id = generator.nextInt();
		String requestID = getName()+id;
		r.setRequestID(requestID);

		Vector<Request> v = new Vector<Request>();
		v.add(r);
		//add the single request to the vector
		addBehaviour(new RequestAQuote(requestID, v));
	}

	public void requestQuote (Vector<Request> v) {

	}

	////////////////////////////////////////
	//
	// behaviours
	//
	////////////////////////////////////////


	//behaviour to hold all data associated with this request
	private class RequestAQuote extends Behaviour {

		boolean first=false;
		private Vector<Request> currentRequest;
		private String requestID;


		private SequentialBehaviour twoStep;

		private RequestAQuote (String requestID, Vector<Request> request) {
			this.requestID=requestID;
			currentRequest = request;		
			twoStep = new SequentialBehaviour (myAgent);

			offers.put(requestID, new Offers());

		}


		public boolean done() {
			return twoStep.done();
		}


		@Override
		public void action() {
			// TODO Auto-generated method stub
			if (!first) {
				displayMessage("Starting parallel behaviour");

				ParallelBehaviour pb = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ANY);

				//add a parallel behaviour to run the bids
				pb.addSubBehaviour(new RequestManager(myAgent, currentRequest, requestID));

				//add a second parallel behaviour to process offers
				pb.addSubBehaviour(new ResourceNegotiator(requestID));


				twoStep.addSubBehaviour(pb);

				//when pb has finished, run the notifier
				twoStep.addSubBehaviour(new ResourceNotifier(requestID));
				addBehaviour(twoStep);
				first=true;
			} else {

				//wait until the requester is done, then once it is, notify winners

				block();
			}
		}

	}




	//keep requesting until deadline
	private class RequestManager extends TickerBehaviour {

		private long deadline, initTime, deltaT;

		private Vector<Request> requests;
		private int round = 0;
		private String requestID;

		private RequestManager (Agent a, Vector<Request> request, String requestID) {
			super (a, 30000);
			deadline = System.currentTimeMillis() + 120000;
			initTime = System.currentTimeMillis();
			deltaT=deadline - initTime;
			this.requests=requests;
			this.requestID=requestID;
		}

		public void onTick () {
			long currentTime = System.currentTimeMillis();



			//check if we should finish bidding
			if (currentTime > deadline) {
				displayMessage("Deadline passed, stopping requesting");
				stop();
			} else {

				Offers theOffers = offers.get(requestID);

				displayMessage("Starting new resource request. Current time " + currentTime + " deadline " + deadline);
				displayMessage("Bidding round " + ++round);


				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);				
				Iterator<AID> it = resourceAgents.iterator();
				while (it.hasNext()) {
					cfp.addReceiver(it.next());
				}

				//displayMessage("Polling sellers");

				//TODO: make the message more comprehensive


				cfp.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
				cfp.setOntology(MarketOntology.ONTOLOGY_NAME);

				//unique conversation ID for this auction
				String conversation = "compute-auction-"+requestID;

				cfp.setConversationId(conversation);
				cfp.setReplyWith("cfp"+requestID);
				//TODO: Should set reply by too

				MakeRequest req = new DefaultMakeRequest();

				Iterator<Request> reqit = requests.iterator();

				//add each subrequest
				while (reqit.hasNext()) {

					Request r = reqit.next();


					//TODO: need to check cost is better than request

					Offer o = theOffers.getBestOffer(requestID);
					//if an offer has already been made, lower the price
					if (o != null) {

						int bestCost = Integer.parseInt(o.getOFFERCOST().getCPUHOURCOST());

						if (bestCost < r.getCPUCost()) {
							r.setCPUCost(bestCost);
						}
					}

					req.addRFQINSTANCE(r.getRFQObject());
				}


				//add onto object to message 
				try {
					myAgent.getContentManager().fillContent(cfp, req);
				} catch (Exception e) {
					e.printStackTrace();
				}


				//send the message
				myAgent.send(cfp);



			}

		}

	}



	//process received offers
	private class ResourceNegotiator extends Behaviour {

		private String requestID;
		private int step = 0;
		private int repliesCount = 0;


		private MessageTemplate mt;

		ResourceNegotiator (String requestID) {
			this.requestID=requestID;
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("compute-auction-"+requestID),
					MessageTemplate.MatchInReplyTo("cfp"+requestID));

		}

		public void action () {

			Offers theOffers = offers.get(requestID);

			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					//this is an offer message

					AID responder = reply.getSender();


					try {
						ContentManager cm = myAgent.getContentManager();
						Action act = (Action) cm.extractContent(reply);
						MakeOffer off = (MakeOffer)act.getAction();

						//throws exception if we don't understand message

						Iterator<Offer> itr = off.getAllOFFERINSTANCE();

						// process the offers received 
						while (itr.hasNext()) {
							Offer myOffer=itr.next();

							String currentRequest = myOffer.getREQUESTID();

							Offer best = theOffers.getBestOffer(currentRequest);

							displayMessage("Recieved offer " + myOffer.getOFFERID() + " from " + responder);

							//check if this is the best offer or not
							if (best == null) {
								theOffers.setBestOffer(currentRequest, responder, myOffer);
							} else if (RequestEvaluator.naiveEvaluator(best, myOffer)) {
								theOffers.setBestOffer(currentRequest, responder, myOffer);								
							}
							theOffers.addOffer(responder, currentRequest, myOffer);
						}


					} catch (OntologyException oe) {
						//reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					} catch (CodecException ce) {
						//reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					}



					//String conv= reply.getConversationId();
					//System.err.println("Conversation " + conv);

					//store the new offers object
					offers.put(requestID, theOffers);

				}
				repliesCount++;

				//System.out.println("Rep no " + repliesCount +"/"+resourceAgents.size());

				//if (repliesCount == resourceAgents.size()) {
				//	step = 2;
				//}
				//	System.out.println("step = " + step);

			} else {
				block();
			}


		}

		public boolean done () {

			return step == 2;
		}
	}

	private class ResourceNotifier extends Behaviour {

		private String requestID; 
		private int step=1;
		private MessageTemplate mt;
		private Offers theOffers;
		private ResourceNotifier (String requestID) {
			this.requestID = requestID;
			theOffers = offers.get(requestID);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub

			displayMessage("Negotiating sale");
			switch (step) {

			case 1:

				//single unit case
				//TODO: extend for multiunit

				Offer bestOffer = theOffers.getBestOffer(requestID);
				AID bestAgent = theOffers.getBestAgent(requestID);

				//TODO: make sure best offer meets request

				if (bestOffer != null) {
					//TODO: implement the offer evaluation
					displayMessage("Best offer (" + bestOffer.getOFFERID() + ")from " +bestAgent);

					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestAgent);
					order.setContent(bestOffer.getOFFERID());
					order.setReplyWith("order"+requestID);
					order.setConversationId("accept"+requestID);
					myAgent.send(order);

					System.err.println(bestOffer.getOFFERCOST().getCPUHOURCOST());
					System.err.println(order.getReplyWith());

					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("accept"+requestID), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					step=2;
				} else {
					displayMessage("No bids received");
					step=3;
				}
				break;

			case 2:

				ACLMessage reply = myAgent.receive(mt);



				if(reply != null) {
					System.err.println("case 2 reached");

					if (reply.getPerformative() == ACLMessage.INFORM) {
						//sucessfully made the bargin
						displayMessage("Done!");
					} else {
						displayMessage("Not successful");
					}

					step=3;
				} else {
					block();
					System.err.println("blocking");
				}
				break;

			case 4:
				
				
			case 5:
				

			}

		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return step==3;
		}



	}
	
	
	//listen for offer withdraw requests
	private class OfferWithdrawServer extends CyclicBehaviour {

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.DISCONFIRM);

		
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String offerID = msg.getContent();
				//TODO: pull out offer and remove
				//need to update Offers object to rank offers sorted
			}
			
		}
		
	}
	
	
	//list for renegotiate messages
	private class RenegotiateBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	

}

