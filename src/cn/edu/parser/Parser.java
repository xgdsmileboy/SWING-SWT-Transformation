package cn.edu.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cn.edu.trans.Block;
import cn.edu.trans.Clause;
import cn.edu.trans.ISTM;
import cn.edu.trans.Source;

/**
 * 
 * @author Jiajun.Jiang
 * @date 2015-4-29 All rights reserves.
 * 
 *       comment :
 */
public class Parser {
	/*
	 * ����ת��֮����ļ���������ļ�
	 */
	private static File file = null;
	/*
	 * ת��֮����ļ����洢������·�������û�û��������ʹ�ô�·��
	 */
	private static String defaultPath = ".\\src\\";

	/**
	 * ����Ŀ���ļ�����·��
	 * 
	 * @param pathName
	 */
	public static void setTargetFilePath(String pathName) {
		if (pathName == null) {
			System.err.println("Target file path is null!");
		} else {
			// ����ļ������ڣ����´���
//			file = new File(pathName);
			defaultPath = pathName;
//			if (!file.exists()) {
//				try {
//					file.createNewFile();
//				} catch (IOException e) {
//					System.err.println("Create new file " + pathName
//							+ " failed!");
//					e.printStackTrace();
//				}
//			}
		}
	}
	/**
	 * ���ñ�����Ϣ
	 */
	public static void reset(){
		file = null;
		defaultPath = ".\\src\\";
	}

	/**
	 * ��Դ������н���
	 * 
	 * @param source
	 */
	public static void parse(Source source) {
		if (file == null) {
			defaultPath += source.getClassName() + ".java";
			file = new File(defaultPath);
			System.out.println("The result has been set to " + defaultPath);
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, false);
		} catch (IOException e) {
			System.err.println("Init FileWriter failed!");
			e.printStackTrace();
		}
		// ��Ӱ�������Ϣ
		if (source.getPackageDec() != null) {
			try {
				fw.write("package " + source.getPackageDec() + "\n");
			} catch (IOException e) {
				System.err.println("Write package declaration failed!");
				e.printStackTrace();
			}
		}
		// �����Ҫ�����İ���Ϣ
		List<String> importList = source.getImportDec();
		for (String imp : importList) {
			try {
				fw.write("import " + imp + "\n");
			} catch (IOException e) {
				System.err.println("Write import declaration failed!");
				e.printStackTrace();
			}
		}
		// Դ�������
		List<ISTM> statementList = source.getStatementList();
		for (ISTM statement : statementList) {
			writeStatment(fw, statement, "");
		}

		try {
			fw.flush();
			fw.close();
		} catch (IOException e) {
			System.err.println("Close file failed!");
			e.printStackTrace();
		}

		System.out.println("Result has been written to file : " + defaultPath);
	}

	/**
	 * ������֮������д���ļ�
	 * 
	 * @param fw
	 * @param statement
	 */
	private static void writeStatment(FileWriter fw, ISTM statement, String indent) {
		if (statement == null)
			return;
		switch (statement.getStatementType()) {

		case CLAUSE: // ��ͨ�������Ϣ
		{
			Clause clause = (Clause) statement;
			try {
				fw.write(indent + clause.getStatement() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		case BLOCK: // �������
		{
			Block block = (Block) statement;
			List<ISTM> list = block.getStatementList();
			try {
				fw.write(indent+block.getStatement() + "\n"+indent+"{\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (ISTM statem : list) {
				writeStatment(fw, statem, indent+"    ");
			}
			try {
				fw.write(indent+"}\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		default: {
			System.err.println("Statement type error!");
		}

		}
	}
}
