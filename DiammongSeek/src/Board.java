import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

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
	
	private int[][] spiders = {
			//level, direction (0 =x, 1 = y), x, y, way, exists
			{0, 0, 5, 2, 1, 1},
			{1, 1, 4, 3, 1, 1},
			{3, 1, 5, 2, 1, 1},
			{4, 0, 6, 3, 1, 1},
			{7, 1, 5, 3, 1, 1},
			{8, 0, 1, 2, 1, 1}}; 
	
	private int[][] map = {
				{
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 2, 0, 0, 0, 0, 1, 0, 0, 1,
				1, 1, 1, 1, 1, 0, 0, 0, 0, 1,
				1, 0, 0, 0, 0, 0, 0, 0, 0, 32,
				1, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				1, 0, 0, 1, 1, 1, 1, 0, 1, 1,
				1, 0, 0, 0, 0, 0, 0, 0, 4, 4,
				1, 1, 1, 1, 1, 1, 0, 0, 4, 4,
				1, 0, 0, 0, 0, 0, 0, 4, 4, 4,
				1, 16, 1, 1, 1, 1, 4, 4, 4, 4 
				},{
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 1, 1, 1, 0, 2, 1,
				1, 0, 0, 0, 1, 0, 0, 0, 1, 1,
				64, 0, 0, 0, 0, 0, 0, 0, 0, 32,
				1, 1, 0, 0, 0, 0, 1, 1, 0, 1,
				4, 0, 0, 0, 0, 0, 1, 1, 0, 1,
				4, 0, 0, 0, 0, 0, 1, 1, 0, 1,
				4, 0, 0, 1, 1, 1, 1, 1, 0, 1,
				4, 1, 1, 1, 0, 0, 0, 0, 0, 1,
				4, 1, 1, 1, 16, 1, 1, 1, 1, 1
				},{
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 0, 2, 0, 0, 0, 1,
				1, 0, 1, 1, 1, 1, 1, 1, 0, 1,
				64, 0, 1, 0, 0, 0, 0, 0, 0, 1,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 1,
				1, 0, 0, 0, 1, 1, 1, 1, 1, 1,
				1, 0, 1, 1, 1, 0, 0, 0, 0, 1,
				1, 0, 1, 1, 1, 0, 1, 1, 2, 1,
				1, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 16, 1, 1, 1, 1
				},{
				1, 8, 1, 1, 1, 1, 4, 4, 4, 4,
				1, 0, 1, 1, 0, 1, 0, 4, 4, 4,
				1, 0, 1, 1, 0, 0, 0, 4, 4, 4,
				1, 0, 1, 1, 0, 0, 1, 1, 1, 1,
				1, 0, 1, 0, 0, 0, 1, 0, 0, 32,
				4, 0, 1, 0, 0, 0, 1, 0, 1, 1,
				4, 2, 0, 0, 0, 0, 1, 0, 1, 1,
				4, 0, 4, 4, 0, 0, 1, 0, 1, 1,
				1, 0, 4, 4, 0, 0, 0, 0, 1, 1,
				1, 16, 1, 1, 1, 1, 1, 1, 1, 1
				},{
				1, 1, 1, 1, 8, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 0, 1, 0, 0, 0, 1,
				1, 0, 0, 0, 0, 1, 0, 1, 0, 1,
				1, 0, 0, 0, 0, 0, 0, 1, 0, 1,
				64, 0, 0, 0, 0, 1, 1, 1, 0, 1,
				1, 1, 1, 1, 0, 1, 1, 1, 2, 1,
				1, 0, 0, 0, 0, 1, 1, 4, 4, 4,
				1, 0, 1, 1, 1, 1, 4, 4, 4, 1,
				1, 0 ,0, 0, 2, 1, 4, 4, 4, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1 
				},{
				1, 1, 1, 1, 1, 8, 1, 1, 1, 1,
				1, 0, 0, 0, 1, 0, 1, 1, 2, 1,
				1, 2, 1, 0, 1, 0, 1, 1, 0, 1,
				1, 1, 0, 0, 1, 0, 1, 1, 0, 1,
				1, 1, 0, 1, 1, 0, 1, 1, 0, 1,
				1, 1, 0, 1, 1, 0, 1, 1, 0, 1,
				4, 4, 0, 0, 0, 0, 0, 1, 0, 1,
				4, 4, 1, 1, 1, 1, 0, 1, 0, 4,
				1, 2, 0, 0, 1, 1, 0, 0, 0, 4,
				1, 1, 1, 16, 1, 1, 1, 1, 1, 4
				},{
				1, 8, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 0, 0, 0, 1, 1, 1,
				1, 0, 2, 1, 1, 1, 1, 1, 1, 1,
				4, 4, 0, 0, 0, 0, 0, 1, 0, 32,
				4, 4, 1, 1, 1, 1, 0, 1, 0, 1,
				4, 4, 1, 1, 1, 1, 0, 0, 0, 1,
				1, 0, 0, 0, 0, 0, 1, 0, 1, 1,
				1, 0, 4, 4, 4, 0, 1, 0, 1, 1,
				1, 0, 0, 2, 4, 0, 0, 0, 1, 1,
				1, 4, 4, 4, 4, 4, 1, 1, 1, 1
				},{
				1, 1, 1, 1, 1, 1, 1, 4, 4, 1,
				1, 1, 1, 0, 0, 1, 4, 4, 4, 1,
				1, 1, 1, 0, 0, 1, 4, 4, 4, 1,
				64, 0, 0, 0, 0, 0, 0, 0, 2, 1,
				1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 32,
				1, 1, 0, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 0, 0, 0, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1 
				},{
				1, 1, 1, 8, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 0, 1, 0, 0, 0, 1,
				1, 0, 0, 0, 0, 1, 0, 1, 0, 1,
				1, 0, 0, 0, 0, 1, 0, 1, 0, 1,
				1, 0, 0, 0, 0, 0, 0, 1, 0, 1,
				1, 0, 0, 0, 0, 1, 4, 4, 0, 1,
				64, 0, 4, 4, 4, 4, 4, 4, 0, 1,
				1, 1, 4, 4, 4, 4, 4, 4, 2, 1,
				1, 1, 2, 0, 0, 0, 0, 0, 0, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1
				}
			};
	
	private int[] screenData;

	// Booleans - is game playable?
    private boolean gameOn, dying, win;
    private boolean up, down, left, right;
	
    //Images
    private Image diammond;
    private Image seeker;
    private Image tree;
    private Image spiderUp, spiderDown, spiderRight, spiderLeft;
    private Timer timer;
    
	public Board(){
		addKeyListener(new TAdapter());
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    	setBackground(new Color(0, 200, 25));
        setFocusable(true);
        
        setDoubleBuffered(true);
        
        initVar();
        loadImages();
        initGame();
        timer.start();
	}
	
	//board set
	public void initVar(){
	    gameOn = true;
	    win = false;
	    up = true;
	    down = false;
	    left = false;
	    right = false;
	    screenData = new int[NRO_BLOCKS * NRO_BLOCKS];
	    level = 4;
	    seekerX = 3;
		seekerY = 4;
		timer = new Timer(150, new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 repaint();
			 }
		});
	 
	}
	
	public void loadImages(){
		ImageIcon iiDia = new ImageIcon("diammond.png");
		diammond = iiDia.getImage();
		
		ImageIcon iiSeek = new ImageIcon("seeker.png");
		seeker = iiSeek.getImage();
		
		ImageIcon iiTree = new ImageIcon("tree.png");
		tree = iiTree.getImage();
		
		ImageIcon iiSpiderUp = new ImageIcon("spiderup.png");
		spiderUp = iiSpiderUp.getImage();
		
		ImageIcon iiSpiderDown = new ImageIcon("spiderdown.png");
		spiderDown = iiSpiderDown.getImage();
		
		ImageIcon iiSpiderRight = new ImageIcon("spiderright.png");
		spiderRight = iiSpiderRight.getImage();
		
		ImageIcon iiSpiderLeft = new ImageIcon("spiderleft.png");
		spiderLeft = iiSpiderLeft.getImage();
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
			if ((screenData[i] & 128) != 0) {
				drawBlood(g2d, i);
			}
		}
	}
	
	public void drawScore(Graphics2D g2d){
		Font font = new Font("Arial", Font.BOLD, 20);
		String str = "Diammons: "+ score +"/15";
		String lifes = "Lives Left: " + lifeLeft;
		
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
			moveSpider(g2d);
			score();
		}
		if(win){
			drawWin(g2d);
		}
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
		
		g2d.drawImage(diammond, (x * 60) + 12, (y *  60) + 5, this);
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
        bloodPosSeek();
       
        level = 4;
        seekerX = 3;
        seekerY = 4;

        initLevel();
    }
	
	public void bloodPosSeek( ){
		int i = seekerX + (10 * seekerY);
		
		if((map[level][i] & 4) == 0){
			map[level][i] = 128;
		}
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
            	repaint();
            }
            if ((screenData[i] & 4) != 0) { 
            	die();
            }
		}
		
		if (up && (screenData[i] & 8) != 0) { 
        	level -= 3;
        	seekerY = 9;
        	g2d.dispose();
        	initLevel();
        }
		if (down && (screenData[i] & 16) != 0) { 
        	level += 3;
        	seekerY = 0;
        	g2d.dispose();
        	initLevel();
        }
		if (right && (screenData[i] & 32) != 0) { 
        	level++;
        	seekerX = 0;
        	g2d.dispose();
        	initLevel();
        }
		if (left && (screenData[i] & 64) != 0) { 
        	level--;
        	seekerX = 9;
        	g2d.dispose();
        	initLevel();
        }
		seekerDX = 0;
		seekerDY = 0;
	}
	
	private void checkDiammonds(Graphics2D g2d, int i){
		map[level][i] = 0;
	}
	
	private void score(){
		if(score == 15){
			win = true;
		}
	}
	
	public void drawWin(Graphics2D g2d){
		Font font = new Font("Arial", Font.BOLD, 20);
		String str = "You WIN!!";
		
		
		//backgroud
		g2d.setColor(new Color(100, 100, 100));
        g2d.fillRect(0, 600, 600, 30);
        g2d.drawRect(0, 600, 600, 30);
        
        //Game Over
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(str, 50, 616);
	}
	
	public void moveSpider(Graphics2D g2d){
		boolean hasSpider = false;
		int idxLvl = 0;
	
		for(int i = 0;i < 6;i++){
			if(level == spiders[i][0] && spiders[i][5] == 1){
				hasSpider = true;
				idxLvl = i;
			}
		}
		
		if(hasSpider){
			int pos = spiders[idxLvl][4];
			if(spiders[idxLvl][1] == 0){
				int i = spiders[idxLvl][3] * 10 + spiders[idxLvl][2];
				
				if((screenData[i + pos] & 1) != 0){
					spiders[idxLvl][2] -= pos;
					spiders[idxLvl][4] = pos * (-1);
				}else{
					spiders[idxLvl][2] += pos;
				}
				
			}else{
				int i = spiders[idxLvl][3] * 10 + spiders[idxLvl][2];
				
				if((screenData[i + (pos * 10)] & 1) != 0){
					spiders[idxLvl][3] -= pos;
					spiders[idxLvl][4] = pos * (-1);
				}else{
					spiders[idxLvl][3] += pos;
				}
			}
			drawSpider(g2d, pos, idxLvl);
			
			if(spiders[idxLvl][2] == seekerX && spiders[idxLvl][3] == seekerY){
				die();
			}
		}
	}
	
	public void drawSpider(Graphics2D g2d, int pos, int idx){
		int x = spiders[idx][2], y = spiders[idx][3];
		if(spiders[idx][1] == 0){
			if(pos == 1){
				g2d.drawImage(spiderRight, x * TILE_SIZE, y * TILE_SIZE, this);
			}
			if(pos == -1){
				g2d.drawImage(spiderLeft, x * TILE_SIZE, y * TILE_SIZE, this);
			}
		}
		if(spiders[idx][1] == 1){
			if(pos == 1){
				g2d.drawImage(spiderUp, x * TILE_SIZE, y * TILE_SIZE, this);
			}
			if(pos == -1){
				g2d.drawImage(spiderDown, x * TILE_SIZE, y * TILE_SIZE, this);
			}
		}
	}
	
	public void interact(){
		boolean hasSpider = false;
		int spiderI = -1, idx = -1;
	
		for(int i = 0;i < 6;i++){
			if(level == spiders[i][0]){
				hasSpider = true;
				idx = i;
				spiderI = spiders[i][2] + (spiders[i][3] * 10);
			}
		}
		int seekerI = seekerX + (seekerY * 10);
		
		if(up && hasSpider){
			if(spiderI == (seekerI - 10)){
				spiders[idx][5] = 0;
				map[level][spiderI] = 128;
			}
		}
		if(down && hasSpider){
			if(spiderI == (seekerI + 10)){
				spiders[idx][5] = 0;
				map[level][spiderI] = 128;
			}
			repaint();
		}
		if(left && hasSpider){
			if(spiderI == (seekerI - 1)){
				spiders[idx][5] = 0;
				map[level][spiderI] = 128;
			}
			repaint();
		}
		if(right && hasSpider){
			if(spiderI == (seekerI + 1)){
				spiders[idx][5] = 0;
				map[level][spiderI] = 128;
			}
			repaint();
		}
	}
	public void drawBlood(Graphics2D g2d, int i){
		int y = (int) Math.floorDiv(i, 10);
		int x = i % 10;
		
		g2d.setColor(new Color(230, 0, 25));
		g2d.drawRoundRect((x * 60) + 12, (y * 60) + 10, 35, 40, 15, 15);
		g2d.fillRoundRect((x * 60) + 12, (y * 60) + 10, 35, 40, 15, 15);
		
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
	            if ((key == KeyEvent.VK_SPACE)){
	            	interact();
	            }
	        }
	    }
	
	public void actionPerformed(ActionEvent e) {
		
        repaint();
    } 
}
