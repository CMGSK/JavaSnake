import javax.swing.JFrame;

public class Window extends JFrame{
	
	Window(){
		//Create the Panel with its basic initial configurations, but out of main
		this.add(new Board());
		this.setTitle("JavaSnake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

}
