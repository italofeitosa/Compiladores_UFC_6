package syntaxtree;
import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public class Print implements Statement {
  public Exp e;

  public Print(Exp ae) {
    e=ae; 
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public String toString() {
		return "System.out.println(" + e + ");\n";
	}
  
  public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}
}
