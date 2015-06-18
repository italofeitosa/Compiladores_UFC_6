package syntaxtree;

import translate.Translate;
import visitor.TranslateVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

public class ClassDeclExtends implements ClassDecl {
	public Identifier i;
	public Identifier j;
	public VarDeclList vl;
	public MethodDeclList ml;

	public ClassDeclExtends(Identifier ai, Identifier aj, VarDeclList avl,
			MethodDeclList aml) {
		i = ai;
		j = aj;
		vl = avl;
		ml = aml;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}

	public String toString() {
		return "class " + i + " extends " + j + "{ " + vl + " " + ml + " }";
	}
	
	@Override
	public translate.Exp accept(TranslateVisitor v) {
		return v.visit(this);
	}
}
