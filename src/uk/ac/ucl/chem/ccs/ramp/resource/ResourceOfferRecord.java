package uk.ac.ucl.chem.ccs.ramp.resource;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Cost;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public class ResourceOfferRecord {

	private int calledTimes=0;
	
	private int decrement;
	private String requestID;
	private int minCPUCost;
	private Cost returnedCost;
	private String offerID;
	
	
	
	
	
	public ResourceOfferRecord(int minCPUCost, Cost returnedCost) {
		super();
		this.minCPUCost = minCPUCost;
		this.returnedCost = returnedCost;
	}
	public int getDecrement() {
		return decrement;
	}
	public void setDecrement(int decrement) {
		this.decrement = decrement;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public int getMinCPUCost() {
		return minCPUCost;
	}
	public void setMinCPUCost(int minCPUCost) {
		this.minCPUCost = minCPUCost;
	}
	public String getOfferID() {
		return offerID;
	}
	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}
	public void setCost(Cost returnedCost) {
		this.returnedCost = returnedCost;
	}
	
	public Offer getOffer() {
		
		calledTimes++;
		
		Offer of = new Offer();
		
		of.setOFFERID(offerID);
		of.setREQUESTID(requestID);
		
		//naive cost model - most we can charge is 2x min cost
		
		int factor = minCPUCost/calledTimes;
		// probably rounds to nearest int?
		
		returnedCost.setCPUHOURCOST(Integer.toString(minCPUCost+factor));
		
		of.setOFFERCOST(returnedCost);
		
		return of;
	}
	
	
}
