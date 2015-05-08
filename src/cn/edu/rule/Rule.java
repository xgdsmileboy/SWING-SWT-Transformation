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
	 * �����ļ�·������
	 */
	public final static String _RulePath = "./conf/rule.in";
	/*
	 * ��������Ķ�Ӧ��ϵ�ļ�·��
	 */
	public final static String _PackagePath = "./conf/PKG.in";
	/*
	 * ת��ǰ�����Ͷ�Ӧ��ϵ
	 */
	public final static String _TypeMatcherPath = "./conf/type.in";

	/**
	 * ����ƥ�䷽��
	 * 
	 * @param expression
	 * @return
	 */
	public Rule MatchRule(String expression);

}
