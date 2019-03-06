package Harkkatyo;

public class Varausjarjestelma {

	public static void main(String[] args) {
		
		//Ensimm‰inen parametri on huoneiden kokonaism‰‰r‰ ja toinen luksushuoneiden m‰‰r‰
		Hotelli hotelli=new Hotelli(30, 6);
		
		//Ensimm‰inen parametri on ikkunan leveys, toinen korkeus ja kolmas Hotelli-olio
		new Kayttoliittyma(480, 300, hotelli);
	}

}
