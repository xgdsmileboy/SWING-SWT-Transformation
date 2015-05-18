package cn.edu.test;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JButtonDemo extends Applet{  //继承不同 *1		y1

	JButton btn1 ;		//*1	y1
	JButton btn2 ;		//*1	y1
	JTextField txtField ;	//*1	y1
	
	public JButtonDemo(){		//参数不同 *1		m1	需要添加super()
		btn1 = new JButton("JButton 1");	//*1	m1	L1-2
		btn2 = new JButton("JButton 2");	//*1	m1	L1-2
		txtField = new JTextField(20);		//*1	m1	L1-2
	}
	
	public void init(){
		ActionListener actionListener = new ActionListener() { //*2		y2
			
			@Override
			public void actionPerformed(ActionEvent e) {	//*2	y2
				// TODO Auto-generated method stub
				String name = ((JButton)e.getSource()).getText(); //*1	y1
				txtField.setText(name + " Pressed");
			}
		};
		
		btn1.addActionListener(actionListener);	//*1	y1
		add(btn1);								//*1
		btn2.addActionListener(actionListener);	//*1	y1
		add(btn2);								//*1
		add(txtField);							//*1
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("TextAreaNew"); //*2	y1	m1
		frame.addWindowListener(new WindowAdapter() { //*2	y2
			@Override
			public void windowClosing(WindowEvent e) { //*2	y2
				System.exit(0);
			}
		});
		
		JButtonDemo applet = new JButtonDemo();	//*2	y1	L1-2
		frame.getContentPane().add(applet, BorderLayout.CENTER);	//*1
		frame.setSize(300, 100);	//*1	y1
		applet.init();
		applet.start();		//*1	d1
		frame.setVisible(true);	//*1	m1
	}
}
