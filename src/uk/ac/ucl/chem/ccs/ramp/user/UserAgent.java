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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserAgent extends Agent {

	private Vector resourceAgents = new Vector();
	UserGui myGui=null;
	
	protected void setup () {
		
		
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
		addBehaviour(new RequestAQuote(this, r));
	}
		
	////////////////////////////////////////
	//
	// behaviours
	//
	////////////////////////////////////////
	
	
	//behaviour to hold all data associated with this request
	private class RequestAQuote extends Behaviour {
		
		boolean first=false;
		
		private Request currentRequest;
		private Agent a;
		private int round =0; 
		private HashMap<AID, Offer> quotes;
		private AID bestSeller;
		private int bestPrice; 
		private SequentialBehaviour twoStep;
		
		private RequestAQuote (Agent a, Request r) {
			quotes = new HashMap<AID, Offer>();
			this.a = a;
			currentRequest = r;
	
			
			twoStep = new SequentialBehaviour (a);


			
		
		}
		
		
		public boolean done() {
			return twoStep.done();
		}


		@Override
		public void action() {
			// TODO Auto-generated method stub
			if (!first) {
				displayMessage("Starting twostep behaviour");
				twoStep.addSubBehaviour(new RequestManager (a, currentRequest, this));
				twoStep.addSubBehaviour(new ResourceNotifier(this));
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
			
			private Request r;
			private RequestAQuote raq;
			
			private RequestManager (Agent a, Request r, RequestAQuote raq) {
				super (a, 30000);
				deadline = System.currentTimeMillis() + 120000;
				initTime = System.currentTimeMillis();
				deltaT=deadline - initTime;
				this.r=r;
				this.raq=raq;
			}
			
			public void onTick () {
				long currentTime = System.currentTimeMillis();
				
				if (currentTime > deadline) {
					displayMessage("Deadline passed, stopping requesting");
					stop();
				} else {
					displayMessage("Starting new resource request. Current time " + currentTime + " deadline " + deadline);
					displayMessage("Bidding round " + ++raq.round);
					
					// add a behaviour to start negotiations
					myAgent.addBehaviour(new ResourceNegotiator(r, this, raq));

				}
				
			}
			
		}
	
		
		//this behaviour opens up negotiations
		private class ResourceNegotiator extends Behaviour {
			
			private RequestManager parent;
			private RequestAQuote raq;
			private Request rq;
			private int step = 0;
			private int repliesCount = 0;
			
						
			private MessageTemplate mt;
			
			ResourceNegotiator (Request rq, RequestManager r, RequestAQuote raq) {
				this.raq=raq;
				this.rq=rq;
				parent = r;
			}
			
			public void action () {
				
				switch (step) {
				
				case 0:
					//send the cfp to all resources
					
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

					
					
					Iterator<AID> it = resourceAgents.iterator();
					while (it.hasNext()) {
						cfp.addReceiver(it.next());
					}
					
					displayMessage("Polling sellers");
					
					//TODO: make the message more comprehensive
					
					cfp.setContent(Integer.toString(rq.getCPUCount()));
					cfp.setConversationId("compute-negotiation-"+raq.round);
					cfp.setReplyWith("cfp"+System.currentTimeMillis());
					
					myAgent.send(cfp);
					
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("compute-negotiation-"+raq.round), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
					
					step = 1;
					break;
					
				case 1:
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null) {
						if (reply.getPerformative() == ACLMessage.PROPOSE) {
							//this is an offer message
							
							AID responder = reply.getSender();
							
							int price = Integer.parseInt(reply.getContent());
							
							
							String conversation = reply.getConversationId();
							
							//add this offer to the hash table										
							raq.quotes.put(responder, new Offer(responder, price, conversation));
							
							System.err.println("Conversation " + conversation);
							
							
							if (raq.bestSeller == null || price < raq.bestPrice) {
								raq.bestPrice = price;
								raq.bestSeller = reply.getSender();
							}
						
							displayMessage("Recieved price " + price + " from " + reply.getSender());
							
						}
						repliesCount++;
						
						//System.out.println("Rep no " + repliesCount +"/"+resourceAgents.size());
						
						if (repliesCount == resourceAgents.size()) {
							step = 2;
						}
					//	System.out.println("step = " + step);
						
					} else {
						block();
					}
					break;

				}
				
			}
			
			public boolean done () {
				
				return step == 2;
			}
		}
	
		private class ResourceNotifier extends Behaviour {

			private RequestAQuote raq;
			private boolean done = false;
			private int step=1;
			private MessageTemplate mt;
			
			private ResourceNotifier (RequestAQuote raq) {
				this.raq = raq;
			}
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				
				
				
				
				switch (step) {
				
				case 1:
					
				Collection<Offer> coll = raq.quotes.values();
				Iterator<Offer> itr = coll.iterator();
				while (itr.hasNext()) {
					Offer o = (Offer)itr.next();
					displayMessage(o.toString());
				}
				
					
				if (raq.bestSeller != null) {
					//TODO: implement the offer evaluation
					displayMessage("Accepting offer from " + raq.bestSeller);
					
					
					
					AID winner = raq.bestSeller;
					Offer offer = raq.quotes.get(winner);
					
					if (offer == null) {
						System.err.println("found in hash map");
					}
					
					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					 order.addReceiver(winner);
					 order.setContent(Integer.toString(offer.price));
					 order.setReplyWith("order"+System.currentTimeMillis());
					 order.setConversationId(offer.getConversation());
					 myAgent.send(order);
					 
					 System.err.println(offer.getPrice());
					 System.err.println(offer.getConversation());
					 System.err.println(order.getReplyWith());
					 
					 mt = MessageTemplate.and(MessageTemplate.MatchConversationId(offer.getConversation()), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
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
					
				

				}
				
			}

			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return step==3;
			}
			
			
			
		}
		
}

