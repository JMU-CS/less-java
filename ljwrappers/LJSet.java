package wrappers;

import java.util.Collection;
import java.util.HashSet;

public class LJSet<T> extends HashSet<T> {
    private static final long serialVersionUID = 1L;

    public LJSet() {
        super();
    }

    public LJSet(Collection<T> collection) {
        super(collection);
    }
}
