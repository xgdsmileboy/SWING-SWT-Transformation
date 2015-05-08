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
	 * 规则集
	 */
	List<RuleEntity> ruleSet;
	/*
	 * 转换前后类型对应关系
	 */
	Map<String, String> typeMacherMap;
	/*
	 * 转换之后需要包含的包信息
	 */
	Map<String, String> importMatcherMap;

	/**
	 * 构造函数
	 */
	public RuleManager() {
		ruleSet = new ArrayList<>();
		typeMacherMap = new HashMap<>();
		importMatcherMap = new HashMap<>();
	}

	/**
	 * 添加规则实体
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
	 * 根据需要转换的类型信息获取匹配的规则集
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
	 * 获取规则集(全部)
	 * 
	 * @return
	 */
	public List<RuleEntity> getRuleEntitySet() {
		return ruleSet;
	}

	/**
	 * 获取类型转换对应关系
	 * 
	 * @return
	 */
	public Map<String, String> getTypeMatcherMap() {
		return typeMacherMap;
	}

	/**
	 * 设置类型转换对应关系
	 * 
	 * @param map
	 */
	public void setTypeMatcherMap(Map<String, String> map) {
		this.typeMacherMap = map;
	}

	/**
	 * 设置控件与包的对应关系
	 * 
	 * @param map
	 */
	public void setImportMatcherMap(Map<String, String> map) {
		this.importMatcherMap = map;
	}

	/**
	 * 获取控件与薄的对应关系
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
