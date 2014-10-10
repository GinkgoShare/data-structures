package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Document {

	private String fileName;
	
	private File file;
	
	private ObservableList<String> paragraphList;
	
	private ListView<String> documentView;

	public Document(File file) {
		this.file = file;
	}
	
	public void load() {
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
	
	public void buildView() {	
		double listViewWidth = 800.0;
		documentView = new ListView<String>(paragraphList);
		documentView.setPrefWidth(listViewWidth);	
	}
	
	public String getFileName() { return fileName; }
	
	public File getFile() { return file; }
	
	public ObservableList<String> getLineList() { return FXCollections.unmodifiableObservableList(paragraphList); }
	
	public ListView<String> getDocumentView() { return documentView; }

}
