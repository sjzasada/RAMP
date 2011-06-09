package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import jade.util.leap.*;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Reschedule
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultReschedule implements Reschedule {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultReschedule() {
    this._internalInstanceName = "";
  }

  public DefaultReschedule(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: RSCHEDINSTANCE
   */
   private List rschedinstancE = new ArrayList();
   public void addRSCHEDINSTANCE(RFQ elem) { 
     rschedinstancE.add(elem);
   }
   public boolean removeRSCHEDINSTANCE(RFQ elem) {
     boolean result = rschedinstancE.remove(elem);
     return result;
   }
   public void clearAllRSCHEDINSTANCE() {
     rschedinstancE.clear();
   }
   public Iterator getAllRSCHEDINSTANCE() {return rschedinstancE.iterator(); }
   public List getRSCHEDINSTANCE() {return rschedinstancE; }
   public void setRSCHEDINSTANCE(List l) {rschedinstancE = l; }

}
