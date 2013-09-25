package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;


import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.RFQ;
import jade.util.leap.*;

/**
* Protege name: MakeRequest
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class MakeRequest implements jade.content.AgentAction {

	  private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public MakeRequest() {
	    this._internalInstanceName = "";
	  }

	  public MakeRequest(String instance_name) {
	    this._internalInstanceName = instance_name;
	  }

	  public String toString() {
	    return _internalInstanceName;
	  }

	   /**
	   * Protege name: RFQINSTANCE
	   */
	   private List rfqinstancE = new ArrayList();
	   public void addRFQINSTANCE(RFQ elem) { 
	     rfqinstancE.add(elem);
	   }
	   public boolean removeRFQINSTANCE(RFQ elem) {
	     boolean result = rfqinstancE.remove(elem);
	     return result;
	   }
	   public void clearAllRFQINSTANCE() {
	     rfqinstancE.clear();
	   }
	   public Iterator getAllRFQINSTANCE() {return rfqinstancE.iterator(); }
	   public List getRFQINSTANCE() {return rfqinstancE; }
	   public void setRFQINSTANCE(List l) {rfqinstancE = l; }

}
