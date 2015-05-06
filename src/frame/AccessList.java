package frame;

import java.util.ArrayList;

import frame.Access;

@SuppressWarnings("serial")
public class AccessList extends ArrayList<Access> {
	
    public Access head;
    public AccessList tail;
    public AccessList(Access head, AccessList tail){
        this.head=head;
        this.tail=tail;
    }

}