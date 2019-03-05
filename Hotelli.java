package Harkkatyo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Hotelli {
	private final int huonemaara;
	private final int luksushuoneet;
	private final ArrayList<Huone> huoneet=new ArrayList<Huone>();
	
	public Hotelli(int huonemaara, int luksushuoneet){
		this.huonemaara=huonemaara;
		this.luksushuoneet=luksushuoneet;
		
		for(int i=0;i<huonemaara-luksushuoneet;i++) {
			huoneet.add(new Perushuone(i,4));
		}
		for(int i=0;i<luksushuoneet;i++) {
			huoneet.add(new Luksushuone(i,2));
		}
	}
	
	@SuppressWarnings("deprecation")
	public Huone haeVapaa(boolean onko_luksus,Date aloitus_paiva,Date lopetus_paiva) {
		
		Tietokanta tk=null;
		try {
			tk = new Tietokanta();
		} catch (EiTietokantaaPoikkeus e1) {
			e1.printStackTrace();
		}
		
		Connection yhteys=tk.yhdistaTietokantaan();
		
		int i=0;
		int max=huonemaara-luksushuoneet;
		
		if(onko_luksus) {
			i=huonemaara-luksushuoneet;
			max=huonemaara;
		}
		
		ResultSet rs=null;
		
		//Käydään kaikki huoneet läpi
		for(;i<max;i++) {
			try {
				//SQL-kysely, jolla pyydetään varauksia, joiden huoneen numero on sama kuin huonemaara-muuttuja
				PreparedStatement lause=yhteys.prepareStatement("SELECT varaus_alku, varaus_loppu FROM Varaukset WHERE hotelli_huone="+i);
				
				//Lähetetään pyyntö
				rs=lause.executeQuery();
				
				//Boolean-muuttuja huoneen käytettävyyttä varten
				boolean kayko=true;
				
				//Käydään kaikki saadut varaukset läpi
				while(rs.next()) {
					
					//Luodaan päivämäärät tietokannan tiedoista
					String varaus_alku=rs.getString("varaus_alku");
					int vavuosi=Integer.parseInt(varaus_alku.substring(0,4));
					int vakuukausi=Integer.parseInt(varaus_alku.substring(5,7));
					int vapaiva=Integer.parseInt(varaus_alku.substring(8,10));
					Date alku=new Date(vavuosi-1900,vakuukausi-1,vapaiva);
					
					String varaus_loppu=rs.getString("varaus_loppu");
					int vlvuosi=Integer.parseInt(varaus_loppu.substring(0,4));
					int vlkuukausi=Integer.parseInt(varaus_loppu.substring(5,7));
					int vlpaiva=Integer.parseInt(varaus_loppu.substring(8,10));
					Date loppu=new Date(vlvuosi-1900,vlkuukausi-1,vlpaiva);
					
					//Alku ja loppu ovat vertailtavan varauksen päivämääriä
					
					System.out.println("Vanha varaus: "+alku.toString()+"-"+loppu.toString());
					System.out.println();
					System.out.println("Uusi varaus: "+aloitus_paiva.toString()+"-"+lopetus_paiva.toString());
					
					//Testataan ovatko varaukset limittäiset
					if(!(lopetus_paiva.before(alku) || aloitus_paiva.after(loppu))) {
						kayko=false;
						break;
					}
					System.out.println(kayko);
					
				}
				
				//Jos kayko-muuttuja on edelleen true, niin huone on vapaa
				//Palautetaan ensimmäinen vapaa huone
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