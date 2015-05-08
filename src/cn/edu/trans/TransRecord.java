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
	 * 记录被替换的源代码列表
	 */
	private List<String> sourceCodeList;
	/*
	 * 记录替换之后的源代码列表
	 */
	private List<String> targetCodeList;

	/**
	 * 构造函数
	 */
	public TransRecord() {
		sourceCodeList = new ArrayList<>();
		targetCodeList = new ArrayList<>();
	}

	/**
	 * 构造函数
	 * 
	 * @param sourceList
	 * @param targetList
	 */
	public TransRecord(List<String> sourceList, List<String> targetList) {
		sourceCodeList = sourceList;
		targetCodeList = targetList;
	}

	/**
	 * 获取转换之前的源代码列表
	 * 
	 * @return
	 */
	public List<String> getSourceCodeList() {
		return sourceCodeList;
	}

	/**
	 * 添加转换之前的源代码信息
	 * 
	 * @param sourceCode
	 */
	public void addSourceCode(String sourceCode) {
		this.sourceCodeList.add(sourceCode);
	}

	/**
	 * 获取转换之后的源代码列表
	 * 
	 * @return
	 */
	public List<String> getTargetCodeList() {
		return targetCodeList;
	}

	/**
	 * 添加转换之后的源代码信息
	 * 
	 * @param targetCode
	 */
	public void addTargetCode(String targetCode) {
		this.targetCodeList.add(targetCode);
	}

}
