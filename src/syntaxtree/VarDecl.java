package syntaxtree;

import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public class VarDecl {
  public Type t;
  public Identifier i;
  
  public VarDecl(Type at, Identifier ai) {
    t=at; i=ai;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public String toString() {
		return t + " " + i;
	}
	
	public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}
}