import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// Represents a collection that supports duplicates
public class Multiset {
    /* @citation Adapted from: https://algs4.cs.princeton.edu/code/edu/
     * princeton/cs/algs4/RedBlackBST.java. Accessed 3/30/2021 */

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;  // root of the tree

    private class Node {
        private final int key; // key
        private int frequency; // number of occurrences of key
        private Node left, right; // links to left and right subtrees
        private boolean color; // color of parent link
        private int size; // subtree count

        public Node(int key, boolean color) {
            this.key = key;
            this.color = color;
            this.frequency = 1;
        }
    }

    // is node x red?; false if x is null
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + h.frequency;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + h.frequency;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // size of the multiset
    public int size() {
        return size(root);
    }

    // return the keys in a given range
    public Iterable<Integer> keys(Integer lo, Integer hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Integer> queue = new Queue<>();
        keys(root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x to the queue
    private void keys(Node x, Queue<Integer> queue, Integer lo, Integer hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    // number of nodes in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    // adds a node to the multiset
    public void add(int key) {
        root = add(root, key);
        root.color = BLACK;
    }

    // adds a node to the subtree rooted at x
    private Node add(Node x, int key) {
        if (x == null) return new Node(key, RED);

        if (key < x.key) x.left = add(x.left, key);
        else if (key > x.key) x.right = add(x.right, key);
        else x.frequency++;

        if (isRed(x.right) && !isRed(x.left)) x = rotateLeft(x);
        if (isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left) && isRed(x.right)) flipColors(x);

        x.size = size(x.left) + size(x.right) + x.frequency;

        return x;
    }

    // returns the count of a given key
    public int count(int key) {
        Node x = root;
        while (x != null) {
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else return x.frequency;
        }
        return 0;
    }

    // number of keys strictly greater than key
    public int backrank(int key) {
        return backrank(key, root);
    }

    // number of keys strictly greater than key in the subtree rooted at x
    private int backrank(int key, Node x) {
        if (x == null) return 0;
        if (key > x.key) return backrank(key, x.right);
        else if (key < x.key) return size(x.right) + backrank(key, x.left) + x.frequency;
        else return size(x.right);
    }

    // rank of the key
    public int rank(int key) {
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(int key, Node x) {
        if (x == null) return 0;
        if (key < x.key) return rank(key, x.left);
        else if (key > x.key) return size(x.left) + rank(key, x.right) + x.frequency;
        else return size(x.left);
    }

    public static void main(String[] args) {
        Multiset multiset = new Multiset();
        for (int i = 0; i < 100000; i++) {
            multiset.add(i);
            if (multiset.rank(i) != i) {
                throw new IllegalArgumentException("Doesn't work");
            }
        }
        multiset = new Multiset();
        for (int i = 0; i < 100000; i++) {
            multiset.add(0);
            if (multiset.rank(1) != i + 1) {
                throw new IllegalArgumentException("Doesn't work");
            }
            if (multiset.backrank(i) != 0) {
                throw new IllegalArgumentException("Doesn't work");
            }
        }

        multiset = new Multiset();
        for (int i = 100000; i > 0; i--) {
            multiset.add(i);
            multiset.add(i);
            int backrank = multiset.backrank(i - 1);
            int shouldbe = (100001 - i) * 2;
            if (shouldbe != backrank) {
                StdOut.println(i);
                throw new IllegalArgumentException("Doesn't work");
            }
        }
    }
}
