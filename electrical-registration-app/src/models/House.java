package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class House {
	private IntegerProperty idHouse;
	private StringProperty address;
	private IntegerProperty people;
	private IntegerProperty number;	
	private BooleanProperty canceled;
	private StringProperty accountant;
	
	public House(int idHouse, String address, int amountPeople, int number, boolean canceled, String accountant){
		this.idHouse = new SimpleIntegerProperty(idHouse);
		this.address = new SimpleStringProperty(address);
		this.people = new SimpleIntegerProperty(amountPeople);
		this.number = new SimpleIntegerProperty(number);
		this.canceled = new SimpleBooleanProperty(canceled);
		this.accountant = new SimpleStringProperty(accountant);
	}
	
	public String getAccountant() {
		return accountant.getValue();
	}

	public void setAccountant(StringProperty accountant) {
		this.accountant = accountant;
	}

	public Integer getIdHouse() {
		return idHouse.getValue();
	}

	public void setIdHouse(IntegerProperty idHouse) {
		this.idHouse = idHouse;
	}

	public String getAddress() {
		return address.getValue();
	}

	public void setAddress(StringProperty address) {
		this.address = address;
	}

	public Integer getPeople() {
		return people.getValue();
	}

	public void setAmountPeople(IntegerProperty amountPeople) {
		this.people = amountPeople;
	}

	public Integer getNumber() {
		return number.getValue();
	}

	public void setNumber(IntegerProperty number) {
		this.number = number;
	}

	public Boolean getCanceled() {
		return canceled.getValue();
	}

	public void setCanceled(BooleanProperty canceled) {
		this.canceled = canceled;
	}
	
	public String toString(){
		String house = "No. "+getNumber()+ " "+ "Direccion: "+ getAddress();
		return house;
	}
	
	
}
