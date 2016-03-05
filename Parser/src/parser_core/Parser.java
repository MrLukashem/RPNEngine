package parser_core;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;

import Wykres.Wektor;
import Wykres.Wykres;

public class Parser {
	
	private ArrayList< Function > funcsArray = new ArrayList< Function >();
	private ArrayList< Vector > vecsArray = new ArrayList< Vector >();
	private ArrayList< Variable > varsArray = new ArrayList< Variable >();
	
	private Pattern funcPatternId = Pattern.compile("\\s+[a-zA-Z]+\\s*=");
	private Pattern funcPattern = Pattern.compile("[a-z|A-Z|0-9|\\+|\\-|\\*|\\/|\\^|\\(|\\)|\\s]+");
	private Pattern vecPatternId = Pattern.compile("\\s+[a-zA-Z]+\\s*=");
	private Pattern vecPattern = Pattern.compile("\\s*[0-9]+\\s*,\\s*[0-9]+\\s*,\\s*[0-9]+\\s*");
	static private final String __double = "((([0-9]+\\.[0-9]+)|([0-9]+))|(\\-\\s*(([0-9]+\\.[0-9]+)|([0-9]+))))";
	static private final String __int    = "([0-9]+)";
	private Pattern varPatternId = Pattern.compile("\\s+[a-zA-Z]+\\s*=");
	private Pattern varPattern = Pattern.compile("=\\s*" +__double + "\\s*");
	private Pattern drawFuncPattern = Pattern.compile("\\(\\s*[a-zA-Z]+\\s*,");
	private Pattern drawPattern = Pattern.compile("\\s*" +__double + "\\s*,\\s*" +
	__double + "\\s*,\\s*" +__int);
	private Pattern valuePatternId = Pattern.compile("\\s*[a-zA-Z]+\\s*\\(");
	private Pattern valuePattern = Pattern.compile("\\(\\s*" +__double + "\\s*");
	private final String __randn = new String("randn\\(\\s*" +__double + "\\s*,\\s*" +
			__double + "\\s*,\\s*" +__int + "\\s*\\)\\s*");
	private Pattern randnPattern = Pattern.compile(__randn);
	private Pattern randnArgs = Pattern.compile("\\(\\s*" +__double + "\\s*,\\s*" +__double +
			"\\s*,\\s*" +__int + "\\s*");
	private Pattern drawVec = Pattern.compile("\\(\\s*[a-zA-Z]+\\s*");
//	private final String __rand  = new String("rand\\(\\s*");
	private final String exit = new String("exit");
	private static final String operationString = new String("([a-zA-Z]|" +__double 
			+"|\\+|\\-|\\*|\\^|\\/|\\(|\\)|\\s)+");
	private Pattern updateVarId = Pattern.compile("[a-zA-Z]+\\s*=");
	private Pattern updateVar   = Pattern.compile("=\\s*[0-9]+");
	
	private String bladSklaniowy = "blad sklaniowy!";
	
	public boolean redefVar = false;
	// vec u = [3, 5, 10000]
	
	public ArrayList< Function > giveFunctionsArray() {
		if(funcsArray.isEmpty())
			return null;
		else
			return funcsArray;
	}
	
	public ArrayList< Vector > giveVecsArray() {
		if(vecsArray.isEmpty())
			return null;
		else
			return vecsArray;
	}
	
	public ArrayList< Variable > giveVarsArray() {
		if(varsArray.isEmpty())
			return null;
		else
			return varsArray;
	}
	
	public String analisis(String s) {
		String output = new String();
		
		if(s.isEmpty())
			return "\n	  brak polecenia";
		
		String str = new String(s);
		if(str.matches("\\s*func\\s+[a-zA-Z]+\\s*\\=[a-z|A-Z|0-9|\\+|\\-|\\*|\\/|\\^|\\(|\\)|\\s]+")) {
			String id_string = new String();
			String func_string = new String();
			
			try {
				Matcher m = funcPatternId.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
				
				m = funcPattern.matcher(str);
				while(m.find()) {
					func_string = m.group();
				}
			
				id_string = id_string.replaceAll("=|\\s", "");
				func_string = func_string.replaceAll("=|\\s", "");
			
				Function F = new Function(id_string, func_string);
				funcsArray.add(F);
			} catch(Exception iae) {
				iae.printStackTrace();
				output = "\n	" + bladSklaniowy;
			}
		}
		else
		if(str.matches("\\s*vec\\s+[a-zA-Z]+\\s*\\="+
				"\\s*\\[\\s*" +__double + "\\s*,\\s*" +__double + "\\s*,\\s*" +__int + "\\s*\\]\\s*")) {
			String id_string = new String();
			String vec_string = new String();
			String[] vec_table;
			
			try {
				Matcher m = vecPatternId.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
				
				m = vecPattern.matcher(str);
				while(m.find()) {
					vec_string = m.group();
				}
				
				id_string = id_string.replaceAll("=|\\s", "");
				vec_string = vec_string.replaceAll("\\s", "");
				vec_table = vec_string.split(",");
				
				double p, q, samples;
				p = Double.valueOf(vec_table[0]);
				q = Double.valueOf(vec_table[1]);
				samples = Double.valueOf(vec_table[2]);
				
				Vector V = new Vector(id_string, p, q, (int)samples);
				vecsArray.add(V);
			} catch(Exception iae) {
				iae.printStackTrace();
				output = "\n	" + bladSklaniowy;
			}
			
		}
		else
		if(str.matches("\\s*vec\\s+[a-zA-Z]+\\s*\\=\\s*" +__randn + "\\s*")) {
			String id_string = new String();
			String randn_string = new String();
			String[] vec_table;
			
			try {
				Matcher m = vecPatternId.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
			
				m = randnPattern.matcher(str);
				while(m.find()) {
					randn_string = m.group();
				}
			
				m = randnArgs.matcher(randn_string);
				while(m.find()) {
					randn_string = m.group();
				}
			
				id_string = id_string.replaceAll("\\s|=", "");
				randn_string = randn_string.replaceAll("\\(|\\s", "");
				vec_table = randn_string.split(",");
				System.out.println(id_string);
				double mean = Double.valueOf(vec_table[0]);
				double dev  = Double.valueOf(vec_table[1]);
				int samples = Integer.valueOf(vec_table[2]);
				ArrayList< Float > al = new ArrayList< Float >(samples);
			
				for(int i = 0; i < samples; i++) {
					al.add((float)randn(mean, dev));
				}
			
				Vector V = new Vector(id_string, al);
				vecsArray.add(V);
			} catch(Exception e) {
				e.printStackTrace();
				output = "\n	" + bladSklaniowy;
			}
		}
		else
		if(str.matches("\\s*var\\s+[a-zA-Z]+\\s*=\\s*" +__double + "\\s*")) {
			String id_string = new String();
			String var_string = new String();
			
			try {
				Matcher m = varPatternId.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
			
				m = varPattern.matcher(str);
				while(m.find()) {
					var_string = m.group();
				}
				
				id_string = id_string.replaceAll("\\s|=", "");
				var_string = var_string.replaceAll("\\s|=", "");
				System.out.println(var_string);
				Variable V = new Variable(id_string, Double.valueOf(var_string));
				varsArray.add(V);
			} catch(Exception e) {
				e.printStackTrace();
				output = "\n	" + bladSklaniowy;
			}
		}
		else //draw(f, 2, 4, 1000 )
		if(str.matches("\\s*draw\\s*\\(\\s*[a-zA-Z]+\\s*,\\s*" +__double + "\\s*,\\s*" +
		__double + "\\s*,\\s*" +__int + "\\s*\\)\\s*")) {
			String id_string = new String();
			String vec_string = new String();
			String[] vec_table;

			Matcher m = drawFuncPattern.matcher(str);
			while(m.find()) {
				id_string = m.group();
			}
			id_string = id_string.replaceAll("\\s|\\(|,", "");
			
			m = drawPattern.matcher(str);
			while(m.find()) {
				vec_string = m.group();
			}
			vec_string = vec_string.replaceAll("\\s", "");
			vec_table = vec_string.split(",");
			
			double p, q, samples;
			p = Double.valueOf(vec_table[0]);
			q = Double.valueOf(vec_table[1]);
			samples = Double.valueOf(vec_table[2]);
			for(Function f : funcsArray) {
				if(f.giveId().equals(id_string)) {
					ArrayList< Float > array = f.valuesList(p, q, (int)samples);
					Wektor w = new Wektor((float)p,(float)q);
					w.setWektor(array);
					Wykres wykres = new Wykres(w);
					
					return output = "\n	   generowanie wykresu";
				}
			}
			
			output = " :\n	 Nie ma takiej funkcji";
		}
		else
		if(str.matches("\\s*[a-zA-Z]+\\s*\\(\\s*" +__double + "\\s*\\)\\s*")) {
			String id_string = new String();
			String arg0 = new String();
			
			try {
				Matcher m = valuePatternId.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
			
				m = valuePattern.matcher(str);
				while(m.find()) {
					arg0 = m.group();
				}
			
				id_string = id_string.replaceAll("\\(|\\s", "");
				arg0 = arg0.replaceAll("\\(|\\s", "");
			
				for(Function f : funcsArray) {
					if(f.giveId().equals(id_string)) {
						output = "=" + String.valueOf(f.giveValue(Double.valueOf(arg0)));
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				output = "\n	" + bladSklaniowy;
			}
		} // draw(
		else
		if(str.matches("\\s*draw\\(\\s*[a-zA-Z]+\\s*\\)\\s*")) {
			String id_string = new String();
			
			try {
				Matcher m = drawVec.matcher(str);
				while(m.find()) {
					id_string = m.group();
				}
			
				id_string = id_string.replaceAll("\\s|\\(", "");

				for(Vector v : vecsArray) {
					if(v.giveId().equals(id_string)) {
						Wektor w = new Wektor(-5,5);
						w.setWektor(v.giveArray());
						Wykres wykr = new Wykres(w);
						return output;
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				output = bladSklaniowy;
			}
			
			output = " \n	Brak wektora";
		}
		else
		if(str.matches("\\s*clear\\s*")) {
			funcsArray.clear();
			vecsArray.clear();
			varsArray.clear();
			
			output = "skasowano dane z pamieci";
		}
		else
		if(str.matches("\\s*exit\\s*")) {
			output = exit;
		}
		else
		if(str.matches("\\s*clearw\\s*")) {
			output = "clearw";
		}
		else
		if(str.matches(operationString)) {
			String _var_id = new String();
			double _var = 0.0;
			try {
				_var_id = findVarId(str);
				for(Variable v : varsArray) {
					if(v.giveId().equals(_var_id)) {
						_var = v.giveValue();
						double _result = (double)ParserCore.parse(str,_var);
						return " :\n	" + String.valueOf(_result);
					}
				}
			} catch(NoVarException e) {
			//	e.printStackTrace();
				double _result = ParserCore.parse(str);
				String _result_str = "\n	= ";
				_result_str += String.valueOf(_result);
				return _result_str;
			} catch(java.lang.IllegalArgumentException e) {
				return " :\n	brak zmiennej w pamieci";
			} catch(Exception e) {
				e.printStackTrace();
				return " :\n	" + bladSklaniowy;
			}
			
			return " :\n	brak zmiennej w pamieci";
		}
		else //podmiana zmiennej
		if(str.matches("\\s*[a-zA-Z]+\\s*=\\s*" +__double +"\\s*")) {
			String _update_id = new String();
			String _update_var= new String();
			
			Matcher m = updateVarId.matcher(str);
			while(m.find()) {
				_update_id = m.group();
			}
			
			m = updateVar.matcher(str);
			while(m.find()) {
				_update_var = m.group();
			}
			
			_update_id =_update_id.replaceAll("\\s|=", "");
			_update_var=_update_var.replaceAll("\\s|=", "");
			
			for(Variable v : varsArray) {
				if(v.giveId().equals(_update_id)) {
					v.newValue(Double.valueOf(_update_var));
					redefVar = true;
					return " :\n	nowa wartosc zmiennej";
				}
			}
			
			return " :\n	brak zmiennej o podanej etykiecie";
		}
		else //podmiana zmiennej przez kombinacje innej lub tej samej zmiennej
		if(str.matches("\\s*[a-zA-Z]+\\s*=[a-z|A-Z|0-9|\\+|\\-|\\*|\\/|\\^|\\(|\\)|\\s]+")) {
			String _update_id = new String(); //string na etykiete zmiennej
			String _update_var= new String(); //string na wartosc funkcji przypisywanej
										      //do zmiennej
			Matcher m = updateVarId.matcher(str);
			while(m.find()) {
				_update_id = m.group();
			}
			
			m = funcPattern.matcher(str);
			while(m.find()) {
				_update_var = m.group();
			}
			
			_update_id =_update_id.replaceAll("\\s|=", "");
			_update_var=_update_var.replaceAll("\\s|=", "");
			
			for(Variable v : varsArray) { //wyszukiwanie zmiennej o podanej etykiecie
				if(v.giveId().equals(_update_id)) {
					try {
						double _var = findVar(_update_var);
						double _result = ParserCore.parse(_update_var,_var);
						
						v.newValue(_result);
						return ": \n	nowa wartosc zmiennej";
					} catch(NoVarException e) { //wyjatek findVar jezeli nie ma zmiennej w pamieci
						double _result = ParserCore.parse(_update_var);
						
						v.newValue(_result);
						return ": \n	nowa wartosc zmiennej";
					} catch(Exception e) { //reszta wyjatkow na czele z wyjatkiem ParserCore o bledzie skladniowym
						e.printStackTrace();
						return " :\n	" + bladSklaniowy;
					}
				}
			}
			
			return " :\n	brak zmiennej o podanej etykiecie";
		}
		else
			return " :\n	" + bladSklaniowy;
		
		return output;
	}
	
	private boolean checkID(final String id) {
		boolean busy_id = false;
		
		for(Function f : funcsArray) {
			if(f.giveId().equals(id))
				busy_id = true;
		}
		for(Vector v : vecsArray) {
			if(v.giveId().equals(id))
				busy_id = true;
		}
		for(Variable v : varsArray) {
			if(v.giveId().equals(id))
				busy_id = true;
		}
		
		return busy_id;
	}
	
	/*
	 * dodanie nowej zmiennej do listy
	 * @param id reprezentacja tekstowa zmiennej
	 * @param value reprezentacja liczbowa zmiennej
	 */
	public void addVariable(String id, double value) {
		varsArray.add(new Variable(id, value));
	}
	
	/*
	 * dodanie nowego wektora do listy
	 * @param id reprezentacja tekstowa wektora
	 * @param al lista liczb zawartych w wektorze
	 */
	public void addVector(String id, ArrayList< Float > al) {
		vecsArray.add(new Vector(id, al));
	}
	
	/*
	 * dodanie nowego wektora do listy
	 * @param id reprezentacja tekstowa wektora
	 * @param p lewy kraniec przedzialu wektora
	 * @param q prawy kraniec przedzialu
	 * @param samples ilosc elementow wektora
	 */
	public void addVector(String id, double p, double q, int samples) {
		vecsArray.add(new Vector(id, p, q, samples));
	}
	
	/*
	 * dodanie nowej funkcji do listy
	 * @param id reprezentacja tekstowa funkcji
	 * @param s wzor funkcji
	 */
	public void addFunction(String id, String s) {
		funcsArray.add(new Function(id, s));
	}
	
	/*
	 *	param1 wejsciowy ciag znakow - wyrazenie arytmetyczne
	 *  
	 *  funkcja wyszukuje zmiennych w wyrazeniu arytmetycznym, jesli takowe znajdzie, to
	 *  szuka czy istnieja zmienne w bazie.
	 *  
	 *  return zwraca zmienna - w przeciwnym wypadku pusty string
	 */
	public double findVar(final String __str)
		throws NoVarException {
		String _var;
		String _result = new String();
		
		for(Variable v : varsArray) {
			_var = v.giveId();
			Pattern ptr = Pattern.compile("(\\*|\\/|\\+|\\-|\\(|\\s|^)" +_var +
					"(\\*|\\/|\\+|\\-|\\)|\\s|$)");
			Matcher m = ptr.matcher(__str);
		
			while(m.find()) {
				_result = m.group();
			}
			_result =_result.replaceAll("\\s|\\*|\\/|\\+|\\-|\\(|\\)", "");
			
			if(_result.equals(v.giveId()))
				return v.giveValue();
		}
		
		throw new NoVarException();
	}
	
	public String findVarId(final String __str)
			throws NoVarException, java.lang.IllegalArgumentException {
			String _var;
			String _result = new String();
			
			for(Variable v : varsArray) {
				_var = v.giveId();
				Pattern ptr = Pattern.compile("(\\*|\\/|\\+|\\-|\\(|\\s|^)" +_var +
						"(\\*|\\/|\\+|\\-|\\)|\\s|$)");
				Matcher m = ptr.matcher(__str);
			
				while(m.find()) {
					_result = m.group();
				}
				_result =_result.replaceAll("\\s|\\*|\\/|\\+|\\-|\\(|\\)", "");
				
				if(_result.equals(v.giveId()))
					return v.giveId();
			}
			
			Pattern ptr = Pattern.compile("(\\*|\\/|\\+|\\-|\\(|\\s|^)" + "[a-zA-Z]+" +
						"(\\*|\\/|\\+|\\-|\\)|\\s|$)");
			Matcher m = ptr.matcher(__str);
			
			while(m.find()) {
				_result = m.group();
			}
			_result =_result.replaceAll("\\s|\\*|\\/|\\+|\\-|\\(|\\)", "");
			
			if(_result.matches("[a-zA-Z]+")) {
				throw new java.lang.IllegalArgumentException();
			}
			
			throw new NoVarException();
		}
	// operator..spacje... zmienna ...spacje..operator
	
	
	public double rand(double x, double y) {
		Random _random = new Random();
		return (y *_random.nextDouble() + x);
	}
	
	public double randn(double mean, double dev) {
		Random _random = new Random();
		return (dev *_random.nextGaussian() + mean);
	}
	
	public static void main(String[] args) throws NoVarException {
		String str = new String("213-sin(x) +23-x");
		Parser e = new Parser();
		System.out.println(e.findVar(str));
	}
}

/*
 * func - funkcja
 * func u = x^2
 * 
 * var  - zmienna
 * vec  - wektor
 * 
 * funkcje:
 * draw(vec v1, vec v2)
 * 
 * funcValues(vec v)
 * vec v1 = funvValues(vec v2)
 * 
 */
 