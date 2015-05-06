package translate;

import temp.Label;
import tree.Stm;

public class Nx extends Exp {
	Stm stm;

	public Nx(Stm s) {
		stm = s;
	}

	public Stm unNx() {
		return stm;
	}

	public tree.Exp unEx() {
		System.err
				.println("ERROR:  In well-typed MiniJava, (Nx n).unEx() should never be used.");
		return null;
	}

	public Stm unCx(Label t, Label f) {
		System.err
				.println("ERROR:  In well-typed MiniJava, (Nx n).unCx() should never be used.");
		return null;
	}
}
