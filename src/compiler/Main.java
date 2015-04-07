package compiler;

import compiler.lexical.MiniJavaParser;
import compiler.lexical.ParseException;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		System.out.println("escreve:");
		try {
			new MiniJavaParser(System.in).Goal();
			System.out.println("Lexical analysis successfull");
		} catch (ParseException e) {
			System.out.println("Lexer Error : \n" + e.toString());
		}
	}

}
