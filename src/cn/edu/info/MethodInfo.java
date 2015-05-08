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
	 * 抽象语法树中的函数节点信息
	 */
	private MethodDeclaration mMethodDeclaration;
	/*
	 * 存储函数的父节点，即所在的类
	 */
	private ClassInfo mParentClass;
	/*
	 * 函数的修饰符列表：public、static、final等
	 */
	private List<Modifier> mModifierList;
	/*
	 * 函数的返回类型
	 */
	private Type mMethodReturnType;
	/*
	 * 函数的名称信息
	 */
	private String mMethodName;
	/*
	 * 函数的参数列表名称信息
	 */
	private List<Type> mParamsTypeList;
	/*
	 * 函数的参数列表类型信息，与参数列表的名称信息一一对应
	 */
	private List<String> mParamsNameList;
	/*
	 * 函数体内容
	 */
	private MethodBodyInfo mBodyInfo;

	/**
	 * 构造函数，传入抽象语法树的方法节点进行解析保存
	 * 
	 * @param methodDeclaration
	 * @param parentClass
	 */
	public MethodInfo(MethodDeclaration methodDeclaration, ClassInfo parentClass) {
		// 设置函数所在的类信息
		setmParentClass(parentClass);
		// 设置函数的抽象语法树结点信息
		setmMethodDeclaration(methodDeclaration);
		// 设置函数的修饰符信息 如：public、private等
		setmModifierList(methodDeclaration.modifiers());
		// 设置函数的返回类型
		setmMethodReturnType(methodDeclaration.getReturnType2());
		// 设置函数的名称
		setmMethodName(methodDeclaration.getName().toString());
		// 设置函数的参数列表
		setmParamsList(methodDeclaration.parameters());
		// 设置函数体
		mBodyInfo = new MethodBodyInfo(methodDeclaration.getBody(), this);
	}

	/**
	 * 设置函数在抽象语法树中的结构信息
	 * 
	 * @param methodDeclaration
	 */
	void setmMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.mMethodDeclaration = methodDeclaration;
	}

	/**
	 * 设置函数的修饰符信息
	 * 
	 * @param modifiers
	 */
	void setmModifierList(List modifiers) {
		this.mModifierList = modifiers;
	}

	/**
	 * 设置函数的返回类型信息
	 * 
	 * @param returnType
	 */
	void setmMethodReturnType(Type returnType) {
		this.mMethodReturnType = returnType;
	}

	/**
	 * 设置函数的名称
	 * 
	 * @param methodName
	 */
	void setmMethodName(String methodName) {
		this.mMethodName = methodName;
	}

	/**
	 * 设置函数的参数列表信息
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
	 * 获取函数的抽象语法树中的节点信息
	 * 
	 * @return
	 */
	public MethodDeclaration getmMethodDeclaration() {
		return mMethodDeclaration;
	}

	/**
	 * 获取函数的修饰符信息
	 * 
	 * @return
	 */
	public List<Modifier> getmModifierList() {
		return mModifierList;
	}

	/**
	 * 获取函数的返回类型
	 * 
	 * @return
	 */
	public Type getmMethodReturnType() {
		return mMethodReturnType;
	}

	/**
	 * 获取函数的名称
	 * 
	 * @return
	 */
	public String getmMethodName() {
		return mMethodName;
	}

	/**
	 * 获取函数的参数列表名称信息
	 * 
	 * @return
	 */
	public List<String> getmParamsNameList() {
		return this.mParamsNameList;
	}

	/**
	 * 获取函数的参数列表类型信息
	 * 
	 * @return
	 */
	public List<Type> getmParamsTypeList() {
		return this.mParamsTypeList;
	}

	/**
	 * 判断函数的参数列表中是否含有parameterName名称的变量
	 * 
	 * @param parameterName
	 * @return
	 */
	public boolean containParameter(String parameterName) {
		return this.mParamsNameList.contains(parameterName);
	}

	/**
	 * 判断函数的参数列表中是否含有parameterType类型的变量
	 * 
	 * @param parameterType
	 * @return
	 */
	public boolean containParameter(Type parameterType) {
		return this.mParamsTypeList.contains(parameterType);
	}

	/**
	 * 获取函数的参数名称为parameterName的参数类型信息
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
	 * 获取函数的参数类型为parameterType的参数类型信息
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
	 * 设置函数所在类信息
	 * 
	 * @param parentClass
	 */
	public void setmParentClass(ClassInfo parentClass) {
		mParentClass = parentClass;
	}

	/**
	 * 获取函数所在类
	 * 
	 * @return
	 */
	public ClassInfo getmParentClass() {
		return mParentClass;
	}

	/**
	 * 获取函数体内容
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
