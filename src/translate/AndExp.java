package translate;

import temp.Label;
import temp.Temp;
import tree.CONST;
import tree.ESEQ;
import tree.LABEL;
import tree.MOVE;
import tree.SEQ;
import tree.Stm;
import tree.TEMP;

public class AndExp extends Exp {

	Label label = new Label();
	Exp left;
	Exp right;

	public AndExp(Exp l, Exp r) {
		left = l;
		right = r;
	}

	public tree.Exp unEx() {
		Temp r = new Temp();
		Label t = new Label();
		Label f = new Label();

		return new ESEQ(new SEQ(new MOVE(new TEMP(r), new CONST(1)), new SEQ(
				left.unCx(label, f), new SEQ(new LABEL(label), new SEQ(
						right.unCx(t, f), new SEQ(new LABEL(f), new SEQ(
								new MOVE(new TEMP(r), new CONST(0)), new LABEL(
										t))))))), new TEMP(r));
	}

	public Stm unNx() {
		System.err
				.println("ERROR:  In well-typed MiniJava, (AndExp a).unNx() should never be used.");
		return null;
	}

	@Override
	Stm unCx(Label t, Label f) {
		return new SEQ(left.unCx(label, f), new SEQ(new LABEL(label),
				right.unCx(t, f)));
	}
}
