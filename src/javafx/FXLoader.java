package javafx;

import java.io.File;

import file.Document;
import spellcheck.SpellChecker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXLoader extends Application {
	
	private SpellChecker spellChecker = new SpellChecker();
	
	private VBox root;
	
	private Scene mainScene;
	
	private HBox hBox = new HBox();
	
	public static final double SCENE_WIDTH = 1000.0;
	
	public static final double SCENE_HEIGHT = 800.0;

	public static void main(String[] args) { Application.launch(args); }
	
	private MenuBar buildMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu();
		fileMenu.setText("File");
		
		MenuItem load = new MenuItem();
		load.setText("Load");
		load.setOnAction(event -> {
			hBox.getChildren().add(spellChecker.getDocument().getDocumentView());
		});
		
		Menu projectMenu = new Menu();
		projectMenu.setText("Project");
		
		MenuItem loadRef = new MenuItem();
		loadRef.setText("Load Reference");
		loadRef.setOnAction(event -> {
			spellChecker.loadReference();
			spellChecker.getReference().loadList();
			spellChecker.getReference().buildView();
			hBox.getChildren().add(spellChecker.getReference().getReferenceView());
		});
		
		MenuItem loadDoc = new MenuItem();
		loadDoc.setText("Load Document");
		loadDoc.setOnAction(event -> {
			spellChecker.loadDocument();
			//spellChecker.getDocument().load();
			spellChecker.getDocument().buildView();
			hBox.getChildren().add(spellChecker.getDocument().getDocumentView());
		});
		
		MenuItem spellCheck = new MenuItem();
		spellCheck.setText("Spell Check");
		spellCheck.setOnAction(event -> {
			spellChecker.loadMisspelledSet();
			spellChecker.buildView();
			hBox.getChildren().add(spellChecker.getMisspelledView());
		});
		
		fileMenu.getItems().addAll(load);
		projectMenu.getItems().addAll(loadRef, loadDoc, spellCheck);
		menuBar.getMenus().addAll(fileMenu, projectMenu);
		return menuBar;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new VBox();
		root.getChildren().addAll(buildMenuBar(), hBox);
		
//		spellChecker.getReference().getReferenceView().prefHeightProperty().bind(hBox.heightProperty());
//		hBox.prefHeightProperty().bind(root.heightProperty());
		
		mainScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		primaryStage.setScene(mainScene);
		primaryStage.show();		
	}

}

