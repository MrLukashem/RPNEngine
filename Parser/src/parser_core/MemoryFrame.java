package parser_core;

import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class MemoryFrame extends JFrame {
	
	private JPanel myPanel = new JPanel();
	private JTextArea textArea = new JTextArea(17, 22);
	private ArrayList< Function > funcsArray = new ArrayList< Function >();
	private ArrayList< Vector > vecsArray = new ArrayList< Vector >();
	private ArrayList< Variable > varsArray = new ArrayList< Variable >();
	
	//buttons
	private JButton exitButton = new JButton("Zamknij");
	
	private int width = 250;
	private int height= 292;
	
	public MemoryFrame() {
		super("Pamiec nazw");
		
		textArea.setEditable(false);
		
		JScrollPane _scroll_panel = new JScrollPane(textArea);
		myPanel.add(_scroll_panel);
		
		this.setContentPane(myPanel);
		
		this.setSize(width, height);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.setResizable(false);
	}
	
	public MemoryFrame(int __w, int __h) {
		super("Pamiec nazw");
		
		this.setSize(__w,__h);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	
	public void close() {
		this.dispose();
		return;
	}
	
	private void rePaint() {
		String _out = new String();
		
		for(Function f : funcsArray) {
			_out += "Funkcja: " + f.giveId() + " = " + f.giveFunction() + "\n";
		}
		
		for(Vector v : vecsArray) {
			_out += "Wektor: " + v.giveId() + "\n";
		}
		
		for(Variable v : varsArray) {
			_out += "Zmienna: " + v.giveId() + " = " + String.valueOf(v.giveValue()) + "\n";
		}
		
		textArea.setText(_out);
	}
	
	public void updateFuncsArray(ArrayList< Function > __array) {
		funcsArray = new ArrayList< Function >(__array);
		rePaint();
	}
	
	public void updateVecsArray(ArrayList< Vector > __array) {
		vecsArray = new ArrayList< Vector >(__array);
		rePaint();
	}
	
	public void updateVarsArray(ArrayList< Variable > __array) {
		varsArray = new ArrayList< Variable >(__array);
		rePaint();
	}
	
	public static void main(String[] s) {
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				new MemoryFrame();
			}
		});
		
		t.start();
	}
}
