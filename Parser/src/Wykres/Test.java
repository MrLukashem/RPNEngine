package Wykres;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Test implements Runnable {

	public Wektor wektor;

	public static void main(String[] args) {

		new Test();

	}

	public Test() {

		try {
			test();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void test() throws InterruptedException {

		Thread t = new Thread(this);
		t.start();

		Thread.sleep(100);
		
		Wykres w = new Wykres(wektor);
		//new Wykres(wektor,2);
		//w.setHistIlosc(100);
	}

	@Override
	public void run() {

		int n = 2;
		wektor = new Wektor(0, (float) (n * Math.PI));
		//wektor = new Wektor(1, 10);
		// wczytajPlik();

		for (float i = wektor.getXMin(); i < wektor.getXMax(); i += 0.01)
			// wektor.add((float) Math.sin(i));
			  wektor.add((float)  ( i*i));
			// wektor.add((float)i);
	}

	public void wczytajPlik() {

		String line = "";
		FileInputStream plik = null;

		try {
			plik = new FileInputStream("RozkladNormalny.txt");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Float> dane = new ArrayList<Float>(1000);
		ArrayList<Integer> liczby = new ArrayList<Integer>(1000);

		BufferedReader reader = new BufferedReader(new InputStreamReader(plik));
		int ii = 0;

		try {
			while ((line = reader.readLine()) != null) {

				System.out.println(line);
				line = line.replace(',', '.');
				float test = Float.parseFloat(line);

				//dane.add(test);
				wektor.add(test);
				ii++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		for (int i = 0; i < 1000; i++)
//			liczby.add(i);
//
//		int t = 0;
//
//		for (int i = 0; i < dane.size(); i++) {
//
//			t = (int) (Math.random() * (dane.size() - 10));
//
//			wektor.add(dane.get(liczby.get(t)));
//
//			dane.remove(liczby.get(t));
//		}

		wektor.add(-2);
		wektor.add(3);
	}

}
