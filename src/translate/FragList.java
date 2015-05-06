package translate;

import java.util.Iterator;
import java.util.LinkedList;

public class FragList implements Iterable<Frag> {
    private LinkedList<Frag> frags;

    public FragList(){
            frags = new LinkedList<Frag>();
    }
    
    public void add(Frag frag) {
            frags.add(frag);
    }

    
    public Iterator<Frag> iterator() {
            return frags.iterator();
    }

    public int size() {
            return frags.size();
    }

    public Frag get(int i) {
            return frags.get(i);
    }
}