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
	 * ������
	 */
	private JFrame jframe;
	/*
	 * �û��������
	 */
	private JPanel jp_Control;
	/*
	 * ��ת��Դ������ʾ��
	 */
	private JPanel jp_Source;
	/*
	 * �����б���ʾ��
	 */
	private JPanel jp_Variable;
	/*
	 * �����б���ʾ��
	 */
	private JPanel jp_Function;
	/*
	 * ת��֮���Ŀ�������ʾ��
	 */
	private JPanel jp_Target;
	/*
	 * ת�������ʾ��
	 */
	private JPanel jp_Print;
	/*
	 * ����ת���Ĳ���Դ�����б���Ϣ
	 * 
	 */
	private JComboBox<String> jcb_sourceFile;
	/*
	 * ��ʼת����ť
	 */
	private JButton jbtn_trans;
	/*
	 * ��ʾĿ��·����Ϣ
	 */
	private JTextField jtf_targetPath;
	/*
	 * Ŀ��·��ѡ��ť
	 */
	private JButton jbtn_selectPath;
	/*
	 * Դ������ʾ����
	 */
	private JTextArea jta_source;
	/*
	 * Ŀ�������ʾ����
	 */
	private JTextArea jta_target;
	/*
	 * ������ʾ��
	 */
	private JTable jtable_variable;
	/*
	 * ������ʾ��
	 */
	private JTable jtable_function;
	/*
	 * ת���������
	 */
	private JTextArea jta_print;
	/*
	 * Ĭ�ϵĴ�ת���Ĳ���Դ����·��
	 */
	private final String sourceFilePath = "./src/cn/edu/test/";
	/*
	 * ��ת����Դ�����ļ�����������ѡ�����ѡ��
	 */
	private String sourceFileName;
	/*
	 * Ŀ���ļ���·����Ϣ
	 */
	private String targetFilePath;
	
	/**
	 * ���캯������ʼ���û�����
	 */
	public TransGUI(){
		jframe = new JFrame("SWING 2 SWT ��ʾ");
		jframe.setSize(800, 600);
		jframe.setResizable(false);
		jframe.setLayout(new FlowLayout());
		//��ʼ���û��������
		initControlPane();
		//��ʼ��ת�����
		initTransPane();
		//��ʼ��ת��������
		initPrintPane();
		
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * ��ʼ���û��������
	 */
	public void initControlPane(){
		jp_Control = new JPanel();
		jp_Control.setBorder(new TitledBorder("�������"));
		jp_Control.setPreferredSize(new Dimension(780, 100));
		jp_Control.setLayout(new FlowLayout());
		
		JLabel jlab_source = new JLabel("Դ�ļ������ƣ�");
		jcb_sourceFile = new JComboBox<String>();
		List<String> list = getName(sourceFilePath);
		//��Ĭ�ϵ�·���еĲ��԰������뵽�����˵��й��û�ѡ��
		for(String name : list){
			jcb_sourceFile.addItem(name);
		}
		
		jcb_sourceFile.setPreferredSize(new Dimension(280, 25));
		//��ʼ����ת����Դ�����ļ���
		sourceFileName = (String)jcb_sourceFile.getSelectedItem();
		
		jbtn_trans = new JButton("��ʼת��");
		
		JLabel jlab_targetFilePath = new JLabel("Ŀ���ļ�·����");
		jtf_targetPath = new JTextField(25);
		jtf_targetPath.setEditable(false);
		
		jbtn_selectPath = new JButton("ѡ��·��");
		
		//���沼�ֲ���
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
	 * ��ȡĬ�ϲ��԰���·���еĲ��԰����б�
	 * �û����õĲ��԰�������Ҫ�ŵ����Ĭ�ϵ�·������
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
	 * ��ʼ��ת����ʾ���
	 */
	public void initTransPane(){
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(1, 3));
		jp.setPreferredSize(new Dimension(780, 300));
		
		//���Դ������ʾ�ؼ�
		jp_Source = new JPanel();
		jp_Source.setBorder(new TitledBorder("Դ����"));
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
		
		//��ӱ����ͺ�����ʾ����
		JPanel jp_double = new JPanel();
		jp_double.setLayout(new GridLayout(2, 1));
		
		jp_Variable = new JPanel();
		jp_Variable.setBorder(new TitledBorder("�����б�"));
		jp_Variable.setLayout(new BorderLayout());
		Vector title1 = new Vector();
		title1.addElement("������");
		title1.addElement("ԭ����");
		title1.addElement("Ŀ������");
		
		jtable_variable = new JTable(new Vector(), title1);
		JScrollPane jscrollPane2= new JScrollPane(jtable_variable);
		jscrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Variable.add(jscrollPane2, BorderLayout.CENTER);
		
		jp_Function = new JPanel();
		jp_Function.setBorder(new TitledBorder("�����б�"));
		jp_Function.setLayout(new BorderLayout());
		Vector title2 = new Vector();
		title2.addElement("������");
		title2.addElement("�����б�");
		title2.addElement("��������");
		jtable_function = new JTable(new Vector(), title2);
		JScrollPane jscrollPane3= new JScrollPane(jtable_function);
		jscrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jp_Function.add(jscrollPane3, BorderLayout.CENTER);
		
		
		jp_double.add(jp_Variable);
		jp_double.add(jp_Function);
		jp.add(jp_double);
		
		//���Ŀ��������
		jp_Target = new JPanel();
		jp_Target.setBorder(new TitledBorder("Ŀ�����"));
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
	 * ��ʼ��ת��������
	 */
	public void initPrintPane(){
		jp_Print = new JPanel();
		jp_Print.setBorder(new TitledBorder("ת�����"));
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
	 * ��������Ϣ�б�ͺ�����Ϣ�б����
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
	 * ��ʼ����Դ����APIת��
	 */
	public void startTrans(){
		resetAll();
		//����Ŀ���ļ���·����Ϣ
		Parser.setTargetFilePath(targetFilePath);
		//ʵ����ת��ʵ��
		new Transformation(sourceFilePath+sourceFileName);
		//��ת���ı�����Ϣ��ʾ�ڽ�����
		showVariableOnView();
		//��ת���ĺ�����Ϣ�б���ʾ�ڽ�����
		showMethodOnView();
		//����û�û������Ŀ���ļ���·���������Ĭ�ϵ�·��
		if(targetFilePath == null){
			targetFilePath = ".\\src\\";
		}
		//��ת����¼��ʾ�ڽ�����
		showTransRecordOnView(Trans.getTransRecord());
		
		//��ת��֮��Ľ����ʾ���û�������
		showFileOnView(targetFilePath+sourceFileName, jta_target);
	}
	
	/**
	 * ���û�ѡ��·��ʱ������·��ѡ��Ի��򣬻�ȡ�û�ѡ���·����Ϣ
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
	 * ��ת����������ʾ��Ҫת���ı�����Ϣ�б�
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
	 * ��ת����������ʾ��Ҫת�ĺ�����Ϣ�б�
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
	 * �ڽ����ϵ�jtextArea����ʾfileName�ļ�������
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
			
			jta_print.append("-------------ת����¼  "+count+"----------\n");
			jta_print.append("Դ���룺 \n");
			for(String source : sourceList){
				jta_print.append(source);
				jta_print.append("\n");
			}
			jta_print.append("Ŀ����룺 \n");
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
