package cn.edu.rule;

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
public class RuleManager implements Rule {
	/*
	 * ����
	 */
	List<RuleEntity> ruleSet;
	/*
	 * ת��ǰ�����Ͷ�Ӧ��ϵ
	 */
	Map<String, String> typeMacherMap;
	/*
	 * ת��֮����Ҫ�����İ���Ϣ
	 */
	Map<String, String> importMatcherMap;

	/**
	 * ���캯��
	 */
	public RuleManager() {
		ruleSet = new ArrayList<>();
		typeMacherMap = new HashMap<>();
		importMatcherMap = new HashMap<>();
	}

	/**
	 * ��ӹ���ʵ��
	 * 
	 * @param entity
	 * @return
	 */
	public boolean addRuleEntity(RuleEntity entity) {
		if (entity == null) {
			return false;
		}
		ruleSet.add(entity);
		return true;
	}

	/**
	 * ������Ҫת����������Ϣ��ȡƥ��Ĺ���
	 * 
	 * @param type
	 * @return
	 */
	public List<RuleEntity> getRuleEntitySet(String type) {
		List<RuleEntity> list = new ArrayList<>();

		for (RuleEntity ruleEntity : ruleSet) {
			List<TermEntity> l = ruleEntity.getTermList();
			for (TermEntity term : l) {
				if (term.sType.equals(type)) {
					list.add(ruleEntity);
					break;
				}
			}
		}

		return list;
	}

	/**
	 * ��ȡ����(ȫ��)
	 * 
	 * @return
	 */
	public List<RuleEntity> getRuleEntitySet() {
		return ruleSet;
	}

	/**
	 * ��ȡ����ת����Ӧ��ϵ
	 * 
	 * @return
	 */
	public Map<String, String> getTypeMatcherMap() {
		return typeMacherMap;
	}

	/**
	 * ��������ת����Ӧ��ϵ
	 * 
	 * @param map
	 */
	public void setTypeMatcherMap(Map<String, String> map) {
		this.typeMacherMap = map;
	}

	/**
	 * ���ÿؼ�����Ķ�Ӧ��ϵ
	 * 
	 * @param map
	 */
	public void setImportMatcherMap(Map<String, String> map) {
		this.importMatcherMap = map;
	}

	/**
	 * ��ȡ�ؼ��뱡�Ķ�Ӧ��ϵ
	 * 
	 * @return
	 */
	public Map<String, String> getImportMatcherMap() {
		return importMatcherMap;
	}

	@Override
	public Rule MatchRule(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

}
