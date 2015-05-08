package cn.edu.trans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class Source {
	/*
	 * �ļ���
	 */
	private String className;
	/*
	 * �洢���������
	 */
	private String packageDec;
	/*
	 * �洢��Ҫ���õİ��б���Ϣ
	 */
	private List<String> importDec;
	/*
	 * �洢ת��֮��������Ϣ
	 */
	private List<ISTM> statementList;

	/**
	 * ���캯��
	 */
	public Source() {
		packageDec = null;
		importDec = new ArrayList<>();
		statementList = new ArrayList<>();
	}

	/**
	 * ����������
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getClassName() {
		if (className == null) {
			return "nullClassName";
		}
		return className;
	}

	/**
	 * ���ð��������
	 * 
	 * @param packag
	 */
	public void setPackageDec(String packag) {
		packageDec = packag;
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public String getPackageDec() {
		return packageDec;
	}

	/**
	 * �����Ҫ���õİ����������
	 * 
	 * @param imp
	 */
	public void addImportDec(String imp) {
		importDec.add(imp);
	}

	/**
	 * ��ȡ��Ҫ���õİ����������
	 * 
	 * @return
	 */
	public List<String> getImportDec() {
		return importDec;
	}

	/**
	 * �����䵽ת��֮���Դ�����б���
	 * 
	 * @param statement
	 */
	public void addStatement(ISTM statement) {
		statementList.add(statement);
	}

	/**
	 * ��ȡת��֮���Դ�����б�
	 * 
	 * @return
	 */
	public List<ISTM> getStatementList() {
		return statementList;
	}
}
