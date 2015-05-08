package cn.edu.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.trans.ETYPE;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class BuildRule {
	/**
	 * 将传入的具有固定格式的语句列表编译成规则实体
	 * 
	 * @param textList
	 * @return
	 */
	public static RuleEntity build(List textList) {
		if (textList.size() <= 0) {
			return null;
		}

		RuleEntity ruleEnty = new RuleEntity();
		// 设置规则中涉及到的变量信息
		ruleEnty.setTermList(getTermList(textList));
		// 设置规则的类型 instance 或 invocation
		ruleEnty.setRuleType(getRuleType(textList));
		// 设置需要匹配的语句
		ruleEnty.setDeleteExpList(getDelExpressionList(textList));
		// 设置需要改变的语句
		ruleEnty.setAddExpList(getAddExpressionList(textList));

		return ruleEnty;
	}

	/**
	 * 从文件中读入的字符串中解析出规则的类型 instance 或 invocation
	 * 
	 * @param textList
	 * @return
	 */
	private static ERuleType getRuleType(List textList) {
		String[] text = ((String) textList.get(0)).trim().split("\\$");
		ERuleType type = ERuleType.UNKNOWN;
		if (text.length < 2) {
			System.err.println("Rule type error!");
			return ERuleType.UNKNOWN;
		}
		if ("instance".equals(text[1])) {
			type = ERuleType.INSTANCE;
		} else if ("invocation".equals(text[1])) {
			type = ERuleType.INVOCATION;
		} else {
			System.err
					.println("The rule contain other keywords rather than \"instance\" and \"invocation\"!");
		}
		return type;
	}

	/**
	 * 从字符串列表中提取出涉及到的变量信息，包含变量的名称和转换前后的类型信息
	 * 
	 * @param textList
	 * @return
	 */
	private static List<TermEntity> getTermList(List textList) {
		List<TermEntity> list = new ArrayList<>();
		String text = ((String) textList.get(0)).trim();
		// 提取变量定义的部分，在括号中
		Pattern p = Pattern.compile("(\\([^\\)]*\\))");
		Matcher m = p.matcher(text);
		String context = "";
		if (m.find()) {
			context = m.group();
		}
		// 过滤到括号和空格信息
		context = ((String) context.subSequence(1, context.length() - 1))
				.replaceAll("\\s+", "");
		// 将多个变量进行拆分
		String[] rslt = context.split(",");
		// 对涉及到的变量一一处理
		for (String item : rslt) {
			String[] name_change = item.split(":");
			String[] change = name_change[1].split("->");
			TermEntity termEntity = new TermEntity(name_change[0], change[0],
					change[1]);
			list.add(termEntity);
		}
		return list;
	}

	/**
	 * 从文件中读取到的字符串中解析出需要匹配的语句列表
	 * 
	 * @param textList
	 * @return
	 */
	private static List<String> getDelExpressionList(List textList) {
		List<String> list = new ArrayList<>();

		for (int i = 1; i < textList.size(); i++) {
			String text = ((String) textList.get(i)).trim();
			// 需要匹配的语句都是以"-"开头
			if (text.startsWith("-")) {
				text = text.substring(1).trim();
				list.add(text);
			}
		}
		return list;
	}

	/**
	 * 从文件中读入的字符串列表中解析出需要替换(添加)的语句列表信息
	 * 
	 * @param textList
	 * @return
	 */
	private static List<String> getAddExpressionList(List textList) {
		List<String> list = new ArrayList<>();

		for (int i = 1; i < textList.size(); i++) {
			String text = ((String) textList.get(i)).trim();
			// 需要添加的语句都是以"+"开头
			if (text.startsWith("+")) {
				text = text.substring(1).trim();
				list.add(text);
			}
		}

		return list;
	}
}
