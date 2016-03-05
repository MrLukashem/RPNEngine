package parser_core;

import java.util.ArrayList;

public class Function {
	
	private String id;
	private String function;
	
	public Function(String id, String f) {
		this.id = new String(id);
		this.function = new String(f);
	}
	
	public String giveId() {
		return id;
	}
	
	public String giveFunction() {
		return function;
	}
	
	public double giveValue(double arg0) throws java.lang.IllegalArgumentException {
		try {
			double result = ParserCore.onpResult(ParserCore.toOnp(function), arg0);
			return result;
		}
		catch(java.lang.IllegalArgumentException iae) { 
			throw iae;
		}
	} 
	
	public ArrayList< Float > valuesList(double p, double q, int samples) {
		ArrayList< Float > array = null;
		
		try {
			array = ParserCore.parse(function, p, q, samples);
		} catch(Exception e ) {
			e.printStackTrace();
		}
		return array;
		
	}
}
