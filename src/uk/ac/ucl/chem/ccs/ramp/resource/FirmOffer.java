package uk.ac.ucl.chem.ccs.ramp.resource;

import java.util.Date;

public class FirmOffer {

	private ResourceOfferRecord ror;
	private Date offerMade;
	private String reservationID;
	
	public enum Status {MADE, CONFIRMED, FINALISED};
	
	private Status status;

	public FirmOffer(ResourceOfferRecord ror,
			Status status) {
		super();
		this.ror = ror;
		this.reservationID = null;
		this.status = status;
		this.offerMade=new Date();//set to now
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ResourceOfferRecord getRor() {
		return ror;
	}

	public Date getOfferMade() {
		return offerMade;
	}

	public String getReservationID() {
		return reservationID;
	}
	
	
	public void setReservationID(String resID) {
		this.reservationID = resID;
	}
	
	
}
