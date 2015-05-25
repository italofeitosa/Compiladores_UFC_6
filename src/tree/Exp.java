package tree;

public class Exp extends Stm {
  public Exp exp; 
  //public EXP(Exp e) {exp=e;}
  public ExpList kids() {return new ExpList(exp,null);}
  public Exp build(ExpList kids) {
    return new EXP(kids.head);
  }
}

