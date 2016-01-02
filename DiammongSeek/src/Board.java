import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener{

	//Const
	private final int TILE_SIZE = 60;
	private final int BOARD_WIDTH = 600;
	private final int BOARD_HEIGHT = 630;
	private final int NRO_BLOCKS = 10;
	
	private int lifeLeft, score, level;
	private int seekerX, seekerY, seekerDX, seekerDY;
	
	private int[][] map = {
	{0},
	{0},
	{0},
	{0},
	{
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	1, 1, 1, 1, 0, 1, 0, 0, 0, 1,
	1, 0, 0, 0, 0, 1, 0, 1, 0, 1,
	1, 0, 0, 0, 0, 0, 0, 1, 0, 1,
	1, 0, 0, 0, 0, 1, 1, 1, 0, 1,
	1, 1, 1, 1, 0, 1, 1, 1, 2, 1,
	1, 0, 0, 0, 0, 1, 1, 4, 4, 4,
	1, 0, 1, 1, 1, 1, 4, 4, 4, 1,
	1, 0 ,0, 0, 2, 1, 4, 4, 4, 1,
	1, 1, 1, 1, 1, 1, 1, 1, 1, 1 
	},
	{0},
	{0},
	{0},
	{0}
	};
	
	private int[] screenData;

	// Booleans - is game playable?
    private boolean gameOn, dying;
    private boolean up, down, left, right;
	
    //Images
    private Image diammond;
    private Image seeker;
    private Image tree;
    
	public Board(){
		addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    	setBackground(new Color(0, 200, 25));
        setFocusable(true);
        
        setDoubleBuffered(true);
        
        initVar();
        loadImages();
        initGame();
	}
	
	//board set
	public void initVar(){
	    gameOn = true;
	    up = false;
	    down = false;
	    left = false;
	    right = false;
	    screenData = new int[NRO_BLOCKS * NRO_BLOCKS];
	    level = 4;
	    
	}
	
	public void loadImages(){
		ImageIcon iiDia = new ImageIcon("diammond.png");
		diammond = iiDia.getImage();
		
		ImageIcon iiSeek = new ImageIcon("seeker.png");
		seeker = iiSeek.getImage();
		
		ImageIcon iiTree = new ImageIcon("tree.png");
		tree = iiTree.getImage();
	}
	
	public void initGame(){
		lifeLeft = 3;
	    score = 0;
	    initLevel();
	}
	
	//init game set
	public void initLevel(){
		for(int i = 0;i < NRO_BLOCKS * NRO_BLOCKS;i++){
			screenData[i] = map[level][i];
		}
		
		continueLevel();
	}
	
	private void continueLevel(){
		seekerX = 3;
		seekerY = 4;
		seekerDX = 0;
		seekerDY = 0;
		dying = false;
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
	
	//draw stuff
	public void doDrawing(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(0, 200, 25));
		g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		
		drawMap(g2d);
		drawScore(g2d);
		
		if(gameOn){
			playGame(g2d);
		}else{
			drawGameOver(g2d);
		}
		
	}
	
	//do drawing
	public void drawMap(Graphics2D g2d){
		
		for(int i = 0;i < NRO_BLOCKS * NRO_BLOCKS;i++){
			if ((screenData[i] & 1) != 0) { 
                drawTree(g2d, i);
            }
			if ((screenData[i] & 2) != 0) { 
                drawDiammond(g2d, i);
            }
			if ((screenData[i] & 4) != 0) { 
				int y = (int) i / 10;
				int x = i % 10;
				
                g2d.setColor(new Color(15, 0, 220));
                g2d.drawRect(x * 60, y * 60, TILE_SIZE, TILE_SIZE);
                g2d.fillRect(x * 60, y * 60, TILE_SIZE, TILE_SIZE);
            }
		}

		//drawSeeker(g2d);
	}
	
	public void drawScore(Graphics2D g2d){
		Font font = new Font("Arial", Font.BOLD, 20);
		String str = "Diammons: "+ score +"/15";
		String lifes = "Lifes Left: " + lifeLeft;
		
		//backgroud
		g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(0, 600, 600, 30);
        g2d.drawRect(0, 600, 600, 30);
        
        //Score
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(str, 50, 616);
        
        //lifes
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(lifes, 350, 616);
	}
	
	public void drawGameOver(Graphics2D g2d){
		Font font = new Font("Arial", Font.BOLD, 20);
		String str = "GAME OVER!!";
		
		
		//backgroud
		g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(0, 600, 600, 30);
        g2d.drawRect(0, 600, 600, 30);
        
        //Game Over
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(str, 50, 616);
	}
	
	private void playGame(Graphics2D g2d){
		if(dying){
			
			die();
		
		}else{
			moveSeeker(g2d);
			drawSeeker(g2d);
		}
		repaint();
	}
	
	//draw map set
	private void drawTree(Graphics2D g2d, int i){
		int y = (int) Math.floorDiv(i, 10);
		int x = i % 10;
		
		g2d.drawImage(tree, x * 60, y * 60, this);
	}
	
	private void drawDiammond(Graphics2D g2d, int i){
		int y = (int) Math.floorDiv(i, 10);
		int x = i % 10;
		
		g2d.drawImage(diammond, x * 60, y *  60, this);
	}
	
	private void drawSeeker(Graphics2D g2d){
		g2d.drawImage(seeker, seekerX * 60, seekerY * 60, this);
	}
	
	//play game set
	private void die() {

        lifeLeft--;

        if (lifeLeft == 0) {
            gameOn = false;
        }

        continueLevel();
    }
	
	private void moveSeeker(Graphics2D g2d){
		int newSeekX = seekerX;
		int newSeekY = seekerY;
		
		if(left || right){
			newSeekX += seekerDX;
		}
		if(up || down){
			newSeekY += seekerDY;
		}
		
		int i = (newSeekY * 10) + newSeekX;
		
		if ((screenData[i] & 1) == 0) { 
            seekerX = newSeekX;
            seekerY = newSeekY;
            
            if ((screenData[i] & 2) != 0) { 
            	score++;
            	screenData[i] = 0;
            	checkDiammonds(g2d, i);
            }
            if ((screenData[i] & 4) != 0) { 
            	die();
            }
		}
		seekerDX = 0;
		seekerDY = 0;
	}
	
	private void checkDiammonds(Graphics2D g2d, int i){
		map[level][i] = 0;
	}
	
	//Key Adapter
	private class TAdapter extends KeyAdapter {

	        @Override
	        public void keyPressed(KeyEvent e) {
	        	int key = e.getKeyCode();

	            if ((key == KeyEvent.VK_LEFT)) {
	                seekerDX = -1;
	                seekerDY = 0;
	                up = false;
	        	    down = false;
	        	    left = true;
	        	    right = false;
	            }

	            if ((key == KeyEvent.VK_RIGHT)) {
	            	seekerDX = 1;
	                seekerDY = 0;
	                up = false;
	        	    down = false;
	        	    left = false;
	        	    right = true;
	            }

	            if ((key == KeyEvent.VK_UP)) {
	            	seekerDX = 0;
	                seekerDY = -1;
	                up = true;
	        	    down = false;
	        	    left = false;
	        	    right = false;
	            }

	            if ((key == KeyEvent.VK_DOWN)) {
	            	seekerDX = 0;
	                seekerDY = 1;
	                up = false;
	        	    down = true;
	        	    left = false;
	        	    right = false;
	            }
	        }
	    }
	
	public void actionPerformed(ActionEvent e) {
		
        repaint();
    } 
}
