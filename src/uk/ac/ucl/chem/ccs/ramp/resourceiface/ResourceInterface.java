package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Cost;

public interface ResourceInterface {

	public ResourceOfferRecord canSatisfy (Cost c);
	
	public String makeReservation (Cost c);
}
