package Harkkatyo;

//T�m� on ty�n aloitusmetodi
public class Varausjarjestelma {

	public static void main(String[] args) {   
		//Ensimm�inen parametri on huoneiden kokonaism��r� ja toinen luksushuoneiden m��r�
		Hotelli hotelli=new Hotelli(30, 6, 4, 80, 2, 210);
		
		//Ensimm�inen parametri on ikkunan leveys, toinen korkeus ja kolmas Hotelli-olio
		new Kayttoliittyma(480, 300, hotelli);
	}
}