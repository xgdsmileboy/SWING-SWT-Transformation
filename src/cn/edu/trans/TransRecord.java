package cn.edu.trans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class TransRecord {
	/*
	 * ��¼���滻��Դ�����б�
	 */
	private List<String> sourceCodeList;
	/*
	 * ��¼�滻֮���Դ�����б�
	 */
	private List<String> targetCodeList;

	/**
	 * ���캯��
	 */
	public TransRecord() {
		sourceCodeList = new ArrayList<>();
		targetCodeList = new ArrayList<>();
	}

	/**
	 * ���캯��
	 * 
	 * @param sourceList
	 * @param targetList
	 */
	public TransRecord(List<String> sourceList, List<String> targetList) {
		sourceCodeList = sourceList;
		targetCodeList = targetList;
	}

	/**
	 * ��ȡת��֮ǰ��Դ�����б�
	 * 
	 * @return
	 */
	public List<String> getSourceCodeList() {
		return sourceCodeList;
	}

	/**
	 * ���ת��֮ǰ��Դ������Ϣ
	 * 
	 * @param sourceCode
	 */
	public void addSourceCode(String sourceCode) {
		this.sourceCodeList.add(sourceCode);
	}

	/**
	 * ��ȡת��֮���Դ�����б�
	 * 
	 * @return
	 */
	public List<String> getTargetCodeList() {
		return targetCodeList;
	}

	/**
	 * ���ת��֮���Դ������Ϣ
	 * 
	 * @param targetCode
	 */
	public void addTargetCode(String targetCode) {
		this.targetCodeList.add(targetCode);
	}

}
