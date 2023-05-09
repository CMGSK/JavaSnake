import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{

	//screen parameters
	static final int WIDTH = 600;
	static final int HEIGHT = 400;
	static final int USIZE = 17; //Unit size
	static final int UNITS = (int) ((WIDTH*HEIGHT)/(Math.pow(USIZE, 2)));
	
	static final int DELAY = 100; //the lower, the fastest
	
	final int[] x = new int[UNITS]; //x body parts
	final int[] y = new int[UNITS]; //y body parts
	
	int body = 6; //body length
	char direction = 'D'; // W(Up)  A(Left) S(Down) D(Right)
	
	int score;

	int foodX;
	int foodY;
	
	boolean running = false;
	Timer timer;
	Random random;
	
	Board(){
		//Basic render config
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new Keys());
		//Initialization
		start();

	}

	public void start() {
		popFood();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		//Actual render	
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		//Helping grid
		for (int i=0; i<WIDTH/USIZE; i++) {
			g.drawLine(i*USIZE, 0, i*USIZE, HEIGHT); //draw some vertical lines
			g.drawLine(0, i*USIZE, WIDTH, i*USIZE); //draw some horizontal lines
		}
		
		//Color food then place it on XY with 1x1 usize;
		g.setColor(Color.RED);
		g.fillOval(foodX, foodY, USIZE, USIZE);
		
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
			y[0] = y[0] - USIZE;
			break;
		case 's':
			y[0] = y[0] + USIZE;
			break;
		case 'a':
			x[0] = x[0] - USIZE;
			break;
		case 'd':
			x[0] = x[0] + USIZE;
			break;
			
			
		}
		
	}
	
	public void popFood() {
		foodX = random.nextInt((int)WIDTH/USIZE)*USIZE;
		foodY = random.nextInt((int)HEIGHT/USIZE)*USIZE;
	}
	
	public void isFood() {
		
	}
	
	public void collide() {
		
	}
	
	public void gameOver(Graphics g) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public class Keys extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
		}
	}

}
