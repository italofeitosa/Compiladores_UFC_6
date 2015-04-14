package syntaxtree;

import compiler.lexical.Token;

import visitor.Visitor;
import visitor.TypeVisitor;

public class Identifier extends Exp {
	  public String s;
	  private Token tok;

	  public Identifier(Identifier i) {
	    this(i.s, i.tok);
	  }
	  
	  public Identifier(String as, Token token) {
	    s = as;
	    tok = token;
	  }

	  public Token getToken() {
	    return tok;
	  }

	  public void accept(Visitor v) {
	    v.visit(this);
	  }

	  public Type accept(TypeVisitor v) {
	    return v.visit(this);
	  }
	  
	  public String toString() {
	    return s;
	  }

	  public String getDescriptor() {
	    return s + ":" + tok.beginLine;
	  }
	  
	  

	}