package MipsFrame;import temp.Temp;public class InReg extends Frame.Access {    Temp temp;    InReg(Temp t) {	temp = t;    }    public tree.Exp exp(tree.Exp fp) {        return new tree.TEMP(temp);    }    public String toString() {        return temp.toString();    }}