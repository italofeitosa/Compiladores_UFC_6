package syntaxtree;

import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public abstract class Exp {
	public abstract void accept(Visitor v);
	public abstract Type accept(TypeVisitor v);
	private Type type;
	public abstract translate.Exp accept(TranslateVisitor v);
	public void setType(Type t) {
	    type = t;
	  }
}