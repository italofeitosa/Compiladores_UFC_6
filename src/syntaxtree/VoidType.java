package syntaxtree;

import visitor.*;

public class VoidType extends Type{
	  private static VoidType instance = new VoidType();

	  public static VoidType instance() {
	    return instance;
	  }

	  private VoidType() { }

	  public void accept(Visitor v) {
	    v.visit(this);
	  }

	  public Type accept(TypeVisitor v) {
	    return v.visit(this);
	  }

	  @Override
	  public String toString() {
	    return "void";
	  }
	}