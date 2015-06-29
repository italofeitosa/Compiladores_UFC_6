package temp;

public class TempList {
   public Temp head;
   public TempList tail;
   public TempList(Temp h, TempList t) {head=h; tail=t;}
public Temp getHead() {
	return head;
}
public void setHead(Temp head) {
	this.head = head;
}
public TempList getTail() {
	return tail;
}
public void setTail(TempList tail) {
	this.tail = tail;
}
   
   
}

