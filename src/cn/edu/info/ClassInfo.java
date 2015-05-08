package cn.edu.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment : 类的中间层结构
 */
public class ClassInfo {
	/*
	 * 类或是接口节点
	 */
	TypeDeclaration mTypeDeclaration;
	/*
	 * 标注是否为接口类型
	 */
	boolean mIsInterface;
	/*
	 * 类的父类
	 */
	Type mSuperClassType;
	/*
	 * 类的修饰符列表
	 */
	List<Modifier> mClassModifierList;
	/*
	 * 类名
	 */
	String mClassName;
	/*
	 * 实现的接口列表
	 */
	List<Type> mImplementsInterfaceList;
	/*
	 * 类中全局变量列表
	 */
	List<FieldInfo> mFieldDeclarationList;
	/*
	 * 类中方法列表
	 */
	List<MethodInfo> mMethodInfoList;

	// /*
	// * 类中定义的内部类列表
	// */
	// List<TypeDeclaration> mTypeDeclarationList;

	public ClassInfo(TypeDeclaration typeDeclaration) {
		// 将原类或接口结构进行存储，目前在解析过程中没有使用
		mTypeDeclaration = typeDeclaration;
		// 设置类或接口的修饰符 如：public、private等等
		setClassModifiers(typeDeclaration.modifiers());
		// 设置类或接口的名称
		setClassName(typeDeclaration.getName().toString());
		// 设置父类
		setSuperClassType(typeDeclaration.getSuperclassType());
		// 设置是否为接口
		setIsInterface(typeDeclaration.isInterface());
		// 设置实现的接口信息
		setmImplementsInterfaceList(typeDeclaration.superInterfaceTypes());
		// 保存类或接口中所定义的全局变量
		mFieldDeclarationList = new ArrayList<>();
		// 保存类或接口中定义的方法信息
		mMethodInfoList = new ArrayList<>();
	}

	/**
	 * 获取类或接口的抽象语法树结构
	 * 
	 * @return
	 */
	public TypeDeclaration getmTypeDeclaration() {
		return mTypeDeclaration;
	}

	/**
	 * 设置类名
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		mClassName = className;
	}

	/**
	 * 设置类的修饰符信息
	 * 
	 * @param classModifierList
	 */
	public void setClassModifiers(List classModifierList) {
		mClassModifierList = classModifierList;
	}

	/**
	 * 设置和获取是否为接口信息
	 * 
	 * @param isInterface
	 */
	public void setIsInterface(boolean isInterface) {
		mIsInterface = isInterface;
	}

	/**
	 * 获取是否为接口类型
	 * 
	 * @return
	 */
	public boolean isInterface() {
		return mIsInterface;
	}

	/**
	 * 类的父类的设置和获取
	 * 
	 * @param superClassType
	 */
	public void setSuperClassType(Type superClassType) {
		mSuperClassType = superClassType;
	}

	public Type getSuperClassType() {
		return mSuperClassType;
	}

	/**
	 * 获取类名
	 * 
	 * @return
	 */
	public String getClassName() {
		return mClassName;
	}

	/**
	 * 获取类的修饰符信息
	 * 
	 * @return
	 */
	public List<Modifier> getmClassModiferList() {
		return mClassModifierList;
	}

	/**
	 * 设置和获取实现接口列表
	 * 
	 * @param implementsIterfaceList
	 */
	public void setmImplementsInterfaceList(List implementsIterfaceList) {
		mImplementsInterfaceList = implementsIterfaceList;
	}

	public List<Type> getmImplementsInterfaceList() {
		return mImplementsInterfaceList;
	}

	/**
	 * 获取类中定义的全局变量列表
	 * 
	 * @return
	 */
	public List<FieldInfo> getmFieldDeclarationList() {
		return mFieldDeclarationList;
	}

	/**
	 * 获取类中所定义的方法列表
	 * 
	 * @return
	 */
	public List<MethodInfo> getmMethodDeclarationList() {
		return mMethodInfoList;
	}

	// /**
	// * 获取类中的内部类列表信息
	// * @return
	// */
	// public List<TypeDeclaration> getmTypeDeclarationList() {
	// return mTypeDeclarationList;
	// }
	/**
	 * 添加全局变量信息
	 * 
	 * @param fieldDeclaration
	 */
	public void addFieldDeclaration(FieldDeclaration fieldDeclaration) {
		Type type = fieldDeclaration.getType();
		List fragments = fieldDeclaration.fragments();
		for (int i = 0; i < fragments.size(); i++) {
			FieldInfo fieldInfo = new FieldInfo(
					(VariableDeclarationFragment) fragments.get(i), type);
			fieldInfo.setmVariableModifierList(fieldDeclaration.modifiers());
			this.mFieldDeclarationList.add(fieldInfo);
			fieldInfo.setmParentClass(this);
		}

	}

	/**
	 * 添加方法信息
	 * 
	 * @param methodInfo
	 */
	public void addMethodInfo(MethodInfo methodInfo) {
		this.mMethodInfoList.add(methodInfo);
	}

	@Override
	public String toString() {
		return "ClassInfo [" + "\n" + "mIsInterface=" + mIsInterface
				+ ", mSuperClassType=" + mSuperClassType
				+ ", mClassModifierList=" + mClassModifierList
				+ ", mClassName=" + mClassName + ", mImplementsInterfaceList="
				+ mImplementsInterfaceList + "\n" + "mFieldDeclarationList="
				+ "\n" + mFieldDeclarationList + "\n" + "mMethodInfoList="
				+ "\n" + mMethodInfoList + "\n]";
	}

}
