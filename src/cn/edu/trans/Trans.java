package cn.edu.trans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.osgi.baseadaptor.bundlefile.MRUBundleFileList;

import cn.edu.info.ClassInfo;
import cn.edu.info.FieldInfo;
import cn.edu.info.MethodBodyInfo;
import cn.edu.info.MethodInfo;
import cn.edu.info.StatementInfo;
import cn.edu.info.VariableInfo;
import cn.edu.rule.ERuleType;
import cn.edu.rule.RuleEntity;
import cn.edu.rule.RuleManager;
import cn.edu.rule.TermEntity;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :实现中间层到目标代码的转换
 */
public class Trans {

	private static RuleManager mRuleManager = null;
	private static List<TransRecord> mTransRecordList = new ArrayList<>();
	private static Source source = new Source();
	private static Map<String, RuleEntity> matchedMap = new HashMap<String, RuleEntity>();

	/**
	 * 重置记录信息
	 */
	public static void reset(){
		mTransRecordList = new ArrayList<>();
		source = new Source();
	}
	/**
	 * 设置在转换过程中所用到的规则集
	 * 
	 * @param rManager
	 */
	public static void setRuleManager(RuleManager rManager) {
		mRuleManager = rManager;
	}

	/**
	 * 获取在转换时记录的过程
	 * 
	 * @return
	 */
	public static List<TransRecord> getTransRecord() {
		return mTransRecordList;
	}

	/**
	 * 获取转换之后的源代码
	 * 
	 * @return
	 */
	public static Source getTransSource() {
		return source;
	}

	/**
	 * 实现对中间层的整体的转换，其中的参数classInfo是包含有多有类信息的中间层数据
	 * 
	 * @param classInfoList
	 */
	public static void trans(List<ClassInfo> classInfoList) {
		if (mRuleManager == null)
			return;
		if (classInfoList == null)
			return;

		addimportToSource(mRuleManager.getImportMatcherMap().get("SWT"));
		addimportToSource(mRuleManager.getImportMatcherMap().get("Display"));

		for (ClassInfo classInfo : classInfoList) {

			String name = classInfo.getClassName();
			List<Modifier> modifierList = classInfo.getmClassModiferList();
			List<Type> implementList = classInfo.getmImplementsInterfaceList();
			Type superClass = classInfo.getSuperClassType();
			source.setClassName(name);
			String text = "";
			// 修饰符添加
			for (Modifier modifier : modifierList) {
				text += modifier.toString() + " ";
			}
			// 设置类型是否为接口
			if (classInfo.isInterface()) {
				text += "interface " + name;

			} else {
				text += "class " + name;
				if (superClass != null) {
					String sup = superClass.toString();
					// 添加继承父类
					if (mRuleManager.getTypeMatcherMap().containsKey(sup)) {
						sup = (String) mRuleManager.getTypeMatcherMap()
								.get(sup);
						text += " extends " + sup;
						addimportToSource((String) mRuleManager
								.getImportMatcherMap().get(sup));
					} else {
						text += " extends " + superClass.toString();
					}
				}
				// 添加实现的接口信息
				if (implementList.size() > 0) {
					text += " implements ";
					int i;
					for (i = 0; i < implementList.size();) {
						text += implementList.get(i).toString();
						i++;
						if (i < implementList.size()) {
							text += " , ";
						}
					}
				}
			}

			Block block = new Block(text);
			// 对类中的全局变量进行转换
			for (FieldInfo fieldInfo : classInfo.getmFieldDeclarationList()) {
				List<ISTM> list = transField(fieldInfo);
				for (ISTM istm : list) {
					block.addStatement(istm);
				}
			}
			// 对类中的方法进行转换
			for (MethodInfo methodInfo : classInfo.getmMethodDeclarationList()) {
				block.addStatement(transMethod(methodInfo));
			}

			source.addStatement(block);
		}
	}

	/**
	 * 实现对全局变量的转换
	 * 
	 * @param fieldInfo
	 */
	private static List<ISTM> transField(FieldInfo fieldInfo) {
		List<ISTM> istm = new ArrayList<>();
		Type type = fieldInfo.getmVariableType();
		if (type.isPrimitiveType()) { // 一般数据类型的转换
			String text = "";
			for (String modi : fieldInfo.getmVariableModifierList()) {
				text += modi + " ";
			}
			for (String obj : fieldInfo.getmVariableModifierList()) {
				text += obj + " ";
			}
			text = type.toString() + " " + fieldInfo.getmVariableName();
			if (fieldInfo.getmVariableInitializer() != null) {
				text += "=" + fieldInfo.getmVariableInitializer().toString();
			}
			text += ";";
			Clause c = new Clause(text);
			istm.add(c);
		} else if (type.isSimpleType()) { // 自定义数据类型的转换
			String typeStr = type.toString();
			Clause c = new Clause(mRuleManager.getTypeMatcherMap().get(typeStr)
					+ " " + fieldInfo.getmVariableName() + ";");
			istm.add(c);
			
			//添加转换记录
			TransRecord transRecord = new TransRecord();
			transRecord.addSourceCode(typeStr+" "+fieldInfo.getmVariableName()+";");
			transRecord.addSourceCode(c.getStatement());
			mTransRecordList.add(transRecord);
			
			if (fieldInfo.getmVariableInitializer() != null) {
				String statement = fieldInfo.getmVariableName() + " = "
						+ fieldInfo.getmVariableInitializer() + ";";
				List<ISTM> list = instanceAnalysis(statement, null, fieldInfo
						.getmVariableType().toString());
				for (ISTM item : list) {
					istm.add(item);
				}
			}

		} else if (type.isArrayType()) { // 数组类型的转换
			String text = fieldInfo.getmVariableType().toString();
			text += " " + fieldInfo.getmVariableName();
			text += " = " + fieldInfo.getmVariableInitializer() + ";";
			Clause c = new Clause(text);
			istm.add(c);
		} else if (type.isParameterizedType()) { // 含参类型转换

		} else if (type.isQualifiedType()) { // 二级(或以上)类型转换A.B

		} else if (type.isUnionType()) { // 联合类型的转换

		} else if (type.isWildcardType()) { // 通配符类型转换

		} else {
			System.out.println("Trans type error!");
		}

		return istm;
	}

	/**
	 * 实现对类中的函数进行转换
	 * 
	 * @param methodInfo
	 * @return
	 */
	private static ISTM transMethod(MethodInfo methodInfo) {
		// 函数的参数类型列表
		List<Type> typeList = methodInfo.getmParamsTypeList();
		// 函数的参数名称列表
		List<String> nameList = methodInfo.getmParamsNameList();
		// 函数名称列表
		String methodName = methodInfo.getmMethodName();
		// 函数的返回类型列表
		Type returnType = methodInfo.getmMethodReturnType();
		// 函数修饰符列表
		List<Modifier> modifierList = methodInfo.getmModifierList();
		// 保存函数的声明部分的语句
		String declaration = "";
		// 函数添加修饰符关键字
		for (int i = 0; i < modifierList.size(); i++) {
			if (modifierList.get(i) instanceof Modifier) {
				declaration += modifierList.get(i).toString() + " ";
			}
		}

		// 为函数添加返回类型信息
		String rt = "";
		boolean isConstructure = false;
		if (returnType != null) { // 返回类型不为空，非构造函数
			rt = returnType.toString();
			if (mRuleManager.getTypeMatcherMap().containsKey(
					returnType.toString())) {
				rt = (String) mRuleManager.getTypeMatcherMap().get(
						returnType.toString());
				addimportToSource(mRuleManager.getImportMatcherMap().get(
						returnType.toString()));
			}
		} else { // 返回类型为空，是为构造函数
			isConstructure = true;
		}

		// 设置函数名称
		declaration += rt + " ";
		declaration += methodName;

		String params = "";
		// 设置函数的参数列表
		Map<String, String> typeMatcherMap = mRuleManager.getTypeMatcherMap();
		for (int i = 0; i < typeList.size(); i++) {
			String type = typeList.get(i).toString();
			if (typeMatcherMap.containsKey(type)) {
				//添加转换记录
				TransRecord transRecord = new TransRecord();
				transRecord.addSourceCode("参数类型："+type);
				type = typeMatcherMap.get(type);
				transRecord.addTargetCode("参数类型："+type);
				mTransRecordList.add(transRecord);
				
				addimportToSource(mRuleManager.getImportMatcherMap().get(type));
				
			}
			params += type + " " + nameList.get(i) + ",";
		}

		if (isConstructure) {
			params += "Composite parent, int style,";
		}

		int end = params.length() - 1;
		declaration += "(" + params.substring(0, (end > 0 ? end : 0)) + ")";
		// 转换为语句块
		Block block = new Block(declaration);
		if (isConstructure) {
			System.err
					.println("There is a constructor of:"
							+ declaration
							+ ", maybe need add \"super(parent, style);\" into block manully!");
		}

		block.setStatementList(transMethodBody(methodInfo.getmBodyInfo()));
		return block;

	}

	/**
	 * 对函数体的细节进行转换
	 * 
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> transMethodBody(MethodBodyInfo methodBodyInfo) {
		List<ISTM> statementList = new ArrayList<>();

		for (StatementInfo statementInfo : methodBodyInfo.getmStatementList()) {
			List<ISTM> list = transStatement(statementInfo, methodBodyInfo);
			for (ISTM istm : list) {
				statementList.add(istm);
			}
		}
		return statementList;
	}

	/**
	 * 实现语句的转换，由于在转换一些语句时需要在变量表中进行检索变量的类型，所以需要函数体作为参数进行传递
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> transStatement(StatementInfo statementInfo,
			MethodBodyInfo methodBodyInfo) {
		List<ISTM> istmStatement = new ArrayList<>();
		String staString;
		switch (statementInfo.getStatementType()) {
		case Add_Statement: // add语句在SWT中直接删除，在识别控件的父容器时已经使用
			System.out.println("Delete add invocation : "
					+ statementInfo.getStatement().toString());
			break;
		case Assignment_Statement: // 一般常用类型的赋值语句，可以直接进行转换
			staString = statementInfo.getStatement().toString();
			ISTM clause = new Clause(staString);
			istmStatement.add(clause);
			break;
		case Declaration_Instance_Statement: // 控件类型声明&定义一起
			istmStatement = getDeclarationInstanceStatement(statementInfo,
					methodBodyInfo);
			break;
		case Declaration_Statement: // 控件类型声明
			istmStatement = getDeclarationStatement(statementInfo);
			break;
		case Inovation_Statement: // 函数调用语句转换
			istmStatement = getInvocationStatement(statementInfo,
					methodBodyInfo);
			break;
		case Instance_Statement: // 实例化语句
			istmStatement = getInstanceStatement(statementInfo, methodBodyInfo);
			break;
		case For_Statement: // for循环语句
			staString = statementInfo.getStatement().toString().trim();
			int index = staString.indexOf("{");
			if (index >= 0) {
				staString = staString.substring(0, index);
			}
			Block b = new Block(staString);
			istmStatement.add(b);
			// for(statementInfo.get)
			break;
		case If_Statement: // if判断语句
			break;
		case While_Statement: // while循环语句
			break;
		case Default: // 其他一些不能识别语句，直接进行转换
			staString = statementInfo.getStatement().toString();
			Clause c = new Clause(staString);
			istmStatement.add(c);
			break;
		default:
			break;
		}
		return istmStatement;
	}

	/**
	 * 实现对声明性的语句进行转换
	 * 
	 * @param statementInfo
	 * @return
	 */
	private static List<ISTM> getDeclarationStatement(
			StatementInfo statementInfo) {
		String[] type_name = statementInfo.getStatement().toString().trim()
				.split("\\s+");
		String text = "";
		if (type_name.length < 2) {
			System.out.println("Declaration Statement is error! : "
					+ statementInfo.getStatement().toString());
		} else {
			switch (type_name[0]) { // 一般常用数据类型直接进行转换
			case "int":
			case "float":
			case "char":
			case "short":
			case "double":
			case "Integer":
			case "Float":
			case "Double":
			case "String": {
				text = statementInfo.getStatement().toString();
				break;
			}
			default: // 非一般常用数据类型转换
			{
				// 判断是否为需要转换的控件类型
				if (mRuleManager.getTypeMatcherMap().containsKey(type_name[0])) {

					text += mRuleManager.getTypeMatcherMap().get(type_name[0])
							+ " " + type_name[1];
					
					//添加转换记录
					TransRecord transRecord = new TransRecord();
					transRecord.addSourceCode("控件类型："+type_name[0]+" "+type_name[1]);
					transRecord.addTargetCode(text);
					mTransRecordList.add(transRecord);
					
					addimportToSource((String) mRuleManager
							.getImportMatcherMap().get(type_name[0]));
				} else { // 非控件类型，在转换时对用户进行提示：潜在的错误
					text = statementInfo.getStatement().toString();
					System.err.println("Warnning : There May be type error at "
							+ text);
				}
				break;
			}
			}
		}
		List<ISTM> istm = new ArrayList<>();
		Clause c = new Clause(text);
		istm.add(c);
		return istm;
	}

	/**
	 * 实现对函数调用语句进行转换
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> getInvocationStatement(
			StatementInfo statementInfo, MethodBodyInfo methodBodyInfo) {
		List<ISTM> istm = new ArrayList<>();

		String statementStr = statementInfo.getStatement().toString();
		// 将语句进行拆分，实现调用者和函数的分离
		String[] s_temp = statementStr.split("\\.");
		if (s_temp.length < 2) {
			System.err.println(statementStr);
			System.err.println("Invocation statement error!");
		}
		// 调用变量名
		String s_variableName = s_temp[0].trim();
		// 调用的函数名
		String s_funName = s_temp[1].substring(0, s_temp[1].indexOf('('));
		// 保存调用变量的类型信息
		String s_type = null;
		int i = 0;
		List list = methodBodyInfo.getmVariableInfoList();
		// 在函数体中的变量表中进行查找
		for (i = 0; i < list.size(); i++) {
			if (((VariableInfo) list.get(i)).getName().equals(s_variableName)) {
				s_type = ((VariableInfo) list.get(i)).getType();
				break;
			}
		}
		// 在函数体中没有定义时
		if (i == list.size()) {
			list = methodBodyInfo.getmParentMethod().getmParamsNameList();
			int j = 0;
			// 在函数的参数列表中进行查找
			for (j = 0; j < list.size(); j++) {
				if (list.get(j).equals(s_variableName)) {
					s_type = methodBodyInfo.getmParentMethod()
							.getmParamsTypeList().get(j).toString();
					break;
				}
			}
			// 在函数的参数列表中没有找到变量
			if (j == list.size()) {
				list = methodBodyInfo.getRoot().getmFieldDeclarationList();
				int k = 0;
				// 在类的全局变量中进行查找
				for (k = 0; k < list.size(); k++) {
					if (((FieldInfo) list.get(k)).getmVariableName().equals(
							s_variableName)) {
						s_type = ((FieldInfo) list.get(k)).getmVariableType()
								.toString();
						break;
					}
				}
				// 在全局变量中没有找到，则给用户提示没有找到变量定义
				if (k == list.size()) {
					System.err.println("can not find the variable: "
							+ s_variableName);
					return istm;
				}
			}
		}
		// 添加包声明
		addimportToSource(mRuleManager.getImportMatcherMap().get(
				mRuleManager.getTypeMatcherMap().get(s_type)));

		// 根据需要转换的变量的类型获取可能匹配的规则列表
		list = mRuleManager.getRuleEntitySet(s_type);
		for (Object obj : list) {
			RuleEntity ruleEntity = (RuleEntity) obj;
			// 规则的类型必须是函数调用相关方有效
			if (ruleEntity.getRuleType() != ERuleType.INVOCATION)
				continue;
			// 规则中需要进行匹配的语句列表
			List<String> l = ruleEntity.getDeleteExpList();
			int index;
			for (index = 0; index < l.size(); index++) {
				String delStr = l.get(index);
				// 将规则中的调用变量名和函数名进行拆分
				String[] r_temp = delStr.split("\\.");
				if (r_temp.length < 2)
					continue;
				String r_tempName = r_temp[0].trim();
				String r_tempFunName = r_temp[1].substring(0,
						r_temp[1].indexOf("("));
				// 对变量的类型和函数名进行匹配
				if (ruleEntity.getVariableType(r_tempName).equals(s_type)
						&& s_funName.equals(r_tempFunName)) {
					// 对规则中的参数列表进行提取
					Pattern p = Pattern.compile("\\([^\\)]*\\)");
					Matcher m = p.matcher(r_temp[1]);
					String r_paramStr = "";

					if (m.find()) {
						r_paramStr = m.group().trim();
						r_paramStr = r_paramStr.substring(1,
								r_paramStr.length() - 1);
					}
					// 参数列表分离
					String[] r_paramArray = r_paramStr.split(",");
					String s_paramStr = "";
					// 对需要转换的源函数调用程序进行参数提取
					m = p.matcher(s_temp[1].trim());
					if (m.find()) {
						s_paramStr = m.group().trim();
						s_paramStr = s_paramStr.substring(1,
								s_paramStr.length() - 1);
					}
					String[] tempParamArray = s_paramStr.split(",");
					// 参数列表个数需要相同
					if (r_paramArray.length != tempParamArray.length) {
						System.err.println(statementStr + "->" + ruleEntity
								+ " Params not match!!");
						return istm;
					}
					// 该处只实现对 1->m 进行转换
					if (1 == l.size()) {
						//添加转换记录
						TransRecord transRecord = new TransRecord();
						transRecord.addSourceCode(statementStr);
						for (String addStr : ruleEntity.getAddList()) {
							addStr = addStr.replaceAll(r_tempName,
									s_variableName);
							for (int pindex = 0; pindex < r_paramArray.length; pindex++) {
								addStr = addStr.replaceAll(
										r_paramArray[pindex],
										tempParamArray[pindex]);
							}
							Clause c = new Clause(addStr);
							istm.add(c);
							transRecord.addTargetCode(c.getStatement());
						}
						mTransRecordList.add(transRecord);
						
					} else { // 该处实现对 m->n 进行转换

					}
				}
			}

		}

		return istm;
	}

	/**
	 * 实现对实例化语句进行转换
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> getInstanceStatement(StatementInfo statementInfo,
			MethodBodyInfo methodBodyInfo) {
		return instanceAnalysis(statementInfo.getStatement().toString(),
				methodBodyInfo, null);
	}

	/**
	 * 实现对声明&实例化语句进行转换
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> getDeclarationInstanceStatement(
			StatementInfo statementInfo, MethodBodyInfo methodBodyInfo) {
		List<ISTM> istm = new ArrayList<>();

		String typeStr = statementInfo.getStatement().toString().trim();
		// 将变量的类型进行拆分
		String[] temp = typeStr.split("\\s+");
		// 判断是否为需要转换的控件类型
		if (mRuleManager.getTypeMatcherMap().containsKey(temp[0].trim())) {
			// 需要转换为的目标类型
			String type = mRuleManager.getTypeMatcherMap().get(temp[0].trim());
			// 将声明和实例化语句拆分成了两句，声明语句+实例化语句
			String declar = type + " " + (temp[1].split("="))[0].trim() + ";";
			Clause c = new Clause(declar);
			istm.add(c);
			
			//添加转换记录信息
			TransRecord transRecord = new TransRecord();
			transRecord.addSourceCode(temp[0]+" "+(temp[1].split("="))[0]+";");
			transRecord.addTargetCode(c.getStatement());
			mTransRecordList.add(transRecord);
			
			// 将声明部分去掉，剩余部分按照实例化方法进行处理
			String instance = typeStr.substring(temp[0].length());
			List<ISTM> list = instanceAnalysis(instance, methodBodyInfo, null);
			for (ISTM item : list) {
				istm.add(item);
			}
		}

		return istm;
	}

	/**
	 * 实现实例化语句转换，将上面的实例化语句转换和声明&实例化语句的转换中所涉及到的共同部分单独处理
	 * 
	 * @param instance
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> instanceAnalysis(String instance,
			MethodBodyInfo methodBodyInfo, String type) {
		List<ISTM> istm = new ArrayList<>();
		String[] temp = instance.split("=");
		// 变量名称
		String variableName = temp[0].trim();
		// 变量的父容器
		String parent = "";
		if (type == null) {
			// 函数体内的变量列表
			List list = methodBodyInfo.getmVariableInfoList();
			int i;
			// 在本地的函数体内的变量表中进行查找变量的定义
			for (i = 0; i < list.size(); i++) {
				VariableInfo va = (VariableInfo) list.get(i);
				if (va.getName().equals(variableName)) {
					type = va.getType();
					parent = va.getParent();
					break;
				}
			}
			// 在函数体内的变量表中没有找到变量定义
			if (i == list.size()) {
				MethodInfo methodInfo = methodBodyInfo.getmParentMethod();
				List<String> paramList = methodInfo.getmParamsNameList();
				int j;
				// 在函数的参数列表中进行查找
				for (j = 0; j < paramList.size(); j++) {
					if (paramList.get(j).equals(variableName)) {
						type = methodInfo.getParameterType(variableName)
								.toString();
					}
				}
				// 在函数的参数列表中没有找到
				if (j == paramList.size()) {
					ClassInfo classInfo = methodBodyInfo.getRoot();
					int k;
					// 在类定义的全局变量表中进行查找
					for (k = 0; k < classInfo.getmFieldDeclarationList().size(); k++) {
						FieldInfo fieldInfo = classInfo
								.getmFieldDeclarationList().get(k);
						if (fieldInfo.getmVariableName().equals(variableName)) {
							type = fieldInfo.getmVariableType().toString();
							parent = fieldInfo.getmParentContent();
							break;
						}
					}
					// 在以上的三处都没有找到变量的定义
					if (k == classInfo.getmFieldDeclarationList().size()) {
						System.err.println("Can not match variable :"
								+ variableName);
						return istm;
					}
				}
			}
		}
		// 添加包声明
		addimportToSource(mRuleManager.getImportMatcherMap().get(
				mRuleManager.getTypeMatcherMap().get(type)));
		// 获取可能匹配的规则列表
		List<RuleEntity> rList = mRuleManager.getRuleEntitySet(type);
		boolean flag = false;
		for (RuleEntity ruleEntity : rList) {
			// 规则需要是实例化性的规则才能进行匹配
			if (ruleEntity.getRuleType() == ERuleType.INSTANCE
					&& 3 > ruleEntity.getDeleteExpList().size()) {
				flag = true;
				String r_variableName = "uninitialization";
				String r_titleStr = "uninitialization";
				List<TermEntity> mList = ruleEntity.getTermList();

				// 根据类型信息，对规则中的相应类型变量名称进行提取，方便在之后的替换过程中对其进行替换
				for (TermEntity termEntity : mList) {
					if (termEntity.sType().equals(type)) {
						r_variableName = termEntity.Name();
					}
					if (termEntity.sType().equals("String")) {
						r_titleStr = termEntity.Name();
					}
				}

				List<String> delList = ruleEntity.getDeleteExpList();
				int count;
				for (count = 0; count < delList.size(); count++) {
					String delStr = delList.get(count);
					if (delStr.contains("new " + type)) {

						// 确定实例化语句中的参数个数相同
						Pattern p = Pattern.compile("\\([^\\)]*\\)");
						Matcher m = p.matcher(instance);
						String s_temp = "";
						if (m.find()) {
							s_temp = m.group();
						}
						String[] s_paramArray = s_temp.split(",");

						// 确定规则中的实例化语句参数个数，进行参数个数匹配
						m = p.matcher(delList.get(0));
						String r_temp = "";
						if (m.find()) {
							r_temp = m.group();
						}
						String[] r_paramArray = r_temp.split(",");
						if (s_paramArray.length != r_paramArray.length) {
							continue;
						}

						// 获取其中的字符串参数
						p = Pattern.compile("\"[^\"]*\"");
						m = p.matcher(instance);

						String s_title = "uninitialization";
						if (m.find()) {
							s_title = m.group().trim();
						}

						List<String> l = ruleEntity.getAddList();
						if (parent == null) {
							parent = "";
						}
						
						//添加转换记录信息
						TransRecord transRecord = new TransRecord();
						transRecord.addSourceCode(instance);
						
						for (String addStr : l) {
							addStr = addStr.replaceAll(r_variableName,
									variableName);
							addStr = addStr.replaceAll("parent", parent);
							addStr = addStr.replaceAll(r_titleStr, s_title);
							Clause c = new Clause(addStr);
							istm.add(c);
							transRecord.addTargetCode(c.getStatement());
						}
						mTransRecordList.add(transRecord);
						
						break;
					} else { // 如果还有其他需要匹配的语句，再处理

					}
				}
			}
		}

		if (!flag) {
			System.err.println("No rule match!->" + instance);
		}

		return istm;
	}

	/**
	 * 在语句的转换过程中，实现对包含包的识别和添加内部含有去重处理，相同的包避免包含多次
	 * 
	 * @param imp
	 */
	private static void addimportToSource(String imp) {
		if (imp == null) {
			return;
		}
		if (!source.getImportDec().contains(imp)) {
			source.getImportDec().add(imp);
			TransRecord transRecord = new TransRecord();
			transRecord.addTargetCode("添加包声明： import "+imp);
			mTransRecordList.add(transRecord);
			
		}
	}

}
