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
	 * 规则的类型
	 */
	private ERuleType ruleType;
	/*
	 * 规则中涉及到的变量列表
	 */
	private List<TermEntity> termList;
	/*
	 * 规则需要进行匹配的语句
	 */
	private List<String> deleteExpressionList;
	/*
	 * 匹配规则后需要转换的语句
	 */
	private List<String> addExpressionList;

	/**
	 * 构造函数
	 */
	public RuleEntity() {
		termList = new ArrayList<>();
		deleteExpressionList = new ArrayList<>();
		addExpressionList = new ArrayList<>();
	}

	/**
	 * 设置规则的类型
	 * 
	 * @param rType
	 */
	public void setRuleType(ERuleType rType) {
		ruleType = rType;
	}

	/**
	 * 获取规则的类型
	 * 
	 * @return
	 */
	public ERuleType getRuleType() {
		return ruleType;
	}

	/**
	 * 添加变量信息
	 * 
	 * @param name
	 * @param sourceType
	 * @param targetType
	 */
	public void addTerm(String name, String sourceType, String targetType) {
		addTerm(new TermEntity(name, sourceType, targetType));
	}

	/**
	 * 添加变量
	 * 
	 * @param term
	 */
	public void addTerm(TermEntity term) {
		termList.add(term);
	}

	/**
	 * 设置变量列表
	 * 
	 * @param termList
	 */
	public void setTermList(List termList) {
		this.termList = termList;
	}

	/**
	 * 过去变量列表
	 * 
	 * @return
	 */
	public List<TermEntity> getTermList() {
		return termList;
	}

	/**
	 * 设置匹配表达式列表信息
	 * 
	 * @param delExpList
	 */
	public void setDeleteExpList(List delExpList) {
		deleteExpressionList = delExpList;
	}

	/**
	 * 设置转换之后替换的语句列表信息
	 * 
	 * @param addExpList
	 */
	public void setAddExpList(List addExpList) {
		addExpressionList = addExpList;
	}

	/**
	 * 获取替换语句列表
	 * 
	 * @return
	 */
	public List<String> getDeleteExpList() {
		return deleteExpressionList;
	}

	/**
	 * 获取替换语句列表信息
	 * 
	 * @return
	 */
	public List<String> getAddList() {
		return addExpressionList;
	}

	/**
	 * 判断两条规则是否相同
	 * 
	 * @param ruleEntity
	 * @return
	 */
	public boolean equals(RuleEntity ruleEntity) {
		boolean equal = true;
		// 判断规则的类型是否相同
		if (ruleEntity.getRuleType() != ruleType) {
			return false;
		}
		// 对需要匹配的语句进行比较
		for (String del : ruleEntity.getDeleteExpList()) {
			if (!deleteExpressionList.contains(del)) {
				equal = false;
				break;
			}
		}
		// 同上
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
	 * 根据变量的名称获取变量的类型
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
