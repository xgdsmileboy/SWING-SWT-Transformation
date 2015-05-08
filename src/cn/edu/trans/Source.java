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
	 * 文件名
	 */
	private String className;
	/*
	 * 存储包声明语句
	 */
	private String packageDec;
	/*
	 * 存储需要引用的包列表信息
	 */
	private List<String> importDec;
	/*
	 * 存储转换之后的语句信息
	 */
	private List<ISTM> statementList;

	/**
	 * 构造函数
	 */
	public Source() {
		packageDec = null;
		importDec = new ArrayList<>();
		statementList = new ArrayList<>();
	}

	/**
	 * 设置累名称
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 获取类名称
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
	 * 设置包声明语句
	 * 
	 * @param packag
	 */
	public void setPackageDec(String packag) {
		packageDec = packag;
	}

	/**
	 * 获取包声明语句
	 * 
	 * @return
	 */
	public String getPackageDec() {
		return packageDec;
	}

	/**
	 * 添加需要引用的包的声明语句
	 * 
	 * @param imp
	 */
	public void addImportDec(String imp) {
		importDec.add(imp);
	}

	/**
	 * 获取需要引用的包的声明语句
	 * 
	 * @return
	 */
	public List<String> getImportDec() {
		return importDec;
	}

	/**
	 * 添加语句到转换之后的源代码列表中
	 * 
	 * @param statement
	 */
	public void addStatement(ISTM statement) {
		statementList.add(statement);
	}

	/**
	 * 获取转换之后的源代码列表
	 * 
	 * @return
	 */
	public List<ISTM> getStatementList() {
		return statementList;
	}
}
