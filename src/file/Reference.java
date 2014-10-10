package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Reference {

	private String fileName;
	
	private File file;
	
	private ObservableList<String> wordList;
	
	private ListView<String> referenceView;
	
	public Reference(File file) {
		this.file = file;
	}
	
	public void loadList() {
		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		Set<String> tempSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		while (input.hasNext()) {
			String word = input.nextLine();
			tempSet.add(word); // virtual method to add another word to the collection
		}
		wordList = FXCollections.observableArrayList(tempSet);
		input.close();
		double elapseTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.printf("Collection Type:%s loadDictionary() Word Count:%d Elapse Time:%.3f seconds\n", wordList.getClass().getName(), wordList.size(), elapseTime);
	} // end loadDictionary()
	
	public void buildView() {
		double listViewWidth = 200.0;
		double listViewHeight = 800.0;
		referenceView = new ListView<String>(wordList);
		referenceView.setPrefWidth(listViewWidth);
		referenceView.setPrefHeight(listViewHeight);
	}
	
	public String getFileName() { return fileName; }
	
	public File getFile() { return file; }
	
	public ObservableList<String> getWordList() { return FXCollections.unmodifiableObservableList(wordList); }
	
	public ListView<String> getReferenceView() { return referenceView; }

	public boolean lookup(String word) {
		return Collections.binarySearch(wordList, word, String.CASE_INSENSITIVE_ORDER) >= 0;
	}

}
