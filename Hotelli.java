package Harkkatyo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Hotelli {
	//Attribuutteja ovat huoneiden m‰‰r‰ ja luksushuoneiden m‰‰r‰ ja huonelista
	private final int huonemaara;
	private final int luksushuoneet;
	private final ArrayList<Huone> huoneet=new ArrayList<Huone>();
	private final Perushuone ph;
	private final Luksushuone lh;
	
	//Konstruktori saa parametrinaan huoneiden m‰‰r‰n ja luksushuoneiden m‰‰r‰n
	//Perushuoneita on huonemaara-luksushuoneet ja Luksushuoneita luksushuoneet-parametrin maara
	public Hotelli(int huonemaara, int luksushuoneet, int hmaara_perus, int hinta_perus, int hmaara_luksus, int hinta_luksus){
		this.huonemaara=huonemaara;
		this.luksushuoneet=luksushuoneet;
		this.ph=new Perushuone(hinta_perus, 0, hmaara_perus);
		this.lh=new Luksushuone(hinta_luksus, 0, hmaara_luksus);
		
		for(int i=0;i<huonemaara-luksushuoneet;i++) {
			huoneet.add(new Perushuone(hinta_perus, i, hmaara_perus));
		}
		for(int i=0;i<luksushuoneet;i++) {
			huoneet.add(new Luksushuone(hinta_luksus, i, hmaara_luksus));
		}
	}
	
	public Perushuone getPh() {
		return ph;
	}

	public Luksushuone getLh() {
		return lh;
	}


	//haeVapaa(boolean onko_luksus,Date aloitus_paiva,Date lopetus_paiva)-metodi hakee Hotelli-oliosta ensimm‰isen vapaan Huone-olion.
	//Funktio palauttaa tyhj‰n Huone-olion, jos kaikki huoneet on varattu kyseisell‰ aikav‰lill‰
	@SuppressWarnings("deprecation")
	public Huone haeVapaa(boolean onko_luksus,Date aloitus_paiva,Date lopetus_paiva) {
		
		//Luodaan Tietokanta-olio tietokannan k‰yttˆ‰ varten
		Tietokanta tk=null;
		try {
			tk = new Tietokanta();
		} catch (EiTietokantaaPoikkeus e1) {
			e1.printStackTrace();
		}
		
		//Tietokantaan yhdistys
		Connection yhteys=tk.yhdistaTietokantaan();
		
		//Aloituksen ja lopun asetus riippuen siit‰, haetaanko Luksushuoneita vai Perushuoneita
		//Luksushuoneet ovat Hotelli-oliossa huoneet-listan lopussa, joten ensimm‰isen Luksushuone-olion indeksi on huonemaara-luksushuoneet

		int i=0;
		int max=huonemaara-luksushuoneet;
		
		if(onko_luksus) {
			i=huonemaara-luksushuoneet;
			max=huonemaara;
		}
		
		//ResultSet-olion alustus tietokannan antaman tuloksen k‰sittely‰ varten
		ResultSet rs=null;
		
		//K‰yd‰‰n kaikki huoneet l‰pi
		for(;i<max;i++) {
			try {
				//SQL-kysely, jolla pyydet‰‰n varauksia, joiden huoneen numero on sama kuin huonemaara-muuttuja
				PreparedStatement lause=yhteys.prepareStatement("SELECT varaus_alku, varaus_loppu FROM Varaukset WHERE hotelli_huone="+i);
				
				//L‰hetet‰‰n pyyntˆ
				rs=lause.executeQuery();
				
				//Boolean-muuttuja huoneen k‰ytett‰vyytt‰ varten
				boolean kayko=true;
				
				//K‰yd‰‰n kaikki saadut varaukset l‰pi
				while(rs.next()) {
					
					//Luodaan p‰iv‰m‰‰r‰t tietokannan tiedoista
					String varaus_alku=rs.getString("varaus_alku");
					int vavuosi=Integer.parseInt(varaus_alku.substring(6,10));
					int vakuukausi=Integer.parseInt(varaus_alku.substring(3,5));
					int vapaiva=Integer.parseInt(varaus_alku.substring(0,2));
					Date alku=new Date(vavuosi-1900,vakuukausi-1,vapaiva);
					
					String varaus_loppu=rs.getString("varaus_loppu");
					int vlvuosi=Integer.parseInt(varaus_loppu.substring(6,10));
					int vlkuukausi=Integer.parseInt(varaus_loppu.substring(3,5));
					int vlpaiva=Integer.parseInt(varaus_loppu.substring(0,2));
					Date loppu=new Date(vlvuosi-1900,vlkuukausi-1,vlpaiva);
					
					//Alku ja loppu ovat vertailtavan varauksen p‰iv‰m‰‰ri‰
					
					System.out.println("Vanha varaus: "+alku.toString()+"-"+loppu.toString());
					System.out.println();
					System.out.println("Uusi varaus: "+aloitus_paiva.toString()+"-"+lopetus_paiva.toString());
					
					//Testataan ovatko varaukset limitt‰iset
					if(!(lopetus_paiva.before(alku) || aloitus_paiva.after(loppu))) {
						kayko=false;
						break;
					}
					System.out.println(kayko);
					
				}
				
				//Jos kayko-muuttuja on edelleen true, niin huone on vapaa
				//Palautetaan ensimm‰inen vapaa huone
				if(kayko) {
					yhteys.close();
					return new Huone(i,4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			yhteys.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getHuonemaara() {
		return huonemaara;
	}

	public int getLuksushuoneet() {
		return luksushuoneet;
	}
}