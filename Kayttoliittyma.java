package Harkkatyo;
import java.util.Calendar;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Kayttoliittyma extends JPanel{
	
	//alustetaan attribuutit
	private Hotelli hotelli;
	private int leveys;
	private int korkeus;
	private JFrame ikkunankehys;
	private JPanel varaaminen, tiedot, peruutus;
	
	private JLabel enimi_teksti, snimi_teksti, apvm_teksti, lpvm_teksti, lh_teksti, vn_teksti;
	private JTextField enimi, snimi, vnumero;
	private JComboBox<Integer> dropPaivat, dropVuodet, dropPaivat2, dropVuodet2;
	private JComboBox<String> dropKuukaudet, dropKuukaudet2;
	private JCheckBox luksushuone;
	private String virheviesti;
	private UIManager UI;
	private JButton lahetatiedot_nappi, naytatiedot_nappi, peruutavaraus_nappi, peruuta_nappi, teevaraus_nappi;
	
	//Konstruktori
	//Parametreina käyttöliittymäikkunan leveys, korkeus ja Hotelli-olio
	@SuppressWarnings("deprecation")
	public Kayttoliittyma(int leveys, int korkeus, Hotelli hotelli) {
		this.hotelli=hotelli;
		this.leveys = leveys;
		this.korkeus = korkeus;
			
		//luodaan uusi ikkuna
		ikkunankehys=new JFrame();
			
		//asetetaan ikkunalle leveys, korkeus, oletussijainti, sulkemisoperaatio, otsikko, väri
		
		ikkunankehys.setSize(new Dimension(leveys, korkeus));
		ikkunankehys.setLocationRelativeTo(null);
		ikkunankehys.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ikkunankehys.setTitle("Huoneen varaus");
		ikkunankehys.setResizable(false);
		ikkunankehys.setLayout(null);
			
		//Virheenkäsittely kuvan puuttumista varten

		try {
			ikkunankehys.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("gui-tausta.jpg")))));
		}catch (IOException e) {
			System.out.println("Ei löytynyt taustakuvaa");
		}
		
		Container sisalto=ikkunankehys.getContentPane();
		
		varaaminen=new Elementit(0, 0, sisalto.getInsets()).lisaaPaneeli(leveys, korkeus);
		tiedot=new Elementit(0, 0, sisalto.getInsets()).lisaaPaneeli(leveys, korkeus);
		peruutus=new Elementit(0, 0, sisalto.getInsets()).lisaaPaneeli(leveys, korkeus);
		
		//Objektien lisääminen varaamis-ikkunaan
		enimi_teksti=new Elementit(10,30*1, sisalto.getInsets()).lisaaTeksti("Etunimi: ");
		snimi_teksti=new Elementit(10,30*2, sisalto.getInsets()).lisaaTeksti("Sukunimi: ");
		apvm_teksti=new Elementit(10,30*3, sisalto.getInsets()).lisaaTeksti("Aloituspäivämäärä: ");
		lpvm_teksti=new Elementit(10,30*4, sisalto.getInsets()).lisaaTeksti("Lopetuspäivämäärä: ");
		lh_teksti=new Elementit(10,30*5, sisalto.getInsets()).lisaaTeksti("Luksushuone: ");
		
		varaaminen.add(enimi_teksti);
		varaaminen.add(snimi_teksti);
		varaaminen.add(apvm_teksti);
		varaaminen.add(lpvm_teksti);
		varaaminen.add(lh_teksti);
		
		enimi=new Elementit(243,30*1+3, sisalto.getInsets()).lisaaTekstiKentta(5);
		snimi=new Elementit(243,30*2+3, sisalto.getInsets()).lisaaTekstiKentta(5);
		
		varaaminen.add(enimi);
		varaaminen.add(snimi);
		
		luksushuone=new Elementit(180,30*5+8, sisalto.getInsets()).lisaaCheckBox("");
		
		varaaminen.add(luksushuone);
		
		dropPaivat=new Elementit(243,30*3+3, sisalto.getInsets()).lisaaDropDownPaivat();
		dropKuukaudet=new Elementit(288,30*3+3, sisalto.getInsets()).lisaaDropDownKuukaudet();
		dropVuodet=new Elementit(393,30*3+3, sisalto.getInsets()).lisaaDropDownVuosi();
		
		varaaminen.add(dropPaivat);
		varaaminen.add(dropKuukaudet);
		varaaminen.add(dropVuodet);
		
		dropPaivat2=new Elementit(243,30*4+3, sisalto.getInsets()).lisaaDropDownPaivat();
		dropKuukaudet2=new Elementit(288,30*4+3, sisalto.getInsets()).lisaaDropDownKuukaudet();
		dropVuodet2=new Elementit(393,30*4+3, sisalto.getInsets()).lisaaDropDownVuosi();
		
		varaaminen.add(dropPaivat2);
		varaaminen.add(dropKuukaudet2);
		varaaminen.add(dropVuodet2);
		
		lahetatiedot_nappi=new Elementit(330,30*6+25, sisalto.getInsets()).lisaaNappi("Lähetä tiedot");
		naytatiedot_nappi=new Elementit(180,30*6+25, sisalto.getInsets()).lisaaNappi("Näytä tiedot");
		peruutavaraus_nappi=new Elementit(30,30*6+25, sisalto.getInsets()).lisaaNappi("Peruuta varaus");
		
		//Lähetä-napin painallusta tarkkaileva metodi
		lahetatiedot_nappi.addActionListener(new ActionListener() {
			
			@SuppressWarnings({ "deprecation", "static-access" })
			public void actionPerformed(ActionEvent e){
				UI=new UIManager();
				UI.put("OptionPane.background", new Color(255,20,147));
				UI.put("Panel.background", new Color(255,20,147));
				
				boolean oikeellisuus=tarkistaSisallot();
		    	 
				//Jos tiedot ovat kunnossa
				if(oikeellisuus){
		    		  
					//Luodaan päivä-oliot vertailua varten
					Date aloitus_paiva=new Date(dropVuodet.getSelectedIndex()-1900+Calendar.getInstance().get(Calendar.YEAR), dropKuukaudet.getSelectedIndex(), dropPaivat.getSelectedIndex()+1);
					Date lopetus_paiva=new Date(dropVuodet2.getSelectedIndex()-1900+Calendar.getInstance().get(Calendar.YEAR), dropKuukaudet2.getSelectedIndex(), dropPaivat2.getSelectedIndex()+1);
		    		  
		    		  //Etsitään vapaat huoneet
		    		  Huone huone=hotelli.haeVapaa(luksushuone.isSelected(),aloitus_paiva,lopetus_paiva);
		    		  if(huone==null) {
		    			  JOptionPane.showMessageDialog(ikkunankehys, "Hotelli on täynnä valitsemallasi aikavälillä, valitse uudet päivät", "Hotelli on täynnä", JOptionPane.ERROR_MESSAGE);
		    		  }
		    		  
		    		  //Jos vapaa huone löytyy
		    		  else{
		    			  //Otetaan tiedot käyttöliittymästä
		    			  String varaaja=enimi.getText()+" "+snimi.getText();
			    		  boolean onko_luksus=luksushuone.isSelected();
			    		  
			    		  //Luodaan Varaus-olio
			    		  Varaus varaus=new Varaus(huone.getNumero(), varaaja, aloitus_paiva, lopetus_paiva, onko_luksus);
			    		  
			    		  //Lähetetään tiedot tietokantaan
			    		  varaus.varausTietokantaan();
			    		  
			    		  JOptionPane.showMessageDialog(ikkunankehys, "Varaus tehtiin onnistuneesti","Onnistui!",JOptionPane.DEFAULT_OPTION);
			    		  //tyhjentää elementit arvoistaan
			    		  
			    		  enimi.setText("");
			    		  snimi.setText("");
			    		  luksushuone.setSelected(false);
			    		  dropPaivat.setSelectedIndex(0);
			    		  dropKuukaudet.setSelectedIndex(0);
			    		  dropVuodet.setSelectedIndex(0);
			    		  dropPaivat2.setSelectedIndex(0);
			    		  dropKuukaudet2.setSelectedIndex(0);
			    		  dropVuodet2.setSelectedIndex(0);
		    		  }
		    	  }
		    	  else {
			    	  JOptionPane.showMessageDialog(ikkunankehys,
			    			    virheviesti,
			    			    "Virhe",
			    			    JOptionPane.ERROR_MESSAGE);
		    	  }
		      }
		});
		
		//Näytä-napin metodi
		naytatiedot_nappi.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				//piilotetaan vanhat elementit
				varaaminen.hide();
				
				//Tehdään tieto-paneeli näkyväksi
				tiedot.show();	
			}		
		});
		
		//Peruuta-napin metodi
		peruutavaraus_nappi.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {		
				//Piilotetaan vanhat elementit
				varaaminen.hide();
				
	    		//Tehdään peruutus-ikkunan näkyväksi
	    		peruutus.show();
			}	
		});
		
		varaaminen.add(lahetatiedot_nappi);
		varaaminen.add(naytatiedot_nappi);
		varaaminen.add(peruutavaraus_nappi);
		//Objektien laittaminen varaamis-ikkunaan loppuu
		
		
		//Objektit peruutus-ikkunaan
		vn_teksti=new Elementit(150,30*2, sisalto.getInsets()).lisaaTeksti("Varausnumero: ");
		peruutus.add(vn_teksti);
		
		vnumero=new Elementit(150,30*3+3, sisalto.getInsets()).lisaaTekstiKentta(-24);
		peruutus.add(vnumero);
		
		teevaraus_nappi=new Elementit(30,30*6+25, sisalto.getInsets()).lisaaNappi("Tee varaus");
		peruuta_nappi=new Elementit(330,30*6+25, sisalto.getInsets()).lisaaNappi("Peruuta");
		
		teevaraus_nappi.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				//piilotetaan vanhat elementit
				peruutus.hide();
				varaaminen.show();
			}
			
		});
		
		peruutus.add(teevaraus_nappi);
		peruutus.add(peruuta_nappi);
		//Objektien laittaminen peruutus-ikkunaan loppuu
		
		//Objektit tiedot-ikkunaan
		//Objektien laittaminen tiedot-ikkunaan loppuu
		
		tiedot.hide();
		peruutus.hide();
		
		sisalto.add(varaaminen);
		sisalto.add(peruutus);
		sisalto.add(tiedot);
		//asetetaan ikkuna näkyväksi
		ikkunankehys.setVisible(true);
	}
	
	//TarkistaSisallot()-metodi tutkii käyttöliittymän objektien sisällöt ja varmistaa, että ne ovat vaatimuksien mukaiset
	//Etu ja sukunimen pitää olla 2 ja 16 merkin välillä ja alkupäivämäärän pitää olla ennen loppupäivämäärää
	///Lisäksi päivämäärien tulee olla mahdolliset eli 30. helmikuuta ei käy.
	//Päivämäärien pitää olla myös nykyisen päivän jälkeen
	public boolean tarkistaSisallot() {
		//tarkistetaan, että nimien pituus ei ole yli 32 merkkiä tai että nimi ei ole tyhjä merkkijono
		if ((enimi.getText()).length()>=16 || (snimi.getText()).length()>=16 || (enimi.getText()).length()<2 || (snimi.getText()).length()<2){ 
			virheviesti="Nimien tulee olla 2-16 merkkiä";
			return false;
		}
		
		//parsitaan päivämäärät "dd-MM-yyyy" -muotoon
		
		String aloituspvm=String.valueOf(dropPaivat.getSelectedIndex()+1)+"-"+String.valueOf(dropKuukaudet.getSelectedIndex()+1)+"-"+String.valueOf(dropVuodet.getSelectedItem());
		String lopetuspvm=String.valueOf(dropPaivat2.getSelectedIndex()+1)+"-"+String.valueOf(dropKuukaudet2.getSelectedIndex()+1)+"-"+String.valueOf(dropVuodet2.getSelectedItem());
		
		Date aloitusDate=null;
		Date lopetusDate=null;

		//muutetaan päivämäärät Date -olioiksi, ja samalla tarkastetaan onko ne mahdollisia
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			aloitusDate=sdf.parse(aloituspvm);
			lopetusDate=sdf.parse(lopetuspvm);
		}
		catch(Exception e) {
			virheviesti="Päivämäärä(t) ovat virheellisiä";
			return false;
		}
		
		//tarkistetaan, ettei lopetuspäivämäärä ole ennen aloituspäivämäärää
		if (lopetusDate.after(aloitusDate)) {}
		else {
			virheviesti="Varauksen loppu on sama tai ennen sen alkua";
			return false;
		}
		
		//Nykyinen päivä 
		LocalDateTime n = LocalDateTime.now();
		
		//Vähennetään nykyhetkestä yksi, että kuluvalle päivälle voi tehdä varauksen
		n=n.minusDays(1);
		
		Date nyt=java.sql.Timestamp.valueOf(n);
		
		//tarkistetaan, ettei aloituspäivämäärä ole ennen nykyhetkeä
		if (aloitusDate.after(nyt)) {}
		else {
			virheviesti="Vanhentunut päivämäärä, et voi tehdä varausta menneisyyteen";
			return false;
		}
		return true;
	}
}

//Elementit-luokka käyttöliittymää varten
//Elementit-luokalla luodaan kaikki objektit käyttöliittymään
class Elementit extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sijaintiX;
	private int sijaintiY;
	Insets koot;
	
	public Elementit(int sijaintiX, int sijaintiY, Insets koot) {
		this.sijaintiX = sijaintiX;
		this.sijaintiY = sijaintiY;
		this.koot=koot;
	}
	
	//lisaaTeksti(String sisalto)-metodi lisää käyttöliittymään tekstiruudun
	public JLabel lisaaTeksti(String sisalto) {
		JLabel teksti=new JLabel(sisalto);
		Dimension koko=teksti.getPreferredSize();
		teksti.setSize(koko);
		teksti.setBounds(sijaintiX+koot.left,sijaintiY+koot.top,koko.width+200,koko.height+10);
		teksti.setFont(new Font("helvetica",Font.BOLD,24));
		
		return teksti;
	}
	
	//lisaaTekstiKentta(int pituuden_tasaus)-metodi lisää tekstinsyöttökentän käyttöliittymään
	public JTextField lisaaTekstiKentta(int pituuden_tasaus) {
		JTextField tekstikentta=new JTextField();
		tekstikentta.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 200+pituuden_tasaus, 20);
		
		return tekstikentta;
	}
	
	//lisaaNappi(String teksti)-metodi lisää käyttöliittymään napin
	//Napin painallus-funktio pitää implementoida erikseen
	public JButton lisaaNappi(String teksti) {
		JButton nappi=new JButton(teksti);
		nappi.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 120, 40);
		
		return nappi;
	}
	
	//lisaaDropDownKuukaudet()-metodi lisää käyttöliittymään kuukauden valinta listan
	public JComboBox<String> lisaaDropDownKuukaudet() {
		String[] lista_kuukaudet= { "tammikuuta","helmikuuta","maaliskuuta","huhtikuuta","toukokuuta","kesäkuuta","heinäkuuta","elokuuta","syyskuuta","lokakuuta","marraskuuta","joulukuuta"};
		JComboBox<String> kuukaudet=new JComboBox<String>(lista_kuukaudet);
		Dimension koko=kuukaudet.getPreferredSize();
		kuukaudet.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, koko.width, koko.height);
		return kuukaudet;
	}
	
	//lisaaDropDownPaivat()-metodi lisää käyttöliittymään paivan valinta listan
	//Funktio ei ota huomioon, että kaikissa kuukauksissa ei ole 31 päivää, vaan päivämäärät tarkistetaan tietojen lähetyksen yhteydessä
	public JComboBox<Integer> lisaaDropDownPaivat() {
		Integer[] lista_paivat=new Integer[31];
		for (int i=1;i<=31;i++) {
			lista_paivat[i-1]=i;
		}
		JComboBox<Integer> paivat=new JComboBox<Integer>(lista_paivat);
		Dimension koko=paivat.getPreferredSize();
		paivat.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, koko.width, koko.height);
		return paivat;
	}
	
	//lisaaDropDownVuodet()-metodi lisää käyttöliittymään vuoden valinta listan
	//Listassa on kymmenen seuraavaa vuotta
	public JComboBox<Integer> lisaaDropDownVuosi() {
		int a=0;
		Integer[] lista_vuodet=new Integer[10];
		for (int i=Calendar.getInstance().get(Calendar.YEAR);i<=Calendar.getInstance().get(Calendar.YEAR)+10;i++) {
			//try catch lause koska olen laiska ja en jaksa korjata koodia :DDDDDD
			try {
			lista_vuodet[a]=i;
			}
			catch (Exception e) {}
			a++;
			
		}
		JComboBox<Integer> vuodet=new JComboBox<Integer>(lista_vuodet);
		Dimension koko=vuodet.getPreferredSize();
		vuodet.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, koko.width, koko.height);
		return vuodet;
	}
	
	//lisaaCheckBox(String teksti)-metodi lisää käyttöliittymään valintaruudun
	public JCheckBox lisaaCheckBox(String teksti) {
		JCheckBox luksushuone=new JCheckBox(teksti);
		Dimension koko=getPreferredSize();
		//luksushuone.setBackground(new Color(255,255,255));
		luksushuone.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		luksushuone.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 13, 13);	
		
		return luksushuone;
	}
	
	//lisaaCheckBox(String teksti)-metodi lisää käyttöliittymään valintaruudun
	public JPanel lisaaPaneeli(int leveys, int korkeus) {
		JPanel paneeli=new JPanel(null);
		paneeli.setBounds(0, 0, leveys, korkeus);
		paneeli.setOpaque(false);
		
		return paneeli;
	}
}