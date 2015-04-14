package syntaxtree;

import java.util.List;
import java.util.Vector;

import visitor.DepthFirstVisitor;
import visitor.PrettyPrintVisitor;
import visitor.TypeDepthFirstVisitor;

public class StatementList {
	private Vector<Statement> list;

	public StatementList() {
		list = new Vector<Statement>();
	}

	public void addElement(Statement n) {
		list.addElement(n);
	}

	public Statement elementAt(int i) {
		return (Statement) list.elementAt(i);
	}

	public int size() {
		return list.size();
	}

	public List<Statement> getList() {
		return list;
	}

	public String toString() {
		if (list.isEmpty())
			return "EMPTY";

		StringBuffer sb = new StringBuffer(list.get(0).toString());
		for (int i = 1; i < list.size(); i++) {
			sb.append("\n" + list.get(i).toString());
		}

		return sb.toString();
	}

	public void accept(DepthFirstVisitor depthFirstVisitor) {
		// TODO Auto-generated method stub

	}

	public void accept(PrettyPrintVisitor prettyPrintVisitor) {
		// TODO Auto-generated method stub
		
	}

	public void accept(TypeDepthFirstVisitor typeDepthFirstVisitor) {
		// TODO Auto-generated method stub
		
	}
}
