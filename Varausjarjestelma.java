package Harkkatyo;

//Tämä on työn aloitusmetodi
public class Varausjarjestelma {

	public static void main(String[] args) {   
		//Ensimmäinen parametri on huoneiden kokonaismäärä ja toinen luksushuoneiden määrä
		Hotelli hotelli=new Hotelli(30, 6, 4, 80, 2, 210);
		
		//Ensimmäinen parametri on ikkunan leveys, toinen korkeus ja kolmas Hotelli-olio
		new Kayttoliittyma(480, 300, hotelli);
	}
}