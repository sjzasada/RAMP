package uk.ac.ucl.chem.ccs.ramp.rfq.onto;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

public class RFQOntology extends Ontology {

	public static final String ONTOLOGY_NAME= "Resource-Request-Ontology";
	
	
	//VOCAB
	
	//resource
	public static final String RESOURCE = "RESOURCE";
	public static final String REQUESTID = "RequestID";
	public static final String CPUCOUNT = "CPUCount";
	
	//cost predicate
	public static final String ENDTIME = "EndTIme";
	public static final String STARTTIME = "StartTime";
	public static final String COST = "Cost";
	
	//action
	public static final String BUY = "Buy";
	public static final String BUY_ITEM = "Item";

	
	
	//singleton instance
	private static Ontology theInstance = new RFQOntology();
	
	
	//retrieve singleton
	public static Ontology getInstance () {
		return theInstance;
	}
	
	//private constructor
	private RFQOntology () {
		super (ONTOLOGY_NAME, BasicOntology.getInstance());
		
		try {
			add(new ConceptSchema(RESOURCE), Resource.class);
			
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
