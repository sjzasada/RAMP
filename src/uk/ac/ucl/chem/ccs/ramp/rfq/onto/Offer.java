package uk.ac.ucl.chem.ccs.ramp.rfq.onto;


import jade.util.leap.*;

/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public interface Offer extends jade.content.AgentAction {

   /**
   * Protege name: OFFERINSTANCE
   */
   public void addOFFERINSTANCE(RFQ elem);
   public boolean removeOFFERINSTANCE(RFQ elem);
   public void clearAllOFFERINSTANCE();
   public Iterator getAllOFFERINSTANCE();
   public List getOFFERINSTANCE();
   public void setOFFERINSTANCE(List l);

}
