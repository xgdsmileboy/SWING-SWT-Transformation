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
 *       comment : ����м��ṹ
 */
public class ClassInfo {
	/*
	 * ����ǽӿڽڵ�
	 */
	TypeDeclaration mTypeDeclaration;
	/*
	 * ��ע�Ƿ�Ϊ�ӿ�����
	 */
	boolean mIsInterface;
	/*
	 * ��ĸ���
	 */
	Type mSuperClassType;
	/*
	 * ������η��б�
	 */
	List<Modifier> mClassModifierList;
	/*
	 * ����
	 */
	String mClassName;
	/*
	 * ʵ�ֵĽӿ��б�
	 */
	List<Type> mImplementsInterfaceList;
	/*
	 * ����ȫ�ֱ����б�
	 */
	List<FieldInfo> mFieldDeclarationList;
	/*
	 * ���з����б�
	 */
	List<MethodInfo> mMethodInfoList;

	// /*
	// * ���ж�����ڲ����б�
	// */
	// List<TypeDeclaration> mTypeDeclarationList;

	public ClassInfo(TypeDeclaration typeDeclaration) {
		// ��ԭ���ӿڽṹ���д洢��Ŀǰ�ڽ���������û��ʹ��
		mTypeDeclaration = typeDeclaration;
		// �������ӿڵ����η� �磺public��private�ȵ�
		setClassModifiers(typeDeclaration.modifiers());
		// �������ӿڵ�����
		setClassName(typeDeclaration.getName().toString());
		// ���ø���
		setSuperClassType(typeDeclaration.getSuperclassType());
		// �����Ƿ�Ϊ�ӿ�
		setIsInterface(typeDeclaration.isInterface());
		// ����ʵ�ֵĽӿ���Ϣ
		setmImplementsInterfaceList(typeDeclaration.superInterfaceTypes());
		// �������ӿ����������ȫ�ֱ���
		mFieldDeclarationList = new ArrayList<>();
		// �������ӿ��ж���ķ�����Ϣ
		mMethodInfoList = new ArrayList<>();
	}

	/**
	 * ��ȡ���ӿڵĳ����﷨���ṹ
	 * 
	 * @return
	 */
	public TypeDeclaration getmTypeDeclaration() {
		return mTypeDeclaration;
	}

	/**
	 * ��������
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		mClassName = className;
	}

	/**
	 * ����������η���Ϣ
	 * 
	 * @param classModifierList
	 */
	public void setClassModifiers(List classModifierList) {
		mClassModifierList = classModifierList;
	}

	/**
	 * ���úͻ�ȡ�Ƿ�Ϊ�ӿ���Ϣ
	 * 
	 * @param isInterface
	 */
	public void setIsInterface(boolean isInterface) {
		mIsInterface = isInterface;
	}

	/**
	 * ��ȡ�Ƿ�Ϊ�ӿ�����
	 * 
	 * @return
	 */
	public boolean isInterface() {
		return mIsInterface;
	}

	/**
	 * ��ĸ�������úͻ�ȡ
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
	 * ��ȡ����
	 * 
	 * @return
	 */
	public String getClassName() {
		return mClassName;
	}

	/**
	 * ��ȡ������η���Ϣ
	 * 
	 * @return
	 */
	public List<Modifier> getmClassModiferList() {
		return mClassModifierList;
	}

	/**
	 * ���úͻ�ȡʵ�ֽӿ��б�
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
	 * ��ȡ���ж����ȫ�ֱ����б�
	 * 
	 * @return
	 */
	public List<FieldInfo> getmFieldDeclarationList() {
		return mFieldDeclarationList;
	}

	/**
	 * ��ȡ����������ķ����б�
	 * 
	 * @return
	 */
	public List<MethodInfo> getmMethodDeclarationList() {
		return mMethodInfoList;
	}

	// /**
	// * ��ȡ���е��ڲ����б���Ϣ
	// * @return
	// */
	// public List<TypeDeclaration> getmTypeDeclarationList() {
	// return mTypeDeclarationList;
	// }
	/**
	 * ���ȫ�ֱ�����Ϣ
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
	 * ��ӷ�����Ϣ
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
