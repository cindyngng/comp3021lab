package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	String currentNote="";
	
	Stage stage;
	
	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage=stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		//buttonLoad.setDisable(true);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileChooser= new FileChooser();
				fileChooser.setTitle("Please choose a file which contains a NoteBook object!");
				
				FileChooser.ExtensionFilter extFilter= new FileChooser.ExtensionFilter("Serialized Object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				if (file!=null){
					loadNoteBook(file);
				//	updateListView();
				}
			}
			
			
		});
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		//buttonSave.setDisable(true);
		buttonSave.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileChooser= new FileChooser();
				fileChooser.setTitle("Please select file to save!");
				
				FileChooser.ExtensionFilter extFilter= new FileChooser.ExtensionFilter("Serialized Object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				if (file!=null){
					if(noteBook.save(file.getAbsolutePath())){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Successfully saved"); 
						alert.setContentText("You file has been saved to file " + file.getName());
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
						
					};
				}
			}
			
			
		});
		Label l= new Label("Search:");
		TextField t = new TextField();
		Button b1 = new Button("Search");
		b1.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
			public void handle(ActionEvent event) {
			currentSearch = t.getText(); 
			t.setText("");
			
			
			updateListView();
			}
		});
		Button b2 = new Button("Clear Search");
		b2.setOnAction(new EventHandler<ActionEvent>() { @Override
			public void handle(ActionEvent event) { 
				currentSearch = ""; 
				t.setText("");
				updateListView();
			}
			});
		hbox.getChildren().addAll(buttonLoad, buttonSave, l,t,b1,b2);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes
		ArrayList<Folder> allFolder=noteBook.getFolders();
		for(Folder f : allFolder){
			foldersComboBox.getItems().add(f.getName());
		}
//		foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				currentNote=title;
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				Folder thisfolder=null;
				for (Folder f : noteBook.getFolders()){
					if( f.getName().equals(currentFolder)){
						thisfolder=f;
					}
				}
				Note thisnote=null;
				if(thisfolder!=null){
					for(Note n : thisfolder.getNotes()){
						if(n.getTitle().equals(title)){
							thisnote=n;
						}
					}
				}
				String content = "";
				if (thisnote!=null){
					if(thisnote instanceof TextNote){
						TextNote temp= (TextNote)thisnote;
						content= temp.getContent();
					}
				}
				textAreaNote.setText(content);

			}
		});
		
		Button addFolderBtn = new Button("Add Folder");
		addFolderBtn.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				boolean dontAdd= false;
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:"); 
				dialog.setContentText("Please enter the name you want to create:");
				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait(); 
				if (result.isPresent()){
					if(result.get().equals("")){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning"); 
						alert.setContentText("Please input a valid filename!");
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
					}else{
						for(Folder f : noteBook.getFolders()){
							if (f.getName().equals(result.get())){
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Warning"); 
								alert.setContentText("File exists!");
								alert.showAndWait().ifPresent(rs -> {
									if (rs == ButtonType.OK) {
										System.out.println("Pressed OK.");
									}
								});
								dontAdd=true;
								break;
							}
						}
						if(!dontAdd){
							noteBook.addFolder(result.get());
							foldersComboBox.getItems().add(result.get());
							currentFolder=result.get();
							foldersComboBox.getSelectionModel().select(result.get());
							updateListView();
							
						}
					}
				}
			
			}
		});
		
		Button addNoteBtn = new Button("Add Note");
		addNoteBtn.setOnAction(new EventHandler<ActionEvent>(){
			

			@Override
			public void handle(ActionEvent event) {
				if (currentFolder.equals("")|currentFolder.equals("-----")){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning"); 
					alert.setContentText("Please select a folder!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}else{
					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new Note to "+currentFolder); 
					dialog.setContentText("Please enter the name you want to create:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait(); 
					if (result.isPresent()){
						if(result.get().equals("")){
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Warning"); 
							alert.setContentText("Please input a valid name!");
							alert.showAndWait().ifPresent(rs -> {
								if (rs == ButtonType.OK) {
									System.out.println("Pressed OK.");
								}
							});
						}else{
							if(noteBook.createTextNote(currentFolder, result.get())){
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Successful!"); 
								alert.setContentText("Note created successfully!");
								alert.showAndWait().ifPresent(rs -> {
									if (rs == ButtonType.OK) {
										System.out.println("Pressed OK.");
									}
								});
								updateListView();
								
							}
						}
					}
					
				}
			}
			
			
		});
		
		HBox wrapper = new HBox();
		wrapper.setSpacing(10);
		wrapper.getChildren().add(foldersComboBox);
		wrapper.getChildren().add(addFolderBtn);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(wrapper);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNoteBtn);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		Folder thisfolder=null;
		for (Folder f : noteBook.getFolders()){
			if( f.getName().equals(currentFolder)){
				thisfolder=f;
			}
		}
		
		if(thisfolder!=null){
			if(currentSearch==""){
				for(Note n : thisfolder.getNotes()){
					list.add(n.getTitle());
				}
			}else{
				List<Note> searchedNotes=noteBook.searchNotes(currentSearch);
				for(Note searchedNote : searchedNotes){
					for(Note n : thisfolder.getNotes()){
						if(searchedNote.getTitle().equals(n.getTitle())){
							list.add(n.getTitle());
						}
					}
				}
			}
		}
		
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		Button saveNoteBtn = new Button("Save Note");
		saveNoteBtn.setOnAction( e->{
			if(this.currentFolder.equals("")||this.currentFolder.equals("-----")||this.currentNote.equals("")){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning"); 
				alert.setContentText("Please select a folder and note!");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
						System.out.println(this.currentFolder+this.currentNote);
					}
				});
			}else{
				
				for(Folder f:noteBook.getFolders()){
					if(f.getName().equals(currentFolder)){
						for(Note n:f.getNotes()){
							if(n.getTitle().equals(currentNote)){
								TextNote textn=(TextNote)n;
								textn.setContent(textAreaNote.getText());
								break;
							}
							
						}
						break;
					}
				}
				
			}
		});
		
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);
		Button deleteNoteBtn = new Button("Delete Note");
		deleteNoteBtn.setOnAction( e->{
			if(this.currentFolder.equals("")||this.currentFolder.equals("-----")||this.currentNote.equals("")){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning"); 
				alert.setContentText("Please select a folder and note!");
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
						System.out.println(this.currentFolder+this.currentNote);
					}
				});
			}else{
				
				for(Folder f:noteBook.getFolders()){
					if(f.getName().equals(currentFolder)){
						f.removeNotes(currentNote);
						this.updateListView();
						break;
					}
				}
				
			}
		});
		HBox wrapper = new HBox();
		wrapper.getChildren().addAll(saveView,saveNoteBtn,deleteView,deleteNoteBtn);
		wrapper.setSpacing(10);
		grid.add(wrapper, 0, 1);
		grid.add(textAreaNote, 0, 2);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
//		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
//		nb.createTextNote("COMP3021", "course information",
//				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
//		nb.createTextNote("COMP3021", "Lab requirement",
//				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");
//
//		nb.createTextNote("Books", "The Throwback Special: A Novel",
//				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
//		nb.createTextNote("Books", "Another Brooklyn: A Novel",
//				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");
//
//		nb.createTextNote("Holiday", "Vietnam",
//				"What I should Bring? When I should go? Ask Romina if she wants to come");
//		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
//		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

	private void loadNoteBook(File file){
		NoteBook nb = new NoteBook(file.getAbsolutePath());
		noteBook = nb;
		
		foldersComboBox.getItems().clear();
		ArrayList<Folder> tempfolders=noteBook.getFolders();
		for (Folder f : tempfolders){
			foldersComboBox.getItems().add(f.getName());
		}
		
	}
}