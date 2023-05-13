import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Window extends JFrame{
	
	JLayeredPane menuWindow = new JLayeredPane();
	JLabel opt = new JLabel();
	JButton button;
	
	Window(){

//		menuWindow.setBounds(0,0,600,400);
//		menuWindow.setPreferredSize(new Dimension(Board.WIDTH, Board.HEIGHT));
//		menuWindow.setBackground(Board.BACKGROUND);
//		JLabel content = new JLabel();
//		menuWindow.add(content, 1);
//		menuWindow.setOpaque(true);
//		menuWindow.setVisible(true);
//		
//		opt.setOpaque(true);
//		opt.setBackground(Color.green);
//		opt.setBounds(100,50,400,300);
//		
//		button = new JButton("START GAME");
//		button.addActionListener(e -> Board.start());
//		opt.add(button);
//		
//		menuWindow.add(opt);

		
		//Create the Panel with its basic initial configurations, but out of main
		this.add(new Board());
//		this.add(menuWindow);
		this.setTitle("JavaSnake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
	
}
