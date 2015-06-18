package syntaxtree;
import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public class While implements Statement {
  public Exp e;
  public Statement s;

  public While(Exp ae, Statement as) {
    e=ae; s=as; 
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public String toString() {
		return "while(" + e + ") " + s + "\n";
	}
	
	public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}
}
