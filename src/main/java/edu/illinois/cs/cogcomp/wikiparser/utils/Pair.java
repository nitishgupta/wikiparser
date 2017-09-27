package edu.illinois.cs.cogcomp.wikiparser.utils;

/**
 * @author Reuben-PC
 * @param <K>
 * @param <V> 
 * Templated pair class
 */
public class Pair<K, V> implements java.io.Serializable {
    private final K first;
    private final V second;

    public static <K, V> Pair<K, V> createPair(K first, V second) {
        return new Pair<K, V>(first, second);
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}

