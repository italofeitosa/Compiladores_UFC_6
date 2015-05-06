package translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mipsFrame.MipsFrame;
import symbolTable.Symbol;
import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.VarDecl;
import syntaxtree.While;
import temp.Label;
import temp.Temp;
import tree.BINOP;
import tree.CALL;
import tree.CJUMP;
import tree.CONST;
import tree.ESEQ;
import tree.ExpList;
import tree.JUMP;
import tree.LABEL;
import tree.MEM;
import tree.MOVE;
import tree.NAME;
import tree.SEQ;
import tree.Stm;
import tree.TEMP;
import visitor.TranslateVisitor;
import frame.Access;
import frame.Frame;

public class Translate implements TranslateVisitor {
	
	private Frame currFrame;
	private Frag frags = null;
	private HashMap<String, Access> vars = null;
	private HashMap<String, Integer> fieldVars = null;
	private tree.Exp objPtr = null;

	public Translate(MipsFrame mipsFrame) {
		currFrame = mipsFrame;
	}

	@Override
	public Exp visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++)
			n.cl.elementAt(i).accept(this);
		return null;
	}

	@Override
	public Exp visit(MainClass n) {
		Frame newFrame = currFrame.newFrame(new Symbol("main"),
				Arrays.asList(new Boolean[] { true }));
		Frame oldFrame = currFrame;
		currFrame = newFrame;

		// Stm s = (n.statements.accept(this)).unNx();
		Stm s = null;

		tree.Exp retExp = new CONST(
				0);
		Stm body = new MOVE(new TEMP(currFrame.RV()), new ESEQ(s, retExp));

		procEntryExit(body);
		currFrame = oldFrame;

		return null;
	}

	@Override
	public Exp visit(ClassDeclSimple n) {
		for (int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		return null;
	}

	@Override
	public Exp visit(ClassDeclExtends n) {
		for (int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		return null;
	}

	@Override
	public Exp visit(VarDecl n) {
		Access ac = currFrame.allocLocal(false);
		return new Nx(new MOVE(ac.exp(new TEMP(currFrame.FP())), new CONST(0)));
	}

	public Frag getResults() {
		return frags;
	}

	@Override
	public Exp visit(MethodDecl n) {

		int word = currFrame.wordSize();

		Stm method = new SEQ(new LABEL(
				new Label(n.i.toString())), null);

		List<Boolean> formal = new ArrayList<Boolean>();
		for (int i = 0; i < n.fl.size(); i++) {
			formal.add(false);
		}
		Frame oldFrame = currFrame;
		Frame newFrame = currFrame.newFrame(
				new Symbol(n.i.toString()), formal);
		Access ac = currFrame.allocLocal(false);

		// Frame oldFrame = currFrame;
		currFrame = newFrame;

		for (int i = 1; i < n.fl.size(); i++) {
			tree.Exp addr = new BINOP(
					BINOP.MINUS, new TEMP(currFrame.FP()), new CONST(i * word));
			Stm form = new MOVE(new TEMP(new Temp()), new MEM(addr));

			method = new SEQ(method, form);
		}

		for (int i = 0; i < n.vl.size(); i++) {
			method = new SEQ(method, n.vl.elementAt(i).accept(this)
					.unNx());
		}

		objPtr = ac.exp(new ESEQ(new MOVE(new TEMP(currFrame.FP()), new MEM(
				new TEMP(currFrame.FP()))), new TEMP(currFrame.FP())));

		Stm body = n.sl.elementAt(0).accept(this).unNx();
		for (int i = 1; i < n.sl.size(); i++) {
			body = new SEQ(body, n.sl.elementAt(i).accept(this)
					.unNx());
		}
		method = new SEQ(method, body);
		tree.Exp e = new TEMP(
				currFrame.RV());

		procEntryExit(body);

		currFrame = oldFrame;
		return new Ex(new ESEQ(method, e));
	}

	private void procEntryExit(Stm body) {
		ProcFrag newfrag = new ProcFrag(body, currFrame);
		if (frags == null)
			frags = newfrag;
		else
			frags.next = newfrag;
	}

	@Override
	public Exp visit(Formal n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(IntArrayType n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(BooleanType n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(IntegerType n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(IdentifierType n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	@Override
	public Exp visit(If n) {
		Label T = new Label();
		Label F = new Label();
		Label D = new Label();
		Exp exp = n.boolExpr.accept(this);
		Exp stmT = n.trueStmt.accept(this);
		Exp stmF = n.falseStmt.accept(this);
		return new Nx(new SEQ(
				new SEQ(new SEQ(
						new SEQ(new CJUMP(CJUMP.EQ, exp.unEx(), new CONST(1),
								T, F), new SEQ(new LABEL(T), stmT.unNx())),
						new JUMP(D)), new SEQ(new LABEL(F), stmF.unNx())),
				new LABEL(D)));
	}

	@Override
	public Exp visit(While n) {
		Label test = new Label();
		Label T = new Label();
		Label F = new Label();
		Exp exp = n.boolExpr.accept(this);
		Exp body = n.stmt.accept(this);

		return new Nx(new SEQ(new SEQ(new SEQ(new LABEL(test), (new CJUMP(
				CJUMP.EQ, exp.unEx(), new CONST(1), T, F))), (new SEQ(
				new LABEL(T), body.unNx()))), new LABEL(F)));
	}

	@Override
	public Exp visit(Print n) {
		tree.Exp e1 = null;
		if (n.intExpr != null) {
			e1 = (n.intExpr.accept(this)).unEx();
		}
		return new Ex(currFrame.externalCall("printInt", new ExpList(e1, null)));
	}

	@Override
	public Exp visit(Assign n) {
		tree.Exp i = n.varId
				.accept(this).unEx();
		tree.Exp e = n.valueExpr
				.accept(this).unEx();

		return new Nx(new MOVE(i, e));
	}

	@Override
	public Exp visit(ArrayAssign n) {
		tree.Exp e1 = (n.indexExpr
				.accept(this)).unEx();
		tree.Exp e2 = (n.valueExpr
				.accept(this)).unEx();
		tree.Exp expId = (n.i
				.accept(this)).unEx();
		return new Nx(new MOVE(new BINOP(BINOP.PLUS, new MEM(expId), new BINOP(
				BINOP.MUL, e1, new CONST(4))), e2));
	}

	@Override
	public Exp visit(And n) {
		Temp t1 = new Temp();
		Label done = new Label();
		Label ok1 = new Label();
		Label ok2 = new Label();

		tree.Exp left = n.e1
				.accept(this).unEx();
		tree.Exp right = n.e2
				.accept(this).unEx();
		return new Ex(new ESEQ(new SEQ(new SEQ(new SEQ(new SEQ(new SEQ(
				new MOVE(new TEMP(t1), new CONST(0)), new CJUMP(CJUMP.EQ, left,
						new CONST(1), ok1, done)), new SEQ(new LABEL(ok1),
				new CJUMP(CJUMP.EQ, right, new CONST(1), ok2, done))), new SEQ(
				new LABEL(ok2), new MOVE(new TEMP(t1), new CONST(1)))),
				new JUMP(done)), new LABEL(done)), new TEMP(t1)));
	}

	@Override
	public Exp visit(LessThan n) {
		Exp expl = n.e1.accept(this);
		Exp expr = n.e2.accept(this);
		Label T = new Label();
		Label F = new Label();
		Temp t = new Temp();
		return new Ex(new ESEQ(new SEQ(
				new SEQ(new SEQ(new MOVE(new TEMP(t), new CONST(0)), new CJUMP(
						CJUMP.LT, expl.unEx(), expr.unEx(), T, F)), new SEQ(
						new LABEL(T), new MOVE(new TEMP(t), new CONST(1)))),
				new LABEL(F)), new TEMP(t)));
	}

	@Override
	public Exp visit(Plus n) {
		tree.Exp exp1 = (n.e1
				.accept(this)).unEx();
		tree.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.PLUS, exp1, exp2));
	}

	@Override
	public Exp visit(Minus n) {
		tree.Exp exp1 = (n.e1
				.accept(this)).unEx();
		tree.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.MINUS, exp1, exp2));
	}

	@Override
	public Exp visit(Times n) {
		tree.Exp exp1 = (n.e1
				.accept(this)).unEx();
		tree.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.MUL, exp1, exp2));
	}

	@Override
	public Exp visit(ArrayLookup n) {
		Temp t_index = new Temp();
		Temp t_size = new Temp();
		tree.Exp e1 = n.arrayExpr
				.accept(this).unEx();
		tree.Exp e2 = n.indexExpr
				.accept(this).unEx();

		Label F = new Label();
		Label T = new Label();

		ExpList args1 = new ExpList(e2, null);

		Stm s1 = new SEQ(new SEQ(new SEQ(new SEQ(new SEQ(new MOVE(new TEMP(
				t_index), new BINOP(BINOP.MUL, e2, new CONST(4))), new MOVE(
				new TEMP(t_size), new MEM(e1))), new CJUMP(CJUMP.GE, new TEMP(
				t_index), new TEMP(t_size), T, F)), new LABEL(T)), new MOVE(
				new TEMP(new Temp()), new CALL(new NAME(new Label("_error")),
						args1))), new LABEL(F));

		Temp t = new Temp();
		Stm s2 = new SEQ(s1, new MOVE(new TEMP(t), new MEM(new BINOP(
				BINOP.PLUS, e1, new BINOP(BINOP.PLUS, new BINOP(BINOP.MUL, e2,
						new CONST(4)), new CONST(4))))));
		return new Ex(new ESEQ(s2, new TEMP(t)));
	}

	@Override
	public Exp visit(ArrayLength n) {
		n.arrayExpr.accept(this);
		return null;
	}

	@Override
	public Exp visit(Call n) {
		ExpList el = null;
		for (int i = 0; i < n.paramExprList.size(); i++) {
			Exp ex = n.paramExprList.elementAt(i).accept(this);
			el = new ExpList(ex.unEx(), el);
		}

		return new Ex(new CALL(new NAME(new Label(n.objectExpr.accept(this)
				.toString())), el));
	}

	@Override
	public Exp visit(IntegerLiteral n) {
		return new Ex(new CONST(n.value));
	}

	@Override
	public Exp visit(True n) {
		return new Ex(new CONST(1));
	}

	@Override
	public Exp visit(False n) {
		return new Ex(new CONST(0));
	}

	@Override
	public Exp visit(IdentifierExp n) {
		return new Ex(getIdTree(n.s));
	}

	@Override
	public Exp visit(This n) {
		return new Ex(objPtr);
	}

	@Override
	public Exp visit(NewArray n) {
		tree.Exp e = n.sizeExpr
				.accept(this).unEx();
		ExpList params = new ExpList(e, null);
		Temp t = new Temp();

		return new Ex(new ESEQ(new MOVE(new TEMP(t), currFrame.externalCall(
				"newArray", params)), new TEMP(t)));
	}

	@Override
	public Exp visit(NewObject n) {
		return new Ex(new TEMP(new Temp()));
	}

	@Override
	public Exp visit(Not n) {
		return new Ex(new BINOP(BINOP.MINUS, new CONST(1),
				(n.e.accept(this)).unEx()));
	}

	@Override
	public Exp visit(Identifier n) {
		return new Ex(new TEMP(currFrame.FP()));
	}

	private tree.Exp getIdTree(
			String id) {
		Access a = null;

		try {
			a = vars.get(id);

			if (a == null) {
				int offset = fieldVars.get(id).intValue();
				return new MEM(new BINOP(BINOP.PLUS, objPtr, new CONST(offset)));
			}

			return a.exp(new TEMP(currFrame.FP()));
		} catch (Exception ex) {
			return new MEM(new BINOP(BINOP.PLUS, objPtr, new CONST(0)));
		}
	}

	}