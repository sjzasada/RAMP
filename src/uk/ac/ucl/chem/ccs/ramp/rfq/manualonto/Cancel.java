package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;


import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import jade.util.leap.*;

/**
* Protege name: Cancel
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class Cancel implements jade.content.AgentAction {

	  private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public Cancel() {
	    this._internalInstanceName = "";
	  }

	  public Cancel(String instance_name) {
	    this._internalInstanceName = instance_name;
	  }

	  public String toString() {
	    return _internalInstanceName;
	  }

	   /**
	   * Protege name: CANCELINSTANCE
	   */
	   private List cancelinstancE = new ArrayList();
	   public void addCANCELINSTANCE(RFQ elem) { 
	     cancelinstancE.add(elem);
	   }
	   public boolean removeCANCELINSTANCE(RFQ elem) {
	     boolean result = cancelinstancE.remove(elem);
	     return result;
	   }
	   public void clearAllCANCELINSTANCE() {
	     cancelinstancE.clear();
	   }
	   public Iterator getAllCANCELINSTANCE() {return cancelinstancE.iterator(); }
	   public List getCANCELINSTANCE() {return cancelinstancE; }
	   public void setCANCELINSTANCE(List l) {cancelinstancE = l; }

}
