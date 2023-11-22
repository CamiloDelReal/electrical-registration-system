package visual.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import jfxtras.labs.scene.control.Spinner;
import jfxtras.labs.scene.control.SpinnerIntegerList;
import models.Accountant;
import models.House;
import services.AccountantServices;
import services.HouseServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.dialogs.DialogBox;
import visual.interfaces.InsertModifyDelete;

public class HouseContentController extends AnchorPane implements Initializable, InsertModifyDelete {
	
	//Componentes de texto
	@FXML private Label title1;
	@FXML private Label lblNumber;
	@FXML private Label lblPeople;
	@FXML private Label lblAddress;
	@FXML private Label lblAccountant;
	@FXML private Label title2;
	private Main main;
	
	//Componentes de edicion
	@FXML private GridPane gridLayout;
	private Spinner<Integer> spnNumber;
	private Spinner<Integer> spnPeople;
	@FXML private TextField txfAddress;
	@FXML private ComboBox<Accountant> cbxAccountant;
	@FXML private CustomTableView<House> tableHouses;
	private CustomTableColumn<House, Integer> tcNumber;
	private CustomTableColumn<House, String> tcAddress;
	private CustomTableColumn<House, Integer> tcPeople;
	private CustomTableColumn<House, String> tcAccountant;
	
	//Botones
	@FXML private Button btnInsert;
	@FXML private Button btnModify;
	@FXML private Button btnDelete;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		spnNumber = new Spinner<Integer>(new SpinnerIntegerList(1, 99999));
		spnNumber.setPrefSize(95, 21);
		spnNumber.setMaxSize(95, 21);
		GridPane.setConstraints(spnNumber, 1, 0);		

		spnPeople =new Spinner<Integer>(new SpinnerIntegerList(1, 30));
		spnPeople.setPrefSize(95, 21);
		spnPeople.setMaxSize(95, 21);
		GridPane.setConstraints(spnPeople, 1, 1);
		
		gridLayout.getChildren().addAll(spnNumber, spnPeople);
		

		tcNumber = new CustomTableColumn<House, Integer>("#");
		tcNumber.setPercentWidth(10);
		tcNumber.setCellValueFactory(new PropertyValueFactory<House, Integer>("number"));
		tcAddress = new CustomTableColumn<House, String>("Direccion");
		tcAddress.setPercentWidth(45);
		tcAddress.setCellValueFactory(new PropertyValueFactory<House, String>("address"));
		tcPeople = new CustomTableColumn<House, Integer>("Habitantes");
		tcPeople.setPercentWidth(15);
		tcPeople.setCellValueFactory(new PropertyValueFactory<House, Integer>("people"));
		tcAccountant = new CustomTableColumn<House, String>("Contador");
		tcAccountant.setPercentWidth(30);
		tcAccountant.setCellValueFactory(new PropertyValueFactory<House, String>("accountant"));
		tableHouses.getTableView().getColumns().addAll(tcNumber, tcAddress, tcPeople, tcAccountant);
		
		List<Accountant> accountantList = null;
		try {
			accountantList = AccountantServices.getEnlabledAccountants();
			
			cbxAccountant.getItems().clear();//removeAll(cbxAccountant.getItems());
			cbxAccountant.getItems().addAll(accountantList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		fillTable();
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		btnInsert.setText(map.get("btn_insert"));
		btnModify.setText(map.get("btn_modify"));
		btnDelete.setText(map.get("btn_delete"));
		title1.setText(map.get("house_title_1"));
		lblNumber.setText(map.get("house_number"));
		lblPeople.setText(map.get("house_people"));
		lblAddress.setText(map.get("house_address"));
		lblAccountant.setText(map.get("house_accountant"));
		title2.setText(map.get("house_title_2"));
		tcNumber.setText(map.get("table_column_number"));
		tcAddress.setText(map.get("table_column_address"));
		tcPeople.setText(map.get("table_column_people"));
		tcAccountant.setText(map.get("table_column_accountant"));
	}
	
	private void fillTable(){
		List<House> houseList = null;
		try {
			houseList = HouseServices.getEnabledHouses();
			
			tableHouses.getTableView().getItems().clear();//removeAll(tableHouses.getItems());
			tableHouses.getTableView().getItems().addAll(houseList);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void insertMethod(ActionEvent e){
		try {
			validateField();
			
			int number = spnNumber.getValue();
			int people = spnPeople.getValue();
			String address = txfAddress.getText();
			Accountant accountant = cbxAccountant.getSelectionModel().getSelectedItem();
			
			HouseServices.inserthouse(number, address, people ,accountant.getId());
			fillTable();
			
			DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_insert_house"));
			diag.setMain(main);				
			diag.show();
		} catch (ValidationErrorException | SQLException e1) {
			DialogBox diag = new DialogBox(e1.getMessage());
			diag.setMain(main);				
			diag.show();
		}
	}
	
	public void modifyMethod(ActionEvent e){
		if(btnModify.getText().equalsIgnoreCase(main.getHashLanguageMap().get("btn_modify"))){
			House house = tableHouses.getTableView().getSelectionModel().getSelectedItem();
			if(house != null){
				btnInsert.setDisable(true);
				btnModify.setText(main.getHashLanguageMap().get("btn_save"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_cancel"));
				tableHouses.setDisable(true);
				
				spnNumber.setValue(house.getNumber());
				spnPeople.setValue(house.getPeople());
				txfAddress.setText(house.getAddress());
				
				Accountant acc = Accountant.find(cbxAccountant.getItems(), house.getAccountant());
				cbxAccountant.getSelectionModel().select(acc);
				
										
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_house"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			try {
				validateField();
				
				House house = tableHouses.getTableView().getSelectionModel().getSelectedItem();
				int number = spnNumber.getValue();
				int people = spnPeople.getValue();
				String address = txfAddress.getText();
				Accountant accountant = cbxAccountant.getSelectionModel().getSelectedItem();
				
				HouseServices.modifyHouse(house.getIdHouse(), number, address, people ,accountant.getId());
				fillTable();
				
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_modify_house"));
				diag.setMain(main);				
				diag.show();
				
				btnInsert.setDisable(false);
				btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_cancel"));
				tableHouses.setDisable(false);
				
				txfAddress.setText("");
				cbxAccountant.getSelectionModel().select(null);
			} catch (ValidationErrorException | SQLException e1) {
				DialogBox diag = new DialogBox(e1.getMessage());
				diag.setMain(main);				
				diag.show();
			}
			
			
		}
	}
	
	public void deleteMethod(ActionEvent e){
		if(btnDelete.getText().equalsIgnoreCase(main.getHashLanguageMap().get("btn_delete"))){
			House house = tableHouses.getTableView().getSelectionModel().getSelectedItem();
			if(house != null){
				DialogYesNoToDeleteHouse diag = new DialogYesNoToDeleteHouse(house, main);
				diag.setMain(main);
				diag.show();
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_house"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			btnInsert.setDisable(false);
			btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
			btnDelete.setText(main.getHashLanguageMap().get("btn_delete"));
			tableHouses.setDisable(false);
			
			txfAddress.setText("");
			cbxAccountant.getSelectionModel().select(null);
		}
	}
	
	public void setMain (Main main){
		this.main = main;
	}
	
	private void validateField() throws ValidationErrorException{
		String address = txfAddress.getText();
		if(address == null || address.isEmpty() || Utilities.isEmpty(address))
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_address"));
		
		Accountant accountant = cbxAccountant.getSelectionModel().getSelectedItem();
		if(accountant == null)
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_accountant"));
		
		
	}
	
	class DialogYesNoToDeleteHouse extends VBox{
		private StackPane dialogPane;
		private House house;
		private Main main;
		
		public DialogYesNoToDeleteHouse(House house, Main main){
	    	this.house = house;
	    	dialogPane = new StackPane();
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
	    	
	    	Label tag = new Label(main.getHashLanguageMap().get("question_delete_house") + house.toString());
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
	        			HouseServices.cancelHouse(house.getIdHouse());
						
						fillTable();
						
						DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_delete_house"));
						diag.setMain(main);				
						diag.show();
					} catch (SQLException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.setMain(main);
						diag.show();
						//e1.printStackTrace();
					}
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteHouse.this);
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
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteHouse.this);
	        		if(dialogPane.getChildren().isEmpty())
	        			dialogPane.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogPane);
	        	}
	        }); 
	        
	        regButton.getChildren().addAll(spacer4,accept, spacer5, close);
	        
	        getChildren().addAll(content, separation, regButton);
	        
	        dialogPane.getChildren().add(DialogYesNoToDeleteHouse.this);
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
