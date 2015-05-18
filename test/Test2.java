package cn.edu.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


// Takes an array of Strings and makes the first
// element a node and the rest leaves
class Branch{
	DefaultMutableTreeNode r;	//*1	y1
	public Branch(String[] data){	//*1	y1
		r = new DefaultMutableTreeNode(data.toString());	//*1	m1	L1-2
		for(int i = 0; i < data.length; i++){
			r.add(new DefaultMutableTreeNode(data[i]));	//*2	m1
		}
	}
	
	public DefaultMutableTreeNode node(){	//*1	y1
		return r;
	}
}

public class Test2 extends JPanel{		//*1	y1

	String[][] data = {
			{"Colors", "Red", "Blue", "Green"},
			{"Flavors", "Tart", "Sweet", "Bland"},
			{"Length", "Short", "Medium", "Long"},
			{"Volume", "High", "Medium", "Low"},
			{"Temperature", "High", "Medium", "Low"},
			{"Intensity", "High", "Medium", "Low"}
	};
	static int i = 0;
	
	DefaultMutableTreeNode root, child, chosen;	//*3	y3
	
	JTree tree;	//*1	y1
	DefaultTreeModel model;	//n1
	
	public Test2(){		//*1	m1
		this.setLayout(new BorderLayout());		//*1	y1
		root = new DefaultMutableTreeNode("root");	//*1	m1	L1-4
		tree = new JTree(root);		//*1	m1
		//Add it and make it take care of scrolling:
		JScrollPane jscrollPane = new JScrollPane(tree);	//*2	d1
		this.add(jscrollPane, BorderLayout.CENTER);		//*1
		
		//Capture the tree's model:
		model = (DefaultTreeModel)tree.getModel();	//n1
		
		JPanel p = new JPanel();	//*2	y1	L1-2
		JButton test = new JButton("Press Me");		//*2	y1	m1	L1-2
		
		test.addActionListener(new ActionListener() {	//*2	y2
			
			@Override
			public void actionPerformed(ActionEvent e) {	//*2	y2
				if(i < data.length){
					
					//What's the last one you clicked?
					chosen = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();	//*1	y1
					if(chosen == null){
						chosen = root;
					}
					child = new Branch(data[i++]).node();	//*1	y1
					//The model will create the appropriate event.
					//In response, the tree will update itself
					model.insertNodeInto(child, chosen, 0);	//*1	d1
					//this puts the new node on the currently chosen node
				}
			}
		});
		//change the button's colors:
		
//		Color foreColor = Color.white;
//		test.setForeground(foreColor);
		Color backColor = Color.blue;	//*1	y1
		test.setBackground(backColor);	//*1	y1
		
		p.add(test);	//*1
		this.add(p, BorderLayout.SOUTH);	//*1
		
	}
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("Tree");
		jf.setLayout(new FlowLayout());
		JPanel jp = new JPanel();
		jp.add(new Test2());
		jf.add(jp);
		jf.setSize(200,400);
		jf.setVisible(true);
	}
	
}
