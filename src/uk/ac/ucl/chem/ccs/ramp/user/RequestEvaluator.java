/*
 * Created on 15:26:40 13 Jun 2011
 * Project: RAMP 
 * File: ReqestEvaluator.java
 * 
 * @author stefan
 * 
 * TODO: 
 */
package uk.ac.ucl.chem.ccs.ramp.user;

import java.util.Date;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public class RequestEvaluator {

	public static boolean offerMeetsRequest (uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer myOffer, Request b) {
		boolean meetsReq=true;
		
		//request things to test
		int cost = b.getCPUCost();
		int cpucount = b.getCPUCount();
		Date end = b.getEnd();
		Date start = b.getStart();
		
		//offer things to test
		int offerCost = Integer.parseInt(myOffer.getOFFERRFQ().getCPUHOURCOST());
		int offerCPU = myOffer.getOFFERRFQ().getTOTALCORES();
		Date offerStart = new Date(myOffer.getOFFERRFQ().getNOTBEFORE());
		Date offerEnd = new Date(myOffer.getOFFERRFQ().getDEADLINE());
		
		if (cost > offerCost) {
			meetsReq=false;
		} else if (cpucount > offerCPU) {
			meetsReq=false;
		} else if (end.after(offerEnd)) {
			meetsReq=false;
		} else if (start.before(offerStart)) {
			meetsReq=false;
		}
		
		return meetsReq;
		
	}
	
	public static boolean naiveEvaluator (uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer offer, uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer offer2) {
		
	
		//just check that the cost of b is less than the cost of a
		int cpucosta = Integer.parseInt(offer.getOFFERRFQ().getCPUHOURCOST());
		int cpucostb = Integer.parseInt(offer2.getOFFERRFQ().getCPUHOURCOST());
		
		if (cpucosta > cpucostb) {
			return true;
		}
		
		return false;
	}
	
	public static boolean weightedEvaluator (Offer a, Offer b) {
		
		
		return false;
	}
	
}
