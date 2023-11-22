package visual.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import visual.Main;
import visual.dialogs.DialogBox;

public class OptionsContentController extends AnchorPane implements
		Initializable {
	
	@FXML private Label title1;
	@FXML private ComboBox<String> cbxLang;
	@FXML private Button btnSave;
	
	private Main main;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbxLang.getItems().clear();
		//Aqui no se puede inicialiar el combo porque main=null
		
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				try {
					PrintWriter pw = new PrintWriter(new File("src/visual/config.ini"));
					
					pw.println("lang=" + cbxLang.getSelectionModel().getSelectedItem());
					
					pw.close();
					
					DialogBox diag = new DialogBox(main.getHashLanguageMap().get("option_reset"));
					diag.setMain(main);
					diag.show();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
	}

	public void setMain(Main main) {
		this.main = main;
		List<String> lang = main.getLangs();
		cbxLang.getItems().addAll(lang);
		try {
			cbxLang.getSelectionModel().select(find(lang, main.currentLanguage()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String find(List<String> items, String name){
		Iterator<String> it = items.iterator();
		boolean found = false;
		String item = null;
		
		while(it.hasNext() && !found){
			item = it.next();
			if(item.equalsIgnoreCase(name))
				found = true;
		}
		
		return found ? item : null;
	}
	
	public void setLanguage(){
		HashMap<String, String> map = main.getHashLanguageMap();
		
		btnSave.setText(map.get("btn_save"));
		title1.setText(map.get("option_title"));
	}

}
