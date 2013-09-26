package uk.ac.ucl.chem.ccs.ramp.rfq.manualonto;

public class RFQ implements jade.content.Concept {

	 private static final long serialVersionUID = -1439939741134496089L;

	  private String _internalInstanceName = null;

	  public RFQ() {
	    this._internalInstanceName = "";
	  }

	  public RFQ(String instance_name) {
	    this._internalInstanceName = instance_name;
	  }

	  public String toString() {
	    return _internalInstanceName;
	  }

	  private String PUHOURCOST;
	  private String NOTBEFORE;
	  private String DEADLINE;
	  private String OPERATINGSYSTEM;
	  private String OSVERSION;
	  private String ARCHITECTURE;
	  private String CPUSPEED;
	  private String DURATION;
	  private String INTERNODEBANDWIDTH;
	  private String RAMPERCORE;
	  private String NODEDISKSPACE;
	  private String TOTALDISKSPACE;
	  private int TOTALCORES;
	  private int NODECORES;
	  private int NODECOUNT;
	  private String REQUESTID;

	public String getPUHOURCOST() {
		return PUHOURCOST;
	}

	public void setPUHOURCOST(String pUHOURCOST) {
		PUHOURCOST = pUHOURCOST;
	}

	public String getNOTBEFORE() {
		return NOTBEFORE;
	}

	public void setNOTBEFORE(String nOTBEFORE) {
		NOTBEFORE = nOTBEFORE;
	}

	public String getDEADLINE() {
		return DEADLINE;
	}

	public void setDEADLINE(String dEADLINE) {
		DEADLINE = dEADLINE;
	}

	public String getOPERATINGSYSTEM() {
		return OPERATINGSYSTEM;
	}

	public void setOPERATINGSYSTEM(String oPERATINGSYSTEM) {
		OPERATINGSYSTEM = oPERATINGSYSTEM;
	}

	public String getOSVERSION() {
		return OSVERSION;
	}

	public void setOSVERSION(String oSVERSION) {
		OSVERSION = oSVERSION;
	}

	public String getARCHITECTURE() {
		return ARCHITECTURE;
	}

	public void setARCHITECTURE(String aRCHITECTURE) {
		ARCHITECTURE = aRCHITECTURE;
	}

	public String getCPUSPEED() {
		return CPUSPEED;
	}

	public void setCPUSPEED(String cPUSPEED) {
		CPUSPEED = cPUSPEED;
	}

	public String getDURATION() {
		return DURATION;
	}

	public void setDURATION(String dURATION) {
		DURATION = dURATION;
	}

	public String getINTERNODEBANDWIDTH() {
		return INTERNODEBANDWIDTH;
	}

	public void setINTERNODEBANDWIDTH(String iNTERNODEBANDWIDTH) {
		INTERNODEBANDWIDTH = iNTERNODEBANDWIDTH;
	}

	public String getRAMPERCORE() {
		return RAMPERCORE;
	}

	public void setRAMPERCORE(String rAMPERCORE) {
		RAMPERCORE = rAMPERCORE;
	}

	public String getNODEDISKSPACE() {
		return NODEDISKSPACE;
	}

	public void setNODEDISKSPACE(String nODEDISKSPACE) {
		NODEDISKSPACE = nODEDISKSPACE;
	}

	public String getTOTALDISKSPACE() {
		return TOTALDISKSPACE;
	}

	public void setTOTALDISKSPACE(String tOTALDISKSPACE) {
		TOTALDISKSPACE = tOTALDISKSPACE;
	}

	public int getTOTALCORES() {
		return TOTALCORES;
	}

	public void setTOTALCORES(int tOTALCORES) {
		TOTALCORES = tOTALCORES;
	}

	public int getNODECORES() {
		return NODECORES;
	}

	public void setNODECORES(int nODECORES) {
		NODECORES = nODECORES;
	}

	public int getNODECOUNT() {
		return NODECOUNT;
	}

	public void setNODECOUNT(int nODECOUNT) {
		NODECOUNT = nODECOUNT;
	}

	public String getREQUESTID() {
		return REQUESTID;
	}

	public void setREQUESTID(String rEQUESTID) {
		REQUESTID = rEQUESTID;
	}
	  
	  

}
