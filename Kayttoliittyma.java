package Harkkatyo;
import java.util.Calendar;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Kayttoliittyma {
	public static void main(String[] args) {
		new Ikkuna(160*3, 300);
	}
}

class Ikkuna extends JPanel{
	
	//alustetaan attribuutit
	private int leveys;
	private int korkeus;
	private JFrame ikkunankehys;

	
	public Ikkuna(int leveys, int korkeus) {
		this.leveys = leveys;
		this.korkeus = korkeus;
		
		
		//luodaan uusi ikkuna
		ikkunankehys=new JFrame();
		
		
		//asetetaan ikkunalle leveys, korkeus, oletussijainti, sulkemisoperaatio, otsikko, väri
		
		
		
		ikkunankehys.setSize(new Dimension(leveys, korkeus));
		ikkunankehys.setLocationRelativeTo(null);
		ikkunankehys.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ikkunankehys.setTitle("Ajanvaraus");
		ikkunankehys.setResizable(false);
		ikkunankehys.setLayout(null);
		
		
		
		
		
		
		try {
			ikkunankehys.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("gui-tausta.jpg")))));
		}catch (IOException e) {
			System.out.println("Can't find image");
		}
		
		
		/*
		ImageIcon img = new ImageIcon("gui-tausta.png");
		JLabel background = new JLabel("",img,JLabel.CENTER);
		background.setBounds(0, 0, leveys, korkeus);
		*/
		
		
		
		Container sisalto=ikkunankehys.getContentPane();
		
		
		//lisätään elementtejä: tekstit, tekstikentät, dropdownit, checkboxit
		
		sisalto.add(new Elementit(10,30*1, sisalto.getInsets()).lisaaTeksti("Etunimi: "));
		sisalto.add(new Elementit(10,30*2, sisalto.getInsets()).lisaaTeksti("Sukunimi: "));
		sisalto.add(new Elementit(10,30*3, sisalto.getInsets()).lisaaTeksti("Aloituspäivämäärä: "));
		sisalto.add(new Elementit(10,30*4, sisalto.getInsets()).lisaaTeksti("Lopetuspäivämäärä: "));
		sisalto.add(new Elementit(10,30*5, sisalto.getInsets()).lisaaTeksti("Luksushuone "));
		
		JTextField enimi=new Elementit(243,30*1+3, sisalto.getInsets()).lisaaTekstiKentta(5);
		JTextField snimi=new Elementit(243,30*2+3, sisalto.getInsets()).lisaaTekstiKentta(5);
		
		sisalto.add(enimi);
		sisalto.add(snimi);
		
		JCheckBox checkbox=new Elementit(170,30*5+8, sisalto.getInsets()).lisaaCheckBox("");
		
		sisalto.add(checkbox);
		
		JComboBox<Integer> dropPaivat=new Elementit(243,30*3+3, sisalto.getInsets()).lisaaDropDownPaivat();
		JComboBox<String> dropKuukaudet=new Elementit(288,30*3+3, sisalto.getInsets()).lisaaDropDownKuukaudet();
		JComboBox<Integer> dropVuodet=new Elementit(393,30*3+3, sisalto.getInsets()).lisaaDropDownVuosi();
		
		sisalto.add(dropPaivat);
		sisalto.add(dropKuukaudet);
		sisalto.add(dropVuodet);
		
		JComboBox<Integer> dropPaivat2=new Elementit(243,30*4+3, sisalto.getInsets()).lisaaDropDownPaivat();
		JComboBox<String> dropKuukaudet2=new Elementit(288,30*4+3, sisalto.getInsets()).lisaaDropDownKuukaudet();
		JComboBox<Integer> dropVuodet2=new Elementit(393,30*4+3, sisalto.getInsets()).lisaaDropDownVuosi();
		
		sisalto.add(dropPaivat2);
		sisalto.add(dropKuukaudet2);
		sisalto.add(dropVuodet2);
		
		
		JButton nappi=new Elementit(330,30*6+25, sisalto.getInsets()).lisaaNappi("Lähetä tiedot");
		
		//Lähetä-napin painallusta tarkkaileva metodi
		nappi.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e){
		    	  boolean oikeellisuus=tarkistaSisallot();
		    	  
		    	  if(oikeellisuus){
		    		  //Implementoi metodi haeVapaat()-hotelli luokkaan
		    		  //int huone_id=;
		    		  String varaaja=enimi.getText()+" "+snimi.getText();
		    		  Date aloitus_paiva=new Date(dropVuodet.getSelectedIndex(), dropKuukaudet.getSelectedIndex(), dropPaivat.getSelectedIndex());
		    		  Date lopetus_paiva=new Date(dropVuodet2.getSelectedIndex(), dropKuukaudet2.getSelectedIndex(), dropPaivat2.getSelectedIndex());
		    		  boolean onko_luksus=checkbox.isSelected();
		    		  
		    		  
		    		  Varaus varaus=new Varaus(huone_id, varaaja, aloitus_paiva, lopetus_paiva, onko_luksus);
		    		  
		    		  varaus.tiedotTietokantaan();
		    	  }
		    	  else {
		    		  //Uusi ikkuna, jossa lukee "tarkista tiedot"
		    	  }
		      }
		});
		
		sisalto.add(nappi);
		
		//asetetaan ikkuna näkyväksi
		ikkunankehys.setVisible(true);
	}
}
class Elementit extends JPanel{
	private int sijaintiX;
	private int sijaintiY;
	Insets koot;
	
	public Elementit(int sijaintiX, int sijaintiY, Insets koot) {
		this.sijaintiX = sijaintiX;
		this.sijaintiY = sijaintiY;
		this.koot=koot;
	}
	public JLabel lisaaTeksti(String sisalto) {
		JLabel teksti=new JLabel(sisalto);
		Dimension koko=teksti.getPreferredSize();
		teksti.setSize(koko);
		teksti.setBounds(sijaintiX+koot.left,sijaintiY+koot.top,koko.width+200,koko.height+10);
		teksti.setFont(new Font("helvetica",Font.BOLD,24));
		return teksti;
	}
	public JTextField lisaaTekstiKentta(int pituuden_tasaus) {
		JTextField tekstikentta=new JTextField();
		tekstikentta.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 200+pituuden_tasaus, 20);
		
		return tekstikentta;
	}
	public JButton lisaaNappi(String teksti) {
		JButton nappi=new JButton(teksti);
		nappi.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 120, 40);
		
		return nappi;
	}
	public JComboBox<String> lisaaDropDownKuukaudet() {
		String[] lista_kuukaudet= { "tammikuuta","helmikuuta","maaliskuta","huhtikuuta","toukokuuta","kesäkuuta","heinäkuuta","elokuuta","syyskuuta","lokakuuta","marraskuuta","joulukuuta"};
		JComboBox<String> kuukaudet=new JComboBox<String>(lista_kuukaudet);
		Dimension koko=kuukaudet.getPreferredSize();
		kuukaudet.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, koko.width, koko.height);
		return kuukaudet;
	}
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
	public JCheckBox lisaaCheckBox(String teksti) {
		JCheckBox checkbox=new JCheckBox(teksti);
		Dimension koko=getPreferredSize();
		//checkbox.setBackground(new Color(255,255,255));
		checkbox.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		checkbox.setBounds(koot.left+sijaintiX, sijaintiY+koot.top, 13, 13);	
		
		return checkbox;
	}
}