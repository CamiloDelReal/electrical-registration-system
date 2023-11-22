package visual.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import visual.Main;
import visual.interfaces.CommonFuntionalities;

public class TopBarWithMenuButtonController extends AnchorPane implements Initializable, CommonFuntionalities {

	@FXML private Label title;
	@FXML private Button btnHomePane;
	@FXML private Button btnHouse;
	@FXML private Button btnConsumption;
	@FXML private Button btnAccountant;
	@FXML private Button btnReciept;
	@FXML private Button btnReport;
	@FXML private Button btnOptions;
	
	private double mouseDragOffsetX;
	private double mouseDragOffsetY;
	
	private Main main;    
    
    public void setApp(Main application){
        this.main = application;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Aqui van las inicializaciones de los componentes
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		btnHomePane.setText(map.get("btn_home"));
		btnHouse.setText(map.get("btn_houses"));
		btnConsumption.setText(map.get("btn_consumption"));
		btnAccountant.setText(map.get("btn_accountant"));
		btnReciept.setText(map.get("btn_receipt"));
		btnReport.setText(map.get("btn_report"));
		btnOptions.setText(map.get("btn_options"));
	}
	
	public void onPressedMouse(MouseEvent e){
		mouseDragOffsetX = e.getSceneX();
        mouseDragOffsetY = e.getSceneY();
	}
	
	public void onMouseDragged(MouseEvent e){
		main.getStage().setX(e.getScreenX()-mouseDragOffsetX);
		main.getStage().setY(e.getScreenY()-mouseDragOffsetY);
	}
    
	@Override
	public void goToHomePane(ActionEvent e) {
		main.goToMainScene();
	}
	
	@Override
	public void goToHousesPane(ActionEvent e){
		main.goToHouses(false);
	}
	
	@Override
	public void goToConsumptionsPane(ActionEvent e){
		main.goToConsumptions(false);
	}
	
	@Override
	public void goToAccountantsPane(ActionEvent e){
		main.goToAccountants(false);
	}
	
	public void goToReceiptPane(ActionEvent e){
		main.goToReceipts(false);
	}
	
	@Override
	public void goToReportsPane(ActionEvent e){
		main.goToReports(false);
	}
	
	@Override
	public void goToOptionsPane(ActionEvent e){
		main.goToOptions(false);
	}
	
	@Override
	public void goToHelpPane(ActionEvent e){
		System.out.println("Estamos procesando una ida hacia la ventana de ayuda");
	}
	
	
	public void minimizeWindow(ActionEvent e){
		main.getStage().setIconified(true);
	}
	
	
	public void closeWindow(ActionEvent e){
		Platform.exit();
	}

}
