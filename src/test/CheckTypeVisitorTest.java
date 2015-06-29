package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import symbolTable.SymbolTableBuilderVisitor;
import syntaxtree.Program;
import typecheck.TypeCheckerVisitor;
import visitor.PrettyPrintVisitor;

import compiler.lexical.MiniJavaParser;
import compiler.lexical.ParseException;

public class CheckTypeVisitorTest {
	private static final String FILES[] = //{ "Factorial",
		{"QuickSort" };
	private static final String TEST_FILES_FOLDER = "programFiles/";

	public static void main(String[] args) throws FileNotFoundException {
		MiniJavaParser miniJavaParser = null;
		Program program = null;
		for (int i = 0; i < FILES.length; i++) {
			try {
				if (i == 0) {
					miniJavaParser = new MiniJavaParser(new FileInputStream(
							new File(TEST_FILES_FOLDER + FILES[i])));
					program = miniJavaParser.Goal();
				} else {
					miniJavaParser.ReInit(new FileInputStream(new File(
							TEST_FILES_FOLDER + FILES[i])));
					program = miniJavaParser.Goal();
				}
				System.out.println("Lexical analysis successfull");
			} catch (ParseException e) {
				System.out.println("Lexer Error : \n" + e.toString());
			}
			System.out.println("-----------------------------------------------");
			SymbolTableBuilderVisitor symbolTableBuilderVisitor = new SymbolTableBuilderVisitor();
			symbolTableBuilderVisitor.visit(program.m);
			program.accept(new SymbolTableBuilderVisitor());
			//program.accept(new PrettyPrintVisitor());
			//program.accept(new TypeCheckerVisitor());
		}
	}
}
