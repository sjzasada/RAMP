package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import uk.ac.ucl.chem.ccs.ramp.rfq.Request;

public interface ResourceInterface {

	public boolean canSatisfy (Request r);
	
	
}
