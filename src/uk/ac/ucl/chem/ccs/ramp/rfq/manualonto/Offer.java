package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Cost;



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

	   /**
	   * Protege name: OFFERCOST
	   */
	   private Cost offercosT;
	   public void setOFFERCOST(Cost value) { 
	    this.offercosT=value;
	   }
	   public Cost getOFFERCOST() {
	     return this.offercosT;
	   }

	   /**
	   * Protege name: OFFERID
	   */
	   private String offeriD;
	   public void setOFFERID(String value) { 
	    this.offeriD=value;
	   }
	   public String getOFFERID() {
	     return this.offeriD;
	   }


	private String requestiD;
	public String getREQUESTID() {
		// TODO Auto-generated method stub
		return this.requestiD;
	}


	public void setREQUESTID(String value) {
		// TODO Auto-generated method stub
		this.requestiD = value;
	}

}
