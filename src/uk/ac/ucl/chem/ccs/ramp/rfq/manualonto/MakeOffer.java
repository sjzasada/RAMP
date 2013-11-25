package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;


import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Offer;
import jade.util.leap.*;

/**
* Protege name: MakeOffer
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
//public class MakeOffer implements jade.content.Concept {
	public class MakeOffer implements jade.content.Predicate {

	  private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public MakeOffer() {
	    this._internalInstanceName = "";
	  }

	  public MakeOffer(String instance_name) {
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
