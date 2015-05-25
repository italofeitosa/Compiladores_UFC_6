package syntaxtree;

import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class True extends Exp {
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}
}