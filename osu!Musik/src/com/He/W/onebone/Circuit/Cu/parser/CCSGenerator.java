package com.He.W.onebone.Circuit.Cu.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public class CCSGenerator {
		public static BufferedWriter genCCS(BufferedWriter bw, TreeMap<String, TreeMap<String, String>> map, boolean closeBW) throws IOException{
			
			//-------------Declaration-------------
			Iterator<Entry<String, TreeMap<String, String>>> i = map.entrySet().iterator();
			Iterator<Entry<String, String>> tempIterator;
			Entry<String, TreeMap<String, String>> tempEntry;
			Entry<String, String> tempEntry2;
			
			//-------------Writing CCS-------------
			bw.write("MagViewer Setting File v" + CCSParser.MAX_FILE_VERSION);
		
			
			while(i.hasNext()){
				tempEntry = i.next();
				
				bw.newLine();
				bw.append("[" + tempEntry.getKey() + "]");
				//[Content]
				
				tempIterator = tempEntry.getValue().entrySet().iterator();
				
				while(tempIterator.hasNext()){
					tempEntry2 = tempIterator.next();
					bw.newLine();
					bw.append(tempEntry2.getKey() + "=" + tempEntry2.getValue());
					//Key=Value
				}
				
				bw.newLine();
				bw.append("[/]");
			}
			bw.flush();
			if(closeBW){
				bw.close();
			}
			
			i = null;
			tempIterator = null;
			tempEntry = null;
			tempEntry2 = null;
			
			return bw;
		}
}
