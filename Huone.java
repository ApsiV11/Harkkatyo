package Harkkatyo;

public class Huone {
	//Attribuutit henkil�iden m��r�lle ja huoneen numerolle
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
}

class Perushuone extends Huone{
	private final int hinta_yo=150;
	
	public Perushuone(int numero, int hmaara) {
		super(numero, hmaara);
	}
}

class Luksushuone extends Huone{
	private final int hinta_yo=450;
	
	public Luksushuone(int numero, int hmaara) {
		super(numero, hmaara);
	}
}

//Virhe luokka sellaista tilannetta varten, jossa etsitt�v�� huonetta ei l�ydy
class EiKyseistaHuonettaPoikkeus extends Exception{
	public EiKyseistaHuonettaPoikkeus(int huone_id) {
		super("Ei kyseist� huonetta "+huone_id);
	}
}