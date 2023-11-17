package com.zeus.utils.stackset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class StackSet <T> extends ArrayList<T> {
    private final Set<T> set = new HashSet<>();

    @Override
    public boolean add(T t) {
        if (!set.add(t)) {
            super.remove(t);
        }
        super.add(0, t);
        return true;
    }

    public boolean addFromFile(T t) {
        if (!set.add(t)) {
            super.remove(t);
        }
        super.add(t);
        return true;
    }
}
