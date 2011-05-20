/*
 * Created on 16:15:09 11 May 2011
 * Project: RAMP 
 * File: ResourceAgent.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ResourceAgent extends Agent {

	private Map offers = new HashMap();
	int minPrice;
	
	protected void setup () {
		System.out.println("My name is " + getAID().getLocalName());
		System.out.println("My GUID is " + getAID().getName());
		System.out.println("My addresses are:");
		Iterator<String> it = getAID().getAllAddresses();
		while (it.hasNext()) {
			System.out.println("- " +it.next());
		}
		
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("resource-trader");
		sd.setName(getLocalName()+"resource-trader");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			System.out.println("Registered with DFAgent");
		} catch (FIPAException fp) {
			fp.printStackTrace();
		}
		
		Object args[] = getArguments();
		minPrice = Integer.parseInt((String)args[0]);
	
		
		System.out.println("Selling for less than " + minPrice);
		
		addBehaviour(new RFQResponseServer());
		addBehaviour(new PurchaseOrdersServer());
		
		
	}
	
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		System.out.println("Agent " + getAID().getName() + " terminated");
	}
	
	
	protected class RFQResponseServer extends CyclicBehaviour {
		
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		
		public void action () {
			
			ACLMessage msg = myAgent.receive(mt);
			
			if (msg != null) {
				
				
				ACLMessage reply = msg.createReply();
				
				//decide whether to reply
				Random generator = new Random();
				float prob = generator.nextFloat();
				
				if (prob > 0.5) {
					// we can respond
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(minPrice));
				} else {
					//decline
					reply.setPerformative(ACLMessage.REFUSE);
				}
				myAgent.send(reply);
			 
			} else {
				block();
			}
			
		}
	}
	
	private class PurchaseOrdersServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				String price = msg.getContent();
				ACLMessage reply = msg.createReply();

				reply.setPerformative(ACLMessage.INFORM);
				System.out.println("Made deal with agent "+msg.getSender().getName() + " for " + price);
				
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	} 
	
}
