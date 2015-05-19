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
	 * 函数体的抽象语法树接口
	 */
	private Block mBlock;
	/*
	 * 函数中定义的变量列表
	 */
	private List<VariableInfo> mVariableInfoList;
	/*
	 * 存储函数体对应的函数定义结构，在进行变量搜索时，可以回溯到上一级检索变量定义
	 */
	private MethodInfo mParentMethod;
	/*
	 * 存储函数体内的所有语句
	 */
	private List<StatementInfo> mStatementList;

	/**
	 * 构造函数，只含有函数体内容，没有父容器
	 * 
	 * @param block
	 */
	public MethodBodyInfo(Block block) {
		initializition(block, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param block
	 * @param parentMethod
	 */
	public MethodBodyInfo(Block block, MethodInfo parentMethod) {
		initializition(block, parentMethod);
	}

	/**
	 * 初始化函数
	 * 
	 * @param block
	 * @param parentMethod
	 */
	private void initializition(Block block, MethodInfo parentMethod) {
		// 设置函数体的抽象结点信息
		mBlock = block;
		// 设置父容器信息
		mParentMethod = parentMethod;
		// 初始化函数体内的语句列表
		mStatementList = new ArrayList<>();
		// 初始化函数体内的局部变量列表
		mVariableInfoList = new ArrayList<>();
		// 对函数体中的语句进行拆分
		splitStatements();
	}

	/**
	 * 获取函数体的抽象结点内容
	 * 
	 * @return
	 */
	public Block getmBlock() {
		return mBlock;
	}

	/**
	 * 获取根容器，即类信息
	 * 
	 * @return
	 */
	public ClassInfo getRoot() {
		return mParentMethod.getmParentClass();
	}

	/**
	 * 设置函数体所隶属的函数信息
	 * 
	 * @param parentMethod
	 */
	public void setmParentMethod(MethodInfo parentMethod) {
		mParentMethod = parentMethod;
	}

	/**
	 * 获取函数的定义信息
	 * 
	 * @return
	 */
	public MethodInfo getmParentMethod() {
		return mParentMethod;
	}

	/**
	 * 核心函数，将函数体进行拆分
	 */
	void splitStatements() {
		for (int i = 0; i < mBlock.statements().size(); i++) {
			analysisStatement((Statement) mBlock.statements().get(i));
		}
	}

	/**
	 * 对每一个语句进行分类、分析、存储
	 * 
	 * @param statement
	 */
	void analysisStatement(Statement statement) {

		String statementStr = statement.toString().trim();
		// 实例化语句
		if (Regex.isMatch(Regex.Regex_Instance_Statement, statementStr)) {
			// 将语句和其所对应的类型存入语句列表
			addmStatementItem(new StatementInfo(statement,
					EStatementType.Instance_Statement));
			// 对语句进行分析
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
	 * 赋值语句进行分析
	 * 
	 * @param statement
	 */
	void analysisAssignment(Statement statement) {
		// System.out.println("Assignment-->" + statement);
	}

	/**
	 * 对控件添加性语句进行分析
	 * 
	 * @param statement
	 */
	void analysisAdd(Statement statement) {
		// System.out.println("Add-->" + statement);
		// 默认父容器是自身
		String parent = "this";
		String variable = "";
		String statementStr = statement.toString().trim();
		// 将父容器分离
		String[] split = statementStr.split("\\.");
		// 根据拆分的长度获取变量的名称，此处获取到的变量含有add()语句
		if (split.length < 2) {
			variable = split[0];
		} else {
			parent = split[0];
			variable = split[1];
		}
		// 对上面分离不完全的变量进行抽取
		Pattern p = Pattern.compile("\\([^\\)]*\\)");
		Matcher m = p.matcher(variable);

		if (m.find()) {
			variable = m.group().trim();
		}
		// 变量中的参数分离，比如add(widget, NORTH);只截取其中的widget
		split = variable.split(",");
		variable = split[0];
		variable = (variable.replace('(', ' ').replace(')', ' ')).trim();

		int index = 0;
		// 在本地变量表中进行查找变量的定义
		for (index = 0; index < mVariableInfoList.size(); index++) {
			if (mVariableInfoList.get(index).getName().equals(variable)) {
				mVariableInfoList.get(index).setParent(parent);
				mVariableInfoList.get(index).addRelativeStatement(statement);
				break;
			}
		}
		// 本地变量表中没哟找到变量的定义
		if (index == mVariableInfoList.size()) {
			List<String> paramNameList = mParentMethod.getmParamsNameList();
			int i = 0;
			// 在函数的参数列表中进行查找
			for (; i < paramNameList.size(); i++) {
				if (paramNameList.get(i).equals(variable)) {
					// TODO something 当参数作为添加的对象时，应该找到其传入之前的变量，设置其父容器为当前的父容器
					break;
				}
			}
			// 在参数列表中没有找到
			if (i == paramNameList.size()) {
				ClassInfo classInfo = mParentMethod.getmParentClass();
				List<FieldInfo> fieldList = classInfo
						.getmFieldDeclarationList();
				int j = 0;
				// 在类的全局变量中查找变量定义
				for (; j < fieldList.size(); j++) {
					if (fieldList.get(j).getmVariableName().equals(variable)) {
						fieldList.get(j).setmParentContent(parent);
						fieldList.get(j).addmRelativeStatementItem(statement);
						break;
					}
				}
				// 以上三处都没哟找到
				if (j == fieldList.size()) {
					System.err
							.println("Can't find the variable of " + variable);
				}
			}
		}
	}

	/**
	 * 对函数调用语句进行分析
	 * 
	 * @param statement
	 */
	void analysisInov(Statement statement) {
		// System.out.println("Inovation-->" + statement);
		String statementStr = statement.toString().trim();
		// 将调用的变量和函数名称进行分离
		String[] temp = statementStr.split("\\.");
		if (temp.length > 1) {
			String variableName = temp[0];
			int index;
			// 在本地的变量列表中查找变量的定义
			for (index = 0; index < mVariableInfoList.size(); index++) {
				if (mVariableInfoList.get(index).getName().equals(variableName)) {
					mVariableInfoList.get(index)
							.addRelativeStatement(statement);
					break;
				}
			}
			// 在局部变量列表中没有找到变量的定义
			if (index == mVariableInfoList.size()) {
				List<String> paramNameList = mParentMethod.getmParamsNameList();
				int i = 0;
				// 在函数的参数列表中查找变量的定义
				for (; i < paramNameList.size(); i++) {
					if (paramNameList.get(i).equals(variableName)) {
						// TODO something 当前的语句与参数传入的变量存在关系时，需要找到其原始传入的变量
						break;
					}
				}
				// 在函数的参数列表中没有找到变量的定义
				if (i == paramNameList.size()) {
					ClassInfo classInfo = mParentMethod.getmParentClass();
					List<FieldInfo> fieldList = classInfo
							.getmFieldDeclarationList();
					int j = 0;
					// 自全局变量列表中查找变量的定义
					for (; j < fieldList.size(); j++) {
						if (fieldList.get(j).getmVariableName()
								.equals(variableName)) {
							fieldList.get(j).addmRelativeStatementItem(
									statement);
							break;
						}
					}
					// 以上三处都没有找到变量的定义
					if (j == fieldList.size()) {
						System.err.println("Can't find the variable of "
								+ variableName);
					}
				}
			}
		}

	}

	/**
	 * 对声明性的语句进行分析
	 * 
	 * @param statement
	 */
	void analysisDeclaration(Statement statement) {
		// System.out.println("Declaration-->" + statement);

		String statementStr = statement.toString().trim();
		// 将类型和变量名进行分离
		int index = statementStr.indexOf(" ");
		String type = statementStr.substring(0, index).trim();
		statementStr = statementStr.substring(index).trim();
		String name = statementStr.substring(0, statementStr.indexOf(";"))
				.trim();
		// 添加新的变量到局部变量表中
		VariableInfo variableInfo = new VariableInfo(name, type);
		variableInfo.addRelativeStatement(statement);
		mVariableInfoList.add(variableInfo);

	}

	/**
	 * 对实例化语句进行分析
	 * 
	 * @param statement
	 */
	void analysisInstance(Statement statement) {
		// System.out.println("Instance-->" + statement);
	}

	/**
	 * 对声明和实例化在一起的语句进行分析
	 * 
	 * @param statement
	 */
	void analysisDeclarationInstance(Statement statement) {
		// System.out.println("DeclarationInstance-->" + statement);

		String statementStr = statement.toString().trim();
		// 将声明类型和实例化部分进行拆分
		int index = statementStr.indexOf(" ");
		String type = statementStr.substring(0, index).trim();
		statementStr = statementStr.substring(index).trim();
		String name = statementStr.substring(0, statementStr.indexOf("="));
		// 将变量添加到局部变量列表中
		VariableInfo variableInfo = new VariableInfo(name, type);
		variableInfo.addRelativeStatement(statement);
		mVariableInfoList.add(variableInfo);

		// System.out.println("Add a new variable -> name: " + name + ",type: "
		// + type);
	}

	/**
	 * 对if判断语句进行分析
	 * 
	 * @param statement
	 */
	void analysisIfStatement(Statement statement) {
		System.out.println("IfStatement-->" + statement);
	}

	/**
	 * 对for循环语句进行分析
	 * 
	 * @param statement
	 */
	void analysisForStatement(Statement statement) {
		// statement.accept(new IfStatementVisitor());
		System.out.println("ForStatement-->" + statement);
	}

	/**
	 * 对while循环语句进行分析
	 * 
	 * @param statement
	 */
	void analysisWhileStatement(Statement statement) {
		System.out.println("WhileStatement-->" + statement);
	}

	/**
	 * 添加变量信息
	 * 
	 * @param variable
	 */
	void addmVariableItem(VariableInfo variable) {
		mVariableInfoList.add(variable);
	}

	/**
	 * 添加语句
	 * 
	 * @param statementInfo
	 */
	void addmStatementItem(StatementInfo statementInfo) {
		mStatementList.add(statementInfo);
	}

	/**
	 * 获取函数体内的语句列表
	 * 
	 * @return
	 */
	public List<StatementInfo> getmStatementList() {
		return mStatementList;
	}

	/**
	 * 判断列表中对否含有名为 name 的变量
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
	 * 获取变量名为 name 的变量的类型信息
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
	 * 获取变量列表
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
