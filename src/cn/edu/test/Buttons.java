package cn.edu.test;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;

public class Buttons extends JPanel{	//*1	y1

	JButton jb = new JButton("JButton");			//*1	y1
	BasicArrowButton up ;	//*1	L1-2
	BasicArrowButton down ;	//*1	L1-2
	BasicArrowButton right ;//*1	L1-2
	BasicArrowButton left ;	//*1	L1-2
	int a;
	
	public Buttons(){	//*1	m1
		
//		jb = new JButton("JButton");	//*1	m1	L1-2	实例化之后需要设置名称为1-m；需要用到add()方法为m-1
		up = new BasicArrowButton(BasicArrowButton.NORTH);	//*1	y1
		down = new BasicArrowButton(BasicArrowButton.SOUTH);	//*1	y1
		right = new BasicArrowButton(BasicArrowButton.EAST);	//*1	y1
		left = new BasicArrowButton(BasicArrowButton.WEST);		//*1	y1
		
		this.add(jb);	//*1
		JToggleButton jtb = new JToggleButton("JToggleButton");	//*2	y1	m1	L1-2
		this.add(jtb);	//*1
		JCheckBox jcb = new JCheckBox("JCheckBox");	//*2	y1	m1	L1-2
		this.add(jcb);	//*1
		JRadioButton jrb = new JRadioButton("JRadioButton");	//*2	y1	m1	L1-2
		this.add(jrb);	//*1
		
		JPanel jp = new JPanel();	//*2	y1	L1-4
		jp.setBorder(new TitledBorder("Directions"));	//*2	m1
		
		jp.add(up);		//*1
		jp.add(down);	//*1
		jp.add(left);	//*1
		jp.add(right);	//*1
		this.add(jp);	//*1
	}
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("Buttons");
		Buttons b = new Buttons();
		jf.add(b);
		jf.setSize(300, 200);
		jf.setVisible(true);
	}
	
}
