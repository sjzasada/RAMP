package uk.ac.ucl.chem.ccs.ramp.rfq.onto;


import jade.util.leap.*;

/**
* Protege name: MakeOffer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public interface MakeOffer extends jade.content.AgentAction {

   /**
   * Protege name: OFFERINSTANCE
   */
   public void addOFFERINSTANCE(Offer elem);
   public boolean removeOFFERINSTANCE(Offer elem);
   public void clearAllOFFERINSTANCE();
   public Iterator getAllOFFERINSTANCE();
   public List getOFFERINSTANCE();
   public void setOFFERINSTANCE(List l);

}
