package cn.edu.rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class ReadRule {
	public static RuleManager ReadFile(String rulePath) {
		// ת�����������
		RuleManager ruleManager = new RuleManager();
		// ������ļ��ж�����н���
		File file = new File(rulePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			while (tempString != null) {
				List<String> rule = new ArrayList<>();
				tempString = reader.readLine();
				if (tempString == null) {
					continue;
				}
				tempString = tempString.trim();
				// "#"����һ������Ľ���
				while (tempString != null && !tempString.startsWith("#")) {
					// "//"��ʼ��ע��
					if (!tempString.startsWith("//")
							&& tempString.trim().length() > 0) {
						rule.add(tempString);
					}
					tempString = reader.readLine();
				}
				// �Զ���Ĺ����ַ������б��룬��������
				ruleManager.addRuleEntity(BuildRule.build(rule));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		// ���ļ��ж�ȡת�������Ͷ�Ӧ��ϵ
		ruleManager
				.setTypeMatcherMap(createTypeMatcherMap(Rule._TypeMatcherPath));
		// ���ļ��ж��벻ͬ�Ŀؼ�������Ҫ����İ�
		ruleManager.setImportMatcherMap(createPackageMap(Rule._PackagePath));

		// printRule(ruleManager);

		return ruleManager;
	}

	/**
	 * ���ļ��ж������ͺͰ���Ӧ��ϵ
	 * 
	 * @param path
	 * @return
	 */
	private static Map<String, String> createPackageMap(String path) {
		Map<String, String> packageMap = new HashMap<>();

		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String text = null;
		try {
			while ((text = br.readLine()) != null) {
				if (text.length() < 1)
					continue;

				String className = br.readLine();
				className = br.readLine();
				while (!className.equals("}")) {
					if (!packageMap.containsKey(className.trim())) {
						String pak = text + className + ";";
						packageMap.put(className.trim(), pak);
					}
					className = br.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return packageMap;
	}

	/**
	 * ���ļ��ж�ȡ��ͬ��Ŀ��ؼ�������Ҫ����İ�����
	 * 
	 * @param path
	 * @return
	 */
	private static Map<String, String> createTypeMatcherMap(String path) {
		Map<String, String> matcherMap = new HashMap<>();

		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String text = null;
		try {
			while ((text = br.readLine()) != null) {
				String[] s_t = text.split("->");
				if (!matcherMap.containsKey(s_t[0])) {
					matcherMap.put(s_t[0], s_t[1]);
				} else {
					System.err
							.println("The Macher Map hava an repeated key of "
									+ s_t[0]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return matcherMap;
	}

	/**
	 * �������еĹ���������ӡ���
	 * 
	 * @param uRule
	 */
	private static void printRule(RuleManager uRule) {
		List list = uRule.ruleSet;
		int line = 1;
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\nRule " + line);

			RuleEntity rule = (RuleEntity) list.get(i);

			List termlst = rule.getTermList();
			List dellst = rule.getDeleteExpList();
			List addlst = rule.getAddList();

			for (int j = 0; j < termlst.size(); j++) {
				TermEntity term = (TermEntity) termlst.get(j);
				System.out.println("name:" + term.name + "  src:"
						+ term.sType() + "  tar:" + term.tType());
			}
			System.out.println("�h���Z�伯�ϣ�");
			for (int j = 0; j < dellst.size(); j++) {
				System.out.println(dellst.get(j));
			}
			System.out.println("����Z�伯�ϣ�");
			for (int j = 0; j < addlst.size(); j++) {
				System.out.println(addlst.get(j));
			}

			line++;
		}
	}

}
