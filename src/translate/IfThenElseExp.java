package translate;

import temp.Label;
import tree.Stm;


public class IfThenElseExp extends Exp{
    Exp cond, a, b;
    Label t = new Label();
    Label f = new Label();
    Label join = new Label();

    IfThenElseExp (Exp cc, Exp aa, Exp bb){
        cond = cc;
        a = aa;
        b = bb;
    }

    @Override
    tree.Exp unEx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    Stm unNx() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	Stm unCx(Label t, Label f) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
