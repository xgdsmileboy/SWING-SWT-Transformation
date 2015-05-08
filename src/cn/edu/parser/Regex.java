package cn.edu.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class Regex {
	/*
	 * ����������������ʽ
	 */
	public final static String Regex_Declaration_Statement = "[a-zA-Z]\\w*\\s+([a-zA-Z_]\\s*,?\\s*)+;";
	/*
	 * ʵ����������������ʽ
	 */
	public final static String Regex_Instance_Statement = "[a-zA-Z]\\w*\\s*=\\s*new\\s+.*;";
	/*
	 * ������ʵ�����Ե������һ���������ʽ
	 */
	public final static String Regex_Declaration_Instance_Statement = "[a-zA-Z]\\w*\\s+[a-zA-Z]\\w*\\s*=\\s*new\\s+.*;";
	/*
	 * ��ֵ������������ʽ
	 */
	public final static String Regex_Assignment_Statement = "(int|short|float|double|String)?\\s*[a-zA-Z_]\\w*\\s*=.*;";
	/*
	 * �ؼ��������������ʽ
	 */
	public final static String Regex_Add_Statement = "([a-zA-Z_]\\w*\\.)*add\\(.*\\)\\s*;";
	/*
	 * �������õ����������ʽ
	 */
	public final static String Regex_Inovation_Statement = "([a-zA-Z_]\\w*\\.)*[a-zA-Z_]\\w*\\(.*\\)\\s*;";
	/*
	 * if�ж�����������ʽ
	 */
	public final static String Regex_If_Statement = "if\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";
	/*
	 * whileѭ��������������ʽ
	 */
	public final static String Regex_While_Statement = "while\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";
	/*
	 * forѭ��������������ʽ
	 */
	public final static String Regex_For_Statement = "for\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";

	/**
	 * �ж�������� text ��������������ʽ regex �Ƿ�ƥ��
	 * 
	 * @param regex
	 * @param text
	 * @return
	 */
	public static boolean isMatch(String regex, String text) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		return m.matches();
	}
}
