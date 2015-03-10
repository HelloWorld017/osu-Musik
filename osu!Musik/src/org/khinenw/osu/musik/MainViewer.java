package org.khinenw.osu.musik;

import java.io.File;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewer extends Application{
	
	public Stage stage;
	private	GridPane root;
	private Label currentlyPlaying;
	private HBox toolkit;
	private Button prev, play, pause, stop, next, info, library;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		//==========load font==========
		Font icon = Font.loadFont(ClassLoader.getSystemResourceAsStream("/resources/icon.ttf"), 12);
		Font fixedsys = Font.loadFont(ClassLoader.getSystemResourceAsStream("/resource/fixedsys.ttf"), 12);
		
		//==========constraints==========
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setFillHeight(true);
		rowConstraints.setVgrow(Priority.ALWAYS);
		
		//==========root==========
		root = new GridPane();
		root.getColumnConstraints().add(columnConstraints);
		root.getRowConstraints().add(rowConstraints);
		root.getStyleClass().add("background");
		
		//==========toolKit==========
		toolkit = new HBox();
		toolkit.getStyleClass().add("background");
		
		//==========prev==========
		prev = new Button("\uE097");
		prev.setOnAction((e) -> {
			MaiNgine.prevTrack();
		});
		toolkit.getChildren().add(prev);
		
		//==========play==========
		play = new Button("\uE093");
		play.setOnAction((e) -> {
			if(MaiNgine.currentPlayer != null){
				if(MaiNgine.currentPlayer.getStatus() == Status.PAUSED || MaiNgine.currentPlayer.getStatus() == Status.STOPPED){
					MaiNgine.currentPlayer.play();
				}else{
					MaiNgine.playFromStart();
				}
				
			}else{
				MaiNgine.playSelected(0, false);
			}
		});
		toolkit.getChildren().add(play);
		
		//==========pause==========
		pause = new Button("\uE092");
		pause.setOnAction((e) -> {
			try{
				MaiNgine.currentPlayer.pause();
			}catch(NullPointerException | IllegalStateException ex){}
		});
		toolkit.getChildren().add(pause);
		
		//==========stop==========
		stop = new Button("\uE099");
		stop.setOnAction((e) -> {
			try{
				MaiNgine.currentPlayer.stop();
			}catch(NullPointerException | IllegalStateException ex){}
		});
		toolkit.getChildren().add(stop);
		
		//==========next==========
		next = new Button("\uE098");
		next.setOnAction((e) -> {
			MaiNgine.nextTrack();
		});
		toolkit.getChildren().add(next);
		
		//==========info==========
		
		
		//==========toolKit buttons==========
		toolkit.getChildren().forEach((v) -> {
			try{
				Button b = (Button)v;
				b.getStyleClass().clear();
				b.getStyleClass().add("toolkitButton");
				if(b != info){
					b.setFont(icon);
				}else{
					b.setFont(fixedsys);
				}
			}catch(ClassCastException e){}
		});
	}
	
	public File openChooseOsuDir(){
		FileChooser fc = new FileChooser();
		
	    ExtensionFilter TextFile = new ExtensionFilter("Text File", "*.txt");
	    ExtensionFilter AllFiles = new ExtensionFilter("All Files", "*.*");
	    fc.getExtensionFilters().add(TextFile);
	    fc.getExtensionFilters().add(AllFiles);
	    File returnVal = fc.showOpenDialog(stage);

	    return returnVal;
	}
	
	public void refreshStatus(){
		
	}
	
	public static void main(String args[]){
		launch(args);
	}
	/*
	u+E09B - 3 vertical lines
	u+E093 - play
	u+E092 - pause
	u+E099 - stop
	u+E098 - next track
	u+E097 - prev track
	0x69 - information (fixedsys)
	 */
}
