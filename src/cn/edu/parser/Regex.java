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
	 * 声明性语句的正则表达式
	 */
	public final static String Regex_Declaration_Statement = "[a-zA-Z]\\w*\\s+([a-zA-Z_]\\s*,?\\s*)+;";
	/*
	 * 实例化性语句的正则表达式
	 */
	public final static String Regex_Instance_Statement = "[a-zA-Z]\\w*\\s*=\\s*new\\s+.*;";
	/*
	 * 声明和实例化性的语句在一起的正则表达式
	 */
	public final static String Regex_Declaration_Instance_Statement = "[a-zA-Z]\\w*\\s+[a-zA-Z]\\w*\\s*=\\s*new\\s+.*;";
	/*
	 * 赋值性语句的正则表达式
	 */
	public final static String Regex_Assignment_Statement = "(int|short|float|double|String)?\\s*[a-zA-Z_]\\w*\\s*=.*;";
	/*
	 * 控件添加语句的正则表达式
	 */
	public final static String Regex_Add_Statement = "([a-zA-Z_]\\w*\\.)*add\\(.*\\)\\s*;";
	/*
	 * 函数调用的语句正则表达式
	 */
	public final static String Regex_Inovation_Statement = "([a-zA-Z_]\\w*\\.)*[a-zA-Z_]\\w*\\(.*\\)\\s*;";
	/*
	 * if判断语句的正则表达式
	 */
	public final static String Regex_If_Statement = "if\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";
	/*
	 * while循环性语句的正则表达式
	 */
	public final static String Regex_While_Statement = "while\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";
	/*
	 * for循环性语句的正则表达式
	 */
	public final static String Regex_For_Statement = "for\\s*\\(.*\\)\\s*\\{(.*\\s*)*\\}";

	/**
	 * 判断所给语句 text 与所给的正则表达式 regex 是否匹配
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
