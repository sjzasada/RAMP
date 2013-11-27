package uk.ac.ucl.chem.ccs.ramp.resource;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public class ResourceOfferRecord {

	private int calledTimes=0;
	
	private int decrement;
	private String requestID;
	private int minCPUCost;
	private RFQ returnedRFQ;
	private String offerID;
	
	
	
	
	
	public ResourceOfferRecord(int minCPUCost, RFQ returnedRFQ) {
		super();
		this.minCPUCost = minCPUCost;
		this.returnedRFQ = returnedRFQ;
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
	public void setRFQ(RFQ returnedRFQ) {
		this.returnedRFQ = returnedRFQ;
	}
	
	public Offer getOffer() {
		
		calledTimes++;
		
		Offer of = new Offer();
		
		of.setOFFERID(offerID);
		//naive cost model - most we can charge is 2x min cost
		
		int factor = minCPUCost/calledTimes;
		// probably rounds to nearest int?
		
		
		returnedRFQ.setCPUHOURCOST(Integer.toString(minCPUCost+factor));
		
		if (returnedRFQ.getARCHITECTURE() != null) {
		of.setOARCHITECTURE(returnedRFQ.getARCHITECTURE());
		}
		
		if (returnedRFQ.getCPUHOURCOST() != null) {
		of.setOCPUHOURCOST(returnedRFQ.getCPUHOURCOST());
		}
		
		if (returnedRFQ.getCPUSPEED() != null) {
		of.setOCPUSPEED(returnedRFQ.getCPUSPEED());
		}
		
		if (returnedRFQ.getDEADLINE() != null) {
		of.setODEADLINE(returnedRFQ.getDEADLINE());
		}
		
		if (returnedRFQ.getDURATION() != null) {
		of.setODURATION(returnedRFQ.getDURATION());
		}
		
		if (returnedRFQ.getINTERNODEBANDWIDTH() != null) {
		of.setOINTERNODEBANDWIDTH(returnedRFQ.getINTERNODEBANDWIDTH());
		}
		
		of.setONODECORES(returnedRFQ.getNODECORES());
		
		of.setONODECOUNT(returnedRFQ.getNODECOUNT());
		
		of.setOTOTALCORES(returnedRFQ.getTOTALCORES());
		
		if (returnedRFQ.getNODEDISKSPACE() != null) {
		of.setONODEDISKSPACE(returnedRFQ.getNODEDISKSPACE());
		}

		if (returnedRFQ.getTOTALDISKSPACE() != null) {
		of.setOTOTALDISKSPACE(returnedRFQ.getTOTALDISKSPACE());
		}
		
		if (returnedRFQ.getNOTBEFORE() != null) {
		of.setONOTBEFORE(returnedRFQ.getNOTBEFORE());
		}
		
		if (returnedRFQ.getOPERATINGSYSTEM() != null) {
		of.setOOPERATINGSYSTEM(returnedRFQ.getOPERATINGSYSTEM());
		}
		
		if (returnedRFQ.getOSVERSION() != null) {
		of.setOOSVERSION(returnedRFQ.getOSVERSION());
		}
		
		if (returnedRFQ.getRAMPERCORE() != null) {
		of.setORAMPERCORE(returnedRFQ.getRAMPERCORE());
		}
		
		if (returnedRFQ.getREQUESTID() != null) {
		of.setOREQUESTID(returnedRFQ.getREQUESTID());
		}

		return of;
	}
	
	
}
