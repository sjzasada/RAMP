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

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MakeOffer;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MakeRequest;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MarketOntology;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;


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
	private HashMap<String, OfferList<ReceivedOffer>> allOffers = new HashMap<String, OfferList<ReceivedOffer>>();
	private HashMap<String, Request> allRequests = new HashMap<String, Request>();
	private HashMap<String, Vector<String>> subRequests = new HashMap<String, Vector<String>>();

	private Vector resourceAgents = new Vector(); //this stores all of the resource agents
	UserGui myGui=null; //create a new gui

	//sort out the communications ontologu
	private Codec codec = new SLCodec(); 
	private Ontology onto = MarketOntology.getInstance();


	//set up agent
	protected void setup () {

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);

		System.err.println("Main ontology in use is " + getContentManager().getOntologyNames()[0]);
		//register the content language 

		//check for resource agents periodically
		addBehaviour(new TickerBehaviour(this, 60000) {
			public void onStart () {
				//onTick();
			}
			
			protected void onTick () {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("resource-trader"); //define a template of the kind of agents we are looking for
				template.addServices(sd);

				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); //search for resource agents
					resourceAgents.clear();
					for (int i=0; i<result.length; ++i) {
						resourceAgents.addElement(result[i].getName()); //add the agents found to the vector 
						System.err.println("Found resource agent " + result[i].getName());
					}
				} catch (FIPAException fp) {
					fp.printStackTrace();
				}
			}
		});

		// get the args to the prog - should be 1
		Object args[] = getArguments();


		// if the argument is a directory, open a GUI, else open the file;
		if (args.length == 1) {

			String argument=(String)args[0];

			File f = new File(argument);
			if (f.isDirectory()) {
				//open a GUI and load the directory of requests
				myGui=new UserGui(this);
				myGui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				myGui.setVisible(true);
				myGui.loadDir(argument);

			} else {
				//operate cmd line, with just a request file
				Request myRequest = new Request();	
				myRequest.load(argument); //load the request file
				displayMessage("Looking to buy " + myRequest.getCPUCount() + " cores for less than " + myRequest.getCPUCost() + " before " + myRequest.getEnd());

				requestQuote(myRequest);
			}
		} else {
			//open a blank gui
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






	}


	//display a message from the agent, if the GUI is open, do it in the GUI, otherwise, do it on cmd line
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

	//end agent setup

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Negotiations

	//method to begin a negotiation
	public void requestQuote (Request r) {

		//add the single request to a vector
		Vector<Request> v = new Vector<Request>();
		v.add(r);

		//call the main requestQuote method
		requestQuote(v);

	}

	//request multiple resources
	public void requestQuote (Vector<Request> v) {
		//set a request ID
		Random generator = new Random();
		int id = generator.nextInt();
		String requestID = getName()+id;

		//iterate the vector, and update each request with the sub request ID
		int subid=0;

		Vector<String> subReqs = new Vector<String>();

		for (Request r : v) {
			r.setRequestID(requestID+"-"+subid);
			allRequests.put(requestID+"-"+subid, r);
			subReqs.add(requestID+"-"+subid);
			subid++;
		}

		//add the current request(s) to the hashmap of request

		subRequests.put(requestID, subReqs);

		//start the behaviour with this set of requests
		addBehaviour(new RequestAQuote(requestID));

	}

	////////////////////////////////////////
	//
	// behaviours
	//
	////////////////////////////////////////


	//behaviour to hold all data associated with this request
	private class RequestAQuote extends Behaviour {

		boolean first=true; //only do this once
		private String requestID;
		private SequentialBehaviour twoStep;

		private RequestAQuote (String requestID) {
			this.requestID=requestID;//permanent reference to this set of requests
			twoStep = new SequentialBehaviour (myAgent);
			System.err.println ("Request ID " + requestID);

		}


		public boolean done() {
			return twoStep.done();
		}


		@Override
		public void action() {
			// TODO Auto-generated method stub
			if (first) {
				displayMessage("Starting parallel behaviour");

				ParallelBehaviour pb = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ANY);

				//add a parallel behaviour to run the bids
				pb.addSubBehaviour(new RequestManager(myAgent, requestID));

				//add a second parallel behaviour to process offers
				pb.addSubBehaviour(new ProcessOffers(requestID));
				System.err.println ("Request ID " + requestID);


				twoStep.addSubBehaviour(pb);

				//when pb has finished, run the notifier
				twoStep.addSubBehaviour(new ResourceNotifier(requestID));
				addBehaviour(twoStep);
				first=false;
			} else {

				//wait until the requester is done, then once it is, notify winners

				block();
			}
		}

	}




	//keep requesting until deadline
	private class RequestManager extends TickerBehaviour {

		private long deadline, initTime, deltaT;

		private Vector<Request> requests = new Vector<Request>();
		private int round = 0;
		private String requestID;

		private RequestManager (Agent a, String requestID) {
			super (a, 30000);//30 second tick - could be changable parameter
			deadline = System.currentTimeMillis() + 120000;//go for 2 mins - could be a changable parameter
			initTime = System.currentTimeMillis();
			deltaT=deadline - initTime;
			this.requestID=requestID;
			Vector<String> v = subRequests.get(requestID);
			System.err.println ("Request ID " + requestID);

			for (String s : v) {
				this.requests.add(allRequests.get(s));//vector containing all subrequests. 
				System.err.println ("Adding subrequest " + s);
			}

			//test code
			if (requests==null) {
				System.err.println("Requests is null");
			}

			this.requestID=requestID;
		}

		public void onStart() {
			//do nothing
		}
		
		public void onTick () {
			long currentTime = System.currentTimeMillis();

			//check if we should finish bidding
			if (currentTime > deadline) {
				displayMessage("Deadline passed, stopping requesting");
				stop();
			} else {
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

				MakeRequest req = new MakeRequest();


				//TODO: Check for existing offers. If we have a better one, make that the new bidding target. 
				//TODO: !!!!!!!!!!!!!!
				//TODO: !!!!!!!!!!!!!!

				//add each subrequest
				int i = 0;
				for (Request r : requests) {
					System.err.println("Request ID is: " + r.getRequestID() + " --- ffooo ");
					req.addRFQINSTANCE(r.getRFQObject());
					i++;
				}

				//Iterator<Request> reqit = requests.iterator();


				//add onto object to message 
				try {
					
					System.err.println("Using ontology " + myAgent.getContentManager().getOntologyNames()[0]);
					System.err.println("CFP with ontology " + cfp.getOntology());
					

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
	private class ProcessOffers extends Behaviour {

		private String requestID;
		private int step = 0;
		private int repliesCount = 0;


		private MessageTemplate mt;

		ProcessOffers (String requestID) {
			this.requestID=requestID;
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("compute-auction-"+requestID),
					MessageTemplate.MatchInReplyTo("cfp"+requestID));

		}

		public void action () {


			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					//this is an offer message

					AID responder = reply.getSender();


					try {
						//ContentManager cm = myAgent.getContentManager();
						//Action act = (Action) cm.extractContent(reply);
						//MakeOffer off = (MakeOffer)act.getAction();

						MakeOffer off = (MakeOffer)myAgent.getContentManager().extractContent(reply);

						
						System.err.println("Got offer");
						
						//throws exception if we don't understand message

						Iterator<Offer> itr = off.getAllOFFERINSTANCE();

						// process the offers received 
						while (itr.hasNext()) {
							Offer myOffer=itr.next();
							String currentRequestID = myOffer.getOREQUESTID();//use request ID set by offer
							displayMessage("Recieved offer " + myOffer.getOFFERID() + " from " + responder);

							//check offer meets request
							Request currentRequest = allRequests.get(currentRequestID);
							if (!RequestEvaluator.offerMeetsRequest(myOffer, currentRequest)) {
								//reject offer
								ACLMessage reject = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
								reject.addReceiver(responder);
								reject.setContent(myOffer.getOFFERID());
								reject.setReplyWith("order"+requestID);
								reject.setConversationId("reject"+requestID);
								myAgent.send(reject);

							} else {

								OfferList ol = allOffers.get(currentRequest);

								if (ol == null) {
									ol = new OfferList<ReceivedOffer>();
									ol.add(new ReceivedOffer (responder, myOffer));
								} else {
									ol.addSorted(new ReceivedOffer (responder, myOffer));
								}

								//store the latest, sorted offers for this (sub)-request
								allOffers.put(currentRequestID, ol);
							}
						}


					} catch (OntologyException oe) {
						//reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					} catch (CodecException ce) {
						//reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					}



					//String conv= reply.getConversationId();
					//System.err.println("Conversation " + conv);

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

			return step == 2;//parallel behaviours end when first behaviour ends
		}
	}

	private class ResourceNotifier extends Behaviour {

		private String requestID; 
		//private int step=1;
		//private MessageTemplate mt;
		private int noUnits = 0;
		private int confirmedOffers = 0;
		private HashMap<String, ReceivedOffer> negotiatedOffers = new HashMap<String, ReceivedOffer>();
		private SequentialBehaviour twoPhase;
		private boolean fail=false;

		private ResourceNotifier (String requestID) {
			this.requestID = requestID;
			noUnits=subRequests.get(requestID).size();//number of units to look for
			twoPhase = new SequentialBehaviour (myAgent);
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub

			displayMessage("Negotiating sale");

			//check we have enough offers
			int unitOffers=0;

			Vector<String> v = subRequests.get(requestID);
			Iterator<String> itr = v.iterator();

			while (itr.hasNext()) {
				String key = itr.next();
				if (allOffers.containsKey(key)) {
					unitOffers++;
				}
			}

			if (noUnits == unitOffers) {	
				//We have enough offers, so start negotiating

				ParallelBehaviour phase1 = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL);
				ParallelBehaviour phase2 = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL);
				v = subRequests.get(requestID);
				itr = v.iterator();

				//create parallel behaviours for each sub unti, and begin negotiating
				while (itr.hasNext()) {
					String key = itr.next();
					phase1.addSubBehaviour(new PhaseOneBehaviour(key));					
					phase2.addSubBehaviour(new PhaseTwoBehaviour(key));
				}

				//Run the parallel behaviours one after the other.  
				twoPhase.addSubBehaviour(phase1);
				twoPhase.addSubBehaviour(phase2);
				addBehaviour(twoPhase);

				//TODO: need to do cleanup here. 
				displayMessage("All done!");
				
			} else {
				fail = true;
				displayMessage("Insufficient bids received");
			}

		}



		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			if (twoPhase.done()) {
				return true; 
			} else {
				return false;
			}
		}

		//run the phase one negotiation - confirm all the received offers
		//and if an offer is not received, then tell the next offer etc until failure. 


		private class PhaseOneBehaviour extends Behaviour {
			private int step=1;
			private int offerNo=1;
			private OfferList<ReceivedOffer> subOffers; 
			private String subID;
			private MessageTemplate mt;
			private ReceivedOffer bestOffer;

			public PhaseOneBehaviour(String key) {
				subOffers = allOffers.get(key);
				subID=key;
			}

			@Override
			public void action() {

				switch (step) {
				case 1:

					//TODO: implement the offer evaluation
					bestOffer=(ReceivedOffer)subOffers.get(offerNo);

					displayMessage("Best offer (" + bestOffer.getOffer().getOFFERID() + ")from " +bestOffer.getAgent());

					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestOffer.getAgent());
					order.setContent(bestOffer.getOffer().getOFFERID());
					order.setReplyWith("order"+subID);
					order.setConversationId("agree"+subID);
					myAgent.send(order);

					displayMessage(bestOffer.getOffer().getOCPUHOURCOST());
					displayMessage(order.getReplyWith());

					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("agree"+subID), MessageTemplate.MatchInReplyTo("order"+subID));
					step=2;

					break;

				case 2:

					ACLMessage reply = myAgent.receive(mt);

					if(reply != null) {
						AID responder = reply.getSender();
						System.err.println("case 2 reached");

						if (reply.getPerformative() == ACLMessage.AGREE) {
							//successfully made the bargain
							displayMessage("Recieved accepted offer from " + responder);
							negotiatedOffers.put(subID, bestOffer);
							confirmedOffers++;

							step=3;
						} else {
							displayMessage("Offer withdrawn by " + responder);
							step=1;

							offerNo++; //loop to the next offer
							
						}

					} else {
						block();
						System.err.println("blocking");
					}
					break;
				}//end switch
			}//end method

			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return step==3;
			}

		}

		//when all of the offers have been confirmed, buy the units
		private class PhaseTwoBehaviour extends Behaviour {
			private int step=1;
			private String subID;
			private MessageTemplate mt;
			private ReceivedOffer finalOffer;
			
			public PhaseTwoBehaviour(String key) {
				subID=key;
				finalOffer=negotiatedOffers.get(key);
			}
			
			@Override
			public void action() {
				//check that all the offers have been accepted. If not, cancel this offer. 
		
				switch (step) {

				case 1:
					
				if (confirmedOffers != noUnits) {
					fail=true;
					ACLMessage cancel = new ACLMessage(ACLMessage.CANCEL);
					cancel.addReceiver(finalOffer.getAgent());
					cancel.setContent(finalOffer.getOffer().getOFFERID());
					cancel.setConversationId("cancel"+subID);
					myAgent.send(cancel);
					step =3;
					break;
				} else {
					ACLMessage finalise = new ACLMessage(ACLMessage.CONFIRM);
					finalise.addReceiver(finalOffer.getAgent());
					finalise.setContent(finalOffer.getOffer().getOFFERID());
					finalise.setReplyWith("confirm"+subID);
					finalise.setConversationId("confirm"+subID);
					myAgent.send(finalise);
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("confirm"+subID), MessageTemplate.MatchInReplyTo("confirm"+subID));
					step=2;
				}
				
				case 2:
					ACLMessage reply = myAgent.receive(mt);

					if(reply != null) {
						if (reply.getPerformative() == ACLMessage.CONFIRM) {
							displayMessage(reply.getContent());
							//display the reservation ID
						} else {
							fail=true;
						}
						
						step=3;

					} else {
						block();
					}
					break;
				}//end switch
			}

			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return step==3;
			}

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

