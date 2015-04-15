package compiler;

import symbolTable.SymbolTableBuilderVisitor;
import syntaxtree.Program;
import typecheck.TypeCheckerVisitor;
import visitor.PrettyPrintVisitor;
import compiler.lexical.MiniJavaParser;
import compiler.lexical.ParseException;

public class Main {
	public static void main(String[] args) {
		try {
			Program root = new MiniJavaParser(System.in).Goal();
			
			root.accept(new SymbolTableBuilderVisitor());			
			root.accept(new PrettyPrintVisitor());			
			root.accept(new TypeCheckerVisitor());
			
		} catch (ParseException e) {
			System.out.println(e.toString());
		}

	}
}
