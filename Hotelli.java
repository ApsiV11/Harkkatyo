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
	public ArrayList<Huone> haeVapaat(boolean onko_luksus,Date aloitus_paiva,Date lopetus_paiva) {
		ArrayList<Huone> huoneet=new ArrayList<Huone>();
		
		Tietokanta tk=null;
		try {
			tk = new Tietokanta();
		} catch (EiTietokantaaPoikkeus e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Connection yhteys=tk.yhdistaTietokantaan();
		
		int i=0;
		
		if(onko_luksus) {
			i=huonemaara-luksushuoneet;
		}
		
		ResultSet rs=null;
		
		//K‰yd‰‰n kaikki huoneet l‰pi
		for(;i<huonemaara;i++) {
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
					int vavuosi=Integer.parseInt(varaus_alku.substring(0,4));
					int vakuukausi=Integer.parseInt(varaus_alku.substring(5,7));
					int vapaiva=Integer.parseInt(varaus_alku.substring(8,10));
					Date alku=new Date(vavuosi,vakuukausi,vapaiva);
					
					String varaus_loppu=rs.getString("varaus_loppu");
					int vlvuosi=Integer.parseInt(varaus_loppu.substring(0,4));
					int vlkuukausi=Integer.parseInt(varaus_loppu.substring(5,7));
					int vlpaiva=Integer.parseInt(varaus_loppu.substring(8,10));
					Date loppu=new Date(vlvuosi,vlkuukausi,vlpaiva);
					
					//Alku ja loppu ovat vertailtavan varauksen p‰iv‰m‰‰ri‰
					
					//Testataan ovatko varaukset limitt‰iset
					if(!alku.after(lopetus_paiva) && !loppu.before(aloitus_paiva)) {
						kayko=false;
						break;
					}
					
				}
				
				//Jos kayko-muuttuja on edelleen true, niin huone on vapaa
				if(kayko) {
					huoneet.add(new Huone(i,4));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return huoneet;
	}

	public int getHuonemaara() {
		return huonemaara;
	}

	public int getLuksushuoneet() {
		return luksushuoneet;
	}
}