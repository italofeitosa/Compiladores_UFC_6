package translate;

import temp.Label;
import tree.CJUMP;
import tree.Stm;

public class RelCx extends Cx {

	private int relop;
	tree.Exp left;
	tree.Exp right;

	public RelCx(int o, tree.Exp l, tree.Exp r) {
		relop = o;
		left = l;
		right = r;
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(relop, left, right, t, f);
	}
}
