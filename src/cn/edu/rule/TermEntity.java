package cn.edu.rule;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class TermEntity {
	/*
	 * 规则中的变量名称
	 */
	String name;
	/*
	 * 规则中变量的原始类型
	 */
	String sType;
	/*
	 * 规则中变量的目标类型
	 */
	String tType;

	/**
	 * 构造函数
	 * 
	 * @param name
	 * @param sourceType
	 * @param targetType
	 */
	public TermEntity(String name, String sourceType, String targetType) {
		this.name = name;
		this.sType = sourceType;
		this.tType = targetType;
	}

	/**
	 * 获取变量的名称
	 * 
	 * @return
	 */
	public String Name() {
		return name;
	}

	/**
	 * 获取变量转换之前的类型
	 * 
	 * @return
	 */
	public String sType() {
		return sType;
	}

	/**
	 * 获取变量转换之后的类型
	 * 
	 * @return
	 */
	public String tType() {
		return tType;
	}

	@Override
	public String toString() {
		return "TermEntity [name=" + name + ", sType=" + sType + ", tType="
				+ tType + "]";
	}

}
