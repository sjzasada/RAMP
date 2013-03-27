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

import uk.ac.ucl.chem.ccs.ramp.resourceiface.ResourceInterface;
import uk.ac.ucl.chem.ccs.ramp.resourceiface.TestInterface;
import uk.ac.ucl.chem.ccs.ramp.rfq.Request;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MakeOffer;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MakeRequest;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.MarketOntology;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl.DefaultMakeOffer;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl.DefaultOffer;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ResourceAgent extends Agent {

	private HashMap<String, ResourceOfferRecord> currentOffers = new HashMap<String, ResourceOfferRecord>();
	private int minPrice;
	private ResourceInterface resInter;
	
	private Codec codec = new SLCodec(); 
	private Ontology onto = MarketOntology.getInstance();
	
	protected void setup () {
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);
		
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
		
		
		//TODO: Make interface use user configurable
		resInter = new TestInterface();
		
		
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
				
				
				try {
					ContentManager cm = myAgent.getContentManager();
					Action act = (Action) cm.extractContent(msg);
					MakeRequest req = (MakeRequest)act.getAction();
					Iterator<RFQ> it = req.getAllRFQINSTANCE();
					
					boolean anyOffers = false;
					
					MakeOffer offers = new DefaultMakeOffer();
					
					while (it.hasNext()) {
						 RFQ rfq = it.next();
						 
						 ResourceOfferRecord ror = resInter.canSatisfy(rfq.getCOST());//check we can satisfy the offer
						 
						 					 
						 if (ror != null) {
							 //TODO: need to check here if we've offered before and cancel offers if resource now cannot satisfy 
								Random generator = new Random();
								int id = generator.nextInt();
								
							 ror.setOfferID(myAgent.getName()+id);
							 ror.setRequestID(rfq.getREQUESTID());//request ID is set from RFQ - I think this is ok
							 
							 currentOffers.put(rfq.getREQUESTID(), ror);
							 
							 	// we can respond
							 	anyOffers = true;
							 	
							 	//get the current offer to send
							 	Offer myOffer = ror.getOffer();
							 					 	
							 	offers.addOFFERINSTANCE(myOffer);
							 	
							}
						 
						 //RFQ offer = 
						 
					}

					
				
			
				if (anyOffers) {
					reply.setPerformative(ACLMessage.PROPOSE);
					myAgent.getContentManager().fillContent(reply, offers);
				} else	  {
					//decline
					reply.setPerformative(ACLMessage.REFUSE);
				}
				
				
			} catch (OntologyException oe) {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			} catch (CodecException ce) {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
				
				myAgent.send(reply);
			 
			} else {
				block();
			}
			
		}
	}
	
	private class PurchaseOrdersServer extends CyclicBehaviour {
		
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);

		
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				
				String offerID = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				if (currentOffers.containsKey(offerID)) {
					
					ResourceOfferRecord ror = currentOffers.get(offerID);
					
					String reservationID = resInter.makeReservation(ror.getOffer().getOFFERCOST());
					
					
					if (reservationID != null) {
						reply.setContent(reservationID);
						reply.setPerformative(ACLMessage.INFORM);
						System.out.println("Made deal with agent "+msg.getSender().getName() + " reservation " + reservationID);
					} else {
						//can't now satisfy
						reply.setPerformative(ACLMessage.REFUSE);
					}
					
					currentOffers.remove(offerID);
					
				} else {
					//we didn't make the offer. what are you doing?
					
					reply.setPerformative(ACLMessage.FAILURE);
				}
							
				//System.err.println(reply.getInReplyTo());
				//System.err.println(reply.getConversationId());
				
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	} 
	

	
	//listen for reservation cancellation requests
	private class CancelServer extends CyclicBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
