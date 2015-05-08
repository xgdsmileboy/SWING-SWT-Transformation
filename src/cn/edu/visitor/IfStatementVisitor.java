package cn.edu.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sun.org.mozilla.javascript.internal.ast.VariableDeclaration;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class IfStatementVisitor extends ASTVisitor {

	public boolean visit(ExpressionStatement expresstionStatement) {
		System.out.println("IF ExpressionStatement-->" + expresstionStatement);
		return true;
	}

	public boolean visit(
			VariableDeclarationStatement variableDeclarationStatement) {
		System.out.println("IF VariableDeclarationStatement-->"
				+ variableDeclarationStatement);
		return true;
	}
}
