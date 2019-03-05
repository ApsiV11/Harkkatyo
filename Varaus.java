package Harkkatyo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Varaus {
	private int varaus_id;
	private Date aloitus_paiva;
	private Date lopetus_paiva;
	private int huone_id;
	private String varaaja;
	private boolean onko_luksus;
	
	public Varaus(int varaus_id, int huone_id, String varaaja, Date aloitus_paiva, Date lopetus_paiva, boolean onko_luksus) {
		this.varaus_id=varaus_id;
		this.huone_id=huone_id;
		this.varaaja=varaaja;
		this.aloitus_paiva=aloitus_paiva;
		this.lopetus_paiva=lopetus_paiva;
		this.onko_luksus=onko_luksus;
	}
	
	public Varaus(int huone_id, String varaaja, Date aloitus_paiva, Date lopetus_paiva, boolean onko_luksus) {
		this.huone_id=huone_id;
		this.varaaja=varaaja;
		this.aloitus_paiva=aloitus_paiva;
		this.lopetus_paiva=lopetus_paiva;
		this.onko_luksus=onko_luksus;
	}
	
	public void varausTietokantaan() {
		Tietokanta tk=null;
		try {
			tk=new Tietokanta();
		} catch (EiTietokantaaPoikkeus e) {
			e.printStackTrace();
		}
		
		Connection yhteys=tk.yhdistaTietokantaan();
		
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		
		
		String alku=sdf.format(aloitus_paiva);
		String loppu=sdf.format(lopetus_paiva);
		int luksus=0;
		
		if(onko_luksus) {
			luksus=1;
		}
		try {
			
			PreparedStatement lause=yhteys.prepareStatement("INSERT INTO Varaukset (hotelli_huone, varaaja, varaus_alku,"
					+ "varaus_loppu, onko_luksus) VALUES ("+this.huone_id+", \""+this.varaaja+"\", \""+alku+"\", \""+loppu+"\", "+luksus+");");
			lause.executeUpdate();
			
			System.out.println("Tiedot siirretty onnistuneesti");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public int getVaraus_id() {
		return varaus_id;
	}
	public void setVaraus_id(int varaus_id) {
		this.varaus_id = varaus_id;
	}
	public Date getAloitus_paiva() {
		return aloitus_paiva;
	}
	public void setAloitus_paiva(Date aloitus_paiva) {
		this.aloitus_paiva = aloitus_paiva;
	}
	public Date getLopetus_paiva() {
		return lopetus_paiva;
	}
	public void setLopetus_paiva(Date lopetus_paiva) {
		this.lopetus_paiva = lopetus_paiva;
	}
	public int getHuone_id() {
		return huone_id;
	}
	public void setHuone_id(int huone_id) {
		this.huone_id = huone_id;
	}
	public String getVaraaja() {
		return varaaja;
	}
	public void setVaraaja(String varaaja) {
		this.varaaja = varaaja;
	}
	public boolean isOnko_luksus() {
		return onko_luksus;
	}
	public void setOnko_luksus(boolean onko_luksus) {
		this.onko_luksus = onko_luksus;
	}
}
class EiKyseistaVaraustaPoikkeus extends Exception{
	public EiKyseistaVaraustaPoikkeus() {
		super("Ei kyseistä varausta");
	}
}