package uk.ac.ucl.chem.ccs.ramp.user;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;
import jade.core.AID;

public class ReceivedOffer {

	private AID agent;
	private Offer offer;
	
	public ReceivedOffer(AID agent, Offer offer) {
		super();
		this.agent = agent;
		this.offer = offer;
	}

	public AID getAgent() {
		return agent;
	}

	public void setAgent(AID agent) {
		this.agent = agent;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	
	
}
