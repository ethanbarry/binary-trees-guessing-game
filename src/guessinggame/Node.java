package guessinggame;

public class Node {

    String question;
    Node yes;
    Node no;
    
    public boolean isLeaf() {
        if (this.no == null && this.yes == null) {
            return true;
        } else {
            return false;
        }
    }

    public Node(String q) {
        this.question = q;
        this.yes = null;
        this.no = null;
    }

    @Override
    public String toString() {
        return "Node { question: " + this.question
                + " yes: " + this.yes.toString() + " no: "
                + this.no.toString() + " }.";
    }
}
