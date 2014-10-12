package spellcheck;

import java.io.File;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import document.Dictionary;
import document.Document;

public class TextProcessor {
	
	private Dictionary dictionary;
	
	private Document document;
	
	private int wordCount;
	
	private ObservableList<String> misspelledWords;
	
	public TextProcessor() { 
		wordCount = 0;
	}
	
	public void loadDictionary() {
		dictionary = new Dictionary("Dictionary");
		dictionary.load(new File("Main.txt"));
	}
	
	public void loadDocument(File file, String title) {
		document = new Document(title);
		document.load(file);
	}
	
	public void loadmisspelledWords() {
		if (misspelledWords == null) {
			TreeSet<String> tempSet = new TreeSet<>();
			long startTime = System.currentTimeMillis();
			for (String line : document.getParagraphList()) {
				String[] words = line.split("[^a-zA-Z']");
				for (String word : words) {
					++wordCount;
					if ( ! dictionary.lookup(word))
						tempSet.add(word);
				}
			}
			misspelledWords = FXCollections.observableArrayList(tempSet);
			double elapseTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.out.printf("Collection Type:%s loadMisspelledSet() Word Count:%d Elapse Time:%.3f seconds\n", misspelledWords.getClass().getName(), misspelledWords.size(), elapseTime);
		}	
	}
	
	public Dictionary getDictionary() { return this.dictionary; }
	
	public Document getDocument() { return this.document; }
	
	public int getWordCount() { return wordCount; }
	
	public ObservableList<String> getMisspelledList() { return FXCollections.unmodifiableObservableList(misspelledWords); }
	
}
