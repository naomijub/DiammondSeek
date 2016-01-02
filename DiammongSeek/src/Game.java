
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.awt.EventQueue;

public class Game extends JFrame{

	public Game() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(610, 660);
        setLocationRelativeTo(null);
        setVisible(true);   
       
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