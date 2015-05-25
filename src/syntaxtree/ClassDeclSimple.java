package syntaxtree;

import translate.Translate;
import visitor.Visitor;
import visitor.TypeVisitor;
import visitor.TranslateVisitor;

public class ClassDeclSimple implements ClassDecl {
  public Identifier i;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml) {
    i=ai; vl=avl; ml=aml;
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

@Override
public void accept(Translate translate) {
	// TODO Auto-generated method stub
	
}

}


