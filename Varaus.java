package Harkkatyo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Varaus {
	//Attribuutti varaukselle
	private int varaus_id;
	private Date aloitus_paiva;
	private Date lopetus_paiva;
	private int huone_id;
	private String varaaja;
	private boolean onko_luksus;
	
	//Konstrukti varaus_id:n kanssa
	public Varaus(int varaus_id, int huone_id, String varaaja, Date aloitus_paiva, Date lopetus_paiva, boolean onko_luksus) {
		this.varaus_id=varaus_id;
		this.huone_id=huone_id;
		this.varaaja=varaaja;
		this.aloitus_paiva=aloitus_paiva;
		this.lopetus_paiva=lopetus_paiva;
		this.onko_luksus=onko_luksus;
	}
	
	//Konstruktori ilman varaus_id:t‰
	public Varaus(int huone_id, String varaaja, Date aloitus_paiva, Date lopetus_paiva, boolean onko_luksus) {
		this.huone_id=huone_id;
		this.varaaja=varaaja;
		this.aloitus_paiva=aloitus_paiva;
		this.lopetus_paiva=lopetus_paiva;
		this.onko_luksus=onko_luksus;
	}
	
	//varausTietokantaan()-metodi vie kyseisen Varaus-olion tiedot tietokantaan
	public void varausTietokantaan() {	
		//Tietokanta olion luonti
		Tietokanta tk=null;
		try {
			tk=new Tietokanta();
		} catch (EiTietokantaaPoikkeus e) {
			e.printStackTrace();
		}
		
		//Tietokanta-yhteyden muodostus
		Connection yhteys=tk.yhdistaTietokantaan();
		
		//Luodaan SimpleDateFormat-olio p‰iv‰m‰‰rien muotoilua varten
		//P‰iv‰m‰‰r‰t tallennetaan numeroina vuosi ensin
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-YYYY");
		
		//Muotoillaan String-oliot paiv‰m‰‰rist‰
		String alku=sdf.format(aloitus_paiva);
		String loppu=sdf.format(lopetus_paiva);
		
		//Asetetaan luksus arvo
		int luksus=0;
		
		if(onko_luksus) {
			luksus=1;
		}
		try {
			//Luodaan SQL-komento
			PreparedStatement lause=yhteys.prepareStatement("INSERT INTO Varaukset (hotelli_huone, varaaja, varaus_alku,"
					+ "varaus_loppu, onko_luksus) VALUES ("+this.huone_id+", \""+this.varaaja+"\", \""+alku+"\", \""+loppu+"\", "+luksus+");");
			
			//Suoritetaan p‰ivityskomento
			lause.executeUpdate();
			
			System.out.println("Tiedot siirretty onnistuneesti");
			
			//Suljetaan yhteys
			yhteys.close();
		}
		
		//Virheen k‰sittely
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

//Virhe-luokka sit‰ tilannetta varten, jossa kysytty‰ varausta ei lˆydy
class EiKyseistaVaraustaPoikkeus extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EiKyseistaVaraustaPoikkeus() {
		super("Ei kyseist‰ varausta");
	}
}