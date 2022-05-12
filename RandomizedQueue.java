/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randomQueue;
    private int queueSize;
    private int queueCapacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.randomQueue = (Item[]) new Object[2];
        this.queueSize = 0;
        this.queueCapacity = 2;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        if (this.size() == 0) {
            return true;
        }
        return false;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.queueSize;
    }

    // add the item
    public void enqueue(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (this.queueSize == this.queueCapacity) {
            resize(this.queueCapacity * 2);
        }

        int insertPosition = this.nextNull();
        this.randomQueue[insertPosition] = item;

        this.queueSize++;
    }

    // remove and return a random item
    public Item dequeue() {

        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        if (this.queueSize == (this.queueCapacity / 4)) {
            resize(this.queueCapacity / 2);
        }

        Item item = null;
        while (item == null) {
            int popPosition = StdRandom.uniform(this.queueCapacity);
            item = this.randomQueue[popPosition];
            this.randomQueue[popPosition] = null;
        }

        this.queueSize--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {

        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = null;
        while (item == null) {
            int popPosition = StdRandom.uniform(this.queueCapacity);
            item = this.randomQueue[popPosition];
        }

        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(this.randomQueue, this.size());
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        int nonNullCount;
        Item[] queue;
        int capacity;

        private RandomizedQueueIterator(Item[] queue, int size) {
            this.queue = queue;
            this.nonNullCount = size;
            this.capacity = queue.length;
        }

        public boolean hasNext() {
            if (this.nonNullCount > 0) {
                return true;
            }
            return false;
        }

        public Item next() {
            if (this.nonNullCount < 1) {
                throw new NoSuchElementException();
            }
            Item item = null;
            while (item == null) {
                int popPosition = StdRandom.uniform(this.capacity);
                item = this.queue[popPosition];
                this.queue[popPosition] = null;
            }

            this.nonNullCount--;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int newCapacity) {
        Item[] copy = (Item[]) new Object[newCapacity];
        int j = 0;
        for (int i = 0; i < this.queueCapacity; i++) {
            if (this.randomQueue[i] != null) {
                copy[j] = this.randomQueue[i];
                j++;
            }
        }
        this.randomQueue = copy;
        this.queueCapacity = newCapacity;
    }

    private int nextNull() {
        for (int i = 0; i < this.queueCapacity; i++) {
            if (this.randomQueue[i] == null) {
                return i;
            }
        }
        int oldCapacity = this.queueCapacity;
        this.resize(this.queueCapacity * 2);
        return oldCapacity;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> RQ = new RandomizedQueue<String>();

        StdOut.println("RandomizedQueue created");
        StdOut.println("isEmpty: " + RQ.isEmpty());
        StdOut.println("Size: " + RQ.size());
        RQ.enqueue("Jimmy");
        RQ.enqueue("Tim");
        RQ.enqueue("Bob1");
        RQ.enqueue("Bob2");
        RQ.enqueue("Bob3");
        RQ.enqueue("Bob4");
        StdOut.println("Six names added");
        StdOut.println("isEmpty: " + RQ.isEmpty());
        StdOut.println("Size: " + RQ.size());

        Iterator<String> namesIterator = RQ.iterator();
        while (namesIterator.hasNext()) {
            StdOut.println("Name: " + namesIterator.next());
        }

        RQ.enqueue("Jimmy");
        RQ.enqueue("Tim");
        RQ.enqueue("Bob1");
        RQ.enqueue("Bob2");
        RQ.enqueue("Bob3");
        RQ.enqueue("Bob4");
        StdOut.println("Six names added");
        StdOut.println("isEmpty: " + RQ.isEmpty());
        StdOut.println("Size: " + RQ.size());

        StdOut.println("Sample name: " + RQ.sample());
        StdOut.println("Sample name: " + RQ.sample());
        StdOut.println("Sample name: " + RQ.sample());
        StdOut.println("isEmpty: " + RQ.isEmpty());
        StdOut.println("Size: " + RQ.size());

        StdOut.println("Dequeue name: " + RQ.dequeue());
        StdOut.println("Dequeue name: " + RQ.dequeue());
        StdOut.println("Dequeue name: " + RQ.dequeue());
        StdOut.println("Dequeue name: " + RQ.dequeue());
        StdOut.println("Dequeue name: " + RQ.dequeue());
        StdOut.println("Dequeue name: " + RQ.dequeue());

        StdOut.println("isEmpty: " + RQ.isEmpty());
        StdOut.println("Size: " + RQ.size());

        // namesIterator = names.iterator();
        // while (namesIterator.hasNext()) {
        //     StdOut.println("Name: " + namesIterator.next());
        // }
    }

}
