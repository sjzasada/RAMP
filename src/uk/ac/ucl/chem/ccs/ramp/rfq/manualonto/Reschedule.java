package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;


import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import jade.util.leap.*;

/**
* Protege name: Reschedule
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class Reschedule implements jade.content.Concept {

	 private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public Reschedule() {
	    this._internalInstanceName = "";
	  }

	  public Reschedule(String instance_name) {
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
