package test.wrappers;

import java.util.ArrayList;
import java.util.Collection;

public class LJList<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1L;

    public LJList() {
        super();
    }

    public LJList(Collection<T> collection) {
        super(collection);
    }

    public void push(T element) {
        this.add(element);
    }

    public void enqueue(T element) {
        this.add(element);
    }

    public T pop() {
        return this.remove(this.size() - 1);
    }

    public T dequeue() {
        return this.remove(0);
    }

    public T set(int index, T element) {
        return super.set(index, element);
    }

    public void insert(int index, T element) {
        this.add(index, element);
    }

    public void removeAt(int index) {
        this.remove(index);
    }
}
