package com.He.W.onebone.Circuit.Cu.parser;
	
	import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.He.W.onebone.Circuit.Cu.exception.ParseException;

public class CCSParser {
	
	public static int MAX_FILE_VERSION = 1;
	public static int MIN_FILE_VERSION = 0;
		
	public static TreeMap<String, TreeMap<String, String>> parseCCS(BufferedReader br) throws ParseException{
		try{
			
			String vTag = br.readLine();
			if(!vTag.startsWith("MagViewer Setting File v")){
				throw new ParseException(ParseException.WRONG_FILE);
			}
			int version = Integer.parseInt(vTag.replace("MagViewer Setting File v", ""));
			
			if(version < MIN_FILE_VERSION){
				throw new ParseException(ParseException.TOO_OLD);
			}
			
			if(version > MAX_FILE_VERSION){
				throw new ParseException(ParseException.UPDATE);
			}
			
			String filecontent = "";
			String buffer = "";
			while((buffer = br.readLine()) != null){
				filecontent += (buffer + "\n");
			}
			TreeMap<String, TreeMap<String, String>> contents = new TreeMap<String, TreeMap<String, String>>();
			TreeMap<String, String> attributes = new TreeMap<String, String>();
			
			Pattern startPattern = Pattern.compile("\\[([^/]+)\\]"); //[something] supports any unicodes
			Matcher startMatcher = startPattern.matcher(filecontent);
			
			Pattern endPattern = Pattern.compile("\\[/\\]"); //[/]
			Matcher endMatcher = endPattern.matcher(filecontent);
			
			while(startMatcher.find()){
				int start = startMatcher.start();
				int end = startMatcher.end();
					
				String tmp = filecontent.substring(start, end);
				String tag = tmp.substring(1, tmp.length() - 1);
	
				endMatcher.find();
				int tagBlockEnd = endMatcher.start();
				String tagBlock = filecontent.substring(end, tagBlockEnd);
				//Line terminator has 3 type = \r\n, \n, \r (CRLF, LF, CR). So I used Scanner.
				Scanner stringScanner = new Scanner(tagBlock);
				while(stringScanner.hasNextLine()){
					String s = stringScanner.nextLine();
					if(s.startsWith("//"))continue;
					if(s.equals(""))continue;
					String[] data = s.split("=");
					String mergedText = "";
					for(int i = 1; i < data.length; i++){
						mergedText = mergedText +  data[i];
					}
					attributes.put(data[0], mergedText);
				}
				
				stringScanner.close();
				contents.put(tag.toLowerCase(Locale.ENGLISH), attributes);
				attributes = new TreeMap<String, String>();
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
