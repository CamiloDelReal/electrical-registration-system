package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Receipt {
	private int idReceipt;
	private IntegerProperty consumption;
	private DoubleProperty pay;
	private int month;
	private int year;
	private StringProperty hogar;
	private int idHogar;
	
	public Receipt(int idReceipt, int consumption, int month, int year,	String hogar, int idHogar) {
		
		this.idReceipt = idReceipt;
		this.hogar = new SimpleStringProperty(hogar);
		this.consumption = new SimpleIntegerProperty(consumption);
		this.month = month;
		this.year = year;
		this.pay = new SimpleDoubleProperty(consumption * 0.15);
		this.idHogar = idHogar;
	}

	public int getIdHogar() {
		return idHogar;
	}

	public void setIdHogar(int idHogar) {
		this.idHogar = idHogar;
	}

	public int getIdReceipt() {
		return idReceipt;
	}

	public void setIdReceipt(int idReceipt) {
		this.idReceipt = idReceipt;
	}

	public int getConsumption() {
		return consumption.getValue();
	}

	public void setConsumption(IntegerProperty consumption) {
		this.consumption = consumption;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getHogar() {
		return hogar.getValue();
	}

	public void setHogar(StringProperty hogar) {
		this.hogar = hogar;
	}	
	
	public Double getPay(){
		return pay.getValue();
	}
	public void setPay(DoubleProperty pay){
		this.pay = pay;
	}
}
