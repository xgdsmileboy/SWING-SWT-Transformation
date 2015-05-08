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
	 * ת��֮���������ͣ� BLOCK �� CLAUSE
	 */
	private ETYPE mStatementType;
	/*
	 * ��Ӧ�������Ϣ
	 */
	private String mStatement;
	/*
	 * �������������Ҫ�����������к��е�����б�
	 */
	private List<ISTM> mStatementList;

	/**
	 * ���캯��
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
	 * ��block����������
	 * 
	 * @param statement
	 */
	public void addStatement(ISTM statement) {
		this.mStatementList.add(statement);
	}

	/**
	 * ���������е�����б�
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
	 * ��ȡ�����е��б�
	 * 
	 * @return
	 */
	public List<ISTM> getStatementList() {
		return mStatementList;
	}

	/**
	 * ��ȡ��������
	 */
	public ETYPE getStatementType() {
		return mStatementType;
	}

	/**
	 * ��ȡ��ǰ�����
	 * 
	 * @return
	 */
	public String getStatement() {
		return mStatement;
	}
}
