import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/*Tietokanta-luokka k�sittelee SQL-tietokannassa olevaa tietoa
 * Tietokanta-luokka sis�lt�� metodit varauksien hakemista ja k�sittely� varten
 */

public class Tietokanta {
	private Arraylist<Varaus> varaukset;
	private final String tiedosto_nimi="varaukset.db";
	private final String ohjain="org.sqlite.JDBC";
	private final String url="jdbc:sqlite:\\varaukset.db";
	
	//Konstruktori
	public Tietokanta() {
		boolean olemassa=onkoTietokantaa();
		
		if(!olemassa) {
			luoTietokanta();
		}
	}
	
	//OnkoTietokantaa()-metodi tarkistaa onko tietokanta-tiedosto jo olemassa
	private boolean onkoTietokantaa() {
		File tiedosto=new File("varaukset.db");
		
		if(tiedosto.exists()) {
			return true;
		}
		return false;
	}
	
	/*luoTietokanta()-metodi luo tietokannan, jos sit� ei ole viel� olemassa. 
	 * Metodi my�s lue tietokantaan Varaukset-taulukon, johon voidaan sy�tt��
	 * tulevat varaukset.
	 */
	private void luoTietokanta(){
		 
        try (Connection yhteys = DriverManager.getConnection(url)) {
        	//Tietokannan olemassaolon testaus
            if (yhteys != null) {
            	
            	//Luodaan tietokanta
                DatabaseMetaData meta = yhteys.getMetaData();
                System.out.println("Ohjaimen nimi on " + meta.getDriverName());
                System.out.println("Tietokanta luotu");
                }
            }
        //Virheen k�sittely
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        //Yhdistet��n tietokantaan
        Connection yhteys=yhdistaTietokantaan();
        
        //Valmistellaan SQL-lause taulukon luomista varten
        PreparedStatement lause=yhteys.prepareStatement("CREATE TABLE Varaukset" +
        		 										"(varaus_nro int NOT NULL UNIQUE PRIMARY KEY,"+
        												"hotelli_huone int NOT NULL," + 
        												"varaaja string NOT NULL," + 
        												"varaus_alku String NOT NULL," +
        												"varaus_loppu String NOT NULL," +
        												"onko_luksus onko_luksus NOT NULL);");
        System.out.println("Taulukko luotu");
        
        //Yhteyden katkaiseminen
        yhteys.close();
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
}
