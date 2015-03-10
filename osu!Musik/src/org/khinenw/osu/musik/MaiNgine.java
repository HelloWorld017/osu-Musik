package org.khinenw.osu.musik;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import com.He.W.onebone.Circuit.Cu.exception.ParseException;
import com.He.W.onebone.Circuit.Cu.parser.CCSArrayGenerator;
import com.He.W.onebone.Circuit.Cu.parser.CCSArrayParser;
import com.He.W.onebone.Circuit.Cu.parser.CCSGenerator;
import com.He.W.onebone.Circuit.Cu.parser.CCSParser;

public class MaiNgine {
	public static HashMap<String, HashMap<String, File>> libraries = new HashMap<>();
	public static TreeMap<String, String> settings = new TreeMap<>();
	public static ArrayList<Integer> playArray;
	public static int playPointer = 0;
	public static String currentPlayingLibrary = "";
	public static int currentTrack = 0;
	public static File[] currentFiles;
	public static String[] currentTitles;
	public static Random shuffleRandom = new Random();
	public static MediaPlayer currentPlayer = null;
	private static MainViewer viewer = null;
	
	public static void initiatEngine(MainViewer arg0) throws ParseException, IOException{
		viewer = arg0;
		initiateSetting(arg0);
		addAll();
		initiateLibrary();
		setLibrary("all");
	}
	
	public static void initiateSetting(MainViewer arg0) throws ParseException, IOException{
		FileReader fr;
		try {
			fr = new FileReader(new File("Setting.ccs"));
		} catch (FileNotFoundException e) {
			File f = new File("Setting.ccs");
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			TreeMap<String, TreeMap<String, String>> content = new TreeMap<>();
			TreeMap<String, String> attr = new TreeMap<>();
			attr.put("osudir", arg0.openChooseOsuDir().getAbsolutePath());
			attr.put("shuffle", "false");
			attr.put("showcurrent", "false");
			content.put("Setting", attr);
			
			CCSGenerator.genCCS(new BufferedWriter(fw), content, true);
			
			content = null;
			
			fw.close();
			fw = null;
			
			settings = attr;
			return;
		}
		BufferedReader br = new BufferedReader(fr);
		settings = CCSParser.parseCCS(br).get("setting");
	}
	
	public static void initiateLibrary() throws ParseException, IOException{
		addAll();
		File[] ccsFiles = (new File("")).listFiles((File f) -> {
			if(f.getName().endsWith(".ccs")) return true;
			return false;
		});
		
		FileReader fr;
		BufferedReader br;
		for(File ccsLibrary : ccsFiles){
			fr = new FileReader(ccsLibrary);
			br = new BufferedReader(fr);
			HashMap<String, File> attr = new HashMap<>();
			CCSArrayParser.parseCCS(br).forEach((String k, String v) -> {
				attr.put(k, new File(v));
			});
			libraries.put(ccsLibrary.getName().replace(".ccs", ""), attr);
			fr = null;
			br = null;
		}
	}
	
	
	public static void loadFolders(File[] folders) throws IOException{
		File f = null;
		HashMap<String, File> library = new HashMap<>();
		
		for(int i = 0; i < folders.length; i++){
			f = chooseOsuMusic(folders[i]);
			if(f != null){
				String s = folders[i].getName();
				String[] split = s.split(" ");
				if(split.length > 1){
					s = "";
					for(int j = 1; j < split.length; j++){
						s += (" " + split[j]);
					}
					s = s.substring(1);
				}else{
					File[] osuFiles = folders[i].listFiles((File v) -> {
						if(v.getName().endsWith(".osu")){
							System.out.println("osu! file found : " + v.getName());
							return true;
						}
						return false;
					});
					
					FileReader fr = new FileReader(osuFiles[0]);
					BufferedReader br = new BufferedReader(fr);
					String title = "";
					String artist = "";
					String buffer = "";
					while((buffer = br.readLine()) != null){
						if(buffer.startsWith("Title")){
							String[] splits = buffer.split(":");
							buffer = "";
							for(int j = 1; j < splits.length; j++){
								buffer += splits[j];
							}
							while(s.startsWith(" ")){
								buffer = buffer.substring(1);
							}
							title = buffer;
						}
						
						if(buffer.startsWith("Artist")){
							String[] splits = buffer.split(":");
							buffer = "";
							for(int j = 1; j < splits.length; j++){
								buffer += splits[j];
							}
							while(s.startsWith(" ")){
								buffer = buffer.substring(1);
							}
							artist = buffer;
						}
					}
					
					br.close();
					fr.close();
					br = null;
					fr = null;
					
					if(artist != "" && title != ""){
						s = artist + " - " + title;
					}else{
						s = folders[i].getName();
					}
				}
				System.out.println("song folder name : " + s);
				
				System.out.println("File added : "  + s );
				library.put(s, f);
			}
		}
		
		libraries.put("all", library);

	}
	
	public static void addToLibrary(int index, String libraryName) throws IOException{
		libraries.get(libraryName).put(currentTitles[index], currentFiles[index]);
		saveLibrary(libraryName);
		refreshLibrary(libraryName);
	}
	
	public static void removeFromLibrary(int index, String libraryName) throws IOException{
		libraries.get(libraryName).remove(currentTitles[index]);
		saveLibrary(libraryName);
		refreshLibrary(libraryName);
	}
	
	public static File chooseOsuMusic(File folder) throws IOException{
		if(folder.getName() == "tutorial" | folder.getName() == "failed"){
			return null;
		}
		if(folder.isFile()) return null;
		File[] osuFiles = folder.listFiles((File v) -> {
			if(v.getName().endsWith(".osu")){
				System.out.println("osu! file found : " + v.getName());
				return true;
			}
			return false;
		});
		if(osuFiles == null){
			return null;
		}
		
		if(osuFiles.length == 0){
			return null;
		}
		File f = osuFiles[0];
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String s;
		while((s = br.readLine()) != null){
			if(s.startsWith("AudioFilename")){
				String[] splits = s.split(":");
				s = "";
				for(int i = 1; i < splits.length; i++){
					s += splits[i];
				}
				while(s.startsWith(" ")){
					s = s.substring(1);
				}
				System.out.println("music file found : " + s);
				br.close();
				return new File(folder, s);
			}
		}
		br.close();
		return null;
	}
	
	public static void refreshLibrary() throws ParseException, IOException{
		libraries = null;
		currentPlayingLibrary = "";
		currentTrack = 0;
		currentFiles = null;
		currentTitles = null;
		initiateLibrary();
		setLibrary("all");
	}
	
	public static void refreshLibrary(String libraryName){
		int backTrack = currentTrack;
		String backLibrary = currentPlayingLibrary;
		Duration backTime = null;
		
		if(currentPlayer != null){
			backTime = currentPlayer.getCurrentTime();
		}
		
		boolean isNeededBackup = false;
		
		if(currentPlayingLibrary.equals(libraryName)){
			setLibrary("all");
			isNeededBackup = true;
		}
		getSpecs(libraryName);
		
		if(isNeededBackup){
			setLibrary(backLibrary);
			currentPlayer = getSelectedTrack(backTrack);
			currentPlayer.setStartTime(backTime);
			currentPlayer.play();
		}
		//FIXME check this part
	}
	
	public static void saveLibrary(String saveLib) throws IOException{
		HashMap<String, File> lib = libraries.get(saveLib);
		TreeMap<String, String> attr = new TreeMap<>();
		lib.forEach((k, v) -> {
			attr.put(k, v.getAbsolutePath());
		});
		File f = new File(saveLib + ".ccs");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		CCSArrayGenerator.genCCS(bw, attr, true);
	}
	
	public static void addAll() throws IOException{
		File osuDir = new File(settings.get("osudir"));
		loadFolders(osuDir.listFiles());
	}
	
	public static void setLibrary(String library){
		currentPlayingLibrary = library;
		currentTrack = 0;
		getSpecs(library);
	}
	
	public static void getSpecs(String library){
		ArrayList<File> files = new ArrayList<>();
		ArrayList<String> titles = new ArrayList<>();
		libraries.get(library).forEach((k, v) -> {
			titles.add(k);
			files.add(v);
		});
		currentTitles = titles.toArray(new String[]{});
		currentFiles = files.toArray(new File[]{});
	}
	
	public static void playSelected(int index, boolean putToHistory){
		if(currentPlayer != null){
			try{
				currentPlayer.stop();
				
			}catch(IllegalStateException e){}
		}
		
		currentTrack = index;
		currentPlayer = new MediaPlayer(new Media(currentFiles[index].toURI().toString()));
		currentPlayer.setOnEndOfMedia(() -> {
			nextTrack();
		});
		if(putToHistory){
			addToHistory();
		}
		currentPlayer.play();
		statusChanged();
	}
	
	public static void playFromStart(){
		currentPlayer.stop();
		currentPlayer.setStartTime(new Duration(0));
		currentPlayer.play();
		//FIXME check this part
	}
	
	public static MediaPlayer getSelectedTrack(int index){
		MediaPlayer player = new MediaPlayer(new Media(currentFiles[index].toURI().toString()));
		player.setOnEndOfMedia(() -> {
			nextTrack();
		});
		return player;
	}
	
	public static void addToHistory(){
		playPointer++;
		if(playArray.size() <= playPointer){
			playArray.add(currentTrack);
		}else{
			playArray.set(playPointer, currentTrack);
		}
	}
	public static void nextTrack(){
		if(currentPlayer != null){
			try{
				currentPlayer.stop();
			}catch(IllegalStateException e){}
		}
		if(Boolean.parseBoolean(settings.get("shuffle"))){
			int i = currentTrack;
			while(i != currentTrack){
				i = shuffleRandom.nextInt(currentTitles.length);
			}
			currentTrack = i;
		}else{
			if(playArray.size() <= playPointer){
				currentTrack++;
			}else{
				currentTrack = playArray.get(playPointer + 1);
			}
			currentTrack++;
		}
		playSelected(currentTrack, true);
	}
	
	public static void prevTrack(){
		playPointer--;
		playSelected(playArray.get(playPointer), false);
	}
	
	public static void statusChanged(){
		//TODO update ui
		/*
		 * update library title
		 */
		viewer.refreshStatus();
	}
}
