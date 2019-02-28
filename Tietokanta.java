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
import java.util.Date;

/*Tietokanta-luokka k�sittelee SQL-tietokannassa olevaa tietoa
 * Tietokanta-luokka sis�lt�� metodit varauksien hakemista ja k�sittely� varten
 */
public class Tietokanta{
	public static void main(String[] args) {
		try {
			Tietokanta1 tk=new Tietokanta1();
		}
		catch(EiTietokantaaPoikkeus e) {
			e.printStackTrace();
		}
	}
}

class Tietokanta1 {
	private ArrayList<Varaus> varaukset;
	private final String ohjain="org.sqlite.JDBC";
	private final String url="jdbc:sqlite:Tietokanta.db";
	
	//Konstruktori
	public Tietokanta1() throws EiTietokantaaPoikkeus {
		boolean olemassa=onkoTietokantaa();
		
		if(!olemassa) {
			throw new EiTietokantaaPoikkeus();
		}
		
		try {
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
			System.out.println("Suoritettu");
			}
		catch(SQLException e) {
			e.printStackTrace();
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
		
		//Virheen k�sittely
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/* haeVaraukset(int huone_id)-metodi hakee varaukset huoneen numeron perusteella
	 * Metodi tarvitsee parametrina int-arvon huone_id
	 * Metodi palauttaa ArrayList<Varaus>:n, jossa on kaikki kyseisen huoneen varaukset
	 */
	public ArrayList<Varaus>haeVaraukset(int huone_id){
		
		//Listan alustus
		ArrayList<Varaus> lista=new ArrayList<Varaus>();
		
		//Tietokantaan yhdist�minen
		Connection yhteys=yhdistaTietokantaan();
		
		//SQL-kutsun valmistelu
		PreparedStatement lause=yhteys.prepareStatement("SELECT * FROM Varaukset WHERE hotelli_huone="+huone_id);
		
		//Kutsun suoritus
		ResultSet rs=lause.executeQuery();
		
		//Siirryt��n ensimm�iselle riville
		rs.first();
		
		//Silmukassa haetaan tiedet jokaisesta huoneeseen kohdistuvasta varauksesta
		while(!rs.isLast()) {
			int varaus_nro=rs.getInt("varaus_nro");
			String varaaja=rs.getString("varaaja");
			Date varaus_alku=rs.getDate("varaus_alku");
			Date varaus_loppu=rs.getDate("varaus_loppu");
			int ol=rs.getInt("onko_luksus");
			boolean onko_luksus=false;
			
			if(ol==1) {
				onko_luksus=true;
			}
			
			//Luodaan Varaus-olio yll�olevista tiedoista
			Varaus varaus=new Varaus(varaus_nro,varaaja,varaus_alku,varaus_loppu,onko_luksus);
			
			//Lis�t��n Varaus-olio listaan
			lista.add(varaus);
			
			//Seuraava rivi tietokannassa
			rs.next();
		}
		
		
		return lista;
	}
}

//Poikkeusluokka sellaista tilannetta varten, jossa tietokanta-tiedostoa ei l�ydy
class EiTietokantaaPoikkeus extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EiTietokantaaPoikkeus() {
		super("Virhe! Tietokanta.db-tiedostoa ei l�ydy!");
	}
}
