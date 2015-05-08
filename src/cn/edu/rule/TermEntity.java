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
	 * �����еı�������
	 */
	String name;
	/*
	 * �����б�����ԭʼ����
	 */
	String sType;
	/*
	 * �����б�����Ŀ������
	 */
	String tType;

	/**
	 * ���캯��
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
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public String Name() {
		return name;
	}

	/**
	 * ��ȡ����ת��֮ǰ������
	 * 
	 * @return
	 */
	public String sType() {
		return sType;
	}

	/**
	 * ��ȡ����ת��֮�������
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
