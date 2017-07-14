package  org.olerpler.SmartSubtitleGenerator.gui;

import java.util.ArrayList;

public class FancyStack<E> extends ArrayList<E> { 
	private static final long serialVersionUID = -1897415324380601228L; 

	public FancyStack() { 

	} 

	@SuppressWarnings("unchecked") 
	@Override 
	public boolean add(Object o) { 
		if(!this.contains(o)) { 
			super.add((E) o); 
			return true; 
		}  

		return false; 
	} 

	public E pop() { 
		E item = this.get(this.size()-1); 
		this.remove(this.size()-1); 

		return item; 
	} 

	public E peek() { 
		E item = this.get(this.size()-1); 

		return item; 
	} 
}
