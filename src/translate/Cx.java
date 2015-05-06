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

public abstract class Cx extends Exp {

	public tree.Exp unEx() {
		Temp r = new Temp();
		Label t = new Label();
		Label f = new Label();

		return new ESEQ(new SEQ(new MOVE(new TEMP(r), new CONST(1)), new SEQ(
				unCx(t, f), new SEQ(new LABEL(f), new SEQ(new MOVE(new TEMP(r),
						new CONST(0)), new LABEL(t))))), new TEMP(r));
	}

	public abstract Stm unCx(Label t, Label f);

	public Stm unNx() {
		System.err
				.println("ERROR:  In well-typed MiniJava, (Cx c).unNx() should never be used.");
		return null;
	}
}
