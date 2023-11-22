package visual.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import visual.Main;
import visual.interfaces.CommonFuntionalities;

public class MainSceneController extends AnchorPane implements Initializable, CommonFuntionalities {

	private Main application;
	
	private double mouseDragOffsetX;
	private double mouseDragOffsetY;
	
	
	@FXML private Label title1;
	@FXML private Label title2;
	@FXML private Hyperlink hyperOption;
	
	@FXML private Button btnHouses;
	@FXML private Button btnConsumption;
	@FXML private Button btnAccountant;
	@FXML private Button btnReceipt;
	@FXML private Button btnReport;
	
	public void onPressedMouse(MouseEvent e){
		mouseDragOffsetX = e.getSceneX();
        mouseDragOffsetY = e.getSceneY();
	}
	
	public void onMouseDragged(MouseEvent e){
		application.getStage().setX(e.getScreenX()-mouseDragOffsetX);
		application.getStage().setY(e.getScreenY()-mouseDragOffsetY);
	}
	
	public void setApp(Main app){
		this.application = app;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void setLanguage(){
		HashMap<String, String> map = application.getHashLanguageMap();
		
		btnHouses.setText(map.get("btn_houses"));
		btnConsumption.setText(map.get("btn_consumption"));
		btnAccountant.setText(map.get("btn_accountant"));
		btnReceipt.setText(map.get("btn_receipt"));
		btnReport.setText(map.get("btn_report"));
		hyperOption.setText(map.get("btn_options"));
	}

	@Override
	public void goToHomePane(ActionEvent e) {
		System.out.println("Estamos procesando una ida hacia la ventana inicial");
	}
	
	@Override
	public void goToHousesPane(ActionEvent e) {
		System.out.println("Go to houses");
		application.goToHouses(true);		
	}

	@Override
	public void goToConsumptionsPane(ActionEvent e) {
		application.goToConsumptions(true);		
	}

	@Override
	public void goToAccountantsPane(ActionEvent e) {
		application.goToAccountants(true);		
	}

	@Override
	public void goToReportsPane(ActionEvent e) {
		application.goToReports(true);
	}

	@Override
	public void goToOptionsPane(ActionEvent e) {
		application.goToOptions(true);
	}

	public void goToReceiptPane(ActionEvent e) {
		application.goToReceipts(true);
	}
	
	
	@Override
	public void goToHelpPane(ActionEvent e) {
		;
	}
	
	public void minimizeWindow(ActionEvent e){
		application.getStage().setIconified(true);
	}
	
	
	public void closeWindow(ActionEvent e){
		Platform.exit();
	}

}
