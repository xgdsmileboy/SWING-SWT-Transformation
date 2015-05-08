package cn.edu.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class FieldInfo {
	/*
	 * 全局变量在抽象语法树中的节点
	 */
	private VariableDeclarationFragment mVariableDeclarationFragment;
	/*
	 * 全局变量的名称
	 */
	private String mVariableName;
	/*
	 * 全局变量的类型
	 */
	private Type mVariableType;
	/*
	 * 存储全局变量所在的类
	 */
	private ClassInfo mParentClass;
	/*
	 * 全局变量的父容器信息
	 */
	private String mParentContent;
	/*
	 * 全局变量的初始化表达式
	 */
	private Expression mVariableInitializer;
	/*
	 * 全局变量的修饰
	 */
	private List<String> mVariableModifierList;
	/*
	 * 存储所有与此变量有关的语句
	 */
	private List<Statement> mRelativeStatementList;

	/**
	 * 构造函数，全局变量的定义信息，全局变量的类型信息
	 * 
	 * @param variableDeclarationFragment
	 * @param variableType
	 */
	public FieldInfo(VariableDeclarationFragment variableDeclarationFragment,
			Type variableType) {
		// 初始化与全局变量相关的语句列表
		mRelativeStatementList = new ArrayList<>();
		// 设置变量在抽象语法树中的结构
		setVariableDeclarationFragment(variableDeclarationFragment);
		// 设置变量的名城管
		setmVariableName(variableDeclarationFragment.getName().toString());
		// 设置变量的类型
		setmVariableType(variableType);
		// 设置变量的初始化信息
		setmVariableInitializer(variableDeclarationFragment.getInitializer());
	}

	/**
	 * 设置全局变量的修饰信息
	 * 
	 * @param variableModifierList
	 */
	public void setmVariableModifierList(List variableModifierList) {
		this.mVariableModifierList = new ArrayList<>();
		for (Object obj : variableModifierList) {
			mVariableModifierList.add(obj.toString());
		}
	}

	/**
	 * 设置全局变量在抽象语法树种的节点
	 * 
	 * @param variableDeclarationFragment
	 */
	void setVariableDeclarationFragment(
			VariableDeclarationFragment variableDeclarationFragment) {
		this.mVariableDeclarationFragment = variableDeclarationFragment;
	}

	/**
	 * 设置全局变量的名称
	 * 
	 * @param variableName
	 */
	void setmVariableName(String variableName) {
		this.mVariableName = variableName;
	}

	/**
	 * 设置全局变量的类型
	 * 
	 * @param variableType
	 */
	void setmVariableType(Type variableType) {
		this.mVariableType = variableType;
	}

	/**
	 * 设置全局变量的初始化表达式
	 * 
	 * @param expression
	 */
	public void setmVariableInitializer(Expression expression) {
		this.mVariableInitializer = expression;
	}

	/**
	 * 设置控件的父容器
	 * 
	 * @param parentContent
	 */
	public void setmParentContent(String parentContent) {
		this.mParentContent = parentContent;
	}

	/**
	 * 获取全局变量在抽象语法树中的节点
	 * 
	 * @return
	 */
	public VariableDeclarationFragment getmVariableDeclarationFragment() {
		return mVariableDeclarationFragment;
	}

	/**
	 * 获取全局变量的名称
	 * 
	 * @return
	 */
	public String getmVariableName() {
		return mVariableName;
	}

	/**
	 * 获取全局变量的类型信息
	 * 
	 * @return
	 */
	public Type getmVariableType() {
		return mVariableType;
	}

	/**
	 * 获取变量的父容器信息
	 * 
	 * @return
	 */
	public String getmParentContent() {
		return mParentContent;
	}

	/**
	 * 添加变量相关的表达式
	 * 
	 * @param statement
	 */
	public void addmRelativeStatementItem(Statement statement) {
		mRelativeStatementList.add(statement);
	}

	/**
	 * 获取变量相关的表达式列表
	 * 
	 * @return
	 */
	public List<Statement> getmRelativeStatementList() {
		return mRelativeStatementList;
	}

	/**
	 * 判断某一与变量相关表达式是否存在
	 * 
	 * @param statement
	 * @return
	 */
	public boolean containExpression(Statement statement) {
		boolean flag = false;
		for (Statement exp : mRelativeStatementList) {
			if (exp.equals(statement)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 设置全局变量所在的父类
	 * 
	 * @param parentClass
	 */
	public void setmParentClass(ClassInfo parentClass) {
		mParentClass = parentClass;
	}

	/**
	 * 获取全局变量所在的父类
	 * 
	 * @return
	 */
	public ClassInfo getmParentClass() {
		return mParentClass;
	}

	/**
	 * 获取全局变量的初始化表达式
	 * 
	 * @return
	 */
	public Expression getmVariableInitializer() {
		return mVariableInitializer;
	}

	/**
	 * 获取变量的修饰信息
	 * 
	 * @return
	 */
	public List<String> getmVariableModifierList() {
		return mVariableModifierList;
	}

	@Override
	public String toString() {
		return "FieldInfo [mVariableName=" + mVariableName + ", mVariableType="
				+ mVariableType + ", mParentContent=" + mParentContent
				+ ", mVariableInitializer=" + mVariableInitializer
				+ ", mVariableModifierList=" + mVariableModifierList
				+ ", mRelativeExpressionList=" + mRelativeStatementList + "]";
	}
}
