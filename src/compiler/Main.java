package compiler;

import syntaxtree.Program;
import visitor.PrettyPrintVisitor;

import compiler.lexical.MiniJavaParser;
import compiler.lexical.ParseException;

public class Main {
   public static void main(String [] args) {
      try {
         Program root = new MiniJavaParser(System.in).Goal();
          root.accept(new PrettyPrintVisitor());
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
