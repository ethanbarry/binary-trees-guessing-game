package guessinggame;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        System.out.println("Welcome!");

        // Set up our initial questions.
        var tree = new Tree();
        tree.root = new Node("Are you a mammal?");

        var currentNode = tree.root;

        boolean playing = true;
        var response = 'Q';
        while (playing) {
            // This loop MUST update currentNode every iteration.
            if (currentNode != null) {
                // Check whether this node is a leaf. If it is,
                //    we should handle it differently.
                if (currentNode.isLeaf()) {
                    System.out.println("Are you a(n) " + currentNode.question + "? (y/n/q): ");
                    response = scanner.next().toLowerCase().charAt(0);

                    switch (response) {
                        case 'y' -> {
                            System.out.println("Hey, I got it right!");
                            System.out.println("Wanna play again? (y/n): ");
                            var cont = scanner.next().toLowerCase().charAt(0);
                            if (cont == 'n' || cont == 'N') { playing = false; }
                        }
                        case 'n' -> {
                            
                        }
                        case 'q' -> {
                            playing = false;
                        }
                    }
                } else {
                    // Handling the case that current is NOT a leaf.
                    System.out.println(currentNode.question + " (y/n/q): ");
                    response = scanner.next().toLowerCase().charAt(0);

                    switch (response) {
                        case 'y' -> {
                            currentNode = currentNode.yes;
                        }
                        case 'n' -> { // TRICKY PART.
                            currentNode = currentNode.no;
                        }
                        case 'q' -> {
                            playing = false;
                        }
                    }
                    
                }
            }
        }
        
        System.out.println("Goodbye!");
    }

}
