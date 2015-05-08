package cn.edu.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class RuleEntity {
	/*
	 * ���������
	 */
	private ERuleType ruleType;
	/*
	 * �������漰���ı����б�
	 */
	private List<TermEntity> termList;
	/*
	 * ������Ҫ����ƥ������
	 */
	private List<String> deleteExpressionList;
	/*
	 * ƥ��������Ҫת�������
	 */
	private List<String> addExpressionList;

	/**
	 * ���캯��
	 */
	public RuleEntity() {
		termList = new ArrayList<>();
		deleteExpressionList = new ArrayList<>();
		addExpressionList = new ArrayList<>();
	}

	/**
	 * ���ù��������
	 * 
	 * @param rType
	 */
	public void setRuleType(ERuleType rType) {
		ruleType = rType;
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public ERuleType getRuleType() {
		return ruleType;
	}

	/**
	 * ��ӱ�����Ϣ
	 * 
	 * @param name
	 * @param sourceType
	 * @param targetType
	 */
	public void addTerm(String name, String sourceType, String targetType) {
		addTerm(new TermEntity(name, sourceType, targetType));
	}

	/**
	 * ��ӱ���
	 * 
	 * @param term
	 */
	public void addTerm(TermEntity term) {
		termList.add(term);
	}

	/**
	 * ���ñ����б�
	 * 
	 * @param termList
	 */
	public void setTermList(List termList) {
		this.termList = termList;
	}

	/**
	 * ��ȥ�����б�
	 * 
	 * @return
	 */
	public List<TermEntity> getTermList() {
		return termList;
	}

	/**
	 * ����ƥ����ʽ�б���Ϣ
	 * 
	 * @param delExpList
	 */
	public void setDeleteExpList(List delExpList) {
		deleteExpressionList = delExpList;
	}

	/**
	 * ����ת��֮���滻������б���Ϣ
	 * 
	 * @param addExpList
	 */
	public void setAddExpList(List addExpList) {
		addExpressionList = addExpList;
	}

	/**
	 * ��ȡ�滻����б�
	 * 
	 * @return
	 */
	public List<String> getDeleteExpList() {
		return deleteExpressionList;
	}

	/**
	 * ��ȡ�滻����б���Ϣ
	 * 
	 * @return
	 */
	public List<String> getAddList() {
		return addExpressionList;
	}

	/**
	 * �ж����������Ƿ���ͬ
	 * 
	 * @param ruleEntity
	 * @return
	 */
	public boolean equals(RuleEntity ruleEntity) {
		boolean equal = true;
		// �жϹ���������Ƿ���ͬ
		if (ruleEntity.getRuleType() != ruleType) {
			return false;
		}
		// ����Ҫƥ��������бȽ�
		for (String del : ruleEntity.getDeleteExpList()) {
			if (!deleteExpressionList.contains(del)) {
				equal = false;
				break;
			}
		}
		// ͬ��
		if (equal) {
			for (String del : deleteExpressionList) {
				if (!ruleEntity.getDeleteExpList().contains(del)) {
					equal = false;
					break;
				}
			}
		}

		return equal;
	}

	/**
	 * ���ݱ��������ƻ�ȡ����������
	 * 
	 * @param variableName
	 * @return
	 */
	public String getVariableType(String variableName) {
		String type = "";
		for (TermEntity termEntity : termList) {
			if (variableName.equals(termEntity.name)) {
				type = termEntity.sType;
				break;
			}
		}
		return type;
	}

	@Override
	public String toString() {
		return "RuleEntity [termList=" + termList + ", deleteExpressionList="
				+ deleteExpressionList + ", addExpressionList="
				+ addExpressionList + "]";
	}

}
