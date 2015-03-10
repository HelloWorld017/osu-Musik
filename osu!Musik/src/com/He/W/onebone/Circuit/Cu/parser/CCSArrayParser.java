package com.He.W.onebone.Circuit.Cu.parser;
	
	import java.io.BufferedReader;
	import java.io.FileNotFoundException;
	import java.util.TreeMap;

	import com.He.W.onebone.Circuit.Cu.exception.ParseException;

public class CCSArrayParser {
	
	public static int MAX_FILE_VERSION = 3;
	public static int MIN_FILE_VERSION = 2;
		
	public static TreeMap<String, String> parseCCS(BufferedReader br) throws ParseException{
		try{
			
			String vTag = br.readLine();
			if(!vTag.startsWith("osu! music player library v")){
				throw new ParseException(ParseException.WRONG_FILE);
			}
			int version = Integer.parseInt(vTag.replace("osu! music player library v", ""));
			
			if(version < MIN_FILE_VERSION){
				throw new ParseException(ParseException.TOO_OLD);
			}
			
			if(version > MAX_FILE_VERSION){
				throw new ParseException(ParseException.UPDATE);
			}
			TreeMap<String, String> contents = new TreeMap<>();
			String buffer = "";
			String name = "";
			while((buffer = br.readLine()) != null){
				if(buffer.startsWith("[NAME]")){
					name = buffer.replace("[NAME]", "");
				}
				if(buffer.startsWith("[LOC]")){
					contents.put(name, buffer.replace("[LOC]", ""));
				}
			}
			
			return contents;
		
		}catch(IllegalStateException e){
			throw new ParseException(ParseException.WRONG_FILE);
		}catch(StringIndexOutOfBoundsException e){
			throw new ParseException(ParseException.WRONG_FILE);
		}catch(ArrayIndexOutOfBoundsException e){
			throw new ParseException(ParseException.WRONG_FILE);
		}catch(NumberFormatException e){
			throw new ParseException(ParseException.WRONG_FILE);
		}catch(FileNotFoundException e ){
			throw new ParseException(ParseException.NO_FILE);
		}catch(Exception e){
			throw new ParseException(e);
		}
	}
}
