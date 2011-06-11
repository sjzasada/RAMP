package uk.ac.ucl.chem.ccs.ramp.rfq.onto;


import jade.util.leap.*;

/**
* Protege name: Reschedule
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public interface Reschedule extends jade.content.AgentAction {

   /**
   * Protege name: RSCHEDINSTANCE
   */
   public void addRSCHEDINSTANCE(RFQ elem);
   public boolean removeRSCHEDINSTANCE(RFQ elem);
   public void clearAllRSCHEDINSTANCE();
   public Iterator getAllRSCHEDINSTANCE();
   public List getRSCHEDINSTANCE();
   public void setRSCHEDINSTANCE(List l);

}
