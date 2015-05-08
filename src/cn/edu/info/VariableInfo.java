package cn.edu.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class VariableInfo {
	/*
	 * ������
	 */
	private String name;
	/*
	 * ��������
	 */
	private String type;
	/*
	 * ����������
	 */
	private String parent;
	/*
	 * �������ص����
	 */
	private List<Statement> relativeStatementList;

	/**
	 * �޸�������ʼ��
	 * 
	 * @param name
	 * @param type
	 */
	public VariableInfo(String name, String type) {
		this(name, type, null);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param parent
	 */
	public VariableInfo(String name, String type, String parent) {
		this.name = name;
		this.type = type;
		this.parent = parent;
		relativeStatementList = new ArrayList<>();
	}

	/**
	 * ���ñ�������
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * ���ø�����
	 * 
	 * @param parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * ����������ص����
	 * 
	 * @param statement
	 */
	public void addRelativeStatement(Statement statement) {
		relativeStatementList.add(statement);
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * ��ȡ�����ĸ�����
	 * 
	 * @return
	 */
	public String getParent() {
		return this.parent;
	}

	/**
	 * ��ȡ���������ص�����б�
	 * 
	 * @return
	 */
	public List<Statement> getRelativeStatement() {
		return relativeStatementList;
	}

	@Override
	public String toString() {
		return "VariableInfo [name=" + name + ", type=" + type + ", parent="
				+ parent + ", relativeStatementList=" + relativeStatementList
				+ "]";
	}

}
