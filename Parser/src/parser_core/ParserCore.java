package parser_core;

import java.text.StringCharacterIterator;
import java.util.*;

class Operator {
	private int priority;
	private String name;
	private boolean is_function = false;
	
	public static final int EXP_PRIORITY = 3;
	
	public String getName() {
		return name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public boolean isFunction() {
		return is_function;
	}
	
	public double operation(double b, double a) {
		if(name.matches("\\+")) 
			return b + a;
		else
		if(name.matches("\\-"))
			return b - a;
		else
		if(name.matches("/"))
			return b / a;
		else
		if(name.matches("\\*"))
			return b * a;
		else
			return Math.pow(b, a);
	}
	
	public Operator(char c) {
		this.name = Character.toString(c);
		if(c == '(') {
			priority = 0;
		}
		else
		if(c == ')') {
			priority = 0;
		}
		if(c == '+') {
			priority = 1;
		}
		else
		if(c == '-') {
			priority = 1;
		}
		else
		if(c == '*') {
			priority = 2;
		}
		else
		if(c == '/') {
			priority = 2;
		}
		else
		if(c == '^') {
			priority = 3;
		}
	}	
		
	public Operator(String s) {
		this.name = new String(s);
		priority = -1; //function
		is_function = true;
	}
	
}

public class ParserCore {
	
	public static ArrayList< String > toOnp(final String __s) 
		throws java.lang.IllegalArgumentException {
		Stack< Operator > operators_stack = new Stack< Operator >();
		ArrayList< String > out = new ArrayList< String >();
		char ch;
		String s = new String(__s);
		s.replaceAll("\\s", "");
		for(int i = 0; i < s.length(); i++) {
			ch = s.charAt(i);
			
			if(ch >= 0x30 && ch <= 0x39) {
				int j = i+1;
				boolean point = false;
				String _result = new String(Character.toString(ch));
				while(j < s.length()) {
					if(s.charAt(j) == '.') {
						if(point) {
							++j;
							continue;
						}
						point = true;
						_result += Character.toString(s.charAt(j));
						++j;
					}
					else
					if( (s.charAt(j) >= 0x30 && 
						 s.charAt(j) <= 0x39) ) {
						i = j;
						_result += Character.toString(s.charAt(j));
						++j;
					}
					else
						break;
				}
				
				out.add(_result);
			}
			else
			if(ch >= 'a' && ch <= 'z') {
				int j = i+1;
				String _result = new String(Character.toString(ch));
				while(j < s.length()) {
					if(s.charAt(j) >= 'a' && s.charAt(j) <= 'z') {
						i = j;
						_result += Character.toString(s.charAt(j));
						++j;
						continue;
					}
					else
					if(s.charAt(j) == '(') {
						operators_stack.push(new Operator(_result));
						break;
					}
					out.add(_result);
					break;
				}
				if(j == s.length()) {
					out.add(_result);
				}
			}
			else
			if(ch == '(') {
				operators_stack.push(new Operator(ch));
			}
			else
			if(ch == ')') {
				Operator op = operators_stack.pop();
				while(!op.getName().equals("(")) {
					out.add(op.getName());
					op = operators_stack.pop();
				}

				if(!operators_stack.empty()) {
					if(operators_stack.lastElement().isFunction())
						out.add(operators_stack.pop().getName());
				}
			}
			else 
			if(ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
				Operator op = new Operator(ch);
				
				if(i == 0) {
					out.add("0");
				}
				while(!operators_stack.empty() &&
						(op.getPriority() <= (operators_stack.lastElement().getPriority())) ) {
					if((op.getPriority() == Operator.EXP_PRIORITY) &&
							operators_stack.lastElement().getPriority() == Operator.EXP_PRIORITY)
						break;
					out.add(operators_stack.pop().getName());
				}
			
				operators_stack.push(new Operator(ch));
			}
			else {
				throw new java.lang.IllegalArgumentException("bledny format funkcji");
			}
		}
		
		while(!operators_stack.empty()) {
			out.add(operators_stack.pop().getName() );
		}
		
		return out;
	}
	
	public static double onpResult(ArrayList< String > al, double var) 
			throws java.lang.IllegalArgumentException {
		Stack< Double > stack = new Stack< Double >();
		String variable = new String();
		boolean var_saved = false;
		double a, b;
		
		for(String s : al) { 
			if(s.matches("[0-9]+\\.[0-9]+|[0-9]+")) {
				stack.push(Double.valueOf(s));
			}
			else
			if(s.matches("\\+|\\-|/|\\*|\\^")) {
				Operator op = new Operator(s.charAt(0));
				a = stack.pop();
				b = stack.pop();
				stack.push(op.operation(b, a));
			}
			else
			if(s.equals("sin") || s.equals("cos") 
					|| s.equals("tg") || s.equals("ctg") || s.equals("exp")) {
				double result = 0.0;
				if(s.equals("sin")) {
					result = Math.sin(stack.pop());
					stack.push(result);
				}
				else
				if(s.equals("cos")) {
					result = Math.cos(stack.pop());
					stack.push(result);
				}
				else
				if(s.equals("tg")) {
					result = Math.tan(stack.pop());
					stack.push(result);
				}
				else
				if(s.equals("ctg")) {
					result = 1.0 / Math.tan(stack.pop());
					stack.push(result);
				}
				else {
					result = Math.exp(stack.pop());
					stack.push(result);
				}
			}
			else
			if(s.matches("[a-zA-Z]+")) {
				if(!var_saved) {
					var_saved = true;
					variable = s;
				}
				if(!variable.equals(s)) {
					throw new java.lang.IllegalArgumentException("uzyto wiecej niz jedna zmienna");
				}
				stack.push(var);
			}
		}
		
		return stack.firstElement();
	}
	
	public static ArrayList< Float > parse(final String s, double x, double y, int n_o_s)
		throws java.lang.IllegalArgumentException {
		String var_string = s;
		double jump = Math.abs(y-x) / (n_o_s-1);
		
			ArrayList< String > onp_array = toOnp(var_string);
			ArrayList< Float > result_vector = new ArrayList< Float >(n_o_s);
			for(int i = 0; i < n_o_s; i++) {
				result_vector.add((float)onpResult(onp_array, x + i * jump));
			}
			
			return result_vector;

	}
	
	public static float parse(final String __s, double __var) 
		throws java.lang.IllegalArgumentException {
		
		ArrayList< String > _onp_array = toOnp(__s);
		float _result = (float)onpResult(_onp_array,__var);
		
		return _result;
	}
	
	public static float parse(final String __s) 
		throws java.lang.IllegalArgumentException {
		
		ArrayList< String > _onp_array = toOnp(__s);
		float _result = (float)onpResult(_onp_array, 0.0);
		
		return _result;
	}
	
	public static void main(String[] args) {
		ParserCore parserCore = new ParserCore();
		ArrayList< Float > wynik = parserCore.parse("sin(x)", 0.0, 5.0, 201);
		for( double d : wynik) {
			System.out.println(d);
		}
	}
}
