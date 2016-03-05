package parser_core;

public class NoVarException extends Exception {
	
	public NoVarException(final String __s) {
		super(__s);
	}
	
	public NoVarException() {
		super();
	}
	
	public String what() {
		System.err.println("brak zmiennej/stalej w ciagu znakow");
		return "brak zmiennej/stalej w ciagu znakow";
	}
}
