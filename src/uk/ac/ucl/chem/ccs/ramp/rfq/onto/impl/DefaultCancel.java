package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import jade.util.leap.*;
import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Cancel
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultCancel implements Cancel {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultCancel() {
    this._internalInstanceName = "";
  }

  public DefaultCancel(String instance_name) {
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
