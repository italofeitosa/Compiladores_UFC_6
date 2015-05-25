package syntaxtree;

import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.TranslateVisitor;

public class False extends Exp {
	
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