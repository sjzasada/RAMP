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

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;

public class RequestEvaluator {

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
