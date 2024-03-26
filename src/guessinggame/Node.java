package guessinggame;

public class Node {

    String question;
    Node yes;
    Node no;
    
    public boolean isLeaf() {
        return this.no == null && this.yes == null;
    }

    public Node(String q) {
        this.question = q;
        this.yes = null;
        this.no = null;
    }

    @Override
    public String toString() {
        return "Node { question: " + this.question + " }.";
    }
}
