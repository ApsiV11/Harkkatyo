package Harkkatyo;

//T�m� on ty�n aloitusluokka
public class Varausjarjestelma {

	public static void main(String[] args) {   
		//Ensimm�inen parametri on huoneiden kokonaism��r� ja toinen luksushuoneiden m��r�
		//Kolmas on perushuoneen henkil�iden m��r� ja nelj�s perushuoneen hinta per y�
		//Viides on luksushuoneen henkil�iden m��r� ja kuudes luksushuoneen hinta per y�
		Hotelli hotelli=new Hotelli(30, 6, 4, 80, 2, 210);
		
		//Ensimm�inen parametri on ikkunan leveys, toinen korkeus ja kolmas Hotelli-olio
		//Ikkunan kokoa ei kannata muuttaa, koska k�ytt�liittym� ei ole valitettavasti skaalautuva
		new Kayttoliittyma(480, 300, hotelli);
	}
}