/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.first == null;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {

        // create new node and set values
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.previous = null;
        newFirst.next = this.first;

        // Update former first node
        if (this.first != null) {
            this.first.previous = newFirst;
        }

        // Update global values
        this.first = newFirst;
        if (this.isEmpty()) {
            this.last = newFirst;
        }
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        // create new node and set values
        Node newLast = new Node();
        newLast.item = item;
        newLast.previous = this.last;
        newLast.next = null;

        // Update former last node
        if (this.last != null) {
            this.last.next = newLast;
        }

        // Update global values
        this.last = newLast;
        if (this.isEmpty()) {
            this.first = newLast;
        }
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // get item
        Item item = this.first.item;

        // set new first
        Node newFirst = this.first.next;
        newFirst.previous = null;
        this.first = newFirst;

        this.size--;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        Item item = this.last.item;

        Node newLast = this.last.previous;
        newLast.next = null;
        this.last = newLast;

        this.size--;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    // Private iterator class with methods hasNext and next()
    private class DequeIterator implements Iterator<Item> {

        private Node current;

        private DequeIterator(Node first) {
            this.current = first;
        }

        public boolean hasNext() {
            if (this.current.next != null) {
                return true;
            }
            return false;
        }

        public Item next() {
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
        }
    }

    // Private class node
    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // For unit testing, each public method and constructor must be called and the output printed to the terminal
        Deque<String> names = new Deque<String>();

        StdOut.println("Deque created");
        StdOut.println("isEmpty: " + names.isEmpty());
        StdOut.println("Size: " + names.size());
        names.addFirst("Jimmy");
        names.addFirst("Tim");
        names.addLast("Bob");
        StdOut.println("Three names added");
        StdOut.println("isEmpty: " + names.isEmpty());
        StdOut.println("Size: " + names.size());

        Iterator<String> namesIterator = names.iterator();
        while (namesIterator.hasNext()) {
            StdOut.println("Name: " + namesIterator.next());
        }

        StdOut.println("Remove first: " + names.removeFirst());
        StdOut.println("Remove last: " + names.removeLast());
        StdOut.println("Two names removed");

        StdOut.println("isEmpty: " + names.isEmpty());
        StdOut.println("Size: " + names.size());

        while (namesIterator.hasNext()) {
            StdOut.println("Name: " + namesIterator.next());
        }
    }
}
