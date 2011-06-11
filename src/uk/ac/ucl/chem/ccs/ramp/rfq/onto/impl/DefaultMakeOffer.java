package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import jade.util.leap.*;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: MakeOffer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class DefaultMakeOffer implements MakeOffer {

  private static final long serialVersionUID = -1439939741134496089L;

  private String _internalInstanceName = null;

  public DefaultMakeOffer() {
    this._internalInstanceName = "";
  }

  public DefaultMakeOffer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: OFFERINSTANCE
   */
   private List offerinstancE = new ArrayList();
   public void addOFFERINSTANCE(Offer elem) { 
     offerinstancE.add(elem);
   }
   public boolean removeOFFERINSTANCE(Offer elem) {
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
