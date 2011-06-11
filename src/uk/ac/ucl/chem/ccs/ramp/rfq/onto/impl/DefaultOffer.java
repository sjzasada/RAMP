package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class DefaultOffer implements Offer {

  private static final long serialVersionUID = -1439939741134496089L;

  private String _internalInstanceName = null;

  public DefaultOffer() {
    this._internalInstanceName = "";
  }

  public DefaultOffer(String instance_name) {
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

}
