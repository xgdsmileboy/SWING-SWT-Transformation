package cn.edu.info;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.parser.Regex;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class MethodBodyInfo {
	/*
	 * ������ĳ����﷨���ӿ�
	 */
	private Block mBlock;
	/*
	 * �����ж���ı����б�
	 */
	private List<VariableInfo> mVariableInfoList;
	/*
	 * �洢�������Ӧ�ĺ�������ṹ���ڽ��б�������ʱ�����Ի��ݵ���һ��������������
	 */
	private MethodInfo mParentMethod;
	/*
	 * �洢�������ڵ��������
	 */
	private List<StatementInfo> mStatementList;

	/**
	 * ���캯����ֻ���к��������ݣ�û�и�����
	 * 
	 * @param block
	 */
	public MethodBodyInfo(Block block) {
		initializition(block, null);
	}

	/**
	 * ���캯��
	 * 
	 * @param block
	 * @param parentMethod
	 */
	public MethodBodyInfo(Block block, MethodInfo parentMethod) {
		initializition(block, parentMethod);
	}

	/**
	 * ��ʼ������
	 * 
	 * @param block
	 * @param parentMethod
	 */
	private void initializition(Block block, MethodInfo parentMethod) {
		// ���ú�����ĳ�������Ϣ
		mBlock = block;
		// ���ø�������Ϣ
		mParentMethod = parentMethod;
		// ��ʼ���������ڵ�����б�
		mStatementList = new ArrayList<>();
		// ��ʼ���������ڵľֲ������б�
		mVariableInfoList = new ArrayList<>();
		// �Ժ������е������в��
		splitStatements();
	}

	/**
	 * ��ȡ������ĳ���������
	 * 
	 * @return
	 */
	public Block getmBlock() {
		return mBlock;
	}

	/**
	 * ��ȡ��������������Ϣ
	 * 
	 * @return
	 */
	public ClassInfo getRoot() {
		return mParentMethod.getmParentClass();
	}

	/**
	 * ���ú������������ĺ�����Ϣ
	 * 
	 * @param parentMethod
	 */
	public void setmParentMethod(MethodInfo parentMethod) {
		mParentMethod = parentMethod;
	}

	/**
	 * ��ȡ�����Ķ�����Ϣ
	 * 
	 * @return
	 */
	public MethodInfo getmParentMethod() {
		return mParentMethod;
	}

	/**
	 * ���ĺ���������������в��
	 */
	void splitStatements() {
		for (int i = 0; i < mBlock.statements().size(); i++) {
			analysisStatement((Statement) mBlock.statements().get(i));
		}
	}

	/**
	 * ��ÿһ�������з��ࡢ�������洢
	 * 
	 * @param statement
	 */
	void analysisStatement(Statement statement) {

		String statementStr = statement.toString().trim();
		// ʵ�������
		if (Regex.isMatch(Regex.Regex_Instance_Statement, statementStr)) {
			// ������������Ӧ�����ʹ�������б�
			addmStatementItem(new StatementInfo(statement,
					EStatementType.Instance_Statement));
			// �������з���
			analysisInstance(statement);

		} else if (Regex
				.isMatch(Regex.Regex_Assignment_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.Assignment_Statement));
			analysisAssignment(statement);

		} else if (Regex.isMatch(Regex.Regex_Add_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.Add_Statement));
			analysisAdd(statement);

		} else if (Regex.isMatch(Regex.Regex_New_Statement, statementStr)) {
			
			addmStatementItem(new StatementInfo(statement,
					EStatementType.New_Statement));
			analysisNewStatement(statement);
			
		} else if (Regex.isMatch(Regex.Regex_Declaration_Statement,
				statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.Declaration_Statement));
			analysisDeclaration(statement);

		} else if (Regex.isMatch(Regex.Regex_Inovation_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.Inovation_Statement));
			analysisInov(statement);

		} else if (Regex.isMatch(Regex.Regex_Declaration_Instance_Statement,
				statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.Declaration_Instance_Statement));
			analysisDeclarationInstance(statement);

		} else if (Regex.isMatch(Regex.Regex_If_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.If_Statement));
			analysisIfStatement(statement);

		} else if (Regex.isMatch(Regex.Regex_For_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.For_Statement));
			analysisForStatement(statement);

		} else if (Regex.isMatch(Regex.Regex_While_Statement, statementStr)) {

			addmStatementItem(new StatementInfo(statement,
					EStatementType.While_Statement));
			analysisWhileStatement(statement);

		} else {
			addmStatementItem(new StatementInfo(statement,
					EStatementType.Default));
			System.err.println("no match-->" + statement.toString());
		}

	}
	
	void analysisNewStatement(Statement statement){
		
	}

	/**
	 * ��ֵ�����з���
	 * 
	 * @param statement
	 */
	void analysisAssignment(Statement statement) {
		// System.out.println("Assignment-->" + statement);
	}

	/**
	 * �Կؼ�����������з���
	 * 
	 * @param statement
	 */
	void analysisAdd(Statement statement) {
		// System.out.println("Add-->" + statement);
		// Ĭ�ϸ�����������
		String parent = "this";
		String variable = "";
		String statementStr = statement.toString().trim();
		// ������������
		String[] split = statementStr.split("\\.");
		// ���ݲ�ֵĳ��Ȼ�ȡ���������ƣ��˴���ȡ���ı�������add()���
		if (split.length < 2) {
			variable = split[0];
		} else {
			parent = split[0];
			variable = split[1];
		}
		// ��������벻��ȫ�ı������г�ȡ
		Pattern p = Pattern.compile("\\([^\\)]*\\)");
		Matcher m = p.matcher(variable);

		if (m.find()) {
			variable = m.group().trim();
		}
		// �����еĲ������룬����add(widget, NORTH);ֻ��ȡ���е�widget
		split = variable.split(",");
		variable = split[0];
		variable = (variable.replace('(', ' ').replace(')', ' ')).trim();

		int index = 0;
		// �ڱ��ر������н��в��ұ����Ķ���
		for (index = 0; index < mVariableInfoList.size(); index++) {
			if (mVariableInfoList.get(index).getName().equals(variable)) {
				mVariableInfoList.get(index).setParent(parent);
				mVariableInfoList.get(index).addRelativeStatement(statement);
				break;
			}
		}
		// ���ر�������ûӴ�ҵ������Ķ���
		if (index == mVariableInfoList.size()) {
			List<String> paramNameList = mParentMethod.getmParamsNameList();
			int i = 0;
			// �ں����Ĳ����б��н��в���
			for (; i < paramNameList.size(); i++) {
				if (paramNameList.get(i).equals(variable)) {
					// TODO something ��������Ϊ��ӵĶ���ʱ��Ӧ���ҵ��䴫��֮ǰ�ı����������丸����Ϊ��ǰ�ĸ�����
					break;
				}
			}
			// �ڲ����б���û���ҵ�
			if (i == paramNameList.size()) {
				ClassInfo classInfo = mParentMethod.getmParentClass();
				List<FieldInfo> fieldList = classInfo
						.getmFieldDeclarationList();
				int j = 0;
				// �����ȫ�ֱ����в��ұ�������
				for (; j < fieldList.size(); j++) {
					if (fieldList.get(j).getmVariableName().equals(variable)) {
						fieldList.get(j).setmParentContent(parent);
						fieldList.get(j).addmRelativeStatementItem(statement);
						break;
					}
				}
				// ����������ûӴ�ҵ�
				if (j == fieldList.size()) {
					System.err
							.println("Can't find the variable of " + variable);
				}
			}
		}
	}

	/**
	 * �Ժ������������з���
	 * 
	 * @param statement
	 */
	void analysisInov(Statement statement) {
		// System.out.println("Inovation-->" + statement);
		String statementStr = statement.toString().trim();
		// �����õı����ͺ������ƽ��з���
		String[] temp = statementStr.split("\\.");
		if (temp.length > 1) {
			String variableName = temp[0];
			int index;
			// �ڱ��صı����б��в��ұ����Ķ���
			for (index = 0; index < mVariableInfoList.size(); index++) {
				if (mVariableInfoList.get(index).getName().equals(variableName)) {
					mVariableInfoList.get(index)
							.addRelativeStatement(statement);
					break;
				}
			}
			// �ھֲ������б���û���ҵ������Ķ���
			if (index == mVariableInfoList.size()) {
				List<String> paramNameList = mParentMethod.getmParamsNameList();
				int i = 0;
				// �ں����Ĳ����б��в��ұ����Ķ���
				for (; i < paramNameList.size(); i++) {
					if (paramNameList.get(i).equals(variableName)) {
						// TODO something ��ǰ��������������ı������ڹ�ϵʱ����Ҫ�ҵ���ԭʼ����ı���
						break;
					}
				}
				// �ں����Ĳ����б���û���ҵ������Ķ���
				if (i == paramNameList.size()) {
					ClassInfo classInfo = mParentMethod.getmParentClass();
					List<FieldInfo> fieldList = classInfo
							.getmFieldDeclarationList();
					int j = 0;
					// ��ȫ�ֱ����б��в��ұ����Ķ���
					for (; j < fieldList.size(); j++) {
						if (fieldList.get(j).getmVariableName()
								.equals(variableName)) {
							fieldList.get(j).addmRelativeStatementItem(
									statement);
							break;
						}
					}
					// ����������û���ҵ������Ķ���
					if (j == fieldList.size()) {
						System.err.println("Can't find the variable of "
								+ variableName);
					}
				}
			}
		}

	}

	/**
	 * �������Ե������з���
	 * 
	 * @param statement
	 */
	void analysisDeclaration(Statement statement) {
		// System.out.println("Declaration-->" + statement);

		String statementStr = statement.toString().trim();
		// �����ͺͱ��������з���
		int index = statementStr.indexOf(" ");
		String type = statementStr.substring(0, index).trim();
		statementStr = statementStr.substring(index).trim();
		String name = statementStr.substring(0, statementStr.indexOf(";"))
				.trim();
		// ����µı������ֲ���������
		VariableInfo variableInfo = new VariableInfo(name, type);
		variableInfo.addRelativeStatement(statement);
		mVariableInfoList.add(variableInfo);

	}

	/**
	 * ��ʵ���������з���
	 * 
	 * @param statement
	 */
	void analysisInstance(Statement statement) {
		// System.out.println("Instance-->" + statement);
	}

	/**
	 * ��������ʵ������һ��������з���
	 * 
	 * @param statement
	 */
	void analysisDeclarationInstance(Statement statement) {
		// System.out.println("DeclarationInstance-->" + statement);

		String statementStr = statement.toString().trim();
		// ���������ͺ�ʵ�������ֽ��в��
		int index = statementStr.indexOf(" ");
		String type = statementStr.substring(0, index).trim();
		statementStr = statementStr.substring(index).trim();
		String name = statementStr.substring(0, statementStr.indexOf("="));
		// ��������ӵ��ֲ������б���
		VariableInfo variableInfo = new VariableInfo(name, type);
		variableInfo.addRelativeStatement(statement);
		mVariableInfoList.add(variableInfo);

		// System.out.println("Add a new variable -> name: " + name + ",type: "
		// + type);
	}

	/**
	 * ��if�ж������з���
	 * 
	 * @param statement
	 */
	void analysisIfStatement(Statement statement) {
		System.out.println("IfStatement-->" + statement);
	}

	/**
	 * ��forѭ�������з���
	 * 
	 * @param statement
	 */
	void analysisForStatement(Statement statement) {
		// statement.accept(new IfStatementVisitor());
		System.out.println("ForStatement-->" + statement);
	}

	/**
	 * ��whileѭ�������з���
	 * 
	 * @param statement
	 */
	void analysisWhileStatement(Statement statement) {
		System.out.println("WhileStatement-->" + statement);
	}

	/**
	 * ��ӱ�����Ϣ
	 * 
	 * @param variable
	 */
	void addmVariableItem(VariableInfo variable) {
		mVariableInfoList.add(variable);
	}

	/**
	 * ������
	 * 
	 * @param statementInfo
	 */
	void addmStatementItem(StatementInfo statementInfo) {
		mStatementList.add(statementInfo);
	}

	/**
	 * ��ȡ�������ڵ�����б�
	 * 
	 * @return
	 */
	public List<StatementInfo> getmStatementList() {
		return mStatementList;
	}

	/**
	 * �ж��б��жԷ�����Ϊ name �ı���
	 * 
	 * @param name
	 * @return
	 */
	public int containVariable(String name) {
		for (int i = 0; i < mVariableInfoList.size(); i++) {
			if (mVariableInfoList.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ��ȡ������Ϊ name �ı�����������Ϣ
	 * 
	 * @param name
	 * @return
	 */
	public String getVariableType(String name) {
		int index = containVariable(name);
		if (index != -1) {
			return mVariableInfoList.get(index).getType();
		}
		return null;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 */
	public List<VariableInfo> getmVariableInfoList() {
		return mVariableInfoList;
	}

	@Override
	public String toString() {
		return "MethodBodyInfo [mVariableInfoList=" + mVariableInfoList
				+ ", mParentMethod=" + mParentMethod.getmMethodName()
				+ ", mStatementList=" + mStatementList + "]";
	}

}
