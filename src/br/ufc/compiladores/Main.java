package br.ufc.compiladores;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		try {
			new MiniJavaParser(System.in).Goal();
			System.out.println("Lexical analysis successfull");
		} catch (ParseException e) {
			System.out.println("Lexer Error : \n" + e.toString());
		}
	}

}
