import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	
	//screen parameters
	static final int UNITSIZE = 20; //Unit size
	static final int WIDTH = 600;
	static final int HEIGHT = 400;
	static final int UNITS = (int) ((WIDTH*HEIGHT)/(UNITSIZE*UNITSIZE));

	final static Color BACKGROUND = Color.BLACK;
	
	final int[] x = new int[UNITS]; //x body parts
	final int[] y = new int[UNITS]; //y body parts

	int delay = 200; //the lower, the fastest
	
	int body = 6; //body length
	int increase; // body size increase	
	char direction = 'd'; // W(Up) A(Left) S(Down) D(Right)
	
	int score;

	int foodX;
	int foodY;
	
	boolean sp; //special food
	int spX;
	int spY;
	
	boolean running = true;
	boolean menu = false;
	boolean gameover = false;
	
	Timer timer;
	Random random;
	
	BufferedImage pic;
//	ImageIcon img = new ImageIcon("menu.png");
//	JLabel menuLabel = new JLabel(img);
	
	
	
	
	Board(){
		try {
			pic = ImageIO.read(new File("menu.png"));
		}catch(IOException e) {	}
		//Basic render config
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new Keys());
//		menuLabel.setVisible(menu);
//		this.add(menuLabel);
//		this.setLayout(null);
		//Initialization
		start();
	}

	public void start() {
		popFood();
		running = true;
		timer = new Timer(delay,this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		//Actual render	
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running){
			//Color food then place it on XY with 1x1 usize;
			g.setColor(Color.RED);
			g.fillOval(foodX, foodY, UNITSIZE, UNITSIZE);
			if (sp) {
				g.setColor(Color.CYAN);
				g.fillOval(spX, spY, UNITSIZE, UNITSIZE);
			}
			//Color the snake body and head
			for (int i = 0; i<body; i++){
				if (i==0){
					g.setColor(Color.GREEN);
					g.fillRect(x[i], y[i], UNITSIZE, UNITSIZE);
				}else{
					g.setColor(new Color(240, 0, 140));
					g.fillRect(x[i], y[i], UNITSIZE, UNITSIZE);
				}
			}
			//just a grid for snake separation
			for (int i=0; i<WIDTH/UNITSIZE; i++) {
				g.setColor(BACKGROUND);
				g.drawLine(i*UNITSIZE, 0, i*UNITSIZE, HEIGHT); //draw some vertical lines
				g.drawLine(0, i*UNITSIZE, WIDTH, i*UNITSIZE); //draw some horizontal lines
			}
		}
		else if (menu) {
			g.drawImage(pic, 0, 0, this);
		}
		
		else if (gameover) gameOver(g);

	}
	
	public void move () {
		//each part of the body will switch to the previous part location, starting on top position (tail)
		for (int i=body; i>0; i--) {
			x[i] = x[i-1]; 
			y[i] = y[i-1];
		}
		
		//we only move the head here, and the rest are supposed to follow
		switch (direction) {
			case 'w':
				y[0] = y[0] - UNITSIZE;
				break;
			case 's':
				y[0] = y[0] + UNITSIZE;
				break;
			case 'a':
				x[0] = x[0] - UNITSIZE;
				break;
			case 'd':
				x[0] = x[0] + UNITSIZE;
				break;
		}
	}
	
	public void popFood() {
		sp=false;
		boolean busy = true;
		int seg=0;
		while(busy) {
			foodX = random.nextInt((int)WIDTH/UNITSIZE)*UNITSIZE;
			foodY = random.nextInt((int)HEIGHT/UNITSIZE)*UNITSIZE;
			busy = busyField(foodX, foodY);
			seg++;
		}
		
		if (random.nextInt(1000) > 95 && score!=0) { //10% chance to generate special food. Never spawning at the beginning
			sp=true;
			busy = true;
			seg=0;
			while(busy) {
				spX = random.nextInt((int)WIDTH/UNITSIZE)*UNITSIZE;
				spY = random.nextInt((int)HEIGHT/UNITSIZE)*UNITSIZE;
				busy = busyField(spX, spY);
				seg++;
			}
		}
	}
	
	public void eat() {
		if (x[0]==foodX && y[0]==foodY){
			increase += 6;
			body = increase == 0? body++ : body;
			/* More than a single sum makes the body parts get stored in 0,0 because they dont have parent position. 
			 Also cannot sum if increase is still doing its job. */			
			score += 10;
			popFood();
		}
		else if (x[0]==spX && y[0]==spY) { //special food effects. More score, new timer with lower delay with a 40 limit, and food pos reset
			increase += 12;
			body = increase == 0? body++ : body; 
			score += 200;
			if (delay != 40) {
				delay-=20;
				timer.stop();
				timer = new Timer(delay,this);
				timer.start();
			}
			popFood();
		}
	}
	
	public void collide() {
		//if head joins body
		for (int i = body; i > 0; i--) if ((x[0] == x[i]) && (y[0] == y[i])) running=false;
		
		//if head touches Left/Right
		//if head touches Top/Bottom
		if (x[0] < 0 || x[0]+UNITSIZE > WIDTH) running = false;
		if (y[0] < 0 || y[0]+UNITSIZE > HEIGHT) running = false;
		
		//Stop the program if there was a collision
		if (!running) timer.stop();
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.RED);
		String res = "Game over! Your Score:" + score;
		g.drawString(res, WIDTH/2, HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running){
			if(increase != 0) { //handling body growth to avoid ghost parts spawning at 0,0
				body++;
				increase--;
			}
			move();
			eat();
			collide();
		}	
		repaint();
	}

	public class Keys extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			switch(e.getKeyCode()){
				case KeyEvent.VK_W:
					if (direction != 's') direction = 'w';
				  	break;
				case KeyEvent.VK_S:
					if (direction != 'w') direction = 's';
					break;
				case KeyEvent.VK_D:
					if (direction != 'a') direction = 'd';
					break;
				case KeyEvent.VK_A:
					if (direction != 'd') direction = 'a';
					break;
				case KeyEvent.VK_P:
					if (timer.isRunning()) timer.stop();
					else timer.start();
					break;
				case KeyEvent.VK_M:
					if (!menu) {
						running=false;
						menu=true;
						timer.stop();
					}else {
						running=true;
						menu=false;
						timer.start();
					}
					break;					
			}
		}
	}
	
	//It would be nice to reimplement the body as an object for the whole project
	//This generates a little overhead
	public boolean busyField(int foodx, int foody) { 
		//for body length check if X and index and Y at index equals to X and Y coordinates of food
		for (int index=0; index<body; index++) {
			if (foodx == x[index] && foody == y[index]) {
				return true; 
			}
		}
		return false;
	}
	
}