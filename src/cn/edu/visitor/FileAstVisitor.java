package cn.edu.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.edu.Transformation;
import cn.edu.info.ClassInfo;
import cn.edu.info.MethodInfo;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class FileAstVisitor extends ASTVisitor {

	@Override
	public boolean visit(FieldDeclaration node) {
		int size = Transformation.mClassInfoList.size();
		Transformation.mClassInfoList.get(size - 1).addFieldDeclaration(node);
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		int size = Transformation.mClassInfoList.size();
		MethodInfo methodInfo = new MethodInfo(node,
				Transformation.mClassInfoList.get(size - 1));
		Transformation.mClassInfoList.get(size - 1).addMethodInfo(methodInfo);
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		Transformation.mClassInfoList.add(new ClassInfo(node));
		return true;
	}

}