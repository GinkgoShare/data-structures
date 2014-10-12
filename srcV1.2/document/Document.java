package document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Document {
	
	private String title;
	
	private ObservableList<String> paragraphList;
	
	public Document() {
		this.title = "Document";
	}
	
	public Document(String title) {
		this.title = title;
	}
	
	public void load(File file) {
		long startTime = System.currentTimeMillis();
		try (Scanner input = new Scanner(file)) { //??????????????????????????????????????????????
			paragraphList = FXCollections.observableList(new LinkedList<>());
			// lineList = FXCollections.observableArrayList();
			while (input.hasNext()) {
				paragraphList.add(input.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		double elapseTime = (double) (System.currentTimeMillis() - startTime) / 1000.0;
		System.out.printf("Collection Type:%s loadDocument() Line Count:%d Elapse Time:%.3f seconds\n", paragraphList.getClass().getName(), paragraphList.size(), elapseTime);
	}
	
	public boolean lookup(String word) {
		return Collections.binarySearch(paragraphList, word, String.CASE_INSENSITIVE_ORDER) >= 0;
	}
	
	public String getTitle() { return title; }
	
	public ObservableList<String> getParagraphList() { return FXCollections.unmodifiableObservableList(paragraphList); }

}
