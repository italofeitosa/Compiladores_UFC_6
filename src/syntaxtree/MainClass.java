package syntaxtree;

import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public class MainClass {
	
	  public Identifier i1,i2;
	  public StatementList s;
	  public VarDeclList localVars;

	  public MainClass(Identifier ai1, Identifier ai2, VarDeclList vl , StatementList as) {
	    i1=ai1; i2=ai2; s=as; localVars=vl;
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