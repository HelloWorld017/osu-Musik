package com.He.W.onebone.Circuit.Cu.exception;

public class ParseException extends Exception{
	private static final long serialVersionUID = 2997898579090794698L;
	
	public static final int UPDATE = -4;
	public static final int TOO_OLD = -3;
	public static final int NO_FILE = -2;
	public static final int WRONG_FILE = -1;
	public static final int UNKNOWN = 0;
	
	private int type;
	private String str;
	
	public ParseException(int type){
		this.type = type;
		switch(type){
			case 0: str = "Unknown Error";break;
			case -1: str = "Wrong File";break;
			case -2: str = "File doesn't exists.";break;
			case -3: str = "File is too old to read.";break;
			case -4: str = "Please update MagViewer!";break;
		}
	}
	
	public ParseException(int type, String str){
		this.type = type;
		this.str = str;
	}
	
	public ParseException(String str){
		this.str = str;
	}
	
	public ParseException(Exception e) {
		this.type = 0;
		this.str = e.toString();
	}

	public int getType(){
		return type;
	}
	
	public String toString(){
		return str;
	}
}
