package uk.ac.ucl.chem.ccs.ramp.rfq.onto.impl;


import uk.ac.ucl.chem.ccs.ramp.rfq.onto.*;

/**
* Protege name: Resource
* @author OntologyBeanGenerator v4.1
* @version 2011/06/9, 15:37:32
*/
public class DefaultResource implements Resource {

  private static final long serialVersionUID = 2389062215155035045L;

  private String _internalInstanceName = null;

  public DefaultResource() {
    this._internalInstanceName = "";
  }

  public DefaultResource(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: TOTALDISKSPACE
   */
   private String totaldiskspacE;
   public void setTOTALDISKSPACE(String value) { 
    this.totaldiskspacE=value;
   }
   public String getTOTALDISKSPACE() {
     return this.totaldiskspacE;
   }

   /**
   * Protege name: RAMPERCORE
   */
   private String rampercorE;
   public void setRAMPERCORE(String value) { 
    this.rampercorE=value;
   }
   public String getRAMPERCORE() {
     return this.rampercorE;
   }

   /**
   * Protege name: INTERNODEBANDWIDTH
   */
   private String internodebandwidtH;
   public void setINTERNODEBANDWIDTH(String value) { 
    this.internodebandwidtH=value;
   }
   public String getINTERNODEBANDWIDTH() {
     return this.internodebandwidtH;
   }

   /**
   * Protege name: CPUSPEED
   */
   private String cpuspeeD;
   public void setCPUSPEED(String value) { 
    this.cpuspeeD=value;
   }
   public String getCPUSPEED() {
     return this.cpuspeeD;
   }

   /**
   * Protege name: OPERATINGSYSTEM
   */
   private String operatingsysteM;
   public void setOPERATINGSYSTEM(String value) { 
    this.operatingsysteM=value;
   }
   public String getOPERATINGSYSTEM() {
     return this.operatingsysteM;
   }

   /**
   * Protege name: ARCHITECTURE
   */
   private String architecturE;
   public void setARCHITECTURE(String value) { 
    this.architecturE=value;
   }
   public String getARCHITECTURE() {
     return this.architecturE;
   }

   /**
   * Protege name: OSVERSION
   */
   private String osversioN;
   public void setOSVERSION(String value) { 
    this.osversioN=value;
   }
   public String getOSVERSION() {
     return this.osversioN;
   }

   /**
   * Protege name: NODEDISKSPACE
   */
   private String nodediskspacE;
   public void setNODEDISKSPACE(String value) { 
    this.nodediskspacE=value;
   }
   public String getNODEDISKSPACE() {
     return this.nodediskspacE;
   }

}
