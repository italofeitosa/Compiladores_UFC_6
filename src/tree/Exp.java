package tree;
import temp.Temp;
import temp.Label;
public class Exp extends Stm {
  public Exp exp; 
  //public EXP(Exp e) {exp=e;}
  public ExpList kids() {return new ExpList(exp,null);}
  public Stm build(ExpList kids) {
    return new EXP(kids.head);
  }
}
