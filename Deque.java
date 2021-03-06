/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

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

        if (item == null) {
            throw new IllegalArgumentException();
        }

        // create new node and set values
        Node newFirst = new Node();
        newFirst.item = item;
        if (this.isEmpty()) {
            newFirst.next = null;
            newFirst.previous = null;
            this.last = newFirst;
        }
        else {
            newFirst.previous = null;
            newFirst.next = this.first;
            this.first.previous = newFirst;
        }

        this.first = newFirst;
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }

        // create new node and set values
        Node newLast = new Node();
        newLast.item = item;
        if (this.isEmpty()) {
            newLast.previous = null;
            newLast.next = null;
            this.first = newLast;
        }
        else {
            newLast.previous = this.last;
            newLast.next = null;
            this.last.next = newLast;
        }

        // Update global values
        this.last = newLast;
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {

        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        // get item
        Item item = this.first.item;

        if (size == 1) {
            this.first = null;
            this.last = null;
        }
        else {
            Node newFirst = this.first.next;
            if (newFirst.previous != null) { // last item in deque has previous = null
                newFirst.previous = null;
            }
            this.first = newFirst;
        }

        this.size--;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {

        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = this.last.item;

        if (size == 1) {
            this.first = null;
            this.last = null;
        }
        else {
            Node newLast = this.last.previous;
            if (newLast.next != null) { // last item in deque has previous = null
                newLast.next = null;
            }
            this.last = newLast;
        }

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
            if (this.current == null) {
                return false;
            }
            return true;
        }

        public Item next() {
            if (this.current == null) {
                throw new NoSuchElementException();
            }

            if (this.current.next == null) {
                Item item = this.current.item;
                this.current = null;
                return item;
            }
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
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

        namesIterator = names.iterator();
        while (namesIterator.hasNext()) {
            StdOut.println("Name: " + namesIterator.next());
        }
    }
}
