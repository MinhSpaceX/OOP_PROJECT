package com.zeus.utils.stackset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Data structure has attribute of a stack and set.
 *
 * @param <T> Type of object.
 */
public class StackSet<T> extends ArrayList<T> {
    private final Set<T> set = new HashSet<>();

    /**
     * Add method.
     *
     * @param t element whose presence in this collection is to be ensured
     * @return true if added.
     * <p>false if not.</p>
     */
    @Override
    public boolean add(T t) {
        if (!set.add(t)) {
            super.remove(t);
        }
        super.add(0, t);
        return true;
    }
}
