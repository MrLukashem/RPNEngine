package Wykres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.JobAttributes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Wykres extends JFrame implements MouseListener, ActionListener,
		WindowListener, ComponentListener, Runnable {

	public static int licznik = 1;
	private Wektor wektor;
	private boolean test = false;
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem mntmZamknij = new JMenuItem("Zamknij");
	private JRadioButtonMenuItem rdbtnmntmLiniowy;
	private JRadioButtonMenuItem rdbtnmntmPunktowy;
	private JRadioButtonMenuItem rdbtnmntmProbkowy;
	private JRadioButtonMenuItem rdbtnmntmHistogram;
	private JCheckBoxMenuItem chckbxmntmPokaWsprzdne;
	private JMenuItem mntmZapiszWykresjpg;
	private JCheckBoxMenuItem chckbxmntmOsieOxOy;
	private JCheckBoxMenuItem chckbxmntmSkalujWykres;
	
	JPanel panel = new JPanel();
	private int rodzajWykresu; // 1 - ci¹g³y, 2 - punktowy, 3 - probkowy, 4 -
								// histogram

	PanelWykres pW1;
	private JMenuItem mntmIloSupkw;
	private JMenuItem mntmZaokraglenie;
	private JMenuItem mntmKolorWykresu;
	private JCheckBoxMenuItem chckbxmntmSiatka;

	public Wykres(Wektor wektor) {

		rodzajWykresu = 1;
		this.wektor = wektor;
		createFrame();
		createPanel();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public Wykres(Wektor wektor, int rodzaj) {

		if (rodzaj == 1 || rodzaj == 2 || rodzaj == 3 || rodzaj == 4)
			rodzajWykresu = rodzaj;
		else
			rodzajWykresu = 1;

		this.wektor = wektor;
		createFrame();
		createPanel();
	}

	// metoda do tworzenia okna
	private void createFrame() {

		setVisible(true);
		setTitle("Wykres: " + licznik);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		getContentPane().setLayout(null);

		menuBar.setBounds(0, 0, getWidth() - 16, 21);
		getContentPane().add(menuBar);

		JMenu mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);

		mntmZamknij.addActionListener(this);

		mntmZapiszWykresjpg = new JMenuItem("Zapisz wykres .jpg");
		mnPlik.add(mntmZapiszWykresjpg);
		mntmZapiszWykresjpg.addActionListener(this);

		JSeparator separator = new JSeparator();
		mnPlik.add(separator);
		mnPlik.add(mntmZamknij);

		JMenu mnEdycja = new JMenu("Edycja");
		menuBar.add(mnEdycja);

		JMenu mnRodzajWykresu = new JMenu("Rodzaj wykresu:");
		mnEdycja.add(mnRodzajWykresu);

		rdbtnmntmLiniowy = new JRadioButtonMenuItem("Liniowy");
		mnRodzajWykresu.add(rdbtnmntmLiniowy);
		rdbtnmntmLiniowy.addActionListener(this);
		rdbtnmntmLiniowy.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));

		rdbtnmntmPunktowy = new JRadioButtonMenuItem("Punktowy");
		mnRodzajWykresu.add(rdbtnmntmPunktowy);
		rdbtnmntmPunktowy.addActionListener(this);
		rdbtnmntmPunktowy.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));

		rdbtnmntmProbkowy = new JRadioButtonMenuItem("Pr\u00F3bkowy");
		mnRodzajWykresu.add(rdbtnmntmProbkowy);
		rdbtnmntmProbkowy.addActionListener(this);
		rdbtnmntmProbkowy.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));

		rdbtnmntmHistogram = new JRadioButtonMenuItem("Histogram");
		mnRodzajWykresu.add(rdbtnmntmHistogram);
		rdbtnmntmHistogram.addActionListener(this);
		rdbtnmntmHistogram.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));

		mntmIloSupkw = new JMenuItem("Ilo\u015B\u0107 s\u0142upk\u00F3w");
		mnEdycja.add(mntmIloSupkw);
		mntmIloSupkw.addActionListener(this);

		mntmZaokraglenie = new JMenuItem("Zaokraglenie");
		mnEdycja.add(mntmZaokraglenie);
		mntmZaokraglenie.addActionListener(this);
		
		mntmKolorWykresu = new JMenuItem("Kolor wykresu");
		mnEdycja.add(mntmKolorWykresu);
		mntmKolorWykresu.addActionListener(this);
		
		JMenu mnRysuj = new JMenu("Rysuj");
		mnEdycja.add(mnRysuj);
		
	    chckbxmntmOsieOxOy = new JCheckBoxMenuItem("Osie OX, OY");
		mnRysuj.add(chckbxmntmOsieOxOy);
		chckbxmntmOsieOxOy.setAccelerator(KeyStroke.getKeyStroke("shift O"));
		chckbxmntmOsieOxOy.addActionListener(this);
		
		chckbxmntmSiatka = new JCheckBoxMenuItem("Siatka");
		mnRysuj.add(chckbxmntmSiatka);
		chckbxmntmSiatka.setAccelerator(KeyStroke.getKeyStroke("shift S"));
		chckbxmntmSiatka.addActionListener(this);
		
	    chckbxmntmSkalujWykres = new JCheckBoxMenuItem("Skaluj wykres");
		mnEdycja.add(chckbxmntmSkalujWykres);
		chckbxmntmSkalujWykres.setAccelerator(KeyStroke.getKeyStroke("shift R"));
		chckbxmntmSkalujWykres.addActionListener(this);

		chckbxmntmPokaWsprzdne = new JCheckBoxMenuItem(
				"Poka\u017C wsp\u00F3\u0142rz\u0119dne");
		mnEdycja.add(chckbxmntmPokaWsprzdne);
		chckbxmntmPokaWsprzdne.setAccelerator(KeyStroke.getKeyStroke("shift W"));
		chckbxmntmPokaWsprzdne.addActionListener(this);

		panel.setBounds(0, 21, 484, 241);
		getContentPane().add(panel);

		this.addWindowListener(this);
		this.addComponentListener(this);

		licznik++;

		zaznaczWyborWykresu();

		Thread t = new Thread(this);
		t.start();
	}

	public void createPanel() {
		panel.setLayout(null);

		pW1 = new PanelWykres(this.getWidth() - 35, this.getHeight() - 80,
				wektor, rodzajWykresu);
		pW1.setLocation(10, 10);
		panel.add(pW1);

	}

	private void resizeWindow() {

		panel.setSize(getWidth() - 16, getHeight() - 59);
		menuBar.setSize(getWidth() - 16, 21);

		pW1.setSize(this.getWidth() - 35, this.getHeight() - 80);
		pW1.resizeWindow();

	}

	private void zaznaczWyborWykresu() {

		switch (rodzajWykresu) {

		case 1:
			rdbtnmntmLiniowy.setSelected(true);
			break;

		case 2:
			rdbtnmntmProbkowy.setSelected(true);
			break;

		case 3:
			rdbtnmntmProbkowy.setSelected(true);
			break;

		case 4:
			rdbtnmntmHistogram.setSelected(true);
			break;
		}

	}

	public void setHistIlosc(int i) {

		pW1.setHistIlosc(i);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Object zrodlo = e.getSource();
		if (zrodlo == mntmZamknij)
			System.exit(0);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object zrodlo = e.getSource();
		if (zrodlo == mntmZamknij)
			System.exit(0);

		// *** Obs³uga przycisków wyboru wykresu z menu ***//
		if (zrodlo == rdbtnmntmLiniowy) {
			rdbtnmntmLiniowy.setSelected(true);
			rdbtnmntmPunktowy.setSelected(false);
			rdbtnmntmProbkowy.setSelected(false);
			rdbtnmntmHistogram.setSelected(false);

			rodzajWykresu = 1;
		}

		if (zrodlo == rdbtnmntmPunktowy) {

			rdbtnmntmPunktowy.setSelected(true);
			rdbtnmntmLiniowy.setSelected(false);
			rdbtnmntmProbkowy.setSelected(false);
			rdbtnmntmHistogram.setSelected(false);

			rodzajWykresu = 2;
		}

		if (zrodlo == rdbtnmntmProbkowy) {

			rdbtnmntmProbkowy.setSelected(true);
			rdbtnmntmLiniowy.setSelected(false);
			rdbtnmntmPunktowy.setSelected(false);
			rdbtnmntmHistogram.setSelected(false);

			rodzajWykresu = 3;
		}

		if (zrodlo == rdbtnmntmHistogram) {

			rdbtnmntmHistogram.setSelected(true);
			rdbtnmntmLiniowy.setSelected(false);
			rdbtnmntmPunktowy.setSelected(false);
			rdbtnmntmProbkowy.setSelected(false);

			rodzajWykresu = 4;
			resizeWindow();
		}

		// *** Wczytanie iloœci histogramow ***//
		if (zrodlo == mntmIloSupkw) {

			zmianaPodzialuHistogramu();
		}

		if (zrodlo == mntmZaokraglenie) {

			zmianaZaokragleniaSkali();
		}
		// *** Wy³¹czenie, w³¹czenie wspó³rzêdnych ***//
		if (zrodlo == chckbxmntmPokaWsprzdne) {

			if (chckbxmntmPokaWsprzdne.isSelected())
				pW1.setWspolrzedne(true);
			else
				pW1.setWspolrzedne(false);
		}

		// *** Zapisanie wykresu do jpg ***//

		if (zrodlo == mntmZapiszWykresjpg) {

			zapiszObrazDoPliku();
		}
		
		//*** Zmiana koloru wykresu ***//
		if(zrodlo == mntmKolorWykresu){
			
			Color kolor = JColorChooser.showDialog(null, "Wybierz kolor", Color.BLUE);
			if(kolor != null)
				pW1.setKolorWykresu(kolor);
		}
		
		// *** Wy³¹czenie, w³¹czenie Osi OX OY ***//
		if (zrodlo == chckbxmntmOsieOxOy) {

			if (chckbxmntmOsieOxOy.isSelected())
				pW1.setbOsieWspl(true);
			else
				pW1.setbOsieWspl(false);
		}
		
		// *** Wy³¹czenie, w³¹czenie Siatki***//
		if (zrodlo == chckbxmntmSiatka) {

			if (chckbxmntmSiatka.isSelected())
				pW1.setbSiatka(true);
			else
				pW1.setbSiatka(false);
		}
		
		if (zrodlo == chckbxmntmSkalujWykres) {

			if (chckbxmntmSkalujWykres.isSelected())
				pW1.setSkaluWykres(true);
			else
				pW1.setSkaluWykres(false);
		}		
		

		// ustawienie rodzaju wykresu w danym oknie
		pW1.setRodzajWykresu(rodzajWykresu);
	}

	private void zapiszObrazDoPliku() {

		BufferedImage bufforImage = new BufferedImage(pW1.getWidth(),pW1.getHeight(), BufferedImage.TYPE_INT_RGB);
		JFileChooser chooserFile = new JFileChooser();

		if (chooserFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

			File plik = chooserFile.getSelectedFile();
			pW1.paintAll(bufforImage.getGraphics());

			try {

				ImageIO.write(bufforImage, "jpg", plik);
				JOptionPane.showMessageDialog(null, "Poprawnie zapisano plik: " + plik.getAbsolutePath(), "Informacja", 1);

			} catch (IOException ex) {

				JOptionPane.showMessageDialog(null, "B³¹d","Zapis do pliku nie powiód³ siê", 2);
			}
		}

	}

	private void zmianaPodzialuHistogramu() {

		String tmp = JOptionPane.showInputDialog("Podaj podzia³ histogramu: ",
				pW1.getHistIlosc());
		int iloscTmp;

		if (tmp != null) // jeœli nie wybrano cancel

			try {
				iloscTmp = Integer.parseInt(tmp);

				if (iloscTmp > 0 && iloscTmp < 1000)
					setHistIlosc(iloscTmp);

				else {
					JOptionPane.showMessageDialog(null,
							"Podaj liczbê z przedzia³u <1,1000>");
				}
			}

			catch (Exception ex) {

				JOptionPane
						.showMessageDialog(null,
								"B³¹d podczas konwersji, Podaj liczbê z przedzia³u <1,1000>");
			}
	}

	private void zmianaZaokragleniaSkali() {

		String tmp = JOptionPane.showInputDialog(
				"Podaj iloœæ miejsc po przecinku na skali: ",
				pW1.getZaokraglenie());
		int iloscTmp;

		if (tmp != null) // jeœli nie wybrano cancel

			try {
				iloscTmp = Integer.parseInt(tmp);

				if (iloscTmp >= 0 && iloscTmp < 6)
					pW1.setZaokraglenie(iloscTmp);

				else {
					JOptionPane.showMessageDialog(null,
							"Podaj liczbê z przedzia³u <0,5>");
				}
			}

			catch (Exception ex) {

				JOptionPane
						.showMessageDialog(null,
								"B³¹d podczas konwersji, Podaj liczbê z przedzia³u <0,5>");
			}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

		licznik--;
		
		pW1.exit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {

		if (test)
			resizeWindow();

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		test = true;
		resizeWindow();
	}
}
