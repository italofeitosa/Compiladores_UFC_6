package symbolTable;

import java.util.Hashtable;

public class Symbol{
	private String name;
	private static Hashtable<String, Symbol> dict = new Hashtable<String, Symbol>();
	
	public Symbol(String n){
		name = n;
	}

	public String toString(){
		return name;
	}

	public static Symbol symbol(String n){
		String u = n.intern();
		Symbol s = (Symbol)dict.get(u);
		if(s==null) {
			s = new Symbol(u);
			dict.put(u, s);
		}
		return s;
	}

	public boolean equals(Object s){
		return this.name.equals(s.toString());
	}
}