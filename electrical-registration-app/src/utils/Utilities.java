package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Utilities {
	private static HashMap<String, Integer> month;
	
	public static Integer getMonthInteger(String key){
		if(month == null){
			month = new HashMap<String, Integer>();
			month.put("Enero", 1);
			month.put("Febrero", 2);
			month.put("Marzo", 3);
			month.put("Abril", 4);
			month.put("Mayo", 5);
			month.put("Junio", 6);
			month.put("Julio", 7);
			month.put("Agosto", 8);
			month.put("Septiembre", 9);
			month.put("Octubre", 10);
			month.put("Noviembre", 11);
			month.put("Diciembre", 12);
		}
		
		return month.get(key);
	}
	
	public static boolean isEmpty(String text){
		int i = 0;
		
		while(i < text.length() && text.charAt(i) == ' ')
			i++;
		
		return i == text.length() ? true : false;
	}
	
	public static String getFileExtension(String path){
		path = path.toLowerCase();
		ArrayList<Character> chars = new ArrayList<Character>();
		boolean exited = false;
		for(int i = path.length() - 1; i >= 0 && !exited; i--){
			char c = path.charAt(i);
			if(c != '.'){
				chars.add(0, c);
			}
			else
				exited = true;
		}
		
		String extCad = "";
		for(Character c :chars)
			extCad += c;
		
		return extCad;
	}
}
