package uk.ac.ucl.chem.ccs.ramp.rfq.onto;


import jade.util.leap.*;

/**
* Protege name: MakeRequest
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public interface MakeRequest extends jade.content.AgentAction {

   /**
   * Protege name: RFQINSTANCE
   */
   public void addRFQINSTANCE(RFQ elem);
   public boolean removeRFQINSTANCE(RFQ elem);
   public void clearAllRFQINSTANCE();
   public Iterator getAllRFQINSTANCE();
   public List getRFQINSTANCE();
   public void setRFQINSTANCE(List l);

}
