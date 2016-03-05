package parser_core;

import java.util.ArrayList;

public class Vector {
	
	private String id;
	private ArrayList< Float > vectorArray;
	
	public Vector(String id, ArrayList< Float > al) {
		this.id = new String(id);
		this.vectorArray = new ArrayList< Float >(al);
	}
	
	public Vector(String id, double p, double q, int samples) {
		this.id = new String(id);
		this.vectorArray = new ArrayList< Float >(samples);
		double jump = Math.abs(q - p) / (samples-1);
		
		for(int i = 0; i < samples; i++) {
			this.vectorArray.add((float)(p + i*jump));
		}
	}
	
	public String giveId() {
		return id;
	}
	
	public ArrayList< Float > giveArray() {
		return vectorArray;
	}
}
