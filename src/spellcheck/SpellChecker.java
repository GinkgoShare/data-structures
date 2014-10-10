package spellcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

import file.Document;
import file.Reference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class SpellChecker {
	
	private Reference reference;
	
	private Document document;
	
	private ObservableList<String> misspelledList;
	
	private ListView<String> misspelledView; // Consider moving . . . 
	
	public SpellChecker() {  }
	
	public void loadReference() {
//		FileChooser files = new FileChooser();
//		files.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
//		File file = files.showOpenDialog(new Stage());
//		this.reference = new Reference(file);
		this.reference = new Reference(new File("Main.txt"));
	}
	
	public void loadDocument() {
		FileChooser files = new FileChooser();
		files.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		File file = files.showOpenDialog(new Stage());
		this.document = new Document(file);
		document.load();
	}
	
	public void loadMisspelledSet() {
//		Pattern pattern = Pattern.compile("[^a-zA-Z']");
		TreeSet<String> tempSet = new TreeSet<>();
		long startTime = System.currentTimeMillis();
//		// TODO the document is already memory resident in the collection managed by the Document object
//		// TODO iterate through the already loaded data in the Document object
//		Scanner input = null;
//		try {
//			input = new Scanner(document.getFile());
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		input.useDelimiter(pattern);
//		while (input.hasNext()) {
//			String word = input.next();
//			if (Collections.binarySearch(reference.getWordList(), word, String.CASE_INSENSITIVE_ORDER) < 0)
//				tempSet.add(word);
//		}
		for (String line : document.getLineList()) {
			String[] words = line.split("[^a-zA-Z']");
			for (String word : words) {
				if ( ! reference.lookup(word))
					tempSet.add(word);
			}
		}
		misspelledList = FXCollections.observableArrayList(tempSet);
		double elapseTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.printf("Collection Type:%s loadMisspelledSet() Word Count:%d Elapse Time:%.3f seconds\n", misspelledList.getClass().getName(), misspelledList.size(), elapseTime);
	}
	
	public void buildView() {
		double listViewWidth = 400.0;
		misspelledView = new ListView<>(misspelledList);
		misspelledView.setPrefWidth(listViewWidth);
	}
	
	public Document getDocument() { return this.document; }
	
	public Reference getReference() { return this.reference; }
	
	public ObservableList<String> getMisspelledList() { return FXCollections.unmodifiableObservableList(misspelledList); }
	
	public ListView<String> getMisspelledView() { return this.misspelledView; }

}
