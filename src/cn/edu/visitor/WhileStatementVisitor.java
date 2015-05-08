package cn.edu.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class WhileStatementVisitor extends ASTVisitor {
	public boolean visit(ExpressionStatement expresstionStatement) {
		System.out.println("While ExpressionStatement-->"
				+ expresstionStatement);
		return true;
	}

	public boolean visit(
			VariableDeclarationStatement variableDeclarationStatement) {
		System.out.println("While VariableDeclarationStatement-->"
				+ variableDeclarationStatement);
		return true;
	}
}
