package tree;
import java.util.List;

import temp.Temp;
import temp.Label;
public class CALL extends Exp {
  public Exp func;
  public ExpList args;
  public CALL(Exp f, ExpList a) {func=f; args=a;}
  public CALL(NAME f, List<Exp> args2) {
	// TODO Auto-generated constructor stub
}
public ExpList kids() {return new ExpList(func,args);}
  public Exp build(ExpList kids) {
    return new CALL(kids.head,kids.tail);
  }
  
}

