package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Cores
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultCores implements Cores {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultCores() {
    this._internalInstanceName = "";
  }

  public DefaultCores(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: NODECOUNT
   */
   private int nodecounT;
   public void setNODECOUNT(int value) { 
    this.nodecounT=value;
   }
   public int getNODECOUNT() {
     return this.nodecounT;
   }

   /**
   * Protege name: DURATION
   */
   private String duratioN;
   public void setDURATION(String value) { 
    this.duratioN=value;
   }
   public String getDURATION() {
     return this.duratioN;
   }

   /**
   * Protege name: NODECORES
   */
   private int nodecoreS;
   public void setNODECORES(int value) { 
    this.nodecoreS=value;
   }
   public int getNODECORES() {
     return this.nodecoreS;
   }

   /**
   * Protege name: TOTALCORES
   */
   private int totalcoreS;
   public void setTOTALCORES(int value) { 
    this.totalcoreS=value;
   }
   public int getTOTALCORES() {
     return this.totalcoreS;
   }

   /**
   * Protege name: RESOURCE
   */
   private Resource resourcE;
   public void setRESOURCE(Resource value) { 
    this.resourcE=value;
   }
   public Resource getRESOURCE() {
     return this.resourcE;
   }

}
