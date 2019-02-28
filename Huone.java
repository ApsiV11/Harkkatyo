package Harkkatyo;

public class Huone {
	private int hmaara;
	
	public Huone(int hmaara) {
		this.hmaara=hmaara;
	}
}

class Perushuone extends Huone{
	private final int hinta_yo=150;
	
	public Perushuone(int hmaara) {
		super(hmaara);
	}
}

class Luksushuone extends Huone{
	private final int hinta_yo=450;
	
	public Luksushuone(int hmaara) {
		super(hmaara);
	}
}

class EiKyseistaHuonettaPoikkeus extends Exception{
	public EiKyseistaHuonettaPoikkeus(int huone_id) {
		super("Ei kyseistä huonetta "+huone_id);
	}
}