package models;


import java.sql.Date;
import java.util.Calendar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ElectricReading {
	private IntegerProperty idHouse;
	private Date date;
	private StringProperty dateString;
	private IntegerProperty endingConsumption;
	private IntegerProperty initialConsumption;
	
	public ElectricReading(Date date, int initialConsumption, int endingConsumption,
			int idHouse) {
		this.date = date;
		Calendar cal = getCalendarOfDate();
		/*this.dateString = new SimpleStringProperty((date.getDay() + 1) + "-" + 
												   (date.getMonth() + 1 ) + "-" +
												   (date.getYear() + 1900) );*/
		this.dateString = new SimpleStringProperty(cal.get(Calendar.DAY_OF_MONTH) + "-" + 
				(cal.get(Calendar.MONTH) + 1) + "-" +
				cal.get(Calendar.YEAR) );
		this.endingConsumption = new SimpleIntegerProperty(endingConsumption);
		this.initialConsumption = new SimpleIntegerProperty(initialConsumption);
		this.idHouse = new SimpleIntegerProperty(idHouse);
	}


	public int getIdHouse() {
		return idHouse.getValue();
	}
	public void setIdHouse(IntegerProperty idHouse) {
		this.idHouse = idHouse;
	}


	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@SuppressWarnings("deprecation")
	public Calendar getCalendarOfDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, date.getYear() + 1900);
		cal.set(Calendar.MONTH, date.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, date.getDate());
		return cal;
	}


	public String getDateString() {
		return dateString.getValue();
	}
	public void setDateString(StringProperty dateString) {
		this.dateString = dateString;
	}


	public int getEndingConsumption() {
		return endingConsumption.getValue();
	}
	public void setEndingConsumption(IntegerProperty endingConsumption) {
		this.endingConsumption = endingConsumption;
	}


	public int getInitialConsumption() {
		return initialConsumption.getValue();
	}
	public void setInitialConsumption(IntegerProperty initialConsumption) {
		this.initialConsumption = initialConsumption;
	}
	

}
