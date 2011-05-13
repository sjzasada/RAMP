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
		System.out.println("My name is " + getAID().getLocalName());
		System.out.println("My GUID is " + getAID().getName());
		System.out.println("My addresses are:");
		Iterator<String> it = getAID().getAllAddresses();
		while (it.hasNext()) {
			System.out.println("- " +it.next());
		}
		
		Object args[] = getArguments();
		int coreCount = Integer.parseInt((String)args[0]);
		int maxPrice = Integer.parseInt((String)args[1]);
		
		long deadline = System.currentTimeMillis() + 240000;
		
		Date finish = new Date(deadline);
		
		
		System.out.println("Looking to buy " + coreCount + " cores for less than " + maxPrice + " before " + finish);
		
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
		
		requestQuote(coreCount, maxPrice, finish);
		
	}
	
	protected void takeDown() {
		System.out.println("Agent " + getAID().getName() + " terminated");
	}

	
	public void requestQuote (int coreCount, int maxPrice, Date deadline) {
		addBehaviour(new RequestManager (this, coreCount, maxPrice, deadline));	
	}
		
		
		private class RequestManager extends TickerBehaviour {
			
			private int coreCount;
			private int maxPrice;
			private long deadline, initTime, deltaT;
			
			private RequestManager (Agent a, int c, int p, Date d) {
				super (a, 30000);
				coreCount = c;
				maxPrice = p;
				deadline = d.getTime();
				initTime = System.currentTimeMillis();
				deltaT=deadline - initTime;
			}
			
			public void onTick () {
				long currentTime = System.currentTimeMillis();
				
				if (currentTime > deadline) {
					stop();
				} else {
					
					myAgent.addBehaviour(new ResourceNegotiator(maxPrice, coreCount, this));
				}
				
			}
			
		}
	
		private class ResourceNegotiator extends Behaviour {
			
			private int coreCount;
			private int maxPrice;
			private RequestManager parent;
			
			private int step = 0;
			private int repliesCount = 0;
			
			private AID bestSeller;
			private int bestPrice;
						
			private MessageTemplate mt;
			
			ResourceNegotiator (int m, int p, RequestManager r) {
				coreCount = p;
				maxPrice = m;
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
					
					System.out.println("Pollsing sellers");
					
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

