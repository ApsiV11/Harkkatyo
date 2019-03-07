package Harkkatyo;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*Tietokanta-luokka käsittelee SQL-tietokannassa olevaa tietoa
 * Tietokanta-luokka sisältää metodit varauksien hakemista ja käsittelyä varten
 */

public class Tietokanta {
	
	//Attribuutit tietokannan käsittelyä varten

	private final String ohjain="org.sqlite.JDBC";
	private final String url="jdbc:sqlite:Tietokanta.db";
	
	//Konstruktori
	@SuppressWarnings("deprecation")
	public Tietokanta() throws EiTietokantaaPoikkeus {
		boolean olemassa=onkoTietokantaa();
		
		//Jos tietokantaa ei ole olemassa
		if(!olemassa) {
			throw new EiTietokantaaPoikkeus();
		}
	}
	
	//OnkoTietokantaa()-metodi tarkistaa onko tietokanta-tiedosto jo olemassa
	private boolean onkoTietokantaa() {
		File tiedosto=new File("Tietokanta.db");
		
		if(tiedosto.exists()) {
			return true;
		}
		return false;
	}
	
	//yhdistaTietokantaan()-metodi muodostaa yhteyden tietokantaan
	public Connection yhdistaTietokantaan() {
		try {
			Class.forName(ohjain);
			
			//Yhteyden muodostus
			Connection yhteys=DriverManager.getConnection(url);
			System.out.println("Yhteys muodostettu");
			
			return yhteys;
		}
		
		//Virheen käsittely
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/* haeVaraukset(int huone_id)-metodi hakee varaukset huoneen numeron perusteella
	 * Metodi tarvitsee parametrina int-arvon huone_id
	 * Metodi palauttaa ArrayList<Varaus>:n, jossa on kaikki kyseisen huoneen varaukset
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<Varaus>haeVaraukset(int huone_id) throws SQLException{
		
		//Listan alustus
		ArrayList<Varaus> lista=new ArrayList<Varaus>();
		
		//Tietokantaan yhdistäminen
		Connection yhteys=yhdistaTietokantaan();
		
		//SQL-kutsun valmistelu
		PreparedStatement lause=yhteys.prepareStatement("SELECT * FROM Varaukset WHERE hotelli_huone="+huone_id);
		
		ResultSet rs=null;
		
		//Virheen käsittely
		try {
			//Kutsun suoritus
			rs=lause.executeQuery();
			
			//Siirrytään ensimmäiselle riville
			if(!rs.first()) {
				throw new EiKyseistaHuonettaPoikkeus(huone_id);
			}
		}
		//Catch-lohko virhettä varten
		catch(EiKyseistaHuonettaPoikkeus e) {
			e.printStackTrace();
		}
			
		//Silmukassa haetaan tiedet jokaisesta huoneeseen kohdistuvasta varauksesta
		while(!rs.isLast()) {
			int varaus_nro=rs.getInt("varaus_nro");
			String varaaja=rs.getString("varaaja");
			String varaus_alku=rs.getString("varaus_alku");
			String varaus_loppu=rs.getString("varaus_loppu");
			int ol=rs.getInt("onko_luksus");
			boolean onko_luksus=false;
			
			if(ol==1) {
				onko_luksus=true;
			}
			
			//Date-olioiden luonti
			Date aloitus_paiva=new Date(Integer.parseInt(varaus_alku.substring(6, 10)),Integer.parseInt(varaus_alku.substring(3, 5)),Integer.parseInt(varaus_alku.substring(0, 2)));
			Date lopetus_paiva=new Date(Integer.parseInt(varaus_loppu.substring(6, 10)),Integer.parseInt(varaus_loppu.substring(3, 5)),Integer.parseInt(varaus_loppu.substring(0, 2)));
			
			//Luodaan Varaus-olio ylläolevista tiedoista
			Varaus varaus=new Varaus(varaus_nro,huone_id,varaaja,aloitus_paiva,lopetus_paiva,onko_luksus);
			
			//Lisätään Varaus-olio listaan
			lista.add(varaus);
			
			//Seuraava rivi tietokannassa
			rs.next();
		}
		
		yhteys.close();
		return lista;
	}
	
	//haeVaraus(int varaus_id)-metodi hakee tietokannasta yksittäisen Varauksen
	@SuppressWarnings("deprecation")
	public Varaus haeVaraus(int varaus_id) throws SQLException {
		
		//Tietokantaan yhdistys
		Connection yhteys=yhdistaTietokantaan();
		
		//SQL-komennon valmistelu
		PreparedStatement lause=yhteys.prepareStatement("SELECT * FROM Varaukset WHERE varaus_nro="+varaus_id);
		
		//ResultSet olion alustus
		ResultSet rs=null;
		
		//Virheen käsittely, jos kyseistä varausta ei ole olemassa
		try {
			rs=lause.executeQuery();
			
			if(!rs.first()) {
				throw new EiKyseistaVaraustaPoikkeus();
			}
		}
		
		//Catch-lohko virhettä varten
		catch(EiKyseistaVaraustaPoikkeus e) {
			e.printStackTrace();
		}
		
		//Haetaan tiedot tietokannasta
		int huone_id=rs.getInt("hotelli_huone");
		String varaaja=rs.getString("varaaja");
		String varaus_alku=rs.getString("varaus_alku");
		String varaus_loppu=rs.getString("varaus_loppu");
		int ol=rs.getInt("onko_luksus");
		boolean onko_luksus=false;
		
		//Boolean arvon luonti tietokannan ykkösestä tai nollasta
		if(ol==1) {
			onko_luksus=true;
		}
		
		//Date-olioiden luonti
		Date aloitus_paiva=new Date(Integer.parseInt(varaus_alku.substring(6, 10)),Integer.parseInt(varaus_alku.substring(3, 5)),Integer.parseInt(varaus_alku.substring(0, 2)));
		Date lopetus_paiva=new Date(Integer.parseInt(varaus_loppu.substring(6, 10)),Integer.parseInt(varaus_loppu.substring(3, 5)),Integer.parseInt(varaus_loppu.substring(0, 2)));
		
		//Luodaan Varaus-olio ylläolevista tiedoista
		Varaus varaus=new Varaus(huone_id,varaaja,aloitus_paiva,lopetus_paiva,onko_luksus);
		
		//Suljetaan yhteys
		yhteys.close();
		return varaus;
	}
}

//Poikkeusluokka sellaista tilannetta varten, jossa tietokanta-tiedostoa ei löydy
class EiTietokantaaPoikkeus extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EiTietokantaaPoikkeus() {
		super("Virhe! Tietokanta.db-tiedostoa ei löydy!");
	}
}