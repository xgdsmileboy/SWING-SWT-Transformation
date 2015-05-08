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
	 * �������
	 */
	private ETYPE mStatementType;
	/*
	 * �������
	 */
	private String mStatement;

	/**
	 * ���캯��
	 * 
	 * @param statement
	 */
	public Clause(String statement) {
		this.mStatement = statement;
		mStatementType = ETYPE.CLAUSE;
	}

	/**
	 * ��ȡ��ǰ�����
	 * 
	 * @return
	 */
	public String getStatement() {
		return mStatement;
	}

	/**
	 * ��ȡ��ǰ���������
	 */
	@Override
	public ETYPE getStatementType() {
		return mStatementType;
	}

}
