package syntaxtree;
import translate.Translate;
import visitor.TypeVisitor;

public class VarDecl {
  public Type t;
  public Identifier i;
  
  public VarDecl(Type at, Identifier ai) {
    t=at; i=ai;
  }

  public void accept(Translate translate) {
    translate.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}