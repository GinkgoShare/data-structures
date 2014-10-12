package javafx;

import java.io.File;

import spellcheck.TextProcessor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXLoader extends Application {
	
	private TextProcessor textProcessor;
	
	private BorderPane root;
	
	private Scene mainScene;
	
	private CheckMenuItem dictionaryCheckMenu;
	
	private CheckMenuItem documentCheckMenu;
	
	private CheckMenuItem spellingCheckMenu;
	
	private ToolBar toolBar;
	
	public static final double SCENE_WIDTH = 1000.0;
	
	public static final double SCENE_HEIGHT = 800.0;

	public static void main(String[] args) { Application.launch(args); }
	
	public void init() {
		textProcessor = new TextProcessor();
		textProcessor.loadDictionary();
		root = new BorderPane();
	}
	
	public void buildDocumentStatisticsWindow() {
		final double height = 200.0;
		final double width = 250.0;
		final double vGap = 10.0;
		final double hGap = 10.0;
		Text docTitle = new Text(textProcessor.getDocument().getTitle());
		Label lines = new Label("Lines");
		Text lineCount = new Text();
		lineCount.setText(Integer.toString(textProcessor.getDocument().getParagraphList().size()));
		Label words = new Label("Words");
		Text wordCount = new Text();
		wordCount.setText(Integer.toString(textProcessor.getWordCount()));
		Button close = new Button("Close");
		Button refresh = new Button("Refresh");
		
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setVgap(vGap);
		root.setHgap(hGap);
		root.add(docTitle, 0, 0);
		root.add(lines, 0, 1);
		root.add(lineCount, 1, 1);
		root.add(words, 0, 2);
		root.add(wordCount, 1, 2);
		root.add(close, 0, 3);
		root.add(refresh, 1, 3);
		
		Scene scene = new Scene(root, width, height);
		Stage stage = new Stage();
		stage.setTitle("Document Statistics");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		close.setOnAction(event -> stage.close());
		stage.show();
	}
	
	private Menu buildFileMenu() {
		Menu fileMenu = new Menu("File");
		MenuItem fileItem = new MenuItem("New");
		fileItem.setOnAction(event -> {
			root.setLeft(new ListView<>(textProcessor.getDictionary().getListOfWords()));
			dictionaryCheckMenu.setSelected(true);
			FileChooser files = new FileChooser();
			files.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = files.showOpenDialog(new Stage());
			if (file != null) {
				textProcessor.loadDocument(file, file.getName());
				root.setCenter(new ListView<String>(textProcessor.getDocument().getParagraphList()));
				documentCheckMenu.setSelected(true);
			}
			root.setRight(null);
		});
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(event -> Platform.exit());
		fileMenu.getItems().addAll(fileItem, exit);
		return fileMenu;
	}
	
	private Menu buildViewMenu() {
		Menu viewMenu = new Menu("View");
		dictionaryCheckMenu = new CheckMenuItem("Dictionary");
		dictionaryCheckMenu.setSelected(false);
		dictionaryCheckMenu.setOnAction(event -> {
			if (dictionaryCheckMenu.isSelected())
				try {
					root.setLeft(new ListView<>(textProcessor.getDictionary().getListOfWords()));
				} catch (NullPointerException e) {
					dictionaryCheckMenu.setSelected(false);
				}
			else
				root.setLeft(null);
		});
		documentCheckMenu = new CheckMenuItem("Document");
		documentCheckMenu.setSelected(false);
		documentCheckMenu.setOnAction(event -> {
			if (documentCheckMenu.isSelected())
				try {
					root.setCenter(new ListView<>(textProcessor.getDocument().getParagraphList()));
				} catch (NullPointerException e) {
					documentCheckMenu.setSelected(false);
				}
			else
				root.setCenter(null);
		});
		spellingCheckMenu = new CheckMenuItem("Misspelled Words");
		spellingCheckMenu.setSelected(false);
		spellingCheckMenu.setOnAction(event -> {
			if (spellingCheckMenu.isSelected())
				try {
					root.setRight(new ListView<>(textProcessor.getMisspelledList()));
				} catch (NullPointerException e) {
					spellingCheckMenu.setSelected(false);
				}
			else
				root.setRight(null);
		});
		viewMenu.getItems().addAll(dictionaryCheckMenu, documentCheckMenu, spellingCheckMenu);
		return viewMenu;
	}
	
	private Menu buildDocumentMenu() {
		Menu document = new Menu("Document");
		MenuItem load = new MenuItem("Load");
		load.setOnAction(event -> {
			FileChooser files = new FileChooser();
			files.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File file = files.showOpenDialog(new Stage());
			if (file != null) {
				textProcessor.loadDocument(file, file.getName());
				root.setCenter(new ListView<String>(textProcessor.getDocument().getParagraphList()));
				documentCheckMenu.setSelected(true);
			}
		});
		document.getItems().addAll(load);
		return document;
	}
	
	private Menu buildSearchMenu() { // TODO
		Menu search = new Menu("Search");
		MenuItem find = new MenuItem("Find");
		find.setOnAction(event -> root.setBottom(buildStatusBar()));
		
		search.getItems().add(find);
		return search;
	}
	
	private Menu buildToolsMenu() {
		Menu tools = new Menu("Tools");
		MenuItem checkSpelling = new MenuItem("Check Spelling");
		checkSpelling.setOnAction(event -> {
			try {
				textProcessor.loadmisspelledWords();
				ListView<String> misspelledList = new ListView<String>(textProcessor.getMisspelledList());
				
				misspelledList.setOnMouseEntered(hoverevent -> { // TODO
//					misspelledList.hoverProperty();
					misspelledList.getSelectionModel().getSelectedItem();
				});
				
				root.setRight(misspelledList);
				spellingCheckMenu.setSelected(true);
			} catch (NullPointerException e) { 
				// does nothing (TODO)
			}
		});
		MenuItem docStats = new MenuItem("Document Statistics");
		docStats.setOnAction(event -> {
			if (root.getCenter() != null) {
				if (root.getRight() == null) 
					textProcessor.loadmisspelledWords();
				buildDocumentStatisticsWindow();
			}	
		});
		tools.getItems().addAll(checkSpelling, docStats);
		return tools;
	}
	
	private MenuBar buildMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = buildFileMenu();
		Menu viewMenu = buildViewMenu();
		Menu documentMenu = buildDocumentMenu();
		Menu searchMenu = buildSearchMenu();
		Menu toolsMenu = buildToolsMenu();
		menuBar.getMenus().addAll(fileMenu, viewMenu, documentMenu, searchMenu, toolsMenu);
		return menuBar;
	}
	
	private ToolBar buildStatusBar() { // TODO
		toolBar = new ToolBar();
		TextField searchBox = new TextField();
		searchBox.setPromptText("Find in document");
		Button previous = new Button("<");
		Button next = new Button(">");
		Text closeButton = new Text(2, 4, "x");
		closeButton.setOnMouseClicked(event -> root.setBottom(null));
		
		toolBar.getItems().addAll(searchBox, previous, next, new Separator(), closeButton);
		return toolBar;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init();
		root.setTop(buildMenuBar());
//		root.setBottom(buildStatusBar());
	
		mainScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
//		mainScene.getStylesheets().add(getClass().getResource("StyleSheet.css").toExternalForm());
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Document Viewer");
		primaryStage.show();	
	}

}
