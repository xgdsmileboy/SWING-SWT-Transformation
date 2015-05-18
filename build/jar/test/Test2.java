package cn.edu.test;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test2 extends JPanel{
	
	public static void main(String[] args) {
		
		JFrame jframe = new JFrame("test");
		JPanel jp;
		float count = 1.2f;
		int[] array = {1,2,3};
		
		jp = new JPanel();
		JButton btn = new JButton("button");
		
		jp.add(btn);
		jframe.add(jp);
		
		jframe.setSize(200, 100);
		jframe.setVisible(true);
	}
	
	public static void func(JFrame jf){
		
	}
}
