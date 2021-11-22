package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CardLayoutUscita{

	private JPanel contentPane; 
    
	private Semaphore blocker;
	
	private Panel[] panels;
    
	
	public CardLayoutUscita() {
		blocker = new Semaphore(0);
		displayGUI();
	}
	
	private void displayGUI() {
		
		//SITUAZIONE INIZIALE: PRIMA USER-VIEW
    	
    	//imposta la finestra
        JFrame frame = new JFrame("Card Layout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        contentPane = new JPanel();//crea JPanel (che farà da super-JPanel)
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //setta i bordi della JPanel
        contentPane.setLayout(new CardLayout()); //LayoutManager    

      //vedi più in basso. In sintesi, creo 3 JPanel "speciali" (create a piacere)
        ButtonPanel panel1 = new ButtonPanel("Benvenuti al pagamento ticket",this); 
        Panel panel2 = new Panel("");

        //in pratica sto raccogliendo più JPanel in un super-JPanel
        contentPane.add(panel1, "Panel 1");
        contentPane.add(panel2, "Panel 2");
        
        //raccolgo i JPanel in un vettore così da poter avere un riferimento in memoria a questi
        Panel[] panelArray = {panel1,panel2};
        panels = panelArray;
        
        //modifico container della frame per:
        
        frame.getContentPane().add(contentPane, BorderLayout.CENTER);   //contenere la JPanel
        frame.pack();   //sistema la finestra e adatta gli oggetti per star dentro la frame, dando a questi dimensioni che siano pari o inferiori rispetto alla dimensione desiderata
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
		
	}
	
	
	
	public Panel getPanel(int numPanel) {
		return panels[numPanel];
	}
	
	
	public void changePanel(String nextPanelName) {
    	
        CardLayout cardLayout = (CardLayout) contentPane.getLayout(); //prendi il LayoutManager usato dal panel
        cardLayout.show(contentPane, nextPanelName); //mostra sul layoutManager il panel contenuto in contentPane( il super-JPanel ) che risponde al nome scelto
  
    
	}
	
	public void timerNreset(long delay) {
		
    	TimeChanger cambiaTempo = new TimeChanger(this); 
    	
    	Timer timer = new Timer();
    	
    	timer.schedule(cambiaTempo, delay);
    	
    }
	
	public void acquire() {
		
		try {
			blocker.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void release() {
		blocker.release();
	}
}






class Panel extends JPanel{
	
	protected JLabel l;
	protected GridBagConstraints gbc;
	
	public Panel(String labelText) {
		
		setLayout(new GridBagLayout());
        setOpaque(true); //ogni componente è opaco (non trasparente)
        setBackground(Color.WHITE);
        
        
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
		
        
        l = new JLabel();
        l.setFont(new Font("", Font.PLAIN,20));
        l.setText(labelText);
        add(l, gbc);
        
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        
	}
	
	public void setText(String text) {
		l.setText(text);
	}
	
	 @Override
	 public Dimension getPreferredSize()
	    {
	        return (new Dimension(500, 500));
	    }
	
}

class ButtonPanel extends Panel{

	private JButton jcomp1;
	//non c'è il campo CardLayoutUscita
	
	public ButtonPanel(String labelText, CardLayoutUscita clu) {
		super(labelText);
		// TODO Auto-generated constructor stub
		jcomp1 = new JButton ("PREMI");
        jcomp1.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e) //qualunque azione subisca l'oggetto, fai questo
            { 
            	clu.getPanel(1).setText("Inserire Biglietto");
            	
                clu.changePanel("Panel 2");
                
                clu.release();
            }
        });

        add(jcomp1,gbc); 
		
		
	}
}

