package uk.ac.ucl.chem.ccs.ramp.user;

import jade.core.AID;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import uk.ac.ucl.chem.ccs.ramp.rfq.onto.Offer;

public class OfferList<E> extends Vector {

	public OfferList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OfferList(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public OfferList(Collection arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public OfferList(int arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public boolean addSorted (E o) {
		ReceivedOffer thisOffer = (ReceivedOffer)o;
		
		//compare this offer to each in the vector, and put in place
		if (isEmpty()) {
			add(o);
			return true;
		} else {
			//do a simple iteration of list and add element at correct position
			
			int count = 0;
			
			Iterator<ReceivedOffer> it = iterator();
			 while (it.hasNext()) {
				 ReceivedOffer ro = it.next();
				 
				 if (RequestEvaluator.naiveEvaluator(thisOffer.getOffer(), ro.getOffer())) {
					 break;
				 } else {
					 count++;
				 }
					
			 }
			 //count should be the position to insert this offer
			 add(count,o);
			return true;
		}
		
		//return false;
	}
	
}
