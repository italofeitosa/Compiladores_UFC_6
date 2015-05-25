package compiler.intermediateRepresentation.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import frame.Access;
import frame.Frame;
import temp.Label;
import temp.Temp;
import tree.BINOP;
import tree.CALL;
import tree.CONST;
import tree.ESEQ;
import compiler.intermediateRepresentation.tree.expression.ExpList;
import compiler.intermediateRepresentation.tree.expression.MEM;
import compiler.intermediateRepresentation.tree.expression.NAME;
import compiler.intermediateRepresentation.tree.expression.TEMP;
import compiler.intermediateRepresentation.tree.statement.CJUMP;
import compiler.intermediateRepresentation.tree.statement.JUMP;
import compiler.intermediateRepresentation.tree.statement.LABEL;
import compiler.intermediateRepresentation.tree.statement.MOVE;
import compiler.intermediateRepresentation.tree.statement.SEQ;
import compiler.intermediateRepresentation.tree.statement.Stm;
import compiler.semantic.TranslateVisitor;
import compiler.symbolTable.Symbol;
import compiler.syntactical.And;
import compiler.syntactical.ArrayAssign;
import compiler.syntactical.ArrayLength;
import compiler.syntactical.ArrayLookup;
import compiler.syntactical.Assign;
import compiler.syntactical.Block;
import compiler.syntactical.BooleanType;
import compiler.syntactical.Call;
import compiler.syntactical.ClassDeclExtends;
import compiler.syntactical.ClassDeclSimple;
import compiler.syntactical.False;
import compiler.syntactical.Formal;
import compiler.syntactical.Identifier;
import compiler.syntactical.IdentifierExp;
import compiler.syntactical.IdentifierType;
import compiler.syntactical.If;
import compiler.syntactical.IntArrayType;
import compiler.syntactical.IntegerLiteral;
import compiler.syntactical.IntegerType;
import compiler.syntactical.LessThan;
import compiler.syntactical.MainClass;
import compiler.syntactical.MethodDecl;
import compiler.syntactical.Minus;
import compiler.syntactical.NewArray;
import compiler.syntactical.NewObject;
import compiler.syntactical.Not;
import compiler.syntactical.Plus;
import compiler.syntactical.Print;
import compiler.syntactical.Program;
import compiler.syntactical.This;
import compiler.syntactical.Times;
import compiler.syntactical.True;
import compiler.syntactical.VarDecl;
import compiler.syntactical.While;

public class Translate implements TranslateVisitor {

	private Frame currFrame;
	private Frag frags = null;
	private HashMap<String, Access> vars = null;
	private HashMap<String, Integer> fieldVars = null;
	private compiler.intermediateRepresentation.tree.expression.Exp objPtr = null;

	public Translate(FrameImpl frameImpl) {
		currFrame = frameImpl;
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

		compiler.intermediateRepresentation.tree.expression.Exp retExp = new CONST(
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
		for (int i = 0; i < n.methodList.size(); i++)
			n.methodList.elementAt(i).accept(this);
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
				new Label(n.methodNameId.toString())), null);

		List<Boolean> formal = new ArrayList<Boolean>();
		for (int i = 0; i < n.formalParamList.size(); i++) {
			formal.add(false);
		}
		Frame oldFrame = currFrame;
		Frame newFrame = currFrame.newFrame(
				new Symbol(n.methodNameId.toString()), formal);
		Access ac = currFrame.allocLocal(false);

		// Frame oldFrame = currFrame;
		currFrame = newFrame;

		for (int i = 1; i < n.formalParamList.size(); i++) {
			compiler.intermediateRepresentation.tree.expression.Exp addr = new BINOP(
					BINOP.MINUS, new TEMP(currFrame.FP()), new CONST(i * word));
			Stm form = new MOVE(new TEMP(new Temp()), new MEM(addr));

			method = new SEQ(method, form);
		}

		for (int i = 0; i < n.localVarList.size(); i++) {
			method = new SEQ(method, n.localVarList.elementAt(i).accept(this)
					.unNx());
		}

		objPtr = ac.exp(new ESEQ(new MOVE(new TEMP(currFrame.FP()), new MEM(
				new TEMP(currFrame.FP()))), new TEMP(currFrame.FP())));

		Stm body = n.statementList.elementAt(0).accept(this).unNx();
		for (int i = 1; i < n.statementList.size(); i++) {
			body = new SEQ(body, n.statementList.elementAt(i).accept(this)
					.unNx());
		}
		method = new SEQ(method, body);
		compiler.intermediateRepresentation.tree.expression.Exp e = new TEMP(
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
		for (int i = 0; i < n.stmtList.size(); i++) {
			n.stmtList.elementAt(i).accept(this);
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
		compiler.intermediateRepresentation.tree.expression.Exp e1 = null;
		if (n.intExpr != null) {
			e1 = (n.intExpr.accept(this)).unEx();
		}
		return new Ex(currFrame.externalCall("printInt", new ExpList(e1, null)));
	}

	@Override
	public Exp visit(Assign n) {
		compiler.intermediateRepresentation.tree.expression.Exp i = n.varId
				.accept(this).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp e = n.valueExpr
				.accept(this).unEx();

		return new Nx(new MOVE(i, e));
	}

	@Override
	public Exp visit(ArrayAssign n) {
		compiler.intermediateRepresentation.tree.expression.Exp e1 = (n.indexExpr
				.accept(this)).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp e2 = (n.valueExpr
				.accept(this)).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp expId = (n.i
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

		compiler.intermediateRepresentation.tree.expression.Exp left = n.e1
				.accept(this).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp right = n.e2
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
		compiler.intermediateRepresentation.tree.expression.Exp exp1 = (n.e1
				.accept(this)).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.PLUS, exp1, exp2));
	}

	@Override
	public Exp visit(Minus n) {
		compiler.intermediateRepresentation.tree.expression.Exp exp1 = (n.e1
				.accept(this)).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.MINUS, exp1, exp2));
	}

	@Override
	public Exp visit(Times n) {
		compiler.intermediateRepresentation.tree.expression.Exp exp1 = (n.e1
				.accept(this)).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp exp2 = (n.e2
				.accept(this)).unEx();
		return new Ex(new BINOP(BINOP.MUL, exp1, exp2));
	}

	@Override
	public Exp visit(ArrayLookup n) {
		Temp t_index = new Temp();
		Temp t_size = new Temp();
		compiler.intermediateRepresentation.tree.expression.Exp e1 = n.arrayExpr
				.accept(this).unEx();
		compiler.intermediateRepresentation.tree.expression.Exp e2 = n.indexExpr
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
		compiler.intermediateRepresentation.tree.expression.Exp e = n.sizeExpr
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
				(n.boolExpr.accept(this)).unEx()));
	}

	@Override
	public Exp visit(Identifier n) {
		return new Ex(new TEMP(currFrame.FP()));
	}

	private compiler.intermediateRepresentation.tree.expression.Exp getIdTree(
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