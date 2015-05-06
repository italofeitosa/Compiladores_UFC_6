package syntaxtree;


import visitor.Visitor;
import visitor.TypeVisitor;

public class IntArrayType extends Type {

	private static IntArrayType instance = new IntArrayType();

	public static IntArrayType instance() {
		return instance;
	}

	private IntArrayType() {
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
}