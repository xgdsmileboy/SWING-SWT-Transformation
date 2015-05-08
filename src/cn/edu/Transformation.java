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
 *       comment : ϵͳ����࣬����Դ����Ľ�����ת�������
 */
public class Transformation {
	// �洢Դ�����м��
	public static List<ClassInfo> mClassInfoList = new ArrayList<>();
	// �洢ת��֮��Ĵ�����Ҫ�����İ��б�
	public static List<String> mImportList = new ArrayList<>();
	// �û��Զ���������
	public static RuleManager ruleManager;

	public Transformation(String path) {
		// ���ļ��ж�ȡת�����򣬴���ruleManager��
		ruleManager = ReadRule.ReadFile(Rule._RulePath);
		// ����Ҫת����Դ���뽨�������﷨��
		CompilationUnit comp = AstCreator.getCompilationUnit(path);
		// ����Ҫת���Ĵ�����б������洢Ϊ�м��
		FileAstVisitor visitor = new FileAstVisitor();
		comp.accept(visitor);
		// ����ת���Ĺ��򼯣���ɴ���ת��
		Trans.setRuleManager(ruleManager);
		Trans.trans(Transformation.mClassInfoList);
		// ת��֮������ݽṹ���н��������ļ�
		Parser.setTargetFilePath(null);
		Parser.parse(Trans.getTransSource());
		// ����м����Ϣ
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
