package parser_core;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent.KeyBinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame {
	
	//elementy GUI
	private JTextArea mainTextArea = new JTextArea(20, 37);
	private JTextField mainTextEdit = new JTextField(37);
	private JPanel mainPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("Plik");
	private JMenu edit = new JMenu("Edytuj");
	private JMenu options = new JMenu("Opcje");
	private JCheckBox memoryOption = new JCheckBox("Pamiec Nazw");
	private JButton execute = new JButton("Wykonaj");
	private JButton close = new JButton("Zamknij");
	private JButton clear = new JButton("Wyczysc");
	
	//stale
	private final String newLine = new String("\n");
	private final String arrows  = new String(">>");
	private final String space   = new String(" ");
	
	//core
	private Parser core = new Parser();
	
	//other frame
	private MemoryFrame memoryFrame = null;
	
	public MainWindow() {
		super("Program");
		
		MyListener _myListener = new MyListener(this);
		menuBar.add(file);
		menuBar.add(edit);
		
		options.add(memoryOption);
		menuBar.add(options);
		
		mainTextArea.setLineWrap(true);
		mainTextArea.setBackground(Color.WHITE);
		mainTextArea.setEditable(false);
		mainTextArea.setText(arrows + space);
		
		JScrollPane scrollPane = new JScrollPane(mainTextArea);
		scrollPane.setBorder(null);
		
		mainPanel.add(scrollPane);
		mainTextEdit.addKeyListener(_myListener);
		mainTextEdit.setBackground(Color.WHITE);
		//mainTextEdit.setBorder(null);
		mainPanel.add(mainTextEdit);
		mainPanel.setBackground(Color.WHITE);

		//buttons / button panel
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.add(clear);
		buttonPanel.add(close);
		memoryOption.setBackground(Color.WHITE);
		buttonPanel.add(memoryOption);
		buttonPanel.add(execute);
		clear.addActionListener(_myListener);
		close.addActionListener(_myListener);
		memoryOption.addActionListener(_myListener);
		execute.addActionListener(_myListener);
		mainPanel.add(buttonPanel);
		
		this.setJMenuBar(menuBar);
		this.setContentPane(mainPanel);
		this.setSize(428, 425);
		this.setResizable(false);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
		t.start();
	}
	
	public void update() {
		if(core.giveFunctionsArray() != null)
			memoryFrame.updateFuncsArray(core.giveFunctionsArray());
		if(core.giveVecsArray() != null)
			memoryFrame.updateVecsArray(core.giveVecsArray());
		if(core.giveVarsArray() != null)
			memoryFrame.updateVarsArray(core.giveVarsArray());
	}

	class MyListener implements KeyListener, ActionListener {
	
		private JFrame frame;
		
		public MyListener(JFrame __frame) {
			this.frame =__frame;
		}
		
		@Override
		public void keyPressed(KeyEvent __arg0) {
			
			if(__arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				String _area_text = mainTextArea.getText();
				String _field_text= mainTextEdit.getText();
				
				String output = core.analisis(_field_text);
				if(output.equals("exit")) {
					frame.dispose();
					return;
				}
				else
				if(output.equals("clearw")) {
					_area_text = (arrows + space);
					mainTextArea.setText(_area_text);
					mainTextEdit.setText("");
					return;
				}
				
				_area_text +=_field_text;
				_area_text += " " + output;
				
				_area_text += (newLine + arrows + space);
				mainTextArea.setText(_area_text);
				mainTextEdit.setText("");
				
				if(memoryFrame != null)
					update();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void actionPerformed(ActionEvent __ae) {
			Object _source =__ae.getSource();
			
			if(_source.equals(memoryOption)) {
				if(memoryOption.isSelected()) {
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							memoryFrame = new MemoryFrame();
							
							update();
						}
					});
				}
				else {
					memoryFrame.close();
				}
			}
			else
			if(_source.equals(close)) {
				frame.dispose();
				return;
			}
			else
			if(_source.equals(clear)) {
				String _area_text = mainTextArea.getText();
				
				_area_text = (arrows + space);
				mainTextArea.setText(_area_text);
				mainTextEdit.setText("");
				
				if(memoryFrame != null)
					update();
				
				return;
			}
			else
			if(_source.equals(execute)) {
				String _area_text = mainTextArea.getText();
				String _field_text= mainTextEdit.getText();
				
				String output = core.analisis(_field_text);
				if(output.equals("exit")) {
					frame.dispose();
					return;
				}
				else
				if(output.equals("clearw")) {
					_area_text = (arrows + space);
					mainTextArea.setText(_area_text);
					mainTextEdit.setText("");
					return;
				}
				
				_area_text +=_field_text;
				_area_text += " " + output;
				
				_area_text += (newLine + arrows + space);
				mainTextArea.setText(_area_text);
				mainTextEdit.setText("");
				
				if(memoryFrame != null)
					update();
			}
		}
	}
}
