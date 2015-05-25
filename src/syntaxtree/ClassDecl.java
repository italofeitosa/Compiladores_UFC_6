package syntaxtree;

import translate.Translate;
import visitor.TranslateVisitor;
import visitor.TypeVisitor;

public interface ClassDecl {
  public void accept(Translate translate);
  public Type accept(TypeVisitor v);
  public translate.Exp accept(TranslateVisitor v);
}
