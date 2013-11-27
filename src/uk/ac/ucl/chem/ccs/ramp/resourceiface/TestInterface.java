/**
 * 
 */
package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import java.util.Random;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

/**
 * @author stef
 *
 */
public class TestInterface implements ResourceInterface {

	
	private static int minCPUCost = 10;
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.chem.ccs.ramp.resourceiface.ResourceInterface#canSatisfy(uk.ac.ucl.chem.ccs.ramp.rfq.Request)
	 */
	
	public ResourceOfferRecord canSatisfy(RFQ c) {
		// TODO Auto-generated method stub
		
		ResourceOfferRecord ror = new ResourceOfferRecord(minCPUCost, c);
		
		Random generator = new Random();
		//float prob = generator.nextFloat();
		float prob = 1.0f;
		if (prob > 0.5) {
			return ror;
		}
		
		return null;
	}


	public String makeReservation(Offer c) {
		// TODO Auto-generated method stub
		return "123456";
	}

}
