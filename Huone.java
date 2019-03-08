package Harkkatyo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Huone {
	//Attribuutit henkilöiden määrälle ja huoneen numerolle
	private final int hmaara;
	private final int numero;
	
	//Konstruktori
	public Huone(int numero, int hmaara) {
		this.hmaara=hmaara;
		this.numero=numero;
	}

	public int getHmaara() {
		return hmaara;
	}

	public int getNumero() {
		return numero;
	}
	
	public int laskeHinta(Date alku, Date loppu) {
		//Haetaan ero millisekunteina
	    long eromillisekunteina = Math.abs(loppu.getTime() - alku.getTime());
	    
	    //Muutetaan päiviksi
	    long ero = TimeUnit.DAYS.convert(eromillisekunteina, TimeUnit.MILLISECONDS);
	    
	    int hinta=0;
	    
	    //Tutkitaan onko huone perus vai luksus ja lasketaan hinta
	    if(this instanceof Perushuone) {
	    	Perushuone ph=(Perushuone) this;
	    	hinta=(int) (ph.getHinta()*ero);
	    }
	    
	    if(this instanceof Luksushuone) {
	    	Luksushuone lh=(Luksushuone) this;
	    	hinta=(int) (lh.getHinta()*ero);
	    }
	    
	    return hinta;
	}
}

class Perushuone extends Huone{
	private final int hinta_yo;
	
	public Perushuone(int hinta, int numero, int hmaara) {
		super(numero, hmaara);
		this.hinta_yo=hinta;
	}
	
	public int getHinta() {
		return hinta_yo;
	}
}

class Luksushuone extends Huone{
	private final int hinta_yo;
	
	public Luksushuone(int hinta, int numero, int hmaara) {
		super(numero, hmaara);
		this.hinta_yo=hinta;
	}
	
	public int getHinta() {
		return hinta_yo;
	}
}


//Virhe luokka sellaista tilannetta varten, jossa etsittävää huonetta ei löydy
class EiKyseistaHuonettaPoikkeus extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EiKyseistaHuonettaPoikkeus(int huone_id) {
		super("Ei kyseistä huonetta "+huone_id);
	}
}