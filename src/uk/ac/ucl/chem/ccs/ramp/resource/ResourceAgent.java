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

import uk.ac.ucl.chem.ccs.ramp.resource.FirmOffer.Status;
import uk.ac.ucl.chem.ccs.ramp.resourceiface.QstatInterface;
import uk.ac.ucl.chem.ccs.ramp.resourceiface.ResourceInterface;
import uk.ac.ucl.chem.ccs.ramp.resourceiface.TestInterface;
import uk.ac.ucl.chem.ccs.ramp.rfq.Request;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MakeOffer;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MakeRequest;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.MarketOntology;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;

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

	private HashMap<String, FirmOffer> currentOffers = new HashMap<String, FirmOffer>();
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
		System.setProperty("ramp.price", Integer.toString(minPrice));
		
		//TODO: Make interface use user configurable
		resInter = new QstatInterface();
		
		
		addBehaviour(new RFQResponseServer());
		addBehaviour(new PurchaseOrdersServer());
		addBehaviour(new FinaliseServer());

		
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
				System.err.println("Received request");
				System.err.println("Agent using ontolgoy " + myAgent.getContentManager().getOntologyNames()[0]);
				System.err.println("Message using ontology " + msg.getOntology());
				System.err.println("Message is "+ msg.toString());
				
				ACLMessage reply = msg.createReply();
				
				//decide whether to reply
				
				
				try {
				//	ContentManager cm = myAgent.getContentManager();
				//	Action act = (Action) cm.extractContent(msg);
					MakeRequest req = (MakeRequest)myAgent.getContentManager().extractContent(msg);
					System.err.println("Action "+ req.toString());

					
					Iterator<RFQ> it = req.getAllRFQINSTANCE();
					
					boolean anyOffers = false;
					
					MakeOffer offers = new MakeOffer();
					
					while (it.hasNext()) {
						 RFQ rfq = it.next();
						 
						 ResourceOfferRecord ror = resInter.canSatisfy(rfq);//check we can satisfy the offer
						 
						 System.err.println("Request " + rfq.getREQUESTID() + " for " + rfq.getTOTALCORES() + " cores " + " @ " + rfq.getCPUHOURCOST());
						 
						 if (ror != null) {
							 System.err.println("Making offer");
							 
							 //TODO: need to check here if we've offered before and cancel offers if resource now cannot satisfy 
								Random generator = new Random();
								int id = generator.nextInt();
								
							 ror.setOfferID(myAgent.getName()+id);
							 ror.setRequestID(rfq.getREQUESTID());//request ID is set from RFQ - I think this is ok
							 
							 FirmOffer fo = new FirmOffer(ror, Status.MADE);
							 
//							 currentOffers.put(rfq.getREQUESTID(), fo);
							 currentOffers.put(ror.getOfferID(), fo);
							 
							 	// we can respond
							 	anyOffers = true;
							 	
							 	//get the current offer to send
							 	Offer myOffer = ror.getOffer();
							 					 	
							 	offers.addOFFERINSTANCE(myOffer);
							 	
								 System.err.println("Offer " + myOffer.getOREQUESTID() + " for " + myOffer.getOTOTALCORES() + " cores " + " @ " + myOffer.getOCPUHOURCOST());

							 	
							} else {
								 System.err.println("Not making offer");
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
				System.err.println("Message not understood - ontology");
				oe.printStackTrace();
			} catch (CodecException ce) {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				System.err.println("Message not understood - codec");
				ce.printStackTrace();
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
			
			System.err.println("Called purchase behaviour");

			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// ACCEPT_PROPOSAL Message received. Process it
				
				String offerID = msg.getContent();//TODO - check this is the way to get the offer ID
				ACLMessage reply = msg.createReply();
				
				System.err.println("Accept proposal "+offerID);
			
				if (currentOffers.containsKey(offerID)) {
				
					System.err.println("We have made this offer");
					
					FirmOffer fo = currentOffers.get(offerID);
					
					String reservationID = resInter.makeReservation(fo.getRor().getOffer());
					
					System.err.println("Made reservation " + reservationID);
					
					if (reservationID != null) {
						fo.setReservationID(reservationID);
						fo.setStatus(Status.CONFIRMED);
						currentOffers.put(offerID, fo);
						
						reply.setContent(reservationID);
						reply.setPerformative(ACLMessage.AGREE);
						System.out.println("Made deal with agent "+msg.getSender().getName() + " reservation " + reservationID);
					} else {
						//can't now satisfy
						reply.setPerformative(ACLMessage.REFUSE);
						currentOffers.remove(offerID);
					}
					
					
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
	
	private class FinaliseServer extends CyclicBehaviour {
		
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);

		//TODO: This behaviour should periodically examine reservation IDs and remove any that have timed out
		
		public void action() {
			

			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CONFIRM Message received. Process it
				System.err.println("Called confirm behaviour");
				
				String offerID = msg.getContent();//TODO - check this is the way to get the offer ID
				ACLMessage reply = msg.createReply();
				
				System.err.println("Accept proposal "+offerID);
			
				if (currentOffers.containsKey(offerID)) {
				
					System.err.println("We have made this offer");
					
					FirmOffer fo = currentOffers.get(offerID);
					
					String reservationID = fo.getReservationID();
					
					System.err.println("Confirming reservation " + reservationID);
					
					if (reservationID != null) {
						fo.setReservationID(reservationID);
						fo.setStatus(Status.CONFIRMED);
						currentOffers.put(offerID, fo);
						
						reply.setContent(reservationID);
						reply.setPerformative(ACLMessage.CONFIRM);
						System.out.println("Confirmed with agent "+msg.getSender().getName() + " reservation " + reservationID);
					} else {
						//can't now satisfy
						reply.setPerformative(ACLMessage.REFUSE);
						currentOffers.remove(offerID);
					}
					
					
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
