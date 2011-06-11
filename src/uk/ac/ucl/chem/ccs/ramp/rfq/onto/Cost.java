package uk.ac.ucl.chem.ccs.ramp.rfq.onto;



/**
* Protege name: Cost
* @author OntologyBeanGenerator v4.1
* @version 2011/06/11, 21:40:02
*/
public interface Cost extends jade.content.Predicate {

   /**
   * Protege name: DEADLINE
   */
   public void setDEADLINE(String value);
   public String getDEADLINE();

   /**
   * Protege name: NOTBEFORE
   */
   public void setNOTBEFORE(String value);
   public String getNOTBEFORE();

   /**
   * Protege name: CPUHOURCOST
   */
   public void setCPUHOURCOST(String value);
   public String getCPUHOURCOST();

   /**
   * Protege name: CORES
   */
   public void setCORES(Cores value);
   public Cores getCORES();

}
