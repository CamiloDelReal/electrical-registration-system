package visual.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
import models.County;
import services.AccountantServices;
import services.CountyServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import utils.Utilities;
import utils.ValidationErrorException;
import visual.Main;
import visual.dialogs.DialogBox;
import visual.interfaces.InsertModifyDelete;

public class AccountantContentController extends AnchorPane implements
		Initializable, InsertModifyDelete {

	//Componentes de texto
	@FXML private Label title1;
	@FXML private Label lblName;
	@FXML private Label lblLastName;
	@FXML private Label lblCI;
	@FXML private Label lblExperience;
	@FXML private Label lblCounty;
	@FXML private Label lblRegAccountant;
	
	//Componentes de edicion
	@FXML private GridPane gridLayout;
	@FXML private TextField txfName;
	@FXML private TextField txfLastName;
	@FXML private TextField txfCI;
	private Spinner<Integer> spnExperience;
	@FXML private ComboBox<County> cbxCounty;
	@FXML private CustomTableView<Accountant> tableAccountant;
	private CustomTableColumn<Accountant, String> tcName;
	private CustomTableColumn<Accountant, String> tcLastName;
	private CustomTableColumn<Accountant, String> tcCI;
	private CustomTableColumn<Accountant, Integer> tcExperience;
	private CustomTableColumn<Accountant, String> tcCounty;
	
	//Botones
	@FXML private Button btnInsertCounty;
	@FXML private Button btnModifyCounty;
	@FXML private Button btnDeleteCounty;
	@FXML private Button btnInsert;
	@FXML private Button btnModify;
	@FXML private Button btnDelete;
	private Main main;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		spnExperience = new Spinner<Integer>(new SpinnerIntegerList(1, 50));
		spnExperience.setPrefSize(105, 21);
		spnExperience.setMaxSize(105, 21);
		GridPane.setConstraints(spnExperience, 1, 3);
		
		gridLayout.getChildren().add(spnExperience);
		
		tcName = new CustomTableColumn<Accountant, String>("Nombre");
		tcName.setPercentWidth(24);
		tcName.setCellValueFactory(new PropertyValueFactory<Accountant, String>("name"));
		tcLastName = new CustomTableColumn<Accountant, String>("Apellidos");
		tcLastName.setPercentWidth(24);
		tcLastName.setCellValueFactory(new PropertyValueFactory<Accountant, String>("lastName"));
		tcCI = new CustomTableColumn<Accountant, String>("CI");
		tcCI.setPercentWidth(15);
		tcCI.setCellValueFactory(new PropertyValueFactory<Accountant, String>("ci"));
		tcExperience = new CustomTableColumn<Accountant, Integer>("Experiencia");
		tcExperience.setPercentWidth(15);
		tcExperience.setCellValueFactory(new PropertyValueFactory<Accountant, Integer>("experience"));
		tcCounty = new CustomTableColumn<Accountant, String>("Municipio");
		tcCounty.setPercentWidth(21);
		tcCounty.setCellValueFactory(new PropertyValueFactory<Accountant, String>("county"));
		tableAccountant.getTableView().getColumns().addAll(tcName, tcLastName, tcCI, tcExperience, tcCounty);
	
		fillCombo();
		
		btnInsertCounty.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				DialogInsertModifyCounty diag = new DialogInsertModifyCounty(-1, "", main);
				diag.setMain(main);
				diag.show();
			}
		});
		btnModifyCounty.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				County county = cbxCounty.getSelectionModel().getSelectedItem();
				if(county != null){
					DialogInsertModifyCounty diag = new DialogInsertModifyCounty(county.getId(), county.getName(), main);
					diag.setMain(main);
					diag.show();
				}
				else{
					DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_county"));
					diag.setMain(main);				
					diag.show();
				}
			}
		});;
		btnDeleteCounty.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				County county = cbxCounty.getSelectionModel().getSelectedItem();
				if(county != null){
					DialogYesNoToDeleteCounty diag = new DialogYesNoToDeleteCounty(county, main);
					diag.setMain(main);
					diag.show();
				}
				else{
					DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_county"));
					diag.setMain(main);				
					diag.show();
				}
			}
		});;
		
		fillTable();
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		btnInsert.setText(map.get("btn_insert"));
		btnModify.setText(map.get("btn_modify"));
		btnDelete.setText(map.get("btn_delete"));
		btnInsertCounty.setText(map.get("btn_insert"));
		btnModifyCounty.setText(map.get("btn_modify"));
		btnDeleteCounty.setText(map.get("btn_delete"));
		title1.setText(map.get("accountant_title_1"));
		lblName.setText(map.get("accountant_name"));
		lblLastName.setText(map.get("accountant_lastname"));
		lblCI.setText(map.get("accountant_ci"));
		lblExperience.setText(map.get("accountant_experience"));
		lblCounty.setText(map.get("accountant_county"));
		lblRegAccountant.setText(map.get("accountant_registry"));
		tcName.setText(map.get("table_column_name"));
		tcLastName.setText(map.get("table_column_lastname"));
		tcCI.setText(map.get("table_column_ci"));
		tcExperience.setText(map.get("table_column_experience"));
		tcCounty.setText(map.get("table_column_county"));
	}
	
	private void fillCombo(){
		List<County> countryList = null;
		try {
			countryList = CountyServices.getEnablesCounty();
			
			cbxCounty.getItems().clear();
			cbxCounty.getItems().addAll(countryList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void fillTable(){
		List<Accountant> accountantList = null;
		try {
			accountantList = AccountantServices.getEnlabledAccountants();
			
			tableAccountant.getTableView().getItems().clear();//removeAll(tableAccountant.getItems());
			tableAccountant.getTableView().getItems().addAll(accountantList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertMethod(ActionEvent e){
		try {
			validateField();
			
			int experience = spnExperience.getValue();			
			String name = txfName.getText();
			String lastName = txfLastName.getText();
			String ci = txfCI.getText();			
			County county = cbxCounty.getSelectionModel().getSelectedItem();
			
			AccountantServices.insertAccountant(Long.valueOf(ci), name, lastName, experience,county.getId());
			fillTable();
			
			DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_insert_accountant"));
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
			Accountant acc = tableAccountant.getTableView().getSelectionModel().getSelectedItem();
			if(acc != null){
				btnInsert.setDisable(true);
				tableAccountant.setDisable(true);
				btnModify.setText(main.getHashLanguageMap().get("btn_save"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_cancel"));
				
				txfName.setText(acc.getName());
				txfLastName.setText(acc.getLastName());
				txfCI.setText(String.valueOf(acc.getCi()));
				spnExperience.setValue(acc.getExperience());
				
				County county = County.find(cbxCounty.getItems(), acc.getCounty());
				cbxCounty.getSelectionModel().select(county);
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_accountant"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			try {
				validateField();
				
				Accountant acc = tableAccountant.getTableView().getSelectionModel().getSelectedItem();
				int experience = spnExperience.getValue();			
				String name = txfName.getText();
				String lastName = txfLastName.getText();
				String ci = txfCI.getText();			
				County county = cbxCounty.getSelectionModel().getSelectedItem();
				
				AccountantServices.modifyAccountant(acc.getId(), Long.valueOf(ci), name, lastName, experience, county.getId());
				fillTable();
				
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_modify_accountant"));
				diag.setMain(main);				
				diag.show();
				
				btnInsert.setDisable(false);
				tableAccountant.setDisable(false);
				btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
				btnDelete.setText(main.getHashLanguageMap().get("btn_delete"));
				
				txfName.setText("");
				txfLastName.setText("");
				txfCI.setText("");
				spnExperience.setValue(1);
			} catch (ValidationErrorException | SQLException e1) {
				DialogBox diag = new DialogBox(e1.getMessage());
				diag.setMain(main);				
				diag.show();
			}
		}
	}
	
	public void deleteMethod(ActionEvent e){
		if(btnDelete.getText().equals(main.getHashLanguageMap().get("btn_delete"))){
			Accountant accountant = tableAccountant.getTableView().getSelectionModel().getSelectedItem();
			if(accountant != null){
				DialogYesNoToDeleteAccountant diag = new DialogYesNoToDeleteAccountant(accountant, main);
				diag.setMain(main);
				diag.show();
			}
			else{
				DialogBox diag = new DialogBox(main.getHashLanguageMap().get("select_accountant"));
				diag.setMain(main);				
				diag.show();
			}
		}
		else{
			btnInsert.setDisable(false);
			tableAccountant.setDisable(false);
			btnModify.setText(main.getHashLanguageMap().get("btn_modify"));
			btnDelete.setText(main.getHashLanguageMap().get("btn_delete"));
			
			txfName.setText("");
			txfLastName.setText("");
			txfCI.setText("");
			spnExperience.setValue(1);
		}
	}
	
	private void validateField() throws ValidationErrorException{
		String name = txfName.getText();
		if(name == null || name.isEmpty() || Utilities.isEmpty(name))
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_name"));
		
		String lasName = txfLastName.getText();
		if(lasName == null || lasName.isEmpty() || Utilities.isEmpty(lasName))
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_lastname"));
		
		String ci = txfCI.getText();
		if(ci == null || ci.isEmpty() || Utilities.isEmpty(ci))
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_ci"));
		else if(ci.length() != 11)
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_ci_11"));
		
		County county = cbxCounty.getSelectionModel().getSelectedItem();
		if(county == null)
			throw new ValidationErrorException(main.getHashLanguageMap().get("must_entry_county"));
	}
	
	public void setMain(Main main){
		this.main = main;
	}
	
	
	class DialogYesNoToDeleteAccountant extends VBox{
		private StackPane dialogPane;
		private Accountant accountant;
		private Main main;
		
		public DialogYesNoToDeleteAccountant(Accountant accountant, Main main){
	    	this.accountant = accountant;
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
	    	
	    	Label tag = new Label(main.getHashLanguageMap().get("question_delete_accountant") + accountant.toString());
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
	        			AccountantServices.cancelAccountant(accountant.getId());
						
						fillTable();
						
						DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_delete_accountant"));
						diag.setMain(main);				
						diag.show();
					} catch (SQLException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.setMain(main);
						diag.show();
						//e1.printStackTrace();
					}
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteAccountant.this);
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
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteAccountant.this);
	        		if(dialogPane.getChildren().isEmpty())
	        			dialogPane.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogPane);
	        	}
	        }); 
	        
	        regButton.getChildren().addAll(spacer4,accept, spacer5, close);
	        
	        getChildren().addAll(content, separation, regButton);
	        
	        dialogPane.getChildren().add(DialogYesNoToDeleteAccountant.this);
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
	
	
	class DialogInsertModifyCounty extends BorderPane {
		private StackPane dialogRoot;
		
		public static final double WIDTH = 300;
		public static final double HEIGHT = 155;
		
		private Label title;
		private HBox header;
		private Label lblName;
		private TextField txfName;
		
		private Button btnSave;
		private Button btnCancel;
		private HBox buttons;
		
		private Main main;
		
		public DialogInsertModifyCounty(final int countyId, final String name, final Main main){
			setId("dialogo");
			setMinSize(WIDTH, HEIGHT);
			setPrefSize(WIDTH, HEIGHT);
			setMaxSize(WIDTH, HEIGHT);
			setMain(main);
			
			if(countyId == -1)
				title = new Label(main.getHashLanguageMap().get("insert_county"));
			else
				title = new Label(main.getHashLanguageMap().get("modify_county"));
			title.getStyleClass().add("title");
			
			Region spacer1 = new Region();
			spacer1.setMinHeight(25);
			spacer1.setPrefHeight(25);
			spacer1.setMaxHeight(25);
			
			VBox titleBox = new VBox();
			titleBox.getChildren().addAll(spacer1, title);
			
			header = new HBox();
			header.setSpacing(15);
			header.getChildren().addAll(titleBox);
			
			setTop(header);
			
			lblName = new Label(main.getHashLanguageMap().get("name") + ": ");
			lblName.getStyleClass().add("bold");
			if(name != null)
				txfName = new TextField(name);
			else
				txfName = new TextField();
			txfName.setPrefWidth(110);
			txfName.setPromptText(main.getHashLanguageMap().get("name"));
			
			GridPane grid = new GridPane();
			grid.setTranslateX(20);
			grid.setTranslateY(20);
			ObservableList<Node> content = grid.getChildren();
			
			GridPane.setConstraints(lblName, 0, 0);
			GridPane.setMargin(lblName, new Insets(10, 5, 2, 10));
			content.add(lblName);
			GridPane.setConstraints(txfName, 1, 0);
			GridPane.setMargin(txfName, new Insets(10, 10, 2, 5));
			content.add(txfName);
			
			setCenter(grid);
			
			btnSave = new Button(main.getHashLanguageMap().get("btn_save"));
			btnSave.setMinSize(80, 20);
			btnSave.setPrefSize(80, 20);
			btnSave.setMaxSize(80, 20);
			btnSave.setDefaultButton(true);
			btnSave.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){				
					String newName = txfName.getText();
					if(newName == null || newName.isEmpty() || Utilities.isEmpty(newName)){
						DialogBox diag = new DialogBox(main.getHashLanguageMap().get("must_entry_name"));
						diag.setMain(main);
						diag.show();
					}
					else{						
						if(countyId == -1){
							try {
								CountyServices.insertCounty(txfName.getText());
								
								fillCombo();
								
								DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_insert_county"));
								diag.setMain(main);				
								diag.show();
								
								
								dialogRoot.getChildren().remove(DialogInsertModifyCounty.this);
				        		if(dialogRoot.getChildren().isEmpty())
				        			dialogRoot.setVisible(false);
				        		main.getStackLayout().getChildren().remove(dialogRoot);
							} catch (SQLException e1) {
								DialogBox diag = new DialogBox(e1.getMessage());
								diag.setMain(main);
								diag.show();
							}
						}
						else{
							try {
								CountyServices.modifyCounty(countyId, txfName.getText());
								
								fillCombo();
								
								DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_modify_county"));
								diag.setMain(main);				
								diag.show();
								
								
								dialogRoot.getChildren().remove(DialogInsertModifyCounty.this);
				        		if(dialogRoot.getChildren().isEmpty())
				        			dialogRoot.setVisible(false);
				        		main.getStackLayout().getChildren().remove(dialogRoot);
							} catch (SQLException e1) {
								DialogBox diag = new DialogBox(e1.getMessage());
								diag.setMain(main);
								diag.show();
							}
						}
						
					}
				}
			});
			
			btnCancel = new Button(main.getHashLanguageMap().get("btn_cancel"));
			btnCancel.setMinSize(80, 20);
			btnCancel.setPrefSize(80, 20);
			btnCancel.setMaxSize(80, 20);
			btnCancel.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e){
					dialogRoot.getChildren().remove(DialogInsertModifyCounty.this);
	        		if(dialogRoot.getChildren().isEmpty())
	        			dialogRoot.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogRoot);
				}
			});
			
			Region spacer2 = new Region();
			HBox.setHgrow(spacer2, Priority.ALWAYS);
			
			buttons = new HBox();
			buttons.setSpacing(7);
			buttons.getChildren().addAll(spacer2, btnSave, btnCancel);
			
			setBottom(buttons);
			
			dialogRoot = new StackPane();
			dialogRoot.setId("dialogRoot");
			dialogRoot.getChildren().add(DialogInsertModifyCounty.this);
		}
		
		public void show(){
			main.getStackLayout().getChildren().add(dialogRoot);
		}
		
		public void setMain(Main main){
			this.main = main;
		}
	}
	
	class DialogYesNoToDeleteCounty extends VBox{
		private StackPane dialogPane;
		private County county;
		private Main main;
		
		public DialogYesNoToDeleteCounty(County county, Main main){
	    	this.county = county;
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
	    	
	    	Label tag = new Label(main.getHashLanguageMap().get("question_delete_county") + county.getName());
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
	        			CountyServices.cancelCounty(county.getId());
						
						fillCombo();
						
						DialogBox diag = new DialogBox(main.getHashLanguageMap().get("good_delete_county"));
						diag.setMain(main);				
						diag.show();
					} catch (SQLException e1) {
						DialogBox diag = new DialogBox(e1.getMessage());
						diag.setMain(main);
						diag.show();
						//e1.printStackTrace();
					}
	        		
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteCounty.this);
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
	        		dialogPane.getChildren().remove(DialogYesNoToDeleteCounty.this);
	        		if(dialogPane.getChildren().isEmpty())
	        			dialogPane.setVisible(false);
	        		main.getStackLayout().getChildren().remove(dialogPane);
	        	}
	        }); 
	        
	        regButton.getChildren().addAll(spacer4,accept, spacer5, close);
	        
	        getChildren().addAll(content, separation, regButton);
	        
	        dialogPane.getChildren().add(DialogYesNoToDeleteCounty.this);
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
