package cn.edu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import cn.edu.info.ClassInfo;
import cn.edu.info.FieldInfo;
import cn.edu.info.MethodBodyInfo;
import cn.edu.info.MethodInfo;
import cn.edu.info.VariableInfo;
import cn.edu.parser.Parser;
import cn.edu.trans.Trans;
import cn.edu.trans.TransRecord;

public class TransGUI {
	/*
	 * 主窗体
	 */
	private JFrame jframe;
	/*
	 * 用户控制面板
	 */
	private JPanel jp_Control;
	/*
	 * 待转换源代码显示区
	 */
	private JPanel jp_Source;
	/*
	 * 变量列表显示区
	 */
	private JPanel jp_Variable;
	/*
	 * 函数列表显示区
	 */
	private JPanel jp_Function;
	/*
	 * 转换之后的目标代码显示区
	 */
	private JPanel jp_Target;
	/*
	 * 转换输出显示区
	 */
	private JPanel jp_Print;
	/*
	 * 供待转换的测试源代码列表信息
	 * 
	 */
	private JComboBox<String> jcb_sourceFile;
	/*
	 * 开始转换按钮
	 */
	private JButton jbtn_trans;
	/*
	 * 显示目标路径信息
	 */
	private JTextField jtf_targetPath;
	/*
	 * 目标路径选择按钮
	 */
	private JButton jbtn_selectPath;
	/*
	 * 源代码显示区域
	 */
	private JTextArea jta_source;
	/*
	 * 目标代码显示区域
	 */
	private JTextArea jta_target;
	/*
	 * 变量显示表
	 */
	private JTable jtable_variable;
	/*
	 * 函数显示表
	 */
	private JTable jtable_function;
	/*
	 * 转换输出区域
	 */
	private JTextArea jta_print;
	/*
	 * 默认的待转换的测试源代码路径
	 */
	private final String sourceFilePath = "./src/cn/edu/test/";
	/*
	 * 待转换的源代码文件名，在下拉选择框中选择
	 */
	private String sourceFileName;
	/*
	 * 目标文件的路径信息
	 */
	private String targetFilePath;
	
	/**
	 * 构造函数，初始化用户界面
	 */
	public TransGUI(){
		jframe = new JFrame("SWING 2 SWT 演示");
		jframe.setSize(800, 600);
		jframe.setResizable(false);
		jframe.setLayout(new FlowLayout());
		//初始化用户控制面板
		initControlPane();
		//初始化转换面板
		initTransPane();
		//初始化转换输出面板
		initPrintPane();
		
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 初始化用户控制面板
	 */
	public void initControlPane(){
		jp_Control = new JPanel();
		jp_Control.setBorder(new TitledBorder("控制面板"));
		jp_Control.setPreferredSize(new Dimension(780, 100));
		jp_Control.setLayout(new FlowLayout());
		
		JLabel jlab_source = new JLabel("源文件的名称：");
		jcb_sourceFile = new JComboBox<String>();
		List<String> list = getName(sourceFilePath);
		//将默认的路径中的测试案例加入到下拉菜单中供用户选择
		for(String name : list){
			jcb_sourceFile.addItem(name);
		}
		
		jcb_sourceFile.setPreferredSize(new Dimension(280, 25));
		//初始化待转换的源代码文件名
		sourceFileName = (String)jcb_sourceFile.getSelectedItem();
		
		jbtn_trans = new JButton("开始转换");
		
		JLabel jlab_targetFilePath = new JLabel("目标文件路径：");
		jtf_targetPath = new JTextField(25);
		jtf_targetPath.setEditable(false);
		
		jbtn_selectPath = new JButton("选择路径");
		
		//界面布局部分
		jp_Control.add(jlab_source);
		jp_Control.add(jcb_sourceFile);
		JPanel jp1 = new JPanel();
		jp1.setPreferredSize(new Dimension(280, 20));
		jp_Control.add(jp1);
		jp_Control.add(jbtn_trans);
		jp_Control.add(jlab_targetFilePath);
		jp_Control.add(jtf_targetPath);
		JPanel jp2 = new JPanel();
		jp2.setPreferredSize(new Dimension(280, 20));
		jp_Control.add(jp2);
		jp_Control.add(jbtn_selectPath);
		jframe.add(jp_Control);
		
		jcb_sourceFile.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				resetAll();
				sourceFileName = (String)jcb_sourceFile.getSelectedItem();
				showFileOnView(sourceFilePath+sourceFileName, jta_source);
			}
		});
		
		jbtn_selectPath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectTargetPath();
			}
		});
		
		jbtn_trans.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startTrans();
			}
		});
	}
	
	/**
	 * 获取默认测试案例路径中的测试案例列表，
	 * 用户想用的测试案例都需要放到这个默认的路径当中
	 * @param path
	 * @return
	 */
	public List getName(String path) {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		if (file.isDirectory()) {
			File[] dirFile = file.listFiles();
			for (File f : dirFile) {
				if (f.getName().endsWith(".java")){
					list.add(f.getName());
				}
			}
		}
		return list;
	}
	
	/**
	 * 初始化转换显示面板
	 */
	public void initTransPane(){
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(1, 3));
		jp.setPreferredSize(new Dimension(780, 300));
		
		//添加源程序显示控件
		jp_Source = new JPanel();
		jp_Source.setBorder(new TitledBorder("源代码"));
		jp_Source.setLayout(new BorderLayout());
		
		jta_source = new JTextArea();
		jta_source.setAutoscrolls(true);
		jta_source.setEditable(false);
		jta_source.setTabSize(2);
		JScrollPane jscrollPane1= new JScrollPane(jta_source);
		jscrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Source.add(jscrollPane1, BorderLayout.CENTER);
		
		jp.add(jp_Source);
		
		//添加变量和函数显示界面
		JPanel jp_double = new JPanel();
		jp_double.setLayout(new GridLayout(2, 1));
		
		jp_Variable = new JPanel();
		jp_Variable.setBorder(new TitledBorder("变量列表"));
		jp_Variable.setLayout(new BorderLayout());
		Vector title1 = new Vector();
		title1.addElement("变量名");
		title1.addElement("原类型");
		title1.addElement("目标类型");
		
		jtable_variable = new JTable(new Vector(), title1);
		JScrollPane jscrollPane2= new JScrollPane(jtable_variable);
		jscrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Variable.add(jscrollPane2, BorderLayout.CENTER);
		
		jp_Function = new JPanel();
		jp_Function.setBorder(new TitledBorder("函数列表"));
		jp_Function.setLayout(new BorderLayout());
		Vector title2 = new Vector();
		title2.addElement("函数名");
		title2.addElement("参数列表");
		title2.addElement("返回类型");
		jtable_function = new JTable(new Vector(), title2);
		JScrollPane jscrollPane3= new JScrollPane(jtable_function);
		jscrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Function.add(jscrollPane3, BorderLayout.CENTER);
		
		
		jp_double.add(jp_Variable);
		jp_double.add(jp_Function);
		jp.add(jp_double);
		
		//添加目标程序界面
		jp_Target = new JPanel();
		jp_Target.setBorder(new TitledBorder("目标程序"));
		jp_Target.setLayout(new BorderLayout());
		
		jta_target = new JTextArea();
		jta_target.setAutoscrolls(true);
		jta_target.setEditable(false);
		jta_target.setTabSize(2);
		JScrollPane jscrollPane4= new JScrollPane(jta_target);
		jscrollPane4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Target.add(jscrollPane4, BorderLayout.CENTER);
		
		
		jp.add(jp_Target);
		
		
		jframe.add(jp);
	}
	
	/**
	 * 初始化转换输出面板
	 */
	public void initPrintPane(){
		jp_Print = new JPanel();
		jp_Print.setBorder(new TitledBorder("转换输出"));
		jp_Print.setPreferredSize(new Dimension(780, 150));
		jp_Print.setLayout(new BorderLayout());
		
		jta_print = new JTextArea();
		jta_print.setAutoscrolls(true);
		jta_print.setEditable(false);
		jta_print.setTabSize(2);
		
		JScrollPane jscrollPane= new JScrollPane(jta_print);
		jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Print.add(jscrollPane, BorderLayout.CENTER);
		jframe.add(jp_Print);
	}
	
	public void resetAll(){
		clearTables();
		jta_target.setText("");
		jta_print.setText("");
		Transformation.mClassInfoList = new ArrayList<>();
		Parser.reset();
		Trans.reset();
	}
	/**
	 * 将变量信息列表和函数信息列表清空
	 */
	public void clearTables(){
		DefaultTableModel dtm = (DefaultTableModel)jtable_variable.getModel();
		for(int i = dtm.getRowCount()-1; i >= 0 ; i--){
			dtm.removeRow(i);
		}
		
		dtm = (DefaultTableModel)jtable_function.getModel();
		for(int i = dtm.getRowCount()-1; i >= 0 ; i--){
			dtm.removeRow(i);
		}
	}
	
	/**
	 * 开始进行源代码API转换
	 */
	public void startTrans(){
		resetAll();
		//设置目标文件的路径信息
		Parser.setTargetFilePath(targetFilePath);
		//实例化转换实例
		new Transformation(sourceFilePath+sourceFileName);
		//将转换的变量信息显示在界面上
		showVariableOnView();
		//将转换的函数信息列表显示在界面上
		showMethodOnView();
		//如果用户没有设置目标文件的路径，则采用默认的路径
		if(targetFilePath == null){
			targetFilePath = ".\\src\\";
		}
		//将转换记录显示在界面上
		showTransRecordOnView(Trans.getTransRecord());
		
		//将转换之后的结果显示在用户界面上
		showFileOnView(targetFilePath+sourceFileName, jta_target);
	}
	
	/**
	 * 当用户选择路径时，弹出路径选择对话框，获取用户选择的路径信息
	 */
	public void selectTargetPath(){
		JFileChooser fileChooser = new JFileChooser("D:\\");
    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(fileChooser);
        if(returnVal == JFileChooser.APPROVE_OPTION){
        	targetFilePath= fileChooser.getSelectedFile().getAbsolutePath();
        	jtf_targetPath.setText(targetFilePath);
        	if(!targetFilePath.endsWith("\\")){
        		targetFilePath += "\\";
        	}
        }
	}
	
	/**
	 * 在转换界面上显示需要转换的变量信息列表
	 */
	public void showVariableOnView(){
		List<ClassInfo> classList = Transformation.mClassInfoList;
		DefaultTableModel dtm = (DefaultTableModel)jtable_variable.getModel();
		
		Map typeMap = Transformation.ruleManager.getTypeMatcherMap();
		for(ClassInfo classInfo : classList){
			List<FieldInfo> fieldList = classInfo.getmFieldDeclarationList();
			for(FieldInfo fieldInfo : fieldList){
				Vector row = new Vector<>();
				row.add(fieldInfo.getmVariableName());
				String type = fieldInfo.getmVariableType().toString();
				row.add(type);
				if(typeMap.containsKey(type)){
					type = (String)typeMap.get(type);
				}
				row.add(type);
				dtm.addRow(row);
			}
			
			List<MethodInfo> methodList = classInfo.getmMethodDeclarationList();
			for(MethodInfo methodInfo : methodList){
				List<VariableInfo> variableList = methodInfo.getmBodyInfo().getmVariableInfoList();
				for(VariableInfo variableInfo : variableList){
					Vector row = new Vector<>();
					row.add(variableInfo.getName());
					String type = variableInfo.getType();
					row.add(type);
					if(typeMap.containsKey(type)){
						type = (String)typeMap.get(type);
					}
					row.add(type);
					dtm.addRow(row);
				}
			}
			
			
		}
		
	}
	
	/**
	 * 在转换界面上显示需要转的函数信息列表
	 */
	public void showMethodOnView(){
		List<ClassInfo> classList = Transformation.mClassInfoList;
		Map typeMap = Transformation.ruleManager.getTypeMatcherMap();
		DefaultTableModel dtm = (DefaultTableModel)jtable_function.getModel();
		
		for(ClassInfo classInfo : classList){
			List<MethodInfo> methodList = classInfo.getmMethodDeclarationList();
			for(MethodInfo methodInfo : methodList){
				Vector row = new Vector<>();
				row.add(methodInfo.getmMethodName());
				String params = "";
				for(Object obj : methodInfo.getmParamsTypeList()){
					params += obj.toString()+",";
				}
				row.add(params);
				row.add(methodInfo.getmMethodReturnType());
				
				dtm.addRow(row);
			}
		}
		
	}
	
	/**
	 * 在界面上的jtextArea上显示fileName文件的内容
	 * @param fileName
	 * @param jtextArea
	 */
	public void showFileOnView(String fileName, JTextArea jtextArea){
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			jtextArea.setText("");
			while ((tempString = reader.readLine()) != null) {
				jtextArea.append(tempString);
				jtextArea.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	public void showTransRecordOnView(List<TransRecord> transRecordList){
		jta_print.setText("");
		int count = 1;
		for(TransRecord transRecord : transRecordList){
			List<String> sourceList = transRecord.getSourceCodeList();
			List<String> targetList = transRecord.getTargetCodeList();
			
			jta_print.append("-------------转换记录  "+count+"----------\n");
			jta_print.append("源代码： \n");
			for(String source : sourceList){
				jta_print.append(source);
				jta_print.append("\n");
			}
			jta_print.append("目标代码： \n");
			for(String target : targetList){
				jta_print.append(target);
				jta_print.append("\n");
			}
			count++;
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TransGUI();
	}
	
}
