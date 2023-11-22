package visual.controller;

import java.io.File;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import models.House;
import models.Receipt;
import services.HouseServices;
import services.ReceiptServices;
import utils.CustomTableColumn;
import utils.CustomTableView;
import visual.Main;
import visual.dialogs.DialogBox;

public class ReceiptContentController extends AnchorPane implements Initializable{
	private Main main;
	
	public void setMain(Main main){
		this.main = main;
	}
	
	//Componentes de texto
	@FXML private Label title1;
	@FXML private Label title2;
	@FXML private Label lblMonth;
	@FXML private Label lblYear;
	
	//Componentes de edicion
	@FXML private TextField txfPath;
	@FXML private TextField txfMonth;
	@FXML private TextField txfYear;
	@FXML private CustomTableView<Receipt> tableReceipt;
	private CustomTableColumn<Receipt, String> tcHogar;
	private CustomTableColumn<Receipt, Integer> tcConsumption;
	private CustomTableColumn<Receipt, Double> tcPay;
	private FileChooser chooser;
	private File path;
	
	//Botones
	@FXML private Button btnPath;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tcHogar = new CustomTableColumn<Receipt, String>("Hogar");
		tcHogar.setPercentWidth(60);
		tcHogar.setCellValueFactory(new PropertyValueFactory<Receipt, String>("hogar"));
		
		tcConsumption = new CustomTableColumn<Receipt, Integer>("Consumo");
		tcConsumption.setPercentWidth(20);
		tcConsumption.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("consumption"));
		
		tcPay = new CustomTableColumn<Receipt, Double>("Pago");
		tcPay.setPercentWidth(20);
		tcPay.setCellValueFactory(new PropertyValueFactory<Receipt, Double>("pay"));
		
		tableReceipt.getTableView().getColumns().addAll(tcHogar, tcConsumption, tcPay);
		
		
		chooser = new FileChooser();
		chooser.setTitle("Seleccionar fichero de recibos");
        chooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("rcb", "*.rcb")
        		);
        
        btnPath.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e){
        		DialogInsert diag = new DialogInsert();
        		diag.setMain(main);
        		diag.show();
        	}
        });
        
        
        fillTable();
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		title1.setText(map.get("receipt_title_1"));
		title2.setText(map.get("receipt_title_2"));
		lblMonth.setText(map.get("month"));
		lblYear.setText(map.get("year"));
		btnPath.setText(map.get("btn_find"));
		tcHogar.setText(map.get("column_house"));
		tcConsumption.setText(map.get("column_consumption"));
		tcPay.setText(map.get("column_pay"));
		chooser.setTitle(map.get("file_chooser"));
	}
	
	public void fillTable(){
		tableReceipt.getTableView().getItems().clear();
		
		List<Receipt> items = null;
		try {
			items = ReceiptServices.getReceipts();
			tableReceipt.getTableView().getItems().addAll(items);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findReceiptFile(ActionEvent e){
		System.out.println("dfdgf");
	}
	
	class DialogInsert extends VBox{    	
		private StackPane dialogPane;
		private Main main;
		public void show(){
	    	this.main.getStackLayout().getChildren().add(dialogPane);
	    }
	    
	    public void setSize(double width, double height){
	    	setMinSize(width, height);
	        setPrefSize(width, height);
	        setMaxSize(width, height);
	    }
	    
	    public void setMain(Main main){
	    	this.main = main;
	    }
	        
	        public DialogInsert(){
	        	dialogPane = new StackPane();
		    	dialogPane.setId("dialogRoot");
		    	
		    	
		        setId("dialogo");
		        setMinSize(450, 170);
		        setPrefSize(450, 170);
		        setMaxSize(450, 170);
		        setSpacing(8);
		        
		        final ComboBox<House> combo = new ComboBox<House>();
		        List<House> houseList = null;
				try {
					houseList = HouseServices.getEnabledHouses();
					
					combo.getItems().clear();//removeAll(cbxHouses.getItems());
					combo.getItems().addAll(houseList);
				} catch (SQLException e1) {
					DialogBox diag = new DialogBox(e1.getMessage());
					diag.setMain(main);				
					diag.show();
				}
		        
		        HBox b1= new HBox();
		        b1.getChildren().addAll(
		        		new Label("Hogar"),combo
		        		);
		        
		        
		        final TextField consumo = new TextField();
		        HBox b2 = new HBox();
		        b2.getChildren().addAll(new Label("Consumo"), consumo);
		        
		        final TextField month = new TextField();
		        HBox b3 = new HBox();
		        b3.getChildren().addAll(new Label("Mes"), month);
		        
		        final TextField year = new TextField();
		        HBox b4 = new HBox();
		        b4.getChildren().addAll(new Label("Año"), year);
		        
		        Button accept = new Button("Insertar");
		        accept.setDefaultButton(true);
		        accept.setMinSize(70, 20);
		        accept.setPrefSize(70, 20);
		        accept.setMaxSize(70, 20);
				accept.setOnAction(new EventHandler<ActionEvent>(){
		        	public void handle(ActionEvent e){
		        		House h = combo.getSelectionModel().getSelectedItem();
		        		
		        		try {
							ReceiptServices.insertReceipt(h.getIdHouse(), Integer.valueOf(consumo.getText()), Integer.valueOf(month.getText()), Integer.valueOf(year.getText()));
							
							DialogBox diag = new DialogBox("Se ha insertado correctamente");
							diag.setMain(main);
							diag.show();
							
							fillTable();
						} catch (NumberFormatException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        		
		        		dialogPane.getChildren().remove(DialogInsert.this);
		        		if(dialogPane.getChildren().isEmpty())
		        			dialogPane.setVisible(false);
		        		main.getStackLayout().getChildren().remove(dialogPane);
		        	}
		        }); 
		        
		        Button close = new Button("Cerrar");
		        close.setDefaultButton(true);
		        close.setMinSize(70, 20);
		        close.setPrefSize(70, 20);
				close.setMaxSize(70, 20);
		        close.setOnAction(new EventHandler<ActionEvent>(){
		        	public void handle(ActionEvent e){
		        		dialogPane.getChildren().remove(DialogInsert.this);
		        		if(dialogPane.getChildren().isEmpty())
		        			dialogPane.setVisible(false);
		        		main.getStackLayout().getChildren().remove(dialogPane);
		        	}
		        });
		        
		        HBox b5 = new HBox();
		        b5.getChildren().addAll(accept, close);
		        
		        getChildren().addAll(
		        		new Label("Insertar Recibo"),
		        		b1,b2,b3,b4, b5
		        		);
		        
		        dialogPane.getChildren().add(this);
	        }
	}

}
