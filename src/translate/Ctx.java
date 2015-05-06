package translate;

import temp.Label;
import tree.Exp;
import tree.Stm;

public interface Ctx {

	Exp unEx();
	Stm unNx();
	Stm unCx(Label t, Label f);
}
