package translate;

import temp.Label;
import tree.Stm;

public abstract class Exp {

	abstract tree.Exp unEx();
    abstract Stm unNx();
    abstract Stm unCx(Label t, Label f);

}