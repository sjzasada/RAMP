package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;


/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class Offer implements jade.content.Predicate {

	 private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public Offer() {
	    this._internalInstanceName = "";
	  }

	  public Offer(String instance_name) {
	    this._internalInstanceName = instance_name;
	  }

	  public String toString() {
	    return _internalInstanceName;
	  }

	    private String OFFERID;
	    private RFQ OFFERRFQ;

		public String getOFFERID() {
			return OFFERID;
		}

		public void setOFFERID(String oFFERID) {
			OFFERID = oFFERID;
		}

		public RFQ getOFFERRFQ() {
			return OFFERRFQ;
		}

		public void setOFFERRFQ(RFQ oFFERRFQ) {
			OFFERRFQ = oFFERRFQ;
		}
	    
	    

}
