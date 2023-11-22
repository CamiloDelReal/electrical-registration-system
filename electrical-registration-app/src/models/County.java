package models;

import java.util.Iterator;
import java.util.List;

public class County {
	private int id;
	private String name;
	private boolean cancelado;
	
	public County(int id, String name, boolean cancelado) {
		this.id = id;
		this.name = name;
		this.cancelado = cancelado;
	}

	public String toString(){		
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public static County find(List<County> items, String name) {
		County county = null;
		Iterator<County> it = items.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			county = it.next();
			if(name.equals(county.getName()))
				found = true;
		}
		
		return found ? county : null;
	}
	
	

	
	

}
