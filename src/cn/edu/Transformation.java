package cn.edu;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.edu.info.ClassInfo;
import cn.edu.parser.Parser;
import cn.edu.rule.ReadRule;
import cn.edu.rule.Rule;
import cn.edu.rule.RuleManager;
import cn.edu.trans.Trans;
import cn.edu.visitor.AstCreator;
import cn.edu.visitor.FileAstVisitor;

/**
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment : 系统入口类，控制源代码的解析、转换和输出
 */
public class Transformation {
	// 存储源代码中间层
	public static List<ClassInfo> mClassInfoList = new ArrayList<>();
	// 存储转换之后的代码需要包含的包列表
	public static List<String> mImportList = new ArrayList<>();
	// 用户自定义规则管理
	public static RuleManager ruleManager;

	public Transformation(String path) {
		// 从文件中读取转换规则，存入ruleManager中
		ruleManager = ReadRule.ReadFile(Rule._RulePath);
		// 对需要转换的源代码建立抽象语法树
		CompilationUnit comp = AstCreator.getCompilationUnit(path);
		// 对需要转换的代码进行遍历，存储为中间层
		FileAstVisitor visitor = new FileAstVisitor();
		comp.accept(visitor);
		// 设置转换的规则集，完成代码转换
		Trans.setRuleManager(ruleManager);
		Trans.trans(Transformation.mClassInfoList);
		// 转换之后的数据结构进行解析存入文件
		Parser.setTargetFilePath(null);
		Parser.parse(Trans.getTransSource());
		// 输出中间层信息
		// printClassInfo();
	}

	private void printClassInfo() {
		for (int i = 0; i < Transformation.mClassInfoList.size(); i++) {
			ClassInfo ci = Transformation.mClassInfoList.get(i);

			System.out.println(ci.toString());
			System.out.println();
		}
	}

	public static void main(String[] args) {
		new Transformation("./src/cn/edu/test/Buttons.java");
	}

}
