package cn.edu.trans;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class Clause implements ISTM {
	/*
	 * 语句类型
	 */
	private ETYPE mStatementType;
	/*
	 * 语句内容
	 */
	private String mStatement;

	/**
	 * 构造函数
	 * 
	 * @param statement
	 */
	public Clause(String statement) {
		this.mStatement = statement;
		mStatementType = ETYPE.CLAUSE;
	}

	/**
	 * 获取当前的语句
	 * 
	 * @return
	 */
	public String getStatement() {
		return mStatement;
	}

	/**
	 * 获取当前的语句类型
	 */
	@Override
	public ETYPE getStatementType() {
		return mStatementType;
	}

}
