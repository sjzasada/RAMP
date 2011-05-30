/**
 * 
 */
package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import java.util.Random;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

/**
 * @author stef
 *
 */
public class TestInterface implements ResourceInterface {

	/* (non-Javadoc)
	 * @see uk.ac.ucl.chem.ccs.ramp.resourceiface.ResourceInterface#canSatisfy(uk.ac.ucl.chem.ccs.ramp.rfq.Request)
	 */
	@Override
	public boolean canSatisfy(Request r) {
		// TODO Auto-generated method stub
		
		Random generator = new Random();
		float prob = generator.nextFloat();
		
		if (prob > 0.5) {
			return true;
		}
		
		return false;
	}

}
