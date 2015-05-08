package cn.edu.info;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment : 枚举类型，在对源程序进行解析时，方便以后识别定义了一些主要类型
 */
public enum EStatementType {
	// 声明性语句
	Declaration_Statement,
	// 实例化性语句
	Instance_Statement,
	// 声明和实例化语句在一起
	Declaration_Instance_Statement,
	// 赋值语句
	Assignment_Statement,
	// 控件添加语句
	Add_Statement,
	// 函数调用语句
	Inovation_Statement,
	// 判断语句
	If_Statement,
	// while循环语句
	While_Statement,
	// for循环语句
	For_Statement,
	// 其他
	Default
}
