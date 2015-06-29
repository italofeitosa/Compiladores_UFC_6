package assem;


import java.util.ArrayList;

import frame.Frame;
import temp.Label;
import temp.LabelList;
import temp.Temp;
import temp.TempList;
import tree.Exp;
import tree.BINOP;
import tree.CALL;
import tree.CONST;
import tree.MEM;
import tree.NAME;
import tree.TEMP;
import tree.CJUMP;
import tree.EXP;
import tree.JUMP;
import tree.MOVE;
import tree.SEQ;
import tree.Stm;
import tree.LABEL;
import assem.OPER;
import util.List;

public class Codegen {

	private Frame frame;
    public ArrayList<Instr> ilist;
    
    public Codegen(Frame f) {
            frame=f;
    }
    
    private void emit(assem.Instr inst) {
            ilist.add(inst);
                    
    }
    
    public void munchStm(Stm s) throws Exception {
            if(s instanceof MOVE) munchMove(((MOVE)s).dst, ((MOVE)s).src);
            else if(s instanceof SEQ) munchSeq((SEQ)s);
            else if(s instanceof CJUMP) munchCJump((CJUMP)s);
            else if(s instanceof JUMP) munchJump((JUMP)s);
            else if(s instanceof EXP) munchExp(((EXP)s).exp);
            
            else munchLabel((LABEL)s);
    }
    

    private void munchJump(JUMP s) {
            emit(new OPER("goto "+((NAME)s.exp).label.toString(), null , null));      
    }

    private void munchCJump(CJUMP s) throws Exception {
            Temp r = munchExp(new BINOP(BINOP.MINUS,s.left,s.right));
            if(s.relop==CJUMP.EQ){
                    emit(new OPER("BRANCHEQ if "+r+" = 0 goto "+s.iftrue, null,
                                    new TempList(r,null), new LabelList(s.iftrue, null)));
            }
            else if(s.relop==CJUMP.GE){
                    emit(new OPER("BRANCHGE if "+r+" >= 0 goto "+s.iftrue, null,
                                    new TempList(r,null), new LabelList(s.iftrue, null)));
            }
            else if(s.relop==CJUMP.LT){
                    emit(new OPER("BRANCHLT if "+r+ " < 0 goto "+s.iftrue, null,
                                    new TempList(r,null), new LabelList(s.iftrue, null)));
            }
            else if(s.relop==CJUMP.NE){
                    emit(new OPER("BRANCHNE if "+r+" != 0 goto "+s.iftrue, null,
                                    new TempList(r,null), new LabelList(s.iftrue, null)));
            }else
            emit(new assem.OPER("goto "+s.iffalse.toString(), null, null, new LabelList(s.iffalse, null)));  
    }

    private void munchLabel(LABEL s) {
            emit(new assem.LABEL(s.label.toString()+":",s.label));
    }

    private void munchSeq(SEQ s) throws Exception {
            munchStm(s.left);
            munchStm(s.right);
    }
    
    void munchMove(Exp dst, Exp src) throws Exception {
            // MOVE(d, e)
            if (dst instanceof MEM) munchMove((MEM)dst,src);
            else if (dst instanceof TEMP && src instanceof CALL) munchMove((TEMP)dst,(CALL)src);
            else if (dst instanceof TEMP) munchMove((TEMP)dst,src);
            else if (src instanceof TEMP) munchMove(dst,(TEMP)src);
    }
    
    void munchMove(TEMP dst, Exp src) throws Exception {
            // MOVE(TEMP(t1), e) 
            Temp t =  munchExp(src); 
            emit(new assem.MOVE("MOVEA "+dst.temp+" <- "+t, dst.temp,t));
    }
    
    void munchMove(Exp dst, TEMP src) throws Exception {
            // MOVE(e, TEMP(t1)) 
            Temp t =  munchExp(dst); 
            emit(new assem.MOVE("MOVED "+t+" <- "+src.temp, t,src.temp));
    }
    
    private void munchMove(MEM dst, Exp src) throws Exception {
            // MOVE(MEM(BINOP(PLUS, e1, CONST(i))), e2)
            if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop==BINOP.PLUS
            && ((BINOP)dst.exp).right instanceof CONST)
            {
                    TempList d = new TempList(munchExp(((BINOP)dst.exp).left),null);
                    
                    TempList s = new TempList(munchExp(src),null);
                    
                    emit(new assem.OPER("STORE M["+d.getHead()+" + "+((CONST)((BINOP)dst.exp).right).value + " ] <- "+s.getHead(), d, s,null));
            }
            
            // MOVE(MEM(BINOP(PLUS, CONST(i), e1)), e2)
            else if (dst.exp instanceof BINOP && ((BINOP)dst.exp).binop==BINOP.PLUS
            && ((BINOP)dst.exp).left instanceof CONST)
            {
                    TempList d = new TempList(munchExp(((BINOP)dst.exp).right),null);
                    
                    TempList s = new TempList(munchExp(src),null);
                                            
                    emit(new assem.OPER("STORE M["+d.getHead()+" + " + ((CONST)((BINOP)dst.exp).left).value + "] <- "+s.getHead(),d, s,null));
                    
            }
            
            // MOVE(MEM(e1), MEM(e2))
            else if (src instanceof MEM)
            {
                    TempList d = new TempList(munchExp(dst.exp),null);
                    
                    TempList s = new TempList( munchExp(((MEM) src).exp),null);
                    
                    emit(new assem.OPER("MOVEM M["+d.getHead()+"] <- M["+s.getHead()+"]", d, s, null));
            }
            
            //MOVE(MEM(CONST(i)),e2)
            else if(dst.exp instanceof MEM && ((MEM)dst.exp).exp instanceof CONST){

                    TempList s = new TempList(munchExp(src), null);
                    TempList d = new TempList(frame.RV(), null);
                    
                    emit(new assem.OPER("STORE M["+frame.RV()+((CONST)((MEM)dst.exp).exp).value + "] <- "+s.getHead(),d,s,null));
            }
            
            // MOVE(MEM(e1), e2)
            else
            {
                    TempList d = new TempList(munchExp(dst.exp),null);
                    
                    TempList s = new TempList(munchExp(src),null);
                    
                    emit(new assem.OPER("STORE M["+d.getHead()+" + 0] <- "+s.getHead(), d, s, null));
            }
            
    }


    private Temp munchCall(CALL c)
    {
            return null;
            /*Temp r = munchExp(((CALL)exp.exp).func); 
            TempList l = munchArgs(0,exp.args);
            
            emit(new OPER("CALL 's0\n",calldefs,L(r,l)));*/
    }
    
    public Temp munchExp(tree.Exp e) throws Exception {
            if(e instanceof MEM) return munchMem((MEM)e);
            if(e instanceof BINOP) return munchBinop((BINOP)e);
            if(e instanceof CONST) return munchConst((CONST)e);
            if(e instanceof CALL) return munchCall((CALL)e);
            else return munchTemp((TEMP) e);
    }
    
    private Temp munchTemp(TEMP e) {
            return e.temp;
    }

    private Temp munchConst(CONST e) {
            // CONST(i)
            Temp r = new Temp();
            TempList d = new TempList(r,null);
            TempList s = new TempList(frame.RV(),null);
            
            emit(new OPER("ADDI "+r+" <- "+frame.RV()+" + "+e.value, d, s, null));
            return r;
    }

    private Temp munchBinop(BINOP e) throws Exception {
            // BINOP(PLUS, e1, CONST(i))
            if(e.binop == BINOP.PLUS && e.right instanceof CONST){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(e.left),null);
                    
                    emit(new OPER("ADDI "+r+" <- "+s.getHead()+" + " + ((CONST)e.right).value, d, s));
                    return r;
            }
            // BINOP(PLUS, CONST(i), e1)
            else if(e.binop == BINOP.PLUS && e.left instanceof CONST){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(e.right),null);
                    
                    emit(new OPER("ADDI "+r+" <- "+s.getHead()+" + " + ((CONST)e.left).value, d, s));
                    return r;
            }
            // BINOP(ADD, e1, e2)
            else if(e.binop == BINOP.PLUS){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(e.left),new TempList(munchExp(e.right),null));
                    
                    emit(new OPER("ADD "+r+" <- "+s.getHead()+" + "+s.getTail(), d, s, null));
                    return r;
            }               
            // BINOP(MINUS, e1, CONST(i))
            else if(e.binop == BINOP.MINUS && e.right instanceof CONST){
                            Temp r = new Temp();
                            TempList d = new TempList(r,null);
                            TempList s = new TempList( munchExp(e.left),null);
                            
                            emit(new OPER("SUBI "+r+" <- "+s.getHead()+"-" + ((CONST)e.right).value, d, s, null));
                            return r;
                    }
            // BINOP(MINUS, e1, e2)
            else if(e.binop == BINOP.MINUS){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(e.left),new TempList(munchExp(e.right),null));
                    
                    emit(new OPER("SUB "+r+" <- "+s.getHead()+" - "+s.getTail(), d, s, null));
                    return r;
            }
            
            // BINOP(MUL, e1, e2)
            else{
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(e.left),new TempList(munchExp(e.right),null));
                    
                    emit(new OPER("MUL "+r+" <- "+s.getHead()+" * "+s.getTail(), d, s, null));
                    return r;
            }
    }

    private Temp munchMem(MEM e) throws Exception {
            // MEM(BINOP(PLUS, e1, CONST(i)))
            if(e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS && 
                            ((BINOP)e.exp).right instanceof CONST ){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(munchExp(((BINOP)e.exp).left),null);
                    
                    emit(new OPER("LOAD "+r+" <- M["+s.getHead()+" + "+((CONST)((BINOP)e.exp).right).value+ "]", d, s));
                    return r;
            }
            // MEM(BINOP(PLUS, CONST(i), e1))
            else if(e.exp instanceof BINOP && ((BINOP)e.exp).binop == BINOP.PLUS && 
                            ((BINOP)e.exp).left instanceof CONST){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList( munchExp(((BINOP)e.exp).right),null);
                    
                    emit(new OPER("LOAD "+r+" <- M["+s.getHead()+" + "+((CONST)((BINOP)e.exp).left).value+ "]", d, s, null));
                    return r;
            } 
            // MEM(CONST(i))
            else if(e.exp instanceof CONST){
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    TempList s = new TempList(frame.RV(),null);
                    
                    emit(new OPER("LOAD "+r+" <- M["+frame.RV()+" + "+ ((CONST)e.exp).value +"]", d, s, null));
                    return r;
            }
            // MEM(e1)
            else{
                    Temp r = new Temp();
                    
                    TempList d = new TempList(r,null);
                    
                    TempList s = new TempList(frame.RV(),new TempList(munchExp(e.exp),null));
                    
                    emit(new OPER("LOAD "+r+" <- M["+frame.RV()+" + "+s.getHead()+"]", d, s, null));
                    return r;
            }
            
    }

    public ArrayList<Instr> codegen(List<Stm> s) throws Exception {
            ArrayList<Instr> l;
            ilist= new ArrayList<Instr>();
            for(List<Stm> st = s; st != null; st = st.tail) munchStm(st.head);
            l=ilist;
            ilist=null;
            return l;
    }
    
}
