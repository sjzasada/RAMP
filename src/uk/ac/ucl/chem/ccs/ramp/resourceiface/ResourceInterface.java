package uk.ac.ucl.chem.ccs.ramp.resourceiface;

import uk.ac.ucl.chem.ccs.ramp.resource.FirmOffer;
import uk.ac.ucl.chem.ccs.ramp.resource.ResourceOfferRecord;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;

public interface ResourceInterface {
	
	public String message="";

	public ResourceOfferRecord canSatisfy (RFQ c, FirmOffer oldRor);
	
	public String makeReservation (Offer c);
}
