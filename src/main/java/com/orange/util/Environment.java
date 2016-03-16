package com.orange.util;

import java.util.Map;

public class Environment {
	public Map<String, String> get(){
		return System.getenv();
	}
	
	public String get(String variableName){
		return this.get().get(variableName);
	}
}
