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
	 * ��java�ļ������ɱ��뵥Ԫ��������﷨��
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
		// ʵ����������
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		// ������Ҫ���������﷨����Դ�ļ���Ϣ
		astParser.setSource(new String(input).toCharArray());
		// ���ý��������﷨��������
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		// �����ɱ��뵥Ԫ
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

		return result;
	}

	/**
	 * ����һ���յĳ����﷨����Ԫ
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
