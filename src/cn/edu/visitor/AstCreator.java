package cn.edu.visitor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class AstCreator {
	/**
	 * 将java文件建立成编译单元，搭建抽象语法树
	 * 
	 * @param javaFilePath
	 * @return
	 */
	public static CompilationUnit getCompilationUnit(String javaFilePath) {
		byte[] input = null;
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					new FileInputStream(javaFilePath));
			input = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(input);
			bufferedInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 实例化解析类
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		// 设置需要建立抽象语法树的源文件信息
		astParser.setSource(new String(input).toCharArray());
		// 设置建立抽象语法树的类型
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		// 建立可编译单元
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

		return result;
	}

	/**
	 * 建立一个空的抽象语法树单元
	 * 
	 * @return
	 */
	public static CompilationUnit getNullCompilationUnit() {
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		astParser.setSource("".toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
		return result;
	}
}
