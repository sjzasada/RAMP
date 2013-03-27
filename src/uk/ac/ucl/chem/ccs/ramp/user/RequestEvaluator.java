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
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;

public class RequestEvaluator {

	public static boolean offerMeetsRequest (Offer a, Request b) {
		boolean meetsReq=true;
		
		//request things to test
		int cost = b.getCPUCost();
		int cpucount = b.getCPUCount();
		Date end = b.getEnd();
		Date start = b.getStart();
		
		//offer things to test
		int offerCost = Integer.parseInt(a.getOFFERCOST().getCPUHOURCOST());
		int offerCPU = a.getOFFERCOST().getCORES().getTOTALCORES();
		Date offerStart = new Date(a.getOFFERCOST().getNOTBEFORE());
		Date offerEnd = new Date(a.getOFFERCOST().getDEADLINE());
		
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
	
	public static boolean naiveEvaluator (Offer a, Offer b) {
		
		Cost costa = a.getOFFERCOST();
		Cost costb = b.getOFFERCOST();
		
		//just check that the cost of b is less than the cost of a
		int cpucosta = Integer.parseInt(costa.getCPUHOURCOST());
		int cpucostb = Integer.parseInt(costb.getCPUHOURCOST());
		
		if (cpucosta > cpucostb) {
			return true;
		}
		
		return false;
	}
	
	public static boolean weightedEvaluator (Offer a, Offer b) {
		
		
		return false;
	}
	
}
