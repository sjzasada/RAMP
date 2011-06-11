package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Cost
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class DefaultCost implements Cost {

  private static final long serialVersionUID = -1439939741134496089L;

  private String _internalInstanceName = null;

  public DefaultCost() {
    this._internalInstanceName = "";
  }

  public DefaultCost(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: DEADLINE
   */
   private String deadlinE;
   public void setDEADLINE(String value) { 
    this.deadlinE=value;
   }
   public String getDEADLINE() {
     return this.deadlinE;
   }

   /**
   * Protege name: NOTBEFORE
   */
   private String notbeforE;
   public void setNOTBEFORE(String value) { 
    this.notbeforE=value;
   }
   public String getNOTBEFORE() {
     return this.notbeforE;
   }

   /**
   * Protege name: CPUHOURCOST
   */
   private String cpuhourcosT;
   public void setCPUHOURCOST(String value) { 
    this.cpuhourcosT=value;
   }
   public String getCPUHOURCOST() {
     return this.cpuhourcosT;
   }

   /**
   * Protege name: CORES
   */
   private Cores coreS;
   public void setCORES(Cores value) { 
    this.coreS=value;
   }
   public Cores getCORES() {
     return this.coreS;
   }

}
