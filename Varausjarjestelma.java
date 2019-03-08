package Harkkatyo;

//Tämä on työn aloitusluokka
public class Varausjarjestelma {

	public static void main(String[] args) {   
		//Ensimmäinen parametri on huoneiden kokonaismäärä ja toinen luksushuoneiden määrä
		//Kolmas on perushuoneen henkilöiden määrä ja neljäs perushuoneen hinta per yö
		//Viides on luksushuoneen henkilöiden määrä ja kuudes luksushuoneen hinta per yö
		Hotelli hotelli=new Hotelli(30, 6, 4, 80, 2, 210);
		
		//Ensimmäinen parametri on ikkunan leveys, toinen korkeus ja kolmas Hotelli-olio
		//Ikkunan kokoa ei kannata muuttaa, koska käyttöliittymä ei ole valitettavasti skaalautuva
		new Kayttoliittyma(480, 300, hotelli);
	}
}