package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import jade.util.leap.*;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultOffer implements Offer {

  private static final long serialVersionUID = 2389062215155035045L;

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
   * Protege name: OFFERINSTANCE
   */
   private List offerinstancE = new ArrayList();
   public void addOFFERINSTANCE(RFQ elem) { 
     offerinstancE.add(elem);
   }
   public boolean removeOFFERINSTANCE(RFQ elem) {
     boolean result = offerinstancE.remove(elem);
     return result;
   }
   public void clearAllOFFERINSTANCE() {
     offerinstancE.clear();
   }
   public Iterator getAllOFFERINSTANCE() {return offerinstancE.iterator(); }
   public List getOFFERINSTANCE() {return offerinstancE; }
   public void setOFFERINSTANCE(List l) {offerinstancE = l; }

}
