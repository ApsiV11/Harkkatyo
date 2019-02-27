package harjoitustyö;

import java.awt.*;
import javax.swing.*;

public class gui {
	public static void main(String[] args) {
		new Ikkuna(160*4, 400);
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
		
		Container sisalto=ikkunankehys.getContentPane();
		sisalto.setBackground(new Color(255, 255, 255));
		
		sisalto.add((new Elementit(10,30*1, sisalto.getInsets())).lisaaTeksti("Etunimi: "));
		sisalto.add((new Elementit(10,30*2, sisalto.getInsets()).lisaaTeksti("Sukunimi: ")));
		sisalto.add((new Elementit(10,30*3, sisalto.getInsets()).lisaaTeksti("Aloituspäivämäärä: ")));
		sisalto.add((new Elementit(10,30*4, sisalto.getInsets()).lisaaTeksti("Lopetuspäivämäärä: ")));
		sisalto.add((new Elementit(10,30*5, sisalto.getInsets()).lisaaTeksti("Luksushuone ")));
		
		sisalto.add((new Elementit(110,30*1+3, sisalto.getInsets()).lisaaTekstiKentta(20)));
		sisalto.add((new Elementit(130,30*2+3, sisalto.getInsets()).lisaaTekstiKentta(0)));
		
		
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
	public JButton lisaaNappi() {
		
		
		return new JButton();
	}
	public JComboBox lisaaDropDown() {
		
		
		return new JComboBox();
	}
	public JCheckBox lisaaCheckBox(String teksti) {
		JCheckBox checkbox=new JCheckBox(teksti);
		
		return checkbox;
	}
}



//kommentoitu pois, en tiedä tuleeko tarvitsemaan enään, poistan jos ei tarvitse
/*
class Elementit extends JComponent{
	private String text;
	private int sijaintiX;
	private int sijaintiY;
		
	public Elementit(String text, int sijaintiX, int sijaintiY) {
		this.text = text;
		this.sijaintiX = sijaintiX;
		this.sijaintiY = sijaintiY;
	}

	@Override
	public void paintComponent(Graphics g) {
		if(g instanceof Graphics2D)
	      {
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);

			g2.drawString(text,sijaintiX,sijaintiY); 
	       }
	}
}
*/
