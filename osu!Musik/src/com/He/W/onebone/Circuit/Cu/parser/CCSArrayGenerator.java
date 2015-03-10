package com.He.W.onebone.Circuit.Cu.parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.TreeMap;

public class CCSArrayGenerator {
	public static BufferedWriter genCCS(BufferedWriter bw, TreeMap<String, String> map, boolean closeBW) throws IOException{
			
			//-------------Writing CCS-------------
			bw.write("osu! music player library v" + CCSArrayParser.MAX_FILE_VERSION);
			map.forEach((k, v) -> {
				try{
					bw.newLine();
					bw.append("[NAME]" + k);
					bw.newLine();
					bw.append("[LOC]" + v);
				}catch(Exception e){
					e.printStackTrace();
				}
			});
			if(closeBW){
				bw.close();
			}
			
			return bw;
	}
}
