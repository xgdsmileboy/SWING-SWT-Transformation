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
 *       comment :ʵ���м�㵽Ŀ������ת��
 */
public class Trans {

	private static RuleManager mRuleManager = null;
	private static List<TransRecord> mTransRecordList = new ArrayList<>();
	private static Source source = new Source();
	private static Map<String, RuleEntity> matchedMap = new HashMap<String, RuleEntity>();

	/**
	 * ���ü�¼��Ϣ
	 */
	public static void reset(){
		mTransRecordList = new ArrayList<>();
		source = new Source();
	}
	/**
	 * ������ת�����������õ��Ĺ���
	 * 
	 * @param rManager
	 */
	public static void setRuleManager(RuleManager rManager) {
		mRuleManager = rManager;
	}

	/**
	 * ��ȡ��ת��ʱ��¼�Ĺ���
	 * 
	 * @return
	 */
	public static List<TransRecord> getTransRecord() {
		return mTransRecordList;
	}

	/**
	 * ��ȡת��֮���Դ����
	 * 
	 * @return
	 */
	public static Source getTransSource() {
		return source;
	}

	/**
	 * ʵ�ֶ��м��������ת�������еĲ���classInfo�ǰ����ж�������Ϣ���м������
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
			// ���η����
			for (Modifier modifier : modifierList) {
				text += modifier.toString() + " ";
			}
			// ���������Ƿ�Ϊ�ӿ�
			if (classInfo.isInterface()) {
				text += "interface " + name;

			} else {
				text += "class " + name;
				if (superClass != null) {
					String sup = superClass.toString();
					// ��Ӽ̳и���
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
				// ���ʵ�ֵĽӿ���Ϣ
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
			// �����е�ȫ�ֱ�������ת��
			for (FieldInfo fieldInfo : classInfo.getmFieldDeclarationList()) {
				List<ISTM> list = transField(fieldInfo);
				for (ISTM istm : list) {
					block.addStatement(istm);
				}
			}
			// �����еķ�������ת��
			for (MethodInfo methodInfo : classInfo.getmMethodDeclarationList()) {
				block.addStatement(transMethod(methodInfo));
			}

			source.addStatement(block);
		}
	}

	/**
	 * ʵ�ֶ�ȫ�ֱ�����ת��
	 * 
	 * @param fieldInfo
	 */
	private static List<ISTM> transField(FieldInfo fieldInfo) {
		List<ISTM> istm = new ArrayList<>();
		Type type = fieldInfo.getmVariableType();
		if (type.isPrimitiveType()) { // һ���������͵�ת��
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
		} else if (type.isSimpleType()) { // �Զ����������͵�ת��
			String typeStr = type.toString();
			Clause c = new Clause(mRuleManager.getTypeMatcherMap().get(typeStr)
					+ " " + fieldInfo.getmVariableName() + ";");
			istm.add(c);
			
			//���ת����¼
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

		} else if (type.isArrayType()) { // �������͵�ת��
			String text = fieldInfo.getmVariableType().toString();
			text += " " + fieldInfo.getmVariableName();
			text += " = " + fieldInfo.getmVariableInitializer() + ";";
			Clause c = new Clause(text);
			istm.add(c);
		} else if (type.isParameterizedType()) { // ��������ת��

		} else if (type.isQualifiedType()) { // ����(������)����ת��A.B

		} else if (type.isUnionType()) { // �������͵�ת��

		} else if (type.isWildcardType()) { // ͨ�������ת��

		} else {
			System.out.println("Trans type error!");
		}

		return istm;
	}

	/**
	 * ʵ�ֶ����еĺ�������ת��
	 * 
	 * @param methodInfo
	 * @return
	 */
	private static ISTM transMethod(MethodInfo methodInfo) {
		// �����Ĳ��������б�
		List<Type> typeList = methodInfo.getmParamsTypeList();
		// �����Ĳ��������б�
		List<String> nameList = methodInfo.getmParamsNameList();
		// ���������б�
		String methodName = methodInfo.getmMethodName();
		// �����ķ��������б�
		Type returnType = methodInfo.getmMethodReturnType();
		// �������η��б�
		List<Modifier> modifierList = methodInfo.getmModifierList();
		// ���溯�����������ֵ����
		String declaration = "";
		// ����������η��ؼ���
		for (int i = 0; i < modifierList.size(); i++) {
			if (modifierList.get(i) instanceof Modifier) {
				declaration += modifierList.get(i).toString() + " ";
			}
		}

		// Ϊ������ӷ���������Ϣ
		String rt = "";
		boolean isConstructure = false;
		if (returnType != null) { // �������Ͳ�Ϊ�գ��ǹ��캯��
			rt = returnType.toString();
			if (mRuleManager.getTypeMatcherMap().containsKey(
					returnType.toString())) {
				rt = (String) mRuleManager.getTypeMatcherMap().get(
						returnType.toString());
				addimportToSource(mRuleManager.getImportMatcherMap().get(
						returnType.toString()));
			}
		} else { // ��������Ϊ�գ���Ϊ���캯��
			isConstructure = true;
		}

		// ���ú�������
		declaration += rt + " ";
		declaration += methodName;

		String params = "";
		// ���ú����Ĳ����б�
		Map<String, String> typeMatcherMap = mRuleManager.getTypeMatcherMap();
		for (int i = 0; i < typeList.size(); i++) {
			String type = typeList.get(i).toString();
			if (typeMatcherMap.containsKey(type)) {
				//���ת����¼
				TransRecord transRecord = new TransRecord();
				transRecord.addSourceCode("�������ͣ�"+type);
				type = typeMatcherMap.get(type);
				transRecord.addTargetCode("�������ͣ�"+type);
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
		// ת��Ϊ����
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
	 * �Ժ������ϸ�ڽ���ת��
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
	 * ʵ������ת����������ת��һЩ���ʱ��Ҫ�ڱ������н��м������������ͣ�������Ҫ��������Ϊ�������д���
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
		case Add_Statement: // add�����SWT��ֱ��ɾ������ʶ��ؼ��ĸ�����ʱ�Ѿ�ʹ��
			System.out.println("Delete add invocation : "
					+ statementInfo.getStatement().toString());
			break;
		case Assignment_Statement: // һ�㳣�����͵ĸ�ֵ��䣬����ֱ�ӽ���ת��
			staString = statementInfo.getStatement().toString();
			ISTM clause = new Clause(staString);
			istmStatement.add(clause);
			break;
		case Declaration_Instance_Statement: // �ؼ���������&����һ��
			istmStatement = getDeclarationInstanceStatement(statementInfo,
					methodBodyInfo);
			break;
		case Declaration_Statement: // �ؼ���������
			istmStatement = getDeclarationStatement(statementInfo);
			break;
		case Inovation_Statement: // �����������ת��
			istmStatement = getInvocationStatement(statementInfo,
					methodBodyInfo);
			break;
		case Instance_Statement: // ʵ�������
			istmStatement = getInstanceStatement(statementInfo, methodBodyInfo);
			break;
		case For_Statement: // forѭ�����
			staString = statementInfo.getStatement().toString().trim();
			int index = staString.indexOf("{");
			if (index >= 0) {
				staString = staString.substring(0, index);
			}
			Block b = new Block(staString);
			istmStatement.add(b);
			// for(statementInfo.get)
			break;
		case If_Statement: // if�ж����
			break;
		case While_Statement: // whileѭ�����
			break;
		case Default: // ����һЩ����ʶ����䣬ֱ�ӽ���ת��
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
	 * ʵ�ֶ������Ե�������ת��
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
			switch (type_name[0]) { // һ�㳣����������ֱ�ӽ���ת��
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
			default: // ��һ�㳣����������ת��
			{
				// �ж��Ƿ�Ϊ��Ҫת���Ŀؼ�����
				if (mRuleManager.getTypeMatcherMap().containsKey(type_name[0])) {

					text += mRuleManager.getTypeMatcherMap().get(type_name[0])
							+ " " + type_name[1];
					
					//���ת����¼
					TransRecord transRecord = new TransRecord();
					transRecord.addSourceCode("�ؼ����ͣ�"+type_name[0]+" "+type_name[1]);
					transRecord.addTargetCode(text);
					mTransRecordList.add(transRecord);
					
					addimportToSource((String) mRuleManager
							.getImportMatcherMap().get(type_name[0]));
				} else { // �ǿؼ����ͣ���ת��ʱ���û�������ʾ��Ǳ�ڵĴ���
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
	 * ʵ�ֶԺ�������������ת��
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> getInvocationStatement(
			StatementInfo statementInfo, MethodBodyInfo methodBodyInfo) {
		List<ISTM> istm = new ArrayList<>();

		String statementStr = statementInfo.getStatement().toString();
		// �������в�֣�ʵ�ֵ����ߺͺ����ķ���
		String[] s_temp = statementStr.split("\\.");
		if (s_temp.length < 2) {
			System.err.println(statementStr);
			System.err.println("Invocation statement error!");
		}
		// ���ñ�����
		String s_variableName = s_temp[0].trim();
		// ���õĺ�����
		String s_funName = s_temp[1].substring(0, s_temp[1].indexOf('('));
		// ������ñ�����������Ϣ
		String s_type = null;
		int i = 0;
		List list = methodBodyInfo.getmVariableInfoList();
		// �ں������еı������н��в���
		for (i = 0; i < list.size(); i++) {
			if (((VariableInfo) list.get(i)).getName().equals(s_variableName)) {
				s_type = ((VariableInfo) list.get(i)).getType();
				break;
			}
		}
		// �ں�������û�ж���ʱ
		if (i == list.size()) {
			list = methodBodyInfo.getmParentMethod().getmParamsNameList();
			int j = 0;
			// �ں����Ĳ����б��н��в���
			for (j = 0; j < list.size(); j++) {
				if (list.get(j).equals(s_variableName)) {
					s_type = methodBodyInfo.getmParentMethod()
							.getmParamsTypeList().get(j).toString();
					break;
				}
			}
			// �ں����Ĳ����б���û���ҵ�����
			if (j == list.size()) {
				list = methodBodyInfo.getRoot().getmFieldDeclarationList();
				int k = 0;
				// �����ȫ�ֱ����н��в���
				for (k = 0; k < list.size(); k++) {
					if (((FieldInfo) list.get(k)).getmVariableName().equals(
							s_variableName)) {
						s_type = ((FieldInfo) list.get(k)).getmVariableType()
								.toString();
						break;
					}
				}
				// ��ȫ�ֱ�����û���ҵ�������û���ʾû���ҵ���������
				if (k == list.size()) {
					System.err.println("can not find the variable: "
							+ s_variableName);
					return istm;
				}
			}
		}
		// ��Ӱ�����
		addimportToSource(mRuleManager.getImportMatcherMap().get(
				mRuleManager.getTypeMatcherMap().get(s_type)));

		// ������Ҫת���ı��������ͻ�ȡ����ƥ��Ĺ����б�
		list = mRuleManager.getRuleEntitySet(s_type);
		for (Object obj : list) {
			RuleEntity ruleEntity = (RuleEntity) obj;
			// ��������ͱ����Ǻ���������ط���Ч
			if (ruleEntity.getRuleType() != ERuleType.INVOCATION)
				continue;
			// ��������Ҫ����ƥ�������б�
			List<String> l = ruleEntity.getDeleteExpList();
			int index;
			for (index = 0; index < l.size(); index++) {
				String delStr = l.get(index);
				// �������еĵ��ñ������ͺ��������в��
				String[] r_temp = delStr.split("\\.");
				if (r_temp.length < 2)
					continue;
				String r_tempName = r_temp[0].trim();
				String r_tempFunName = r_temp[1].substring(0,
						r_temp[1].indexOf("("));
				// �Ա��������ͺͺ���������ƥ��
				if (ruleEntity.getVariableType(r_tempName).equals(s_type)
						&& s_funName.equals(r_tempFunName)) {
					// �Թ����еĲ����б������ȡ
					Pattern p = Pattern.compile("\\([^\\)]*\\)");
					Matcher m = p.matcher(r_temp[1]);
					String r_paramStr = "";

					if (m.find()) {
						r_paramStr = m.group().trim();
						r_paramStr = r_paramStr.substring(1,
								r_paramStr.length() - 1);
					}
					// �����б����
					String[] r_paramArray = r_paramStr.split(",");
					String s_paramStr = "";
					// ����Ҫת����Դ�������ó�����в�����ȡ
					m = p.matcher(s_temp[1].trim());
					if (m.find()) {
						s_paramStr = m.group().trim();
						s_paramStr = s_paramStr.substring(1,
								s_paramStr.length() - 1);
					}
					String[] tempParamArray = s_paramStr.split(",");
					// �����б������Ҫ��ͬ
					if (r_paramArray.length != tempParamArray.length) {
						System.err.println(statementStr + "->" + ruleEntity
								+ " Params not match!!");
						return istm;
					}
					// �ô�ֻʵ�ֶ� 1->m ����ת��
					if (1 == l.size()) {
						//���ת����¼
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
						
					} else { // �ô�ʵ�ֶ� m->n ����ת��

					}
				}
			}

		}

		return istm;
	}

	/**
	 * ʵ�ֶ�ʵ����������ת��
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
	 * ʵ�ֶ�����&ʵ����������ת��
	 * 
	 * @param statementInfo
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> getDeclarationInstanceStatement(
			StatementInfo statementInfo, MethodBodyInfo methodBodyInfo) {
		List<ISTM> istm = new ArrayList<>();

		String typeStr = statementInfo.getStatement().toString().trim();
		// �����������ͽ��в��
		String[] temp = typeStr.split("\\s+");
		// �ж��Ƿ�Ϊ��Ҫת���Ŀؼ�����
		if (mRuleManager.getTypeMatcherMap().containsKey(temp[0].trim())) {
			// ��Ҫת��Ϊ��Ŀ������
			String type = mRuleManager.getTypeMatcherMap().get(temp[0].trim());
			// ��������ʵ��������ֳ������䣬�������+ʵ�������
			String declar = type + " " + (temp[1].split("="))[0].trim() + ";";
			Clause c = new Clause(declar);
			istm.add(c);
			
			//���ת����¼��Ϣ
			TransRecord transRecord = new TransRecord();
			transRecord.addSourceCode(temp[0]+" "+(temp[1].split("="))[0]+";");
			transRecord.addTargetCode(c.getStatement());
			mTransRecordList.add(transRecord);
			
			// ����������ȥ����ʣ�ಿ�ְ���ʵ�����������д���
			String instance = typeStr.substring(temp[0].length());
			List<ISTM> list = instanceAnalysis(instance, methodBodyInfo, null);
			for (ISTM item : list) {
				istm.add(item);
			}
		}

		return istm;
	}

	/**
	 * ʵ��ʵ�������ת�����������ʵ�������ת��������&ʵ��������ת�������漰���Ĺ�ͬ���ֵ�������
	 * 
	 * @param instance
	 * @param methodBodyInfo
	 * @return
	 */
	private static List<ISTM> instanceAnalysis(String instance,
			MethodBodyInfo methodBodyInfo, String type) {
		List<ISTM> istm = new ArrayList<>();
		String[] temp = instance.split("=");
		// ��������
		String variableName = temp[0].trim();
		// �����ĸ�����
		String parent = "";
		if (type == null) {
			// �������ڵı����б�
			List list = methodBodyInfo.getmVariableInfoList();
			int i;
			// �ڱ��صĺ������ڵı������н��в��ұ����Ķ���
			for (i = 0; i < list.size(); i++) {
				VariableInfo va = (VariableInfo) list.get(i);
				if (va.getName().equals(variableName)) {
					type = va.getType();
					parent = va.getParent();
					break;
				}
			}
			// �ں������ڵı�������û���ҵ���������
			if (i == list.size()) {
				MethodInfo methodInfo = methodBodyInfo.getmParentMethod();
				List<String> paramList = methodInfo.getmParamsNameList();
				int j;
				// �ں����Ĳ����б��н��в���
				for (j = 0; j < paramList.size(); j++) {
					if (paramList.get(j).equals(variableName)) {
						type = methodInfo.getParameterType(variableName)
								.toString();
					}
				}
				// �ں����Ĳ����б���û���ҵ�
				if (j == paramList.size()) {
					ClassInfo classInfo = methodBodyInfo.getRoot();
					int k;
					// ���ඨ���ȫ�ֱ������н��в���
					for (k = 0; k < classInfo.getmFieldDeclarationList().size(); k++) {
						FieldInfo fieldInfo = classInfo
								.getmFieldDeclarationList().get(k);
						if (fieldInfo.getmVariableName().equals(variableName)) {
							type = fieldInfo.getmVariableType().toString();
							parent = fieldInfo.getmParentContent();
							break;
						}
					}
					// �����ϵ�������û���ҵ������Ķ���
					if (k == classInfo.getmFieldDeclarationList().size()) {
						System.err.println("Can not match variable :"
								+ variableName);
						return istm;
					}
				}
			}
		}
		// ��Ӱ�����
		addimportToSource(mRuleManager.getImportMatcherMap().get(
				mRuleManager.getTypeMatcherMap().get(type)));
		// ��ȡ����ƥ��Ĺ����б�
		List<RuleEntity> rList = mRuleManager.getRuleEntitySet(type);
		boolean flag = false;
		for (RuleEntity ruleEntity : rList) {
			// ������Ҫ��ʵ�����ԵĹ�����ܽ���ƥ��
			if (ruleEntity.getRuleType() == ERuleType.INSTANCE
					&& 3 > ruleEntity.getDeleteExpList().size()) {
				flag = true;
				String r_variableName = "uninitialization";
				String r_titleStr = "uninitialization";
				List<TermEntity> mList = ruleEntity.getTermList();

				// ����������Ϣ���Թ����е���Ӧ���ͱ������ƽ�����ȡ��������֮����滻�����ж�������滻
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

						// ȷ��ʵ��������еĲ���������ͬ
						Pattern p = Pattern.compile("\\([^\\)]*\\)");
						Matcher m = p.matcher(instance);
						String s_temp = "";
						if (m.find()) {
							s_temp = m.group();
						}
						String[] s_paramArray = s_temp.split(",");

						// ȷ�������е�ʵ�������������������в�������ƥ��
						m = p.matcher(delList.get(0));
						String r_temp = "";
						if (m.find()) {
							r_temp = m.group();
						}
						String[] r_paramArray = r_temp.split(",");
						if (s_paramArray.length != r_paramArray.length) {
							continue;
						}

						// ��ȡ���е��ַ�������
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
						
						//���ת����¼��Ϣ
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
					} else { // �������������Ҫƥ�����䣬�ٴ���

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
	 * ������ת�������У�ʵ�ֶ԰�������ʶ�������ڲ�����ȥ�ش�����ͬ�İ�����������
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
			transRecord.addTargetCode("��Ӱ������� import "+imp);
			mTransRecordList.add(transRecord);
			
		}
	}

}
