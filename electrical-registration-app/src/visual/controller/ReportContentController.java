package visual.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import reports.ReportManager;
import services.CountyServices;
import services.HouseServices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import jfxtras.labs.scene.control.Spinner;
import jfxtras.labs.scene.control.SpinnerIntegerList;
import models.County;
import models.House;
import visual.Main;
import visual.dialogs.DialogBox;

public class ReportContentController extends AnchorPane implements
		Initializable {
	private Main main;
	
	public void setMain(Main main){
		this.main = main;
	}

	//Componentes de texto
	@FXML private AnchorPane layout;
	@FXML private Label lblReport1;
	@FXML private Label lblReport2;
	@FXML private Label lblReport3;
	@FXML private Label lblReport4;
	
	//Componente de edicion
	@FXML private ComboBox<House> cbxHouses;
	private Spinner<Integer> spnValue;
	
	//Botones
	@FXML private Button btnReport1;
	@FXML private Button btnReport2;
	@FXML private Button btnReport3;
	@FXML private Button btnReport4;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		spnValue = new Spinner<Integer>(new SpinnerIntegerList(1, 1000000));
		spnValue.setPrefSize(112, 21);
		spnValue.setMaxSize(112, 21);
		AnchorPane.setTopAnchor(spnValue, Double.valueOf(169));
		AnchorPane.setLeftAnchor(spnValue, Double.valueOf(53));
		
		layout.getChildren().add(spnValue);
		
		fillCombo();

	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		lblReport1.setText(map.get("text_report_1"));
		lblReport2.setText(map.get("text_report_2"));
		lblReport3.setText(map.get("text_report_3"));
		lblReport4.setText(map.get("text_report_4"));
		btnReport1.setText(map.get("btn_generate"));
		btnReport2.setText(map.get("btn_generate"));
		btnReport3.setText(map.get("btn_generate"));
		btnReport4.setText(map.get("btn_generate"));
	}
	
	private void fillCombo(){
		List<House> houseList = null;
		try {
			houseList = HouseServices.getEnabledHouses();
			
			cbxHouses.getItems().clear();
			cbxHouses.getItems().addAll(houseList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void report1(ActionEvent e){
		System.out.println("Report 1");
		House house = cbxHouses.getSelectionModel().getSelectedItem();
		int value = spnValue.getValue();
		if(house ==null  ){
			DialogBox diag = new DialogBox("Debe seleccionar un hogar");
			diag.setMain(main);				
			diag.show();
			
		}
		else
			ReportManager.getInstance().electricReadingByHouse(house.getIdHouse(), value);
	}

	public void report2(ActionEvent e){
		System.out.println("Report 2");	
		ReportManager.getInstance().housesWithBadRealConsumption(main);
	}
	
	public void report3(ActionEvent e){
		System.out.println("Report 3");
		ReportManager.getInstance().TenHousesWithMoreRealConsumption();
	}
	
	public void report4(ActionEvent e){
		System.out.println("Report 4");
		ReportManager.getInstance().AccountantsAndReadingsByHouse(main);
	}
}
