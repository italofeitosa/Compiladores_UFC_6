package syntaxtree;

import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public interface Statement {
  public void accept(Visitor v);
  public Type accept(TypeVisitor v);
  public translate.Exp accept(TranslateVisitor v);
}