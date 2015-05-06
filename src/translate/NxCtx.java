package translate;

import temp.Label;
import tree.Exp;
import tree.Stm;

public class NxCtx implements Ctx {
	Stm stm;
	NxCtx (Stm s) {stm = s;}
	public Stm unNx() {return stm;}
	@Override
	public Exp unEx() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stm unCx(Label t, Label f) {
		// TODO Auto-generated method stub
		return null;
	}
}
