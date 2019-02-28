import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/*Tietokanta-luokka käsittelee SQL-tietokannassa olevaa tietoa
 * Tietokanta-luokka sisältää metodit varauksien hakemista ja käsittelyä varten
 */

public class Tietokanta {
	private ArrayList<Varaus> varaukset;
	private final String tiedosto_nimi="varaukset.db";
	private final String ohjain="org.sqlite.JDBC";
	private final String url="jdbc:sqlite:\\varaukset.db";
	
	//Konstruktori
	public Tietokanta() {
		boolean olemassa=onkoTietokantaa();
		
		if(!olemassa) {
			throw new EiTietokantaaPoikkeus();
		}
		
		Connection yhteys=yhdistaTietokantaan();
		PreparedStatement lause=yhteys.prepareStatement("SELECT * FROM Varaukset");
		ResultSet rs=lause.executeQuery();
		while(rs.next()) {
			int varaus_nro=rs.getInt("varaus_nro");
			int huone_id=rs.getInt("hotelli_huone");
			String varaaja=rs.getString("varaaja");
			Date varaus_alku=rs.getDate("varaus_alku");
			Date varaus_loppu=rs.getDate("varaus_loppu");
			int onko_luksus=rs.getInt("onko_luksus");
			
			varaukset.add(new Varaus(varaus_nro,huone_id,varaaja,varaus_alku,varaus_loppu,onko_luksus));
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
	private Connection yhdistaTietokantaan() {
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
}

class EiTietokantaaPoikkeus extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EiTietokantaaPoikkeus() {
		super("Virhe! Tietokanta.db-tiedostoa ei löydy!");
	}
}
