/*
 * Created on 10:30:32 20 May 2011
 * Project: RAMP 
 * File: Offer.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.user;

import java.util.HashMap;
import java.util.Set;

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;
import jade.core.AID;

public class Offers {

	
	private HashMap<String, Coster> bestoffers = new HashMap<String, Coster>();
	private HashMap<String, HashMap<AID, Offer>> alloffers = new HashMap<String, HashMap<AID, Offer>>();;
	
	//manipulate table of best offers
	public Offer getBestOffer(String reservationID) {	
		return bestoffers.get(reservationID).getTheCost();
	}
	
	public AID getBestAgent(String reservationID) {
		return bestoffers.get(reservationID).getTheAgent();
	}
	
	public void setBestOffer(String reservationID, AID agent, Offer cost) {
		bestoffers.put(reservationID, new Coster(agent, cost));
	}
	
	public void addOffer(AID agent, String reservationID, Offer cost) {
		
		HashMap<AID, Offer> tmp = alloffers.get(reservationID);
		
		if (tmp == null) {
			tmp = new HashMap<AID, Offer>();
		} 
		//alloffers is a hash of hashes
		tmp.put(agent, cost);
		alloffers.put(reservationID, tmp);
	}
	
	public Set<AID> getOfferAgents(String reservationID) {
		HashMap<AID, Offer> tmp = alloffers.get(reservationID);
		
		return tmp.keySet();
		
	}
	
	//return the offer for a given agent and reservation
	public Offer getOffer(String reservationID, AID agent) {
		HashMap<AID, Offer> tmp = alloffers.get(reservationID);
		
		return tmp.get(agent);
	}
	
	private class Coster {
		private AID theAgent;
		private Offer theCost;
		/**
		 * @param theAgent
		 * @param theCost
		 */
		public Coster(AID theAgent, Offer theCost) {
			this.theAgent = theAgent;
			this.theCost = theCost;
		}
		/**
		 * @return the theAgent
		 */
		public AID getTheAgent() {
			return theAgent;
		}
		/**
		 * @param theAgent the theAgent to set
		 */
		public void setTheAgent(AID theAgent) {
			this.theAgent = theAgent;
		}
		/**
		 * @return the theCost
		 */
		public Offer getTheCost() {
			return theCost;
		}
		/**
		 * @param theCost the theCost to set
		 */
		public void setTheCost(Offer theCost) {
			this.theCost = theCost;
		}
		
	}
	
}
