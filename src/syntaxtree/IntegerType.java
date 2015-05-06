package syntaxtree;

import visitor.Visitor;
import visitor.TypeVisitor;

public class IntegerType extends Type {

	private static IntegerType instance = new IntegerType();

	  public static IntegerType instance() {
	    return instance;
	  }

	  private IntegerType() { }
	
	
	
	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
}
