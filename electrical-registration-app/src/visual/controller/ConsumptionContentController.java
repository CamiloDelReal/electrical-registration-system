package visual.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.scene.control.Spinner;
import jfxtras.labs.scene.control.SpinnerIntegerList;
import models.ElectricReading;
import models.House;
import services.ElectricReadingServices;
import services.HouseServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.dialogs.DialogBox;
import visual.interfaces.InsertModifyDelete;

public class ConsumptionContentController extends AnchorPane implements Initializable, InsertModifyDelete  {
	//Componentes de texto
	@FXML private Label title1;
	@FXML private Label title2;
	@FXML private Label title3;
	@FXML private Label lblDate;
	@FXML private Label lblInit;
	@FXML private Label lblEnd;
	@FXML private Label lblMonth;
	@FXML private Label lblYear;
	private Main main;
	
	//Componentes de edicion
	@FXML private GridPane gridLayout;
	@FXML private GridPane gridLayout2;
	private CalendarTextField datePicker;
	private Spinner<Integer> spnInitial;
	private Spinner<Integer> spnEndding;
	@FXML private ComboBox<House> cbxHouses;
	private Spinner<String> spnMonth;
	private Spinner<Integer> spnYear;
	@FXML private CustomTableView<ElectricReading> tableConsumption;
	private CustomTableColumn<ElectricReading, String> tcDate;
	private CustomTableColumn<ElectricReading, Integer> tcInitial;
	private CustomTableColumn<ElectricReading, Integer> tcEndding;
	
	//Botones
	@FXML private Button btnInsert;
	@FXML private Button btnModify;
	@FXML private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		datePicker = new CalendarTextField();
		datePicker.setPrefSize(168, 21);
		datePicker.setMaxSize(168, 21);
		GridPane.setConstraints(datePicker, 1, 0);
		
		spnInitial = new Spinner<Integer>(new SpinnerIntegerList(1, 1000000));
		spnInitial.setPrefSize(105, 21);
		spnInitial.setMaxSize(105, 21);
		GridPane.setConstraints(spnInitial, 1, 1);
		
		spnEndding = new Spinner<Integer>(new SpinnerIntegerList(1, 1000000));
		spnEndding.setPrefSize(105, 21);
		spnEndding.setMaxSize(105, 21);
		GridPane.setConstraints(spnEndding, 1, 2);
		
		spnMonth = new Spinner<String>("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
		spnMonth.setPrefSize(140, 21);
		spnMonth.setMaxSize(140, 21);
		GridPane.setConstraints(spnMonth, 1, 0);
		
		spnYear = new Spinner<Integer>(new SpinnerIntegerList(2010, 2020));
		spnYear.setPrefSize(140, 21);
		spnYear.setMaxSize(140, 21);
		GridPane.setConstraints(spnYear, 1, 1);
		
		gridLayout.getChildren().addAll(datePicker, spnInitial, spnEndding);
		gridLayout2.getChildren().addAll(spnMonth, spnYear);
		
		tcDate = new CustomTableColumn<ElectricReading, String>("Fecha");
		tcDate.setPercentWidth(39);
		tcDate.setCellValueFactory(new PropertyValueFactory<ElectricReading, String>("dateString"));
		tcInitial = new CustomTableColumn<ElectricReading, Integer>("Consumo Inicial");
		tcInitial.setPercentWidth(30);
		tcInitial.setCellValueFactory(new PropertyValueFactory<ElectricReading, Integer>("initialConsumption"));
		tcEndding= new CustomTableColumn<ElectricReading, Integer>("Consumo Final");
		tcEndding.setPercentWidth(30);
		tcEndding.setCellValueFactory(new PropertyValueFactory<ElectricReading, Integer>("endingConsumption"));
		tableConsumption.getTableView().getColumns().addAll(tcDate, tcInitial, tcEndding);
		
		cbxHouses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<House>() {
			@Override
			public void changed(ObservableValue<? extends House> observable,
					House oldValue, House newValue) {
				fillTable(newValue.getIdHouse(), Utilities.getMonthInteger(spnMonth.getValue()), spnYear.getValue());
			}
			
		});
		
		spnMonth.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				House house = cbxHouses.getSelectionModel().getSelectedItem();
				if(house != null)
					fillTable(house.getIdHouse(), Utilities.getMonthInteger(newValue), spnYear.getValue());				
			}
			
		});
		
		spnYear.valueProperty().addListener(new ChangeListener<Integer>(){
			@Override
			public void changed(ObservableValue<? extends Integer> observable,
					Integer oldValue, Integer newValue) {
				House house = cbxHouses.getSelectionModel().getSelectedItem();
				if(house != null)
					fillTable(house.getIdHouse(), Utilities.getMonthInteger(spnMonth.getValue()), newValue);				
			}
			
		});
		
		List<House> houseList = null;
		try {
			houseList = HouseServices.getEnabledHouses();
			
			cbxHouses.getItems().clear();//removeAll(cbxHouses.getItems());
			cbxHouses.getItems().addAll(houseList);
		} catch (SQLException e1) {
			DialogBox diag = new DialogBox(e1.getMessage());
			diag.setMain(main);				
			diag.show();
		}
		
		//fillTable(-1, -1, -1);
		
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		title1.setText(map.get("consumption_title_1"));
		title2.setText(map.get("consumption_title_2"));
		title3.setText(map.get("consumption_title_3"));
		lblDate.setText(map.get("consumption_date"));
		lblInit.setText(map.get("consumption_initial"));
		lblEnd.setText(map.get("consumption_endding"));
		lblMonth.setText(map.get("month"));
		lblYear.setText(map.get("year"));
		tcDate.setText(map.get("date"));
		tcInitial.setText(map.get("consumption_initial"));
		tcEndding.setText(map.get("consumption_endding"));
		btnInsert.setText(map.get("btn_insert"));
		btnModify.setText(map.get("btn_modify"));
		btnDelete.setText(map.get("btn_delete"));
	}
	
	private void fillTable(int idHouse, int month, int year){
		List<ElectricReading> items = null;
		try{
			items = ElectricReadingServices.getElectricReadingByHouse(idHouse, Utilities.getMonthInteger(spnMonth.getValue()), spnYear.getValue());
			
			tableConsumption.getTableView().getItems().clear();//removeAll(tableAccountant.getItems());
			
			if(!items.isEmpty())
				tableConsumption.getTableView().getItems().addAll(items);
		} catch(SQLException e1){
			DialogBox diag = new DialogBox(e1.getMessage());
			diag.setMain(main);				
			diag.show();
		}
	}

	@SuppressWarnings("deprecation")
	public void insertMethod(ActionEvent e){
		try {
			validateField();
			
			House house = cbxHouses.getSelectionModel().getSelectedItem();
			Calendar cal = datePicker.getDateFormat().getCalendar();
			java.sql.Date date = new java.sql.Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			int initial = spnInitial.getValue();
			int endding = spnEndding.getValue();
			
			ElectricReadingServices.insertElectricReading(date, house.getIdHouse(), initial, endding);
			fillTable(house.getIdHouse(), Utilities.getMonthInteger(spnMonth.getValue()), spnYear.getValue());
			
			DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_insert_reading"));
			diag.setMain(main);				
			diag.show();
		} catch (SQLException | ValidationErrorException e1) {
			DialogBox diag = new DialogBox(e1.getMessage());
			diag.setMain(main);				
			diag.show();	
		}
	}
	
	public void modifyMethod(ActionEvent e){
		if(btnModify.getText().equalsIgnoreCase(main.getHashLanguageMap().get("btn_modify"))){
			ElectricReading elec = tableConsumption.getTableView().getSelectionModel().getSelectedItem();
			if(elec != null){
				btnInsert.setDisable(true);
				btnModify.setText(main.getHashLanguageMap().get("btn_save"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_cancel"));
				tableConsumption.setDisable(true);
				cbxHouses.setDisable(true);
				spnMonth.setDisable(true);
				spnYear.setDisable(true);
				datePicker.setDisable(true);
				
				
				datePicker.valueProperty().set(elec.getCalendarOfDate());
				spnInitial.setValue(elec.getInitialConsumption());
				spnEndding.setValue(elec.getEndingConsumption());
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_reading"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			try{
				validateField();
				
				ElectricReading elec = tableConsumption.getTableView().getSelectionModel().getSelectedItem();
				int initial = spnInitial.getValue();
				int endding = spnEndding.getValue();
				
				ElectricReadingServices.modifyElectricReading(elec.getDate(), elec.getIdHouse(), initial, endding);
				fillTable(elec.getIdHouse(), Utilities.getMonthInteger(spnMonth.getValue()), spnYear.getValue());
				
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_modify_reading"));
				diag.setMain(main);				
				diag.show();
				
				btnInsert.setDisable(false);
				btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_delete"));
				tableConsumption.setDisable(false);
				cbxHouses.setDisable(false);
				spnMonth.setDisable(false);
				spnYear.setDisable(false);
				datePicker.setDisable(false);
			} catch(SQLException | ValidationErrorException e1){
				DialogBox diag = new DialogBox(e1.getMessage());
				diag.setMain(main);				
				diag.show();
			}
		}
	}
	
	public void deleteMethod(ActionEvent e){
		if(btnDelete.getText().equalsIgnoreCase(main.getHashLanguageMap().get("btn_delete"))){
			ElectricReading elec = tableConsumption.getTableView().getSelectionModel().getSelectedItem();
			if(elec != null){
				DialogYesNoToDeleteConsumption diag = new DialogYesNoToDeleteConsumption(elec, main);
				diag.setMain(main);
				diag.show();
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_reading"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			btnInsert.setDisable(false);
			btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
			btnDelete.setText(main.getHashLanguageMap().get("btn_delete"));
			tableConsumption.setDisable(false);
			cbxHouses.setDisable(false);
			spnMonth.setDisable(false);
			spnYear.setDisable(false);
			datePicker.setDisable(false);
		}
	}
	
	private void validateField() throws ValidationErrorException{
		House house = cbxHouses.getSelectionModel().getSelectedItem();
		if(house == null)
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_house"));
		
		int initial = spnInitial.getValue();
		int endding = spnEndding.getValue();
		
		if(initial > endding)
			throw new ValidationErrorException(main.getHashLanguageMap().get("initial_over_finale"));
	}
	
	public void setMain (Main main){
		this.main = main;
	}
	
	class DialogYesNoToDeleteConsumption extends VBox{
		private StackPane dialogPane;
		private Main main;
		private ElectricReading reading;
		
		public DialogYesNoToDeleteConsumption(ElectricReading reading, Main main){
	    	dialogPane = new StackPane();
	    	this.reading = reading;
	    	dialogPane.setId("dialogRoot");
	    	setMain(main);
	    	
	        setId("dialogo");
	        setMinSize(320, 125);
	        setPrefSize(320, 125);
	        setMaxSize(320, 125);
	        
	        init();
	    }
		
		public void setMain(Main main){
			this.main = main;
		}
	    
	    private void init(){
	    	HBox content = new HBox();
	    	
	    	Label tag = new Label(main.getHashLanguageMap().get("question_delete_reading") + reading.getDateString());
	    	tag.setWrapText(true);
	    	
	    	VBox textBox = new VBox();
	    	textBox.setTranslateX(8);
	    	Region spacer1 = new Region();
	    	spacer1.setPrefHeight(15);
	    	Region spacer2 = new Region();
	    	VBox.setVgrow(spacer2, Priority.ALWAYS);
	    	
	    	textBox.getChildren().addAll(spacer1, tag, spacer2);
	    	
	    	content.getChildren().add(textBox);
	        
	        Region separation = new Region();
	        VBox.setVgrow(separation, Priority.ALWAYS);
	        
	        HBox regButton = new HBox();
	        
	        Region spacer4 = new Region();
	        HBox.setHgrow(spacer4, Priority.ALWAYS);
	        
	        Button accept = new Button(main.getHashLanguageMap().get("btn_accept"));
	        accept.setMinSize(80, 20);
	        accept.setPrefSize(80, 20);
	        accept.setMaxSize(80, 20);
	        accept.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e){
	        		try {
	        			ElectricReadingServices.deleteElectricReading(reading.getDate(), reading.getIdHouse());
	        			
						fillTable(reading.getIdHouse(), Utilities.getMonthInteger(spnMonth.getValue()), spnYear.getValue());
						
						DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_delete_reading"));
						diag.setMain(main);				
						diag.show();
					} catch (SQLException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.setMain(main);
						diag.show();
					}
	        		
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteConsumption.this);
	        		if(dialogPane.getChildren().isEmpty())
	        			dialogPane.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogPane);
	        	}
	        });
	        
	        Region spacer5 = new Region();
	        spacer5.setMinWidth(12);
	        spacer5.setPrefWidth(12);
	        spacer5.setMaxWidth(12);
	        
	        Button close = new Button(main.getHashLanguageMap().get("btn_cancel"));
	        close.setDefaultButton(true);
	        close.setMinSize(80, 20);
	        close.setPrefSize(80, 20);
			close.setMaxSize(80, 20);
			close.setTextAlignment(TextAlignment.CENTER);
	        close.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e){
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteConsumption.this);
	        		if(dialogPane.getChildren().isEmpty())
	        			dialogPane.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogPane);
	        	}
	        }); 
	        
	        regButton.getChildren().addAll(spacer4,accept, spacer5, close);
	        
	        getChildren().addAll(content, separation, regButton);
	        
	        dialogPane.getChildren().add(DialogYesNoToDeleteConsumption.this);
	    }
	    
	    public void show(){
	    	main.getStackLayout().getChildren().add(dialogPane);
	    }
	    
	    public void setSize(double width, double height){
	    	setMinSize(width, height);
	        setPrefSize(width, height);
	        setMaxSize(width, height);
	    }
	    
	}
}
