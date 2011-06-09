package uk.ac.ucl.chem.ccs.ramp.rfq.onto;


import jade.util.leap.*;

/**
* Protege name: Request
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public interface Request extends jade.content.AgentAction {

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
