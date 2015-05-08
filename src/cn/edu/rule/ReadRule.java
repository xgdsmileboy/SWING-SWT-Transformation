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
		// 转换规则管理类
		RuleManager ruleManager = new RuleManager();
		// 规则从文件中读入进行解析
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
				// "#"代表一条规则的结束
				while (tempString != null && !tempString.startsWith("#")) {
					// "//"开始是注释
					if (!tempString.startsWith("//")
							&& tempString.trim().length() > 0) {
						rule.add(tempString);
					}
					tempString = reader.readLine();
				}
				// 对读入的规则字符串进行编译，构建规则集
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
		// 从文件中读取转换的类型对应关系
		ruleManager
				.setTypeMatcherMap(createTypeMatcherMap(Rule._TypeMatcherPath));
		// 从文件中读入不同的控件类型需要引入的包
		ruleManager.setImportMatcherMap(createPackageMap(Rule._PackagePath));

		// printRule(ruleManager);

		return ruleManager;
	}

	/**
	 * 从文件中读入类型和包对应关系
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
	 * 从文件中读取不同的目标控件类型需要引入的包声明
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
	 * 将规则集中的规则逐条打印输出
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
			System.out.println("刪除語句集合：");
			for (int j = 0; j < dellst.size(); j++) {
				System.out.println(dellst.get(j));
			}
			System.out.println("添加語句集合：");
			for (int j = 0; j < addlst.size(); j++) {
				System.out.println(addlst.get(j));
			}

			line++;
		}
	}

}
