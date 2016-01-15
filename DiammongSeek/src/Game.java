
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.Image;

public class Game extends JFrame{

	private Image logo;
	
	public Game() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
        setTitle("DiamondSeek@naomijub");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(610, 660);
        setLocationRelativeTo(null);
        setVisible(true);  
        
        ImageIcon img = new ImageIcon("logo.png");
        logo = img.getImage();
        setIconImage(logo);
       
    }
    
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 EventQueue.invokeLater(new Runnable() {
			 
	            @Override
	            public void run() {
	                Game ex = new Game();
	                ex.setVisible(true);
	            }
	        });
	}

}