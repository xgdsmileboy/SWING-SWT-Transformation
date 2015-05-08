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
	 * ������ľ��й̶���ʽ������б����ɹ���ʵ��
	 * 
	 * @param textList
	 * @return
	 */
	public static RuleEntity build(List textList) {
		if (textList.size() <= 0) {
			return null;
		}

		RuleEntity ruleEnty = new RuleEntity();
		// ���ù������漰���ı�����Ϣ
		ruleEnty.setTermList(getTermList(textList));
		// ���ù�������� instance �� invocation
		ruleEnty.setRuleType(getRuleType(textList));
		// ������Ҫƥ������
		ruleEnty.setDeleteExpList(getDelExpressionList(textList));
		// ������Ҫ�ı�����
		ruleEnty.setAddExpList(getAddExpressionList(textList));

		return ruleEnty;
	}

	/**
	 * ���ļ��ж�����ַ����н�������������� instance �� invocation
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
	 * ���ַ����б�����ȡ���漰���ı�����Ϣ���������������ƺ�ת��ǰ���������Ϣ
	 * 
	 * @param textList
	 * @return
	 */
	private static List<TermEntity> getTermList(List textList) {
		List<TermEntity> list = new ArrayList<>();
		String text = ((String) textList.get(0)).trim();
		// ��ȡ��������Ĳ��֣���������
		Pattern p = Pattern.compile("(\\([^\\)]*\\))");
		Matcher m = p.matcher(text);
		String context = "";
		if (m.find()) {
			context = m.group();
		}
		// ���˵����źͿո���Ϣ
		context = ((String) context.subSequence(1, context.length() - 1))
				.replaceAll("\\s+", "");
		// ������������в��
		String[] rslt = context.split(",");
		// ���漰���ı���һһ����
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
	 * ���ļ��ж�ȡ�����ַ����н�������Ҫƥ�������б�
	 * 
	 * @param textList
	 * @return
	 */
	private static List<String> getDelExpressionList(List textList) {
		List<String> list = new ArrayList<>();

		for (int i = 1; i < textList.size(); i++) {
			String text = ((String) textList.get(i)).trim();
			// ��Ҫƥ�����䶼����"-"��ͷ
			if (text.startsWith("-")) {
				text = text.substring(1).trim();
				list.add(text);
			}
		}
		return list;
	}

	/**
	 * ���ļ��ж�����ַ����б��н�������Ҫ�滻(���)������б���Ϣ
	 * 
	 * @param textList
	 * @return
	 */
	private static List<String> getAddExpressionList(List textList) {
		List<String> list = new ArrayList<>();

		for (int i = 1; i < textList.size(); i++) {
			String text = ((String) textList.get(i)).trim();
			// ��Ҫ��ӵ���䶼����"+"��ͷ
			if (text.startsWith("+")) {
				text = text.substring(1).trim();
				list.add(text);
			}
		}

		return list;
	}
}
