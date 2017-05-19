/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wiki.parser;

/**
 * @author Reuben-PC
 * @param <K>
 * @param <V> 
 * Templated pair class
 */
public class Pair<K, V> {
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
