package tree;

import tree.ExpList;

abstract public class Exp {
    abstract public ExpList kids();
    abstract public Exp build(ExpList kids);
}


