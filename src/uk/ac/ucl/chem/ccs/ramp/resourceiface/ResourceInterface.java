package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Cost;

public interface ResourceInterface {

	public Cost canSatisfy (Cost c);
	
	
}
