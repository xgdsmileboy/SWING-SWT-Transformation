package cn.edu.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class MethodBodyVisitor extends ASTVisitor {

	public boolean visit(ExpressionStatement expresstionStatement) {
		System.out
				.println("Body ExpressionStatement-->" + expresstionStatement);
		return true;
	}

	public boolean visit(
			VariableDeclarationStatement variableDeclarationStatement) {
		System.out.println("Body VariableDeclarationStatement-->"
				+ variableDeclarationStatement);
		return true;
	}

	public boolean visit(IfStatement ifStatement) {

		Block thenBlock = (Block) ifStatement.getThenStatement();
		thenBlock.accept(new IfStatementVisitor());

		Block elseBlock = (Block) ifStatement.getElseStatement();
		if (elseBlock != null) {
			elseBlock.accept(new IfStatementVisitor());
		}

		return false;
	}

	public boolean visit(ForStatement forStatement) {

		Block block = (Block) forStatement.getBody();
		block.accept(new ForStatementVisitor());

		return false;
	}

	public boolean visit(WhileStatement whileStatement) {

		Block block = (Block) whileStatement.getBody();
		block.accept(new WhileStatementVisitor());

		return false;
	}

}
