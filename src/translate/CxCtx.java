package translate;

import temp.Label;
import temp.Temp;
import tree.Exp;
import tree.Stm;

public abstract class CxCtx implements Ctx {

	public Exp unEx() {
		Temp r = new Temp();
		Label t = new Label();
		Label f = new Label();
//		return ESEQ( SEQ( MOVE (TEMP(r), CONST(1)),
//			SEQ( this.unCx(t,f),
//					SEQ( LABEL(f),
//							SEQ( MOVE (TEMP(r), CONST(0)),
//									LABEL(t))))),
//									TEMP(r)));
		return null;
	}
	public Stm unNx() {
		//... ? ... 
		return null;
	}
	public abstract Stm unCx(Label t, Label f);
	}
