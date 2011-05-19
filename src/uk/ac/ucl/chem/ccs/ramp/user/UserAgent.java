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

import java.util.Date;
import java.util.Vector;

import java.util.Iterator;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserAgent extends Agent {

	private Vector resourceAgents = new Vector();
		
	protected void setup () {
		
		// print out agent details
		System.out.println("My name is " + getAID().getLocalName());
		System.out.println("My GUID is " + getAID().getName());
		System.out.println("My addresses are:");
		Iterator<String> it = getAID().getAllAddresses();
		while (it.hasNext()) {
			System.out.println("- " +it.next());
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
		
		// get the args to the prog - should be 1
		Object args[] = getArguments();
		
		if (args.length == 1) {
		
			Request myRequest = new Request((String)args[0]);	
			System.out.println("Looking to buy " + myRequest.getCPUCount() + " cores for less than " + myRequest.getCPUCost() + " before " + myRequest.getEnd());
	
			requestQuote(myRequest);
	
		}
		
	}
	
	//this is what happens when the agent finishes
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
	}

	//method to begin a negotiation
	public void requestQuote (Request r) {
		addBehaviour(new RequestAQuote(this, r));
	}
		
	
	//behaviour to hold all data associated with this request
	private class RequestAQuote extends Behaviour {
		
		boolean done;
		
		private Request currentRequest;
		private Agent a;
		
		private Vector quotes;
		private AID bestSeller;
		private int bestPrice; 
		
		private RequestAQuote (Agent a, Request r) {
			quotes = new Vector();
			this.a = a;
			currentRequest = r;
		}
		
		
		public boolean done() {
			return done;
		}


		@Override
		public void action() {
			// TODO Auto-generated method stub
			RequestManager requester = new RequestManager (a, currentRequest);
			addBehaviour(requester);
			
			//wait until the requester is done, then once it is, notify winners
			if (!requester.done()) {
				block();
			} else {
				myAgent.addBehaviour(new ResourceNotifier(this));
			}
			
		}
		
	}
	
	
	
	
		//keep requesting until deadline
		private class RequestManager extends TickerBehaviour {
			
			private long deadline, initTime, deltaT;
			
			private Request r;
			
			private RequestManager (Agent a, Request r) {
				super (a, 30000);
				deadline = System.currentTimeMillis() + 240000;
				initTime = System.currentTimeMillis();
				deltaT=deadline - initTime;
				this.r=r;
			}
			
			public void onTick () {
				long currentTime = System.currentTimeMillis();
				
				if (currentTime > deadline) {
					stop();
				} else {
					myAgent.addBehaviour(new ResourceNegotiator(r, this));
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
					
					System.out.println("Polling sellers");
					
					cfp.setContent(Integer.toString(coreCount));
					cfp.setConversationId("compute-negotiation");
					cfp.setReplyWith("cfp"+System.currentTimeMillis());
					
					myAgent.send(cfp);
					
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("compute-negotiation"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
					
					step = 1;
					break;
					
				case 1:
					ACLMessage reply = myAgent.receive(mt);
					if (reply != null) {
						if (reply.getPerformative() == ACLMessage.PROPOSE) {
							//this is an offer message
								
							int price = Integer.parseInt(reply.getContent());
							
							if (bestSeller == null || price < bestPrice) {
								bestPrice = price;
								bestSeller = reply.getSender();
							}
						
							System.out.println("Recieved price " + price + " from " + reply.getSender());
							
						}
						repliesCount++;
						
						System.out.println("Rep no " + repliesCount +"/"+resourceAgents.size());
						
						if (repliesCount == resourceAgents.size()) {
							step = 2;
						}
						System.out.println("step = " + step);
						
					} else {
						block();
					}
					break;

				case 2:
					if (bestSeller != null && bestPrice <= maxPrice) {
						System.out.println("Seller " + bestSeller + " was the lowest bidder at " + bestPrice);
					} else {
						System.out.println("No sellers available");
					}
					
					parent.stop();
					step = 3;
					break;
				}
				
			}
			
			public boolean done () {
				
				return step == 3;
			}
		}
	
}

