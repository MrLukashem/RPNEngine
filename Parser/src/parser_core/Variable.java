package parser_core;

public class Variable {
	
	private String id;
	private double value;
	
	public Variable(String id, double value) {
		this.id = new String(id);
		this.value = value;
	}
	
	public String giveId() {
		return id;
	}
	
	public double giveValue() {
		return value;
	}
	
	public void newValue(double __new) {
		value =__new;
	}
}