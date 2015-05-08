package cn.edu.rule;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public interface Rule {
	/*
	 * 规则文件路径定义
	 */
	public final static String _RulePath = "./conf/rule.in";
	/*
	 * 类型与包的对应关系文件路径
	 */
	public final static String _PackagePath = "./conf/PKG.in";
	/*
	 * 转换前后类型对应关系
	 */
	public final static String _TypeMatcherPath = "./conf/type.in";

	/**
	 * 规则匹配方法
	 * 
	 * @param expression
	 * @return
	 */
	public Rule MatchRule(String expression);

}
