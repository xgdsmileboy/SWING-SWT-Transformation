package cn.edu.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class MethodInfo {
	/*
	 * �����﷨���еĺ����ڵ���Ϣ
	 */
	private MethodDeclaration mMethodDeclaration;
	/*
	 * �洢�����ĸ��ڵ㣬�����ڵ���
	 */
	private ClassInfo mParentClass;
	/*
	 * ���������η��б�public��static��final��
	 */
	private List<Modifier> mModifierList;
	/*
	 * �����ķ�������
	 */
	private Type mMethodReturnType;
	/*
	 * ������������Ϣ
	 */
	private String mMethodName;
	/*
	 * �����Ĳ����б�������Ϣ
	 */
	private List<Type> mParamsTypeList;
	/*
	 * �����Ĳ����б�������Ϣ��������б��������Ϣһһ��Ӧ
	 */
	private List<String> mParamsNameList;
	/*
	 * ����������
	 */
	private MethodBodyInfo mBodyInfo;

	/**
	 * ���캯������������﷨���ķ����ڵ���н�������
	 * 
	 * @param methodDeclaration
	 * @param parentClass
	 */
	public MethodInfo(MethodDeclaration methodDeclaration, ClassInfo parentClass) {
		// ���ú������ڵ�����Ϣ
		setmParentClass(parentClass);
		// ���ú����ĳ����﷨�������Ϣ
		setmMethodDeclaration(methodDeclaration);
		// ���ú��������η���Ϣ �磺public��private��
		setmModifierList(methodDeclaration.modifiers());
		// ���ú����ķ�������
		setmMethodReturnType(methodDeclaration.getReturnType2());
		// ���ú���������
		setmMethodName(methodDeclaration.getName().toString());
		// ���ú����Ĳ����б�
		setmParamsList(methodDeclaration.parameters());
		// ���ú�����
		mBodyInfo = new MethodBodyInfo(methodDeclaration.getBody(), this);
	}

	/**
	 * ���ú����ڳ����﷨���еĽṹ��Ϣ
	 * 
	 * @param methodDeclaration
	 */
	void setmMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.mMethodDeclaration = methodDeclaration;
	}

	/**
	 * ���ú��������η���Ϣ
	 * 
	 * @param modifiers
	 */
	void setmModifierList(List modifiers) {
		this.mModifierList = modifiers;
	}

	/**
	 * ���ú����ķ���������Ϣ
	 * 
	 * @param returnType
	 */
	void setmMethodReturnType(Type returnType) {
		this.mMethodReturnType = returnType;
	}

	/**
	 * ���ú���������
	 * 
	 * @param methodName
	 */
	void setmMethodName(String methodName) {
		this.mMethodName = methodName;
	}

	/**
	 * ���ú����Ĳ����б���Ϣ
	 * 
	 * @param parameters
	 */
	void setmParamsList(List parameters) {
		this.mParamsTypeList = new ArrayList<>();
		this.mParamsNameList = new ArrayList<>();
		for (int i = 0; i < parameters.size(); i++) {
			SingleVariableDeclaration svd = (SingleVariableDeclaration) parameters
					.get(i);
			this.mParamsTypeList.add(svd.getType());
			this.mParamsNameList.add(svd.getName().toString());
		}
	}

	/**
	 * ��ȡ�����ĳ����﷨���еĽڵ���Ϣ
	 * 
	 * @return
	 */
	public MethodDeclaration getmMethodDeclaration() {
		return mMethodDeclaration;
	}

	/**
	 * ��ȡ���������η���Ϣ
	 * 
	 * @return
	 */
	public List<Modifier> getmModifierList() {
		return mModifierList;
	}

	/**
	 * ��ȡ�����ķ�������
	 * 
	 * @return
	 */
	public Type getmMethodReturnType() {
		return mMethodReturnType;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public String getmMethodName() {
		return mMethodName;
	}

	/**
	 * ��ȡ�����Ĳ����б�������Ϣ
	 * 
	 * @return
	 */
	public List<String> getmParamsNameList() {
		return this.mParamsNameList;
	}

	/**
	 * ��ȡ�����Ĳ����б�������Ϣ
	 * 
	 * @return
	 */
	public List<Type> getmParamsTypeList() {
		return this.mParamsTypeList;
	}

	/**
	 * �жϺ����Ĳ����б����Ƿ���parameterName���Ƶı���
	 * 
	 * @param parameterName
	 * @return
	 */
	public boolean containParameter(String parameterName) {
		return this.mParamsNameList.contains(parameterName);
	}

	/**
	 * �жϺ����Ĳ����б����Ƿ���parameterType���͵ı���
	 * 
	 * @param parameterType
	 * @return
	 */
	public boolean containParameter(Type parameterType) {
		return this.mParamsTypeList.contains(parameterType);
	}

	/**
	 * ��ȡ�����Ĳ�������ΪparameterName�Ĳ���������Ϣ
	 * 
	 * @param parameterName
	 * @return
	 */
	public Type getParameterType(String parameterName) {
		Type type = null;
		if (containParameter(parameterName)) {
			type = this.mParamsTypeList.get(this.mParamsNameList
					.indexOf(parameterName));
		}
		return type;
	}

	/**
	 * ��ȡ�����Ĳ�������ΪparameterType�Ĳ���������Ϣ
	 * 
	 * @param parameterType
	 * @return
	 */
	public String getParameterName(String parameterType) {
		String name = null;
		if (containParameter(parameterType)) {
			name = this.mParamsNameList.get(this.mParamsTypeList
					.indexOf(parameterType));
		}
		return name;
	}

	/**
	 * ���ú�����������Ϣ
	 * 
	 * @param parentClass
	 */
	public void setmParentClass(ClassInfo parentClass) {
		mParentClass = parentClass;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public ClassInfo getmParentClass() {
		return mParentClass;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public MethodBodyInfo getmBodyInfo() {
		return mBodyInfo;
	}

	@Override
	public String toString() {
		return "MethodInfo [mParentClass=" + mParentClass.getClassName()
				+ ", mModifierList=" + mModifierList + ", mMethodReturnType="
				+ mMethodReturnType + ", mMethodName=" + mMethodName
				+ ", mParamsTypeList=" + mParamsTypeList + ", mParamsNameList="
				+ mParamsNameList + ", mBodyInfo=" + mBodyInfo + "]";

	}

}
