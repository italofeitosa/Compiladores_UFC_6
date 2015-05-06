package translate;

import temp.Label;
import tree.CJUMP;
import tree.CONST;
import tree.Stm;

public class Ex extends Exp {
	tree.Exp exp;

	Ex(tree.Exp e) {
		super();
		exp = e;
	}

	public tree.Exp unEx() {
		return exp;
	}

	public Stm unNx() {
		return new tree.EXP(exp);
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.EQ, exp, new CONST(0), f, t);
	}

}
