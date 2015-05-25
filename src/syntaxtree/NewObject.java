package syntaxtree;

import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.TranslateVisitor;

public class NewObject extends Exp {
  public Identifier i;
  
  public NewObject(Identifier ai) {
    i=ai;
  }

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