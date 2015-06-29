package util;

import java.util.Stack;

public class List<E> {
	public E head;
	public List<E> tail;

	public List(E h, List<E> t) {
		if (h == null)
			throw new Error();

		head = h;
		tail = t;
	}

	public int size() {
		if (tail == null)
			return 1;

		return 1 + tail.size();
	}

	public E get(int n) throws Exception {
		int i = 0;
		E h = head;
		List<E> t = tail;
		while (i <= n) {
			if (h == null)
				throw new IndexOutOfBoundsException();
			if (i == n)
				return h;
			else {
				h = t.head;
				t = t.tail;
			}
			i++;
		}
		throw new IndexOutOfBoundsException();
	}

	public void set(int n, E newuse) {
		int i = 0;
		List<E> t = this;
		Stack<E> s = new Stack<E>();
		int tam = size();
		while (i <= tam) {
			if (i == n)
				s.push(newuse);
			else {
				s.push(t.head);
			}
			t = t.tail;
			i++;
		}
		reconstruct(s, this);
	}

	public void reconstruct(Stack<E> s, List<E> l) {
		if (!s.isEmpty()) {
			l.head = s.pop();
			reconstruct(s, l.tail);
		}
	}

}