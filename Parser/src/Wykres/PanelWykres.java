package Wykres;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.ShapeGraphicAttribute;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class PanelWykres extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private static int zaokraglenie;
	
	private int heightW, widthW, skalaY, skalaX;
	private float yMin, yMax, xMin, xMax;
	private ArrayList<Float> yList = new ArrayList<Float>();
	private ArrayList<Float> xList = new ArrayList<Float>();
	private WWykres wykres;
	private boolean bWspolrzedne, close;
	private JLabel txtWspolrzedne;

	private Wektor wektor;

	int szerRamki = 40;

	public PanelWykres(int width, int height, Wektor wektor, int rodzaj) {

		// ************Init okno********;
		this.setSize(width, height);
		setBorder(new LineBorder(Color.black, 1));
		setBackground(new Color(167, 167, 167));

		heightW = height;
		widthW = width;
		this.setLayout(null);
		zaokraglenie = 2;

		wykres = new WWykres(wektor, rodzaj);
		this.wektor = wektor;
		txtWspolrzedne = new JLabel("Wspó³rzêdne: ");
		txtWspolrzedne.setBounds(width/2, 5, 100, 15);
		close = false;
		
		this.add(txtWspolrzedne);

		wykres.setBounds(szerRamki, szerRamki, widthW - (2 * szerRamki),
				heightW - (2 * szerRamki));
		wykres.setBorder(new LineBorder(Color.black));
		this.add(wykres);
		
		Thread t = new Thread(this);
		t.start();

		przygotujWektor();
	}

	public void przygotujWektor() {

		yMax = zaokraglij(wektor.getYMax());
		yMin = zaokraglij(wektor.getYMin());

		xMax = zaokraglij(wektor.getXMax());
		xMin = zaokraglij(wektor.getXMin());

		wykres.setXValue(wektor.getXMin(), wektor.getXMax());
		
		bWspolrzedne = false;
		wykres.setTextWspolrzedne(txtWspolrzedne);
	}

	public static float zaokraglij(float liczba) {

		float tmp;
		float dzielnik = 1;

		for (int i = 0; i < zaokraglenie; i++) {

			liczba *= 10;
			dzielnik = dzielnik * 10;
		}
		liczba = Math.round(liczba);

		tmp = liczba / dzielnik;

		return tmp;
	}

	public void resizeWindow() {

		widthW = getWidth();
		heightW = getHeight();
		
		xMax = wykres.getXMax();
		xMin = wykres.getXMin();
		yMax = wykres.getYMax();
		yMin = wykres.getYMin();

		wykres.setSize(widthW - (2 * szerRamki), heightW - (2 * szerRamki));
		wykres.resizeWindow();

		// wypisanie skali.

		float sumaY = 0, sumaX = 0;
		
		//wyliczenie dla X
		if(xMin < 0 && xMax > 0 || xMax < 0 && xMin > 0)
			sumaX = Math.abs(xMin) + Math.abs(xMax);
		else if(xMin >= 0 && xMax > 0)
			sumaX = xMax - xMin;
		else if(xMin < 0 && xMax <= 0)
			sumaX = xMin - xMax;
		
		//wyliczenie dla Y
		if(yMin < 0 && yMax > 0 || yMax < 0 && yMin > 0)
			sumaY = Math.abs(yMin) + Math.abs(yMax);
		else if(yMin > 0 && yMax > 0)
			sumaY = yMax - yMin;
		else if(yMin < 0 && yMax < 0)
			sumaY = yMin - yMax;

		
		float tmpY = sumaY / wykres.getPodzialY();
		float tmpX = sumaX / wykres.getPodzialX();

		if(yMin < 0 && yMax < 0)
			tmpY = -tmpY;
		
		if(xMin < 0 && xMax < 0)
			tmpX = -tmpX;
		
		// skala Y
		yList.clear();

		yList.add(zaokraglij(yMax));
		for (int i = 1; i < wykres.getPodzialY(); i++)
			yList.add(zaokraglij(yMax - (i * tmpY)));
		yList.add(zaokraglij(yMin));

		skalaY = (heightW - 2 * szerRamki) / wykres.getPodzialY();

		// skala X
		xList.clear();

		xList.add(zaokraglij(xMin));
		for (int i = 1; i < wykres.getPodzialX(); i++)
			xList.add(zaokraglij((xMin + (i * tmpX))));
		xList.add(zaokraglij(xMax));

		skalaX = (widthW - 2 * szerRamki) / wykres.getPodzialX();
		
		//napisz wspo³rzedne
		if(bWspolrzedne){
			
			txtWspolrzedne.setBounds(widthW/3, 5, 250, 15);
			txtWspolrzedne.setVisible(true);;
		}
		else{
			
			txtWspolrzedne.setVisible(false);
		}

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		wypiszSkale(g);

	}

	private void wypiszSkale(Graphics g) {

		if (wykres.getRodzajWykresu() != 4) {

			// wypisanie skali Y
			int tmp = 5;

			for (float s : yList) {

				g.drawString("" + s, 2, szerRamki + tmp);

				tmp += skalaY;
			}

			// wypisanie skaly X
			int tmpx = -5;

			for (float s : xList) {

				g.drawString("" + s, szerRamki + tmpx, heightW - szerRamki + 10);

				tmpx += skalaX;
			}
		}

		else {

			// osie dla histogramu.
			// skala dla Y
			wykres.wyliczHistogram();
			
			float tmpY = wykres.getYMaxHist() / wykres.getPodzialY();

			yList.clear();
			yList.add((float) wykres.getYMaxHist());
			for (int i = wykres.getPodzialY() - 1; i > 0; i--)
				yList.add(i * tmpY);
			yList.add((float) 0);

			skalaY = (heightW - 2 * szerRamki) / wykres.getPodzialY();

			int tmp = 5;

			for (float s : yList) {

				g.drawString("" + s, 2, szerRamki + tmp);
				tmp += skalaY;
			}

			// skala dla X

			float sumaX = Math.abs(yMin) + Math.abs(yMax);
			float tmpX = sumaX / wykres.getPodzialX();

			xList.clear();

			xList.add(yMin);
			for (int i = 1; i < wykres.getPodzialX(); i++) {

				xList.add(zaokraglij((yMin + (i * tmpX))));
			}

			xList.add(yMax);

			int tmpx = -5;

			for (float s : xList) {
				g.drawString("" + s, szerRamki + tmpx, heightW - szerRamki + 10);
				tmpx += skalaX;
			}

		}

	}

	public void setHistIlosc(int i) {

		wykres.setHistIlosc(i);
	}

	public int getHistIlosc() {

		return wykres.getHistIlosc();
	}
	
	public void setRodzajWykresu(int rodzaj){
		
		wykres.setRodzajWykresu(rodzaj);
		
		this.resizeWindow();
	}
	
	public void setZaokraglenie(int zaokr){
		
		zaokraglenie = zaokr;
	}

	public int getZaokraglenie(){
		
		return zaokraglenie;
	}
	
	public void setWspolrzedne(boolean wybro){
		
		bWspolrzedne = wybro;
		wykres.setWspolrzedne(wybro);
	}
	
	public boolean getWspolrzedne(){
		
		return bWspolrzedne;
	}
	
	public void setKolorWykresu(Color kolor){
		
		wykres.setKolorWykresu(kolor);
	}
	
	public void setbOsieWspl(boolean bool){
		
		wykres.setbOsieWspl(bool);
	}
	
	public boolean getbOsieWspl(){
		
		return wykres.getbOsieWspl();
	}
	
	public void setbSiatka(boolean bool){
		
		wykres.setbSiatka(bool);
	}
	
	public boolean getbSiatka(){
		
		return wykres.getbSiatka();
	}
	
	public void setSkaluWykres(boolean bool){
		
		wykres.setSkaluWykres(bool);
	}

	public void exit(){
		
		close = true;
	}
	
	@Override
	public void run() {
		
		while(!close){
			
			if(wykres.getbOdswiez()){
				
				resizeWindow();
				wykres.setbOdzwiez(false);
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class WWykres extends JPanel implements MouseMotionListener, MouseListener {

	private int podzialY, podzialX, sizeSkala, dlugoscSygnalu, xStart, xKoniec;
	private ArrayList<Float> wektor;
	private ArrayList<Integer> histogramLista;
	private float yMin, yMax, xMin, xMax, aY, bY, aX, bX;
	private Wektor calyWektor;
	private Color kolorWykresu;
	private int rodzajWykresu, height, width, histIlosc, yMinHist, yMaxHist;
	private JLabel txtWspolrzedne;
	private boolean bWspolrzedne, bOsieWsp, bSiatka, bskalowanie, bZaznaczanie, bOdswiez;
	// zmienne do skalowania
	private float saX, sbX;
	private Punkt p1 = new Punkt();
	private Punkt p2 = new Punkt();
	
	
	public WWykres(Wektor wektor, int rodzaj) {

		// ********init*************
		podzialX = 8;
		podzialY = 4;
		sizeSkala = 3;
		histIlosc = 10;
		this.setBackground(Color.WHITE);
		rodzajWykresu = rodzaj;
		height = getHeight();
		width = getWidth();

		// dane
		this.calyWektor = wektor;
		this.wektor = calyWektor.getWektor();
		yMax = calyWektor.getYMax();
		yMin = calyWektor.getYMin();
		dlugoscSygnalu = calyWektor.size();
		kolorWykresu = calyWektor.getKolor();
		
		xStart = 1;
		xKoniec = dlugoscSygnalu;
		
		bskalowanie = false;
		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		wyliczWspolczynnikiOsX();
		// podzia³ka na Y
		for (int i = 0; i <= podzialY; i++)
			g.drawLine(0, getHeight() * i / podzialY, sizeSkala, getHeight()
					* i / podzialY);

		// podzia³ka na X
		for (int i = 0; i <= podzialX; i++)
			g.drawLine(getWidth() * i / podzialX, getHeight(), getWidth() * i
					/ podzialX, getHeight() - sizeSkala);

			if (rodzajWykresu == 1)
				rysujWykresLiniowy(g);

			if (rodzajWykresu == 2)
				rysujWykresPunktowy(g);

			if (rodzajWykresu == 3)
				rysujWykresProbkowy(g);

			if (rodzajWykresu == 4)
				rysujWykresHistogram(g);

			rysujProstokatZaznaczania(g);
			
	}

	private void rysujWykresLiniowy(Graphics g) {

		// Ustawienia

		g.setColor(kolorWykresu);
		// *********Rysowanie wykresu***********
		float zmX, zmY;

		zmX = getWidth() / ((float) dlugoscSygnalu - 1);

		int i = 1;
		int tmpX = 0;
		int tmpY = 0;

		// rysowanie wykresu

		tmpY = (int) (wektor.get(0) * aY + bY);
		tmpY = getHeight() - tmpY;

		for (int j = xStart; j < xKoniec; j++) {

			zmY = wektor.get(j) * aY + bY;

			zmY = getHeight() - zmY;

			g.drawLine(tmpX, tmpY, (int) (zmX * i), (int) zmY);

			tmpX = (int) (zmX * i);
			tmpY = (int) zmY;

			i++;
		}

		rysujOsieWspl(g);
		rysujSiatke(g);
		

		
	}

	private void rysujWykresPunktowy(Graphics g) {

		// Ustawienia

		g.setColor(kolorWykresu);
		// *********Rysowanie wykresu***********
		float zmX, zmY;

		zmX = width / ((float) dlugoscSygnalu - 1);

		int i = 1;
		int skala = (int) (sizeSkala * 0.50);

		// rysowanie wykresu
		for (int j = xStart; j < xKoniec; j++) {

			zmY = wektor.get(j) * aY + bY;
			zmY = height - zmY;

			int x = (int) (zmX * i - skala * 0.5);
			int y = (int) (zmY - skala * 0.5);
			g.drawOval(x, y, skala, skala);

			i++;
		}
		rysujOsieWspl(g);
		rysujSiatke(g);
		
	}

	private void rysujWykresProbkowy(Graphics g) {

		// Ustawienia

		g.setColor(kolorWykresu);
		// *********Rysowanie wykresu***********
		float zmX, zmY;

		zmX = width / ((float) dlugoscSygnalu - 1);

		int i = 1;

		int skala = sizeSkala - 2;
		// rysowanie wykresu

		g.drawLine(0, getYNaWykres(0), width, getYNaWykres(0));

		for (int j = xStart; j < xKoniec; j++) {

			zmY = wektor.get(j) * aY + bY;
			zmY = height - zmY;
			g.setColor(kolorWykresu);

			int x = (int) (zmX * i - skala * 0.5);
			int y = (int) (zmY - skala * 0.5);
			g.drawOval(x, y, skala, skala);

			if (zmY > getYNaWykres(0)) {
				g.setColor(Color.RED);
				g.drawLine((int) (zmX * i), y, (int) (zmX * i), getYNaWykres(0));
			} else {
				g.setColor(Color.GREEN);
				g.drawLine((int) (zmX * i), ((int) zmY) + 4, (int) (zmX * i),
						getYNaWykres(0));
			}

			i++;
		}
		rysujOsieWspl(g);
		rysujSiatke(g);
	}

	private void rysujWykresHistogram(Graphics g) {

		wyliczHistogram();

		float a, b;

		// obliczenie nowego y
		yMaxHist = histogramLista.get(0);
		yMinHist = histogramLista.get(0);

		for (int x : histogramLista) {

			if (x > yMaxHist)
				yMaxHist = x;
			if (x < yMinHist)
				yMinHist = x;
		}

		// wyliczenie nowego uk³adu liniowego.
		float w, wX, wY, y, yy;

		w = 0 - yMaxHist;
		wX = -height;
		wY = 0 * height;
		a = (float) ((wX / w) * 0.95);
		b = wY / w;

		float xPodzialka = (float) width / (float) histIlosc;
		xPodzialka *= 0.995;
		int i = 0;

		for (int x : histogramLista) {

			g.setColor(kolorWykresu);

			y = (x * a) + b;
			yy = height - y;
			// rysowanie prostokatow
			g.fillRect((int) (xPodzialka * i), (int) yy, (int) xPodzialka,
					(int) y);

			g.setColor(Color.black);
			g.drawRect((int) (xPodzialka * i), (int) yy, (int) xPodzialka,
					(int) y);

			i++;
		}

	}

	private void rysujOsieWspl(Graphics g) {

		if (bOsieWsp) {
			g.setColor(Color.BLACK);
			// os OY
			g.drawLine((int) bX, 0, (int) bX, height);
			g.drawLine((int) bX +1, 0, (int) bX +1, height);

			// os OX
			g.drawLine(0, height - (int) bY, width, height - (int) bY);
			g.drawLine(0, height - (int) bY -1, width, height - (int) bY -1);

			g.setColor(kolorWykresu);
		}

	}
	
	private void rysujSiatke(Graphics g){
		
		if(bSiatka){
			g.setColor(Color.BLACK);
			
			// Siatka na Y
			for (int i = 0; i <= podzialY; i++)
				g.drawLine(0, getHeight() * i / podzialY, width, getHeight()
						* i / podzialY);

			// Siatka na X
			for (int i = 0; i <= podzialX; i++)
				g.drawLine(getWidth() * i / podzialX, getHeight(), getWidth() * i
						/ podzialX, 0);
			
			g.setColor(kolorWykresu);
		}
				
	}
	
	private void rysujProstokatZaznaczania(Graphics g){
		
		if(bZaznaczanie){
			
			int dlugoscX, dlugoscY;
			
			dlugoscX = p2.x - p1.x;
			dlugoscY = p2.y - p1.y;
			
			g.setColor(Color.GRAY);
			g.drawRect(p1.x, p1.y, dlugoscX, dlugoscY);
			
		}
	}
	
	private int getYNaWykres(float x) {

		float tmp;
		tmp = x * aY + bY;
		tmp = height - tmp;
		return (int) tmp;
	}

	private void wyliczWspolczynniki() {

		// obliczanie uk³adu równañ metod¹ wyznaczników.
		// wyliczenie na Y
		float w, wX, wY;

		w = yMin - yMax;
		wX = -height;
		wY = yMin * height;
		aY = (float) ((wX / w) * 0.99);
		bY = wY / w;

		// wyliczenie dla X
		w = wX = wY = 0;

		w = xMin - xMax;
		wX = -width;
		wY = xMin * width;
		aX = wX / w;
		bX = wY / w;

		bOdswiez = true;	//odswiezenie skali w watku PanelWykres
	}
	
	private void wyliczWspolczynnikiOsX(){
		
		// wyliczenie dla X liczba pr.
		float w, wX, wY;

		w = -getWidth();
		wX = xStart - xKoniec;             
		wY = -xStart * getWidth();
		saX = wX / w;
		sbX = wY / w;
		
	}
	
	private void wyliczWspolczynnikiSkalownia(){
		
		if(p2.x > p1.x){
			
			xMin = wyliczWspolrzednaX(p1.x);
			xMax = wyliczWspolrzednaX(p2.x);
			
			xStart = (int) ((p1.x * saX) + sbX);
			xKoniec = (int) ((p2.x * saX) + sbX);
			
			wyliczWspolczynnikiOsX();
		}
		else{
			
			xMin = wyliczWspolrzednaX(p2.x);
			xMax = wyliczWspolrzednaX(p1.x);
			
			xStart = (int) ((p2.x * saX) + sbX);
			xKoniec = (int) ((p1.x * saX) + sbX);
			
			wyliczWspolczynnikiOsX();
		}
		
		if(wyliczWspolrzednaY(p2.y) > wyliczWspolrzednaY(p1.y)){
			
			yMax = wyliczWspolrzednaY(p2.y);
			yMin = wyliczWspolrzednaY(p1.y);
		}
		else{
			
			yMax = wyliczWspolrzednaY(p1.y);
			yMin = wyliczWspolrzednaY(p2.y);
		}

		dlugoscSygnalu = xKoniec - xStart;
		
		if(xStart < 0)
			xStart = 0;
		if(xKoniec > calyWektor.size())
			xKoniec = calyWektor.size();
		
		if(dlugoscSygnalu < 2 ) //(calyWektor.size() *0.003))
			wstawDefaultWspolczynniki();
		
		wyliczWspolczynniki();
				
	}
	
	private void wstawDefaultWspolczynniki(){
		
		yMax = calyWektor.getYMax();
		yMin = calyWektor.getYMin();
		xMax = calyWektor.getXMax();
		xMin = calyWektor.getXMin();
		
		dlugoscSygnalu = calyWektor.size();
		
		xStart = 1;
		xKoniec = dlugoscSygnalu;
		
		wyliczWspolczynniki();
	}

	public void wyliczHistogram() {

		// dane
		ArrayList<Float> przedzialyLista = new ArrayList<Float>(histIlosc);
		histogramLista = new ArrayList<Integer>(histIlosc);

		// Obliczenie oraz uzupe³nienie listy przedzia³ów.

		float tmpPodzial = (Math.abs(yMin) + Math.abs(yMax)) / histIlosc;

		for (int i = 0; i <= histIlosc; i++) {

			przedzialyLista.add(yMin + (i * tmpPodzial));
		}

		for (int i = 0; i < histIlosc; i++)
			histogramLista.add(0);

		// wyliczenie iloœci elementów wpadaj¹cych do danego przedzia³u.
		for (float f : wektor) {

			for (int i = 1; i <= histIlosc; i++) {

				if (przedzialyLista.get(i - 1) < f
						&& przedzialyLista.get(i) > f)
					histogramLista
							.set((i - 1), (histogramLista.get(i - 1) + 1));
			}
		}
		
		for (int x : histogramLista) {

			if (x > yMaxHist)
				yMaxHist = x;
			if (x < yMinHist)
				yMinHist = x;
		}

	}
	
	private float wyliczWspolrzednaY(int wsp){
		
		float tmp = 0;
		
		wsp = height - wsp;
		
		tmp = (wsp - bY)/aY;
		
		return PanelWykres.zaokraglij(tmp);
	}
	
	private float wyliczWspolrzednaX(int wsp){
		
		float tmp = 0;
		
		tmp = (wsp - bX)/aX;
		
		return PanelWykres.zaokraglij(tmp);	
	}

	public void resizeWindow() {

		height = getHeight();
		width = getWidth();

		// rozmiar podzia³ki
		if (width > 100 && width < 200)
			sizeSkala = 3;
		if (width > 200 && width < 300)
			sizeSkala = 4;
		if (width > 400 && width < 500)
			sizeSkala = 6;
		if (width > 500)
			sizeSkala = 8;

		// skalowanie Y
		if (height < 75)
			podzialY = 2;
		if (height > 75 && height < 300)
			podzialY = 4;
		if (height > 300)
			podzialY = 8;

		// skalowanie X
		if (width < 75)
			podzialX = 2;
		if (width > 75 && width < 300)
			podzialX = 4;
		if (width > 300 && width < 500)
			podzialX = 8;
		if (width > 500)
			podzialX = 16;

		wyliczWspolczynniki();

		repaint();
	}

	public int getPodzialX() {
		return podzialX;
	}

	public int getPodzialY() {
		return podzialY;
	}

	public void setHistIlosc(int i) {

		histIlosc = i;
	}

	public int getHistIlosc() {

		return histIlosc;
	}

	public int getRodzajWykresu() {

		return rodzajWykresu;
	}
	
	public void setRodzajWykresu(int rodzaj){
		
		rodzajWykresu = rodzaj;
	}

	public void setXValue(float Min, float Max) {

		this.xMin = Min;
		this.xMax = Max;
	}

	public int getYMaxHist() {

		return yMaxHist;
	}

	public int getYMinHist() {

		return yMinHist;
	}
	
	public float getXMax() {

		return xMax;
	}

	public float getXMin() {

		return xMin;
	}
	
	public float getYMax() {

		return yMax;
	}

	public float getYMin() {

		return yMin;
	}
	
	public void setTextWspolrzedne(JLabel label){
		
		txtWspolrzedne = label;
	}

	public void setWspolrzedne(boolean test){
		
		bWspolrzedne = test;
	}
	
	public void setKolorWykresu(Color kolor){
		
		kolorWykresu = kolor;
	}
	
	public void setbOsieWspl(boolean bool){
		
		bOsieWsp = bool;
	}
	
	public boolean getbOsieWspl(){
		
		return bOsieWsp;
	}
	
	public void setbSiatka(boolean bool){
		
		bSiatka = bool;
	}
	
	public boolean getbSiatka(){
		
		return bSiatka;
	}
	
	public void setSkaluWykres(boolean bool){
		
		bskalowanie = bool;
		
		// wy³¹czenie skalowania
		if(!bskalowanie){
			
			wstawDefaultWspolczynniki();
			repaint();
		}
		
		else{
			
			repaint();
		}
	}
	
	public void setbOdzwiez(boolean bool){
		
		bOdswiez = bool;
	}
	
	public boolean getbOdswiez() {
		
		return bOdswiez;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {

		if (bZaznaczanie) {

			p2.x = e.getX();
			p2.y = e.getY();
			
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		if(bWspolrzedne)
			
			txtWspolrzedne.setText("Wspó³rzêdne: X= " + wyliczWspolrzednaX(e.getX()) + "   " + "Y= " + wyliczWspolrzednaY(e.getY()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		
		if (bskalowanie) {
			
			bZaznaczanie = true;

			p1.x = e.getX();
			p1.y = e.getY();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(bZaznaczanie){
			
			p2.x = e.getX();
			p2.y = e.getY();
			
			bZaznaczanie = false;
			bskalowanie = true;
			
			wyliczWspolczynnikiSkalownia();
			
			repaint();
		}
	}
}

class Punkt{
	
	public int x, y;
	
	public Punkt(){}
	
	public Punkt(int x, int y){
		
		this.x = x;
		this.y = y;		
	}
	
}