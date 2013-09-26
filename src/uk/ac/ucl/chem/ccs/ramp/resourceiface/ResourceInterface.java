package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;

public interface ResourceInterface {

	public ResourceOfferRecord canSatisfy (RFQ c);
	
	public String makeReservation (RFQ c);
}
