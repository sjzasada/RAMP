package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import jade.util.leap.*;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Request
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultRequest implements Request {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultRequest() {
    this._internalInstanceName = "";
  }

  public DefaultRequest(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: RFQINSTANCE
   */
   private List rfqinstancE = new ArrayList();
   public void addRFQINSTANCE(RFQ elem) { 
     rfqinstancE.add(elem);
   }
   public boolean removeRFQINSTANCE(RFQ elem) {
     boolean result = rfqinstancE.remove(elem);
     return result;
   }
   public void clearAllRFQINSTANCE() {
     rfqinstancE.clear();
   }
   public Iterator getAllRFQINSTANCE() {return rfqinstancE.iterator(); }
   public List getRFQINSTANCE() {return rfqinstancE; }
   public void setRFQINSTANCE(List l) {rfqinstancE = l; }

}
