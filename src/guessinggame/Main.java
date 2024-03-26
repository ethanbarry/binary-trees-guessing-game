package guessinggame;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        System.out.println("Welcome!");

        // Set up our initial questions.
        var tree = new Tree();
        tree.root = new Node("Are you a mammal?");
        tree.root.no = new Node("chicken");
        tree.root.yes = new Node("elephant");

        var currentNode = tree.root;

        boolean playing = true;
        char response;
        while (playing) {
            // This loop MUST update currentNode every iteration.
            if (currentNode != null) {
                // Check whether this node is a leaf. If it is,
                //    we should handle it differently.
                if (currentNode.isLeaf()) {
                    System.out.print("Are you a(n) " + currentNode.question + "? (y/n/q): ");
                    response = scanner.next().toLowerCase().charAt(0);

                    switch (response) {
                        case 'y' -> {
                            System.out.println("Hey, I got it right!");
                            System.out.println("++++++++++++++++++++++++");
                            System.out.println("Wanna play again? (y/n): ");
                            var cont = scanner.next().toLowerCase().charAt(0);
                            if (cont == 'n') {
                                playing = false;
                            }
                            
                            currentNode = tree.root;
                        }
                        case 'n' -> {
                            scanner.nextLine();
                            System.out.print("I give up; what are you? (Enter your animal's name): ");
                            var newAnimal = scanner.nextLine();
                            System.out.print("What is a question that distinguishes you from a(n) "
                                    + currentNode.question + "? (Enter a question ending with '?'.): "
                            );
                            var newQuestion = scanner.nextLine();
                            System.out.print("And what is the correct response to your question, given your animal? (y/n): ");
                            var newResponse = scanner.next().toLowerCase().charAt(0);

                            var tempQuestion = currentNode.question;
                            currentNode.question = newQuestion;
                            switch (newResponse) {
                                case 'n' -> {
                                    // Answer to our new question is 'no'.
                                    // We insert the new animal in the no slot, and
                                    // the old animal in the yes slot.
                                    currentNode.no = new Node(newAnimal);
                                    currentNode.yes = new Node(tempQuestion);
                                }
                                case 'y' -> {
                                    // Same logic, but vice versa.
                                    currentNode.no = new Node(tempQuestion);
                                    currentNode.yes = new Node(newAnimal);
                                }
                                default -> {
                                    // We could handle this with an input loop, but that'd be messy.
                                    // Instead, we'll end the program.
                                    System.out.println("Not a valid response! Terminating program.");
                                    System.exit(0);
                                }
                            }
                            
                            System.out.println("++++++++++++++++++++++++");
                            System.out.print("Wanna play again? (y/n): ");
                            var cont = scanner.next().toLowerCase().charAt(0);
                            if (cont == 'n') {
                                playing = false;
                            }
                            currentNode = tree.root; // Reset our global state.
                        }
                        case 'q' -> {
                            playing = false;
                        }
                    }
                } else {
                    // Handling the case that current is NOT a leaf.
                    System.out.print(currentNode.question + " (y/n/q): ");
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
