package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;

import uk.ac.ucl.chem.ccs.ramp.rfq.manualonto.Resource;



/**
* Protege name: Cores
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public class Cores implements jade.content.Predicate {

	  private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public Cores() {
	    this._internalInstanceName = "";
	  }

	  public Cores(String instance_name) {
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
