package translate;

import temp.Label;
import tree.Exp;
import tree.Stm;

public class ExCtx implements Ctx{

	Exp exp;
	ExCtx (Exp e) {exp = e;}
	public Exp unEx() {return exp;}
	public Stm unNx() {return new EXP(exp);}
	public Stm unCx(Label t, Label f){
		//{ ... ? ... }
		return null;
	}

}
