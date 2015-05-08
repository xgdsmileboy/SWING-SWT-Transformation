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
	 * 变量名
	 */
	private String name;
	/*
	 * 变量类型
	 */
	private String type;
	/*
	 * 变量父容器
	 */
	private String parent;
	/*
	 * 与变量相关的语句
	 */
	private List<Statement> relativeStatementList;

	/**
	 * 无父容器初始化
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
	 * 设置变量类型
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 设置父容器
	 * 
	 * @param parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * 添加与变量相关的语句
	 * 
	 * @param statement
	 */
	public void addRelativeStatement(Statement statement) {
		relativeStatementList.add(statement);
	}

	/**
	 * 获取变量的名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 获取变量的类型
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 获取变量的父容器
	 * 
	 * @return
	 */
	public String getParent() {
		return this.parent;
	}

	/**
	 * 获取与变量先相关的语句列表
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
