package document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dictionary {
	
	private String title;
	
	private ObservableList<String> listOfWords;
	
	public Dictionary(String title) {
		this.title = title;
	}
	
	public void load(File file) {
		long startTime = System.currentTimeMillis();
		try (Scanner input = new Scanner(file)) {
			Set<String> tempSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
			while (input.hasNext()) {
				String word = input.nextLine();
				tempSet.add(word); // virtual method to add another word to the collection
			}
			listOfWords = FXCollections.observableArrayList(tempSet);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double elapseTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.printf("Collection Type:%s loadDictionary() Word Count:%d Elapse Time:%.3f seconds\n", listOfWords.getClass().getName(), listOfWords.size(), elapseTime);
	} // end loadDictionary()
	
	public boolean lookup(String word) {
		return Collections.binarySearch(listOfWords, word, String.CASE_INSENSITIVE_ORDER) >= 0;
	}
	
	public String getTitle() { return title; }
	
	public ObservableList<String> getListOfWords() { return FXCollections.unmodifiableObservableList(listOfWords); }

}
