package syntaxtree;

import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class ArrayAssign implements Statement {
	public Identifier i;
	public Exp e1,e2;

	public ArrayAssign(Identifier ai, Exp ae1, Exp ae2) {
    i=ai; e1=ae1; e2=ae2;
	}

	public void accept(Visitor v) {
    v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}

	public String toString() {
		return i + "[" + e1 + "]=" + e2 + ";\n";
	}
	
	public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}

}
