package syntaxtree;

import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class NewArray extends Exp {
  public Exp e;
  
  public NewArray(Exp ae) {
    e=ae; 
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