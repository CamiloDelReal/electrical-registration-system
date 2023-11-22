package visual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Utilities;
import visual.controller.AccountantContentController;
import visual.controller.ConsumptionContentController;
import visual.controller.HouseContentController;
import visual.controller.MainSceneController;
import visual.controller.OptionsContentController;
import visual.controller.ReceiptContentController;
import visual.controller.ReportContentController;
import visual.controller.TopBarWithMenuButtonController;

public class Main extends Application{
	private StackPane stackLayout;	//Importante para poder sacar ventana de dialogo
	private BorderPane mainRoot;
	private Stage stage;
	private HashMap<String, String> langMap;

	@Override
	public void start(Stage primaryStage) throws Exception {
		langMap = new HashMap<String, String>();
		loadConfiguration();
		stackLayout = new StackPane();
		stackLayout.getStyleClass().add("doTransparent");
		stage = primaryStage;		
		/*mainRoot = new BorderPane();
		stackLayout.getChildren().add(mainRoot);*/
		
		Scene scene = new Scene(stackLayout);
		scene.getStylesheets().add(Main.class.getResource("css/style.css").toExternalForm());
		scene.setFill(null);
	    stage.setScene(scene);
		
		goToMainScene();
		
		stage.setTitle("Estamos probendo SceneBuilder");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public HashMap<String, String> getHashLanguageMap(){
		return langMap;
	}
	
	private void loadConfiguration() throws FileNotFoundException{
		File file = new File("src/visual/config.ini");
		Scanner configFile = new Scanner(file);
		
		//Cargar idioma
		String lang = configFile.nextLine();
		buildlanguageMap(lang.split("=")[1]);
		
		configFile.close();
	}
	
	
	private void buildlanguageMap(String name) throws FileNotFoundException{
		File folderLang = new File("src/visual/lang");
		String path = null;
		File file = null;
		Scanner sc = null;
		
		boolean found = false;
		
		for(int i = 0; i < folderLang.list().length && !found; i++){
			path = folderLang + "\\" + folderLang.list()[i];
			file = new File(path);
			
			if(Utilities.getFileExtension(path).equalsIgnoreCase("lang")){
				sc = new Scanner(file);
				String nameLang = sc.nextLine();
				if(name.equalsIgnoreCase(nameLang.split("=")[1]))
					found = true;
				sc.close();
			}
		}
		
		if(found){
			sc = new Scanner(file);
			
			while(sc.hasNext()){
				String[] line = sc.nextLine().split("=");
				langMap.put(line[0], line[1]);
				//System.out.println(line[0] + " -- " + line[1]);
			}
			
			sc.close();
		}
	}
	
	public List<String> getLangs(){
		List<String> lang = new LinkedList<String>();
		File folderLang = new File("src/visual/lang");
		
		for(String file : folderLang.list()){
			int index = Utilities.getFileExtension(file).length() + 1; // mas 1 por el punto
			String name = file.substring(0, file.length() - index);
			lang.add(name);
		}
		
		return lang;
	}
	
	public String currentLanguage() throws FileNotFoundException{
		File file = new File("src/visual/config.ini");
		Scanner configFile = new Scanner(file);

		String lang = configFile.nextLine();
		
		configFile.close();
		
		return lang.split("=")[1];
	}
	
	public Stage getStage(){
		return stage;
	}
	
	private void initializeBorderLayout(){
		mainRoot = new BorderPane();
		stackLayout.getChildren().removeAll(stackLayout.getChildren());
        stackLayout.getChildren().add(mainRoot);
	}
	
	
	private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }

        stackLayout.getChildren().removeAll(stackLayout.getChildren());
        stackLayout.getChildren().add(page);
        
        return (Initializable) loader.getController();
    }
	
	private Initializable replaceTopContent(String fxml) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        
        mainRoot.setTop(page);
        
        return (Initializable) loader.getController();
	}
	
	private Initializable replaceCenterContent(String fxml) throws IOException{
		FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        
        mainRoot.setCenter(page);
        
        return (Initializable) loader.getController();
	}
	
	public void goToMainScene(){
		try {
			MainSceneController pane = (MainSceneController)replaceSceneContent("fxml/MainScene.fxml");
			pane.setApp(this);
			pane.setLanguage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goToHouses(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);
				pane.setLanguage();
			}
			
			HouseContentController pane = (HouseContentController)replaceCenterContent("fxml/HouseContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void goToConsumptions(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);	
				pane.setLanguage();
			}
			
			ConsumptionContentController pane = (ConsumptionContentController)replaceCenterContent("fxml/ConsumptionContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void goToAccountants(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);
				pane.setLanguage();
			}
			
			AccountantContentController pane = (AccountantContentController)replaceCenterContent("fxml/AccountantContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void goToReceipts(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);
				pane.setLanguage();
			}
			
			ReceiptContentController pane = (ReceiptContentController)replaceCenterContent("fxml/ReceiptContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void goToReports(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);	
				pane.setLanguage();
			}
			
			ReportContentController pane = (ReportContentController)replaceCenterContent("fxml/ReportContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void goToOptions(boolean complete){
		try {
			if(complete){
				initializeBorderLayout();
				TopBarWithMenuButtonController pane = (TopBarWithMenuButtonController)replaceTopContent("fxml/TopBarWithMenuButton.fxml");
				pane.setApp(this);	
				pane.setLanguage();
			}
			
			OptionsContentController pane = (OptionsContentController)replaceCenterContent("fxml/OptionsContent.fxml");
			pane.setMain(this);
			pane.setLanguage();
			
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public StackPane getStackLayout(){
		return stackLayout;
	}
	
	
}
