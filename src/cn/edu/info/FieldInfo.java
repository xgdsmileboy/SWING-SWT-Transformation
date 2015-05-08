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
	 * ȫ�ֱ����ڳ����﷨���еĽڵ�
	 */
	private VariableDeclarationFragment mVariableDeclarationFragment;
	/*
	 * ȫ�ֱ���������
	 */
	private String mVariableName;
	/*
	 * ȫ�ֱ���������
	 */
	private Type mVariableType;
	/*
	 * �洢ȫ�ֱ������ڵ���
	 */
	private ClassInfo mParentClass;
	/*
	 * ȫ�ֱ����ĸ�������Ϣ
	 */
	private String mParentContent;
	/*
	 * ȫ�ֱ����ĳ�ʼ�����ʽ
	 */
	private Expression mVariableInitializer;
	/*
	 * ȫ�ֱ���������
	 */
	private List<String> mVariableModifierList;
	/*
	 * �洢������˱����йص����
	 */
	private List<Statement> mRelativeStatementList;

	/**
	 * ���캯����ȫ�ֱ����Ķ�����Ϣ��ȫ�ֱ�����������Ϣ
	 * 
	 * @param variableDeclarationFragment
	 * @param variableType
	 */
	public FieldInfo(VariableDeclarationFragment variableDeclarationFragment,
			Type variableType) {
		// ��ʼ����ȫ�ֱ�����ص�����б�
		mRelativeStatementList = new ArrayList<>();
		// ���ñ����ڳ����﷨���еĽṹ
		setVariableDeclarationFragment(variableDeclarationFragment);
		// ���ñ��������ǹ�
		setmVariableName(variableDeclarationFragment.getName().toString());
		// ���ñ���������
		setmVariableType(variableType);
		// ���ñ����ĳ�ʼ����Ϣ
		setmVariableInitializer(variableDeclarationFragment.getInitializer());
	}

	/**
	 * ����ȫ�ֱ�����������Ϣ
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
	 * ����ȫ�ֱ����ڳ����﷨���ֵĽڵ�
	 * 
	 * @param variableDeclarationFragment
	 */
	void setVariableDeclarationFragment(
			VariableDeclarationFragment variableDeclarationFragment) {
		this.mVariableDeclarationFragment = variableDeclarationFragment;
	}

	/**
	 * ����ȫ�ֱ���������
	 * 
	 * @param variableName
	 */
	void setmVariableName(String variableName) {
		this.mVariableName = variableName;
	}

	/**
	 * ����ȫ�ֱ���������
	 * 
	 * @param variableType
	 */
	void setmVariableType(Type variableType) {
		this.mVariableType = variableType;
	}

	/**
	 * ����ȫ�ֱ����ĳ�ʼ�����ʽ
	 * 
	 * @param expression
	 */
	public void setmVariableInitializer(Expression expression) {
		this.mVariableInitializer = expression;
	}

	/**
	 * ���ÿؼ��ĸ�����
	 * 
	 * @param parentContent
	 */
	public void setmParentContent(String parentContent) {
		this.mParentContent = parentContent;
	}

	/**
	 * ��ȡȫ�ֱ����ڳ����﷨���еĽڵ�
	 * 
	 * @return
	 */
	public VariableDeclarationFragment getmVariableDeclarationFragment() {
		return mVariableDeclarationFragment;
	}

	/**
	 * ��ȡȫ�ֱ���������
	 * 
	 * @return
	 */
	public String getmVariableName() {
		return mVariableName;
	}

	/**
	 * ��ȡȫ�ֱ�����������Ϣ
	 * 
	 * @return
	 */
	public Type getmVariableType() {
		return mVariableType;
	}

	/**
	 * ��ȡ�����ĸ�������Ϣ
	 * 
	 * @return
	 */
	public String getmParentContent() {
		return mParentContent;
	}

	/**
	 * ��ӱ�����صı��ʽ
	 * 
	 * @param statement
	 */
	public void addmRelativeStatementItem(Statement statement) {
		mRelativeStatementList.add(statement);
	}

	/**
	 * ��ȡ������صı��ʽ�б�
	 * 
	 * @return
	 */
	public List<Statement> getmRelativeStatementList() {
		return mRelativeStatementList;
	}

	/**
	 * �ж�ĳһ�������ر��ʽ�Ƿ����
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
	 * ����ȫ�ֱ������ڵĸ���
	 * 
	 * @param parentClass
	 */
	public void setmParentClass(ClassInfo parentClass) {
		mParentClass = parentClass;
	}

	/**
	 * ��ȡȫ�ֱ������ڵĸ���
	 * 
	 * @return
	 */
	public ClassInfo getmParentClass() {
		return mParentClass;
	}

	/**
	 * ��ȡȫ�ֱ����ĳ�ʼ�����ʽ
	 * 
	 * @return
	 */
	public Expression getmVariableInitializer() {
		return mVariableInitializer;
	}

	/**
	 * ��ȡ������������Ϣ
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
