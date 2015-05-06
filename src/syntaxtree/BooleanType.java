package syntaxtree;


import visitor.Visitor;
import visitor.TypeVisitor;

public class BooleanType extends Type {
	private static BooleanType instance = new BooleanType();

	public static BooleanType instance() {
		return instance;
	}

	private BooleanType() {
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
}