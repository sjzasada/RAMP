package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: RFQ
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultRFQ implements RFQ {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultRFQ() {
    this._internalInstanceName = "";
  }

  public DefaultRFQ(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: COST
   */
   private Cost cosT;
   public void setCOST(Cost value) { 
    this.cosT=value;
   }
   public Cost getCOST() {
     return this.cosT;
   }

   /**
   * Protege name: REQUESTID
   */
   private String requestiD;
   public void setREQUESTID(String value) { 
    this.requestiD=value;
   }
   public String getREQUESTID() {
     return this.requestiD;
   }

}
