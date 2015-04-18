import tester.Tester;

abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;
    ANode(ANode<T> next, ANode<T> prev) {
        this.next = next;
        this.prev = prev;
    }
    // add one for every node 
    abstract int size();
    // return the first node that return true
    abstract ANode<T> find(IPred<T> pred);
    // return true if this is a sentinel
    abstract boolean isSentinel();
    // removes specified node
    abstract void removeNode(ANode<T> node);
    // changes this' next and the next's prev
    void updateNext(ANode<T> node) {
        this.next = node;
    }
    // changes this' prev and the prev's next
    void updatePrev(ANode<T> node) {
        this.prev = node;
    }
}

class Sentinel<T> extends ANode<T> {
    Sentinel() {
        super(null, null);
        this.prev = this;
        this.next = this;
    }
    // recurses for size
    int sizeHelper() {
        return this.next.size();
    }
    // base case for size
    int size() {
        return 0;
    }
    // returns first node that returns true for pred
    ANode<T> find(IPred<T> pred) {
        // checks for empty list
        if (this.next == this) {
            return this;
        }
        // starts find on list
        else { 
            return this.next.find(pred);
        }
    }
    boolean isSentinel() {
        return true; //this is a sentinel
    }
    //removes specified node
    void removeNode(ANode<T> node) {
        // checks if node is a sentinel
        if (!node.isSentinel() && !this.next.isSentinel()) {
            this.next.removeNode(node);
        }
    }
    // places node at specified place
    void addAt(T t, ANode<T> next, ANode<T> prev) {
        new Node<T>(t, next, prev);
    }
    // removes specified node
    T removeFrom(ANode<T> node) {
        if (this.next.isSentinel()) {
            throw new IllegalArgumentException("ur trying to remove from an empty list");
        }
        else {
            this.removeNode(node);
            return ((Node<T>)node).data;
        }
    }
}
// a node
class Node<T> extends ANode<T> {
    T data;
    // this constructer gives node null for next and prev
    Node(T data) {
        super(null, null);
        this.data = data;
    }   
    // this constructer assigns specific nexts and prevs
    Node(T data, ANode<T> next, ANode<T> prev) {
        super(next, prev);
        this.data = data;
        //checks if given node is null
        if (next == null || prev == null) {
            throw new IllegalArgumentException("yo your node is null bruh");
        }
        else {
            next.prev = this;
            prev.next = this;
        }
    }
    //adds one to total size
    int size() {
        return 1 + this.next.size();
    }
    // checks if this returns true for pred
    ANode<T> find(IPred<T> pred) {
        if (pred.apply(this.data)) { // checks if this matches
            return this;
        }
        if (this.next.isSentinel()) { //checks for end of list
            return this.next;
        }
        else { //else, continue finding
            return this.next.find(pred);
        }
    }
    boolean isSentinel() {
        return false; // this is not a sentinel
    }
    // reomves specific node
    void removeNode(ANode<T> node) {
        if (this.sameNode((Node<T>)node)) { // checks if this is the node
            this.prev.updateNext(this.next); // if so update the pointers on next and prev
            this.next.updatePrev(this.prev);
        }
        else if (!this.next.isSentinel()) {
            this.next.removeNode(node); // else continue
        }
    }
    boolean sameNode(Node<T> node) {
        return  this.data == node.data && 
                this.next == node.next && 
                this.prev == node.prev;
    }
}
// a deque
class Deque<T> {
    Sentinel<T> header;
    //new header
    Deque() {
        this.header = new Sentinel<T>();
    }
    //specific header
    Deque(Sentinel<T> header) {
        this.header = header;
    }
    // calls the helper
    int size() {
        return this.header.next.size();
    }
    // calls helper with specific positions
    void addAtHead(T t) {
        this.header.addAt(t, header.next, header);
    }
    //calls helper with specific positions
    void addAtTail(T t) {
        this.header.addAt(t, header, header.prev);
    }
    // removes node with specific positions
    T removeFromHead() {
        return this.header.removeFrom(this.header.next);
    }
    // removes node with specific positions
    T removeFromTail() {
        return this.header.removeFrom(this.header.prev);
    }
    // calls helper on header
    ANode<T> find(IPred<T> pred) {
        return this.header.find(pred);
    }
    // removes specific node
    void removeNode(ANode<T> node) {
        this.header.removeNode(node);
    }
}

//Represents a boolean-valued question over values of type T
interface IPred<T> {
    boolean apply(T t);
}

//checks if string node equals "abc"
class IsABC implements IPred<String> {
    public boolean apply(String s) {
        return (s.equals("abc"));
    }
}

//checks for even int in node
class IsEven implements IPred<Integer> {
    public boolean apply(Integer i) {
        return (i % 2) == 0 ;
    }
}

class ExamplesDequeue {
    Sentinel<String> sen1 = new Sentinel<String>();
    Deque<String> deque1 = new Deque<String>(sen1);
    ANode<String> abc = new Node<String>("abc", deque1.header, deque1.header);
    ANode<String> bcd = new Node<String>("bcd", deque1.header, abc);
    ANode<String> cde = new Node<String>("cde", deque1.header, bcd);
    ANode<String> def = new Node<String>("def", deque1.header, cde);
    
    Sentinel<Integer> sen2 = new Sentinel<Integer>();
    Deque<Integer> deque2 = new Deque<Integer>(sen2);
    ANode<Integer> one = new Node<Integer>(1, deque2.header, deque2.header);
    ANode<Integer> two = new Node<Integer>(2, deque2.header, one);
    ANode<Integer> three = new Node<Integer>(3, deque2.header, two);
    ANode<Integer> four = new Node<Integer>(4, deque2.header, three); 
    
    Deque<String> deque3 = new Deque<String>();
    
    Deque<String> deque4 = new Deque<String>();
    ANode<String> bcd1 = new Node<String>("bcd", deque4.header, deque4.header);
    ANode<String> cde1 = new Node<String>("cde", deque4.header, bcd1);
    ANode<String> def1 = new Node<String>("def", deque4.header, cde1);
    
    Deque<String> deque5 = new Deque<String>(sen1);
    ANode<String> abc2 = new Node<String>("abc", deque1.header, deque1.header);
    ANode<String> bcd2 = new Node<String>("bcd", deque1.header, abc);
    ANode<String> cde2 = new Node<String>("cde", deque1.header, bcd);
    
    void initializeData() {
        this.sen1 = new Sentinel<String>();
        this.deque1 = new Deque<String>(sen1);
        this.abc = new Node<String>("abc", deque1.header, deque1.header);
        this.bcd = new Node<String>("bcd", deque1.header, abc);
        this.cde = new Node<String>("cde", deque1.header, bcd);
        this.def = new Node<String>("def", deque1.header, cde);
        
        this.sen2 = new Sentinel<Integer>();
        this.deque2 = new Deque<Integer>(sen2);
        this.one = new Node<Integer>(1, deque2.header, deque2.header);
        this.two = new Node<Integer>(2, deque2.header, one);
        this.three = new Node<Integer>(3, deque2.header, two);
        this.four = new Node<Integer>(4, deque2.header, three); 
        
        this.deque3 = new Deque<String>();
        
        this.deque4 = new Deque<String>();
        this.bcd1 = new Node<String>("bcd", deque4.header, deque4.header);
        this.cde1 = new Node<String>("cde", deque4.header, bcd1);
        this.def1 = new Node<String>("def", deque4.header, cde1);
        
        this.deque5 = new Deque<String>(sen1);
        this.abc2 = new Node<String>("abc", deque1.header, deque1.header);
        this.bcd2 = new Node<String>("bcd", deque1.header, abc);
        this.cde2 = new Node<String>("cde", deque1.header, bcd);
    }
    
    boolean testSize(Tester t) {
        this.initializeData();
        return t.checkExpect(deque4.size(), 3) &&
                t.checkExpect(deque3.size(), 0);
    }
    boolean testFind(Tester t) {
        this.initializeData();
        return t.checkExpect(deque1.find(new IsABC()), abc) &&
                t.checkExpect(deque2.find(new IsEven()), two);
    }
    boolean testIsSentinel(Tester t) {
        this.initializeData();
        return t.checkExpect(sen1.isSentinel(), true) &&
                t.checkExpect(abc.isSentinel(), false);
    }
    boolean testRemoveNode(Tester t) {
        this.initializeData();
        deque1.removeNode(abc);
        return t.checkExpect(deque1, deque4);
    }
    boolean testAddAtHead(Tester t) {
        this.initializeData();
        deque4.addAtHead("abc");
        return t.checkExpect(deque4, deque1);
    }
    boolean testAddAtTail(Tester t) {
        this.initializeData();
        deque5.addAtHead("def");
        return t.checkExpect(deque5, deque1);
    }
    boolean testRemoveFromTail(Tester t) {
        this.initializeData();
        return t.checkExpect(deque1.removeFromTail(), "cde");
    }
    boolean testRemoveFromHead(Tester t) {
        this.initializeData();
        return t.checkExpect(deque1.removeFromHead(), "abc");
    }
}