package Wykres;

import java.awt.Color;
import java.util.ArrayList;

public class Wektor {
	
	private Color kolor;
	private ArrayList<Float> wektor; 
	private float xMin, xMax, yMin, yMax;
	
	//************Konstruktory**************

	public Wektor(float xmin, float xmax)
	{
		kolor = Color.blue;
		
		wektor = new ArrayList<Float>();
		
		this.xMax = xmax;
		this.xMin = xmin;
	}
	
	public Wektor (Color kolor, float xmin, float xmax)
	{
		this.kolor = kolor;
		
		wektor = new ArrayList<Float>();
		
		this.xMax = xmax;
		this.xMin = xmin;
	}
	
	public Wektor (int lElementow, Color kolor, float xmin, float xmax)
	{
		this.kolor = kolor;
		
		wektor = new ArrayList<Float>(lElementow);
		
		this.xMax = xmax;
		this.xMin = xmin;
	}
	
	public Wektor (ArrayList<Float> wektor, int lElementow, Color kolor, float xmin, float xmax)
	{
		this.kolor = kolor;
		
		this.wektor = wektor;
		
		this.xMax = xmax;
		this.xMin = xmin;
	}
	
	//************Wejœcie, wyjœcie**************
	
	public void add(float d){
		
		wektor.add(d);
	}
	
	public void remove(int index){
		
		wektor.remove(index);
	}
	
	public float get(int index){
		
		return wektor.get(index);
	}
	
	public int size(){
		
		return wektor.size();
	}
	
	public boolean contains(float x){
		
		return wektor.contains(x);
	}
	
	public void setXMax(float xMax){
		
		this.xMax = xMax;
	}
	
	public float getXMax(){
		
		return xMax;
	}

	public void setXMin(float xMin){
		
		this.xMin = xMin;
	}
	
	public float getXMin(){
		
		return xMin;
	}
	
	public float getYMin(){
		
		obliczY();
		return yMin;
	}
	
	public float getYMax(){
		
		obliczY();
		return yMax;
	}
	
	public void setKolor(Color kolor){
		
		this.kolor = kolor;
	}
	
	public Color getKolor(){
		
		return this.kolor;
	}
	
	public ArrayList<Float> getWektor(){
		
		return wektor;
	}
	
	public void setWektor(ArrayList<Float>wektor){
		
		this.wektor = wektor;
	}
	
	public void clear(){
		
		wektor.clear();
	}
	
	//************Obliczenia**************
	
	private void obliczY(){
			
		float tmpMin = wektor.get(0);
		float tmpMax = wektor.get(0);
			
		for(float f : wektor){
				
			if(tmpMax < f)
				tmpMax = f;
			if(tmpMin > f)
				tmpMin = f;
		}
		
		yMax = tmpMax;
		yMin = tmpMin;
	}
	
}
