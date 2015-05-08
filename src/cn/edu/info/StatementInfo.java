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
	// �����﷨���е������Ϣ
	private Statement statement;
	// ���������Ϣ
	private EStatementType statementType;

	/**
	 * ���캯��
	 * 
	 * @param statement
	 * @param type
	 */
	public StatementInfo(Statement statement, EStatementType type) {
		this.statement = statement;
		this.statementType = type;
	}

	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * ��ȡ��������
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
