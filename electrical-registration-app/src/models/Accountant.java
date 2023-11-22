package models;

import java.util.Iterator;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Accountant {
	private IntegerProperty idAccountant;
	private StringProperty county;
	private IntegerProperty experience;
	private LongProperty ci;
	private StringProperty lastName;
	private StringProperty name;
	private BooleanProperty cancelado;
	
	public String toString(){
		String fullName = name.getValue() +" "+ lastName.getValue(); 
		return fullName;
	}
		
	public Accountant(int idAccountat, String country, int experience, long id, String lastName,
			String name, boolean cancelado) {
		this.idAccountant = new SimpleIntegerProperty(idAccountat);
		this.county = new SimpleStringProperty(country);
		this.experience = new SimpleIntegerProperty(experience);
		this.ci = new SimpleLongProperty(id);
		this.lastName = new SimpleStringProperty(lastName);
		this.name = new SimpleStringProperty(name);
		this.cancelado = new SimpleBooleanProperty(cancelado);
		
	}

	public int getId() {
		return idAccountant.getValue();
	}

	public void setId(IntegerProperty idAccountant) {
		this.idAccountant = idAccountant;
	}

	public String getCounty() {
		return county.getValue();
	}

	public void setCounty(StringProperty county) {
		this.county = county;
	}

	public int getExperience() {
		return experience.getValue();
	}

	public void setExperience(IntegerProperty experience) {
		this.experience = experience;
	}

	public long getCi() {
		return ci.getValue();
	}

	public void setCi(LongProperty idNumber) {
		this.ci = idNumber;
	}

	public String getLastName() {
		return lastName.getValue();
	}

	public void setLastName(StringProperty lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public boolean isCancelado() {
		return cancelado.getValue();
	}

	public void setCancelado(BooleanProperty cancelado) {
		this.cancelado = cancelado;
	}
	
	public static Accountant find(List<Accountant> list, String name){
		Accountant acc = null;
		Iterator<Accountant> it = list.iterator();
		boolean found = false;
		
		while(it.hasNext() && !found){
			acc = it.next();
			if(name.equals(acc.getName() + " " + acc.getLastName()))
				found = true;
		}
		
		return found ? acc : null;
	}

}
