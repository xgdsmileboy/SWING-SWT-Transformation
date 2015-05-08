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
public class ForStatementVisitor extends ASTVisitor {
	public boolean visit(ExpressionStatement expresstionStatement) {
		System.out.println("For ExpressionStatement-->" + expresstionStatement);
		return true;
	}

	public boolean visit(
			VariableDeclarationStatement variableDeclarationStatement) {
		System.out.println("For VariableDeclarationStatement-->"
				+ variableDeclarationStatement);
		return true;
	}
}
