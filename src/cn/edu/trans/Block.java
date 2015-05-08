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
public class Block implements ISTM {
	/*
	 * 转换之后的语句类型， BLOCK 或 CLAUSE
	 */
	private ETYPE mStatementType;
	/*
	 * 对应的语句信息
	 */
	private String mStatement;
	/*
	 * 如果是语句块则需要继续访问其中含有的语句列表
	 */
	private List<ISTM> mStatementList;

	/**
	 * 构造函数
	 * 
	 * @param statement
	 */
	public Block(String statement) {
		this(statement, new ArrayList<ISTM>());
	}

	/**
	 * 
	 * @param statement
	 * @param statementList
	 */
	public Block(String statement, List<ISTM> statementList) {
		this.mStatement = statement;
		this.mStatementList = statementList;
		this.mStatementType = ETYPE.BLOCK;
	}

	/**
	 * 向block块中添加语句
	 * 
	 * @param statement
	 */
	public void addStatement(ISTM statement) {
		this.mStatementList.add(statement);
	}

	/**
	 * 设置语句块中的语句列表
	 * 
	 * @param statementList
	 */
	public void setStatementList(List<ISTM> statementList) {
		if (this.mStatementList == null || this.mStatementList.size() == 0) {
			this.mStatementList = statementList;
		} else {
			for (ISTM istm : statementList) {
				this.mStatementList.add(istm);
			}
		}

	}

	/**
	 * 获取语句块中的列表
	 * 
	 * @return
	 */
	public List<ISTM> getStatementList() {
		return mStatementList;
	}

	/**
	 * 获取语句的类型
	 */
	public ETYPE getStatementType() {
		return mStatementType;
	}

	/**
	 * 获取当前的语句
	 * 
	 * @return
	 */
	public String getStatement() {
		return mStatement;
	}
}
