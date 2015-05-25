package syntaxtree;

import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.TranslateVisitor;

public class Call extends Exp {
  public Exp e;
  public Identifier i;
  public ExpList el;
  
  public Call(Exp ae, Identifier ai, ExpList ael) {
    e=ae; i=ai; el=ael;
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
 /* public List<Call> getList() {
	     return el;
}*/
}
