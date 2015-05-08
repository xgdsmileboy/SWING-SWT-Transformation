package cn.edu.info;

import org.eclipse.jdt.core.dom.Statement;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class StatementInfo {
	// 抽象语法树中的语句信息
	private Statement statement;
	// 语句类型信息
	private EStatementType statementType;

	/**
	 * 构造函数
	 * 
	 * @param statement
	 * @param type
	 */
	public StatementInfo(Statement statement, EStatementType type) {
		this.statement = statement;
		this.statementType = type;
	}

	/**
	 * 获取语句
	 * 
	 * @return
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * 获取语句的类型
	 * 
	 * @return
	 */
	public EStatementType getStatementType() {
		return statementType;
	}

	@Override
	public String toString() {
		return "StatementInfo [statement=" + statement + ", statementType="
				+ statementType + "]";
	}

}
