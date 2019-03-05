package Harkkatyo;

public class Huone {
	private final int hmaara;
	private final int numero;
	
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

class EiKyseistaHuonettaPoikkeus extends Exception{
	public EiKyseistaHuonettaPoikkeus(int huone_id) {
		super("Ei kyseistä huonetta "+huone_id);
	}
}