package cn.edu.test;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ListCombo extends JPanel {	//*1	y1
	
	static String[] ids = {"June", "Ward", "Beaver", "Wally", "Eddie", "Lumpy"};
	
	public ListCombo() {	//*1	m1
		this.setLayout(new GridLayout(2,1));	//*1	m1
		JList list = new JList(ids);		//*2	y1	m1	L1-2
		this.add(new JScrollPane(list));	//*2	y1
		
		JComboBox combo = new JComboBox();	//*2	y1	l1-2
		for(int i = 0; i < 100; i++){
			combo.addItem(Integer.toString(i));	//*1	y1
		}
		
		combo.setSelectedIndex(0);	//*1	y1
			
		this.add(combo);	//*1
	}
	
	public static void main(String args[]) {
		JFrame jf = new JFrame("Buttons");
		ListCombo b = new ListCombo();
		jf.add(b);
		jf.setSize(300, 200);
		jf.setVisible(true);
	}
} 