package guessinggame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /* This holds our file name. It has a default value,
     * which the user can change in the setup loop below.
     */
    private static String outFile = "guessinggame.csv";

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        System.out.println("Welcome!");

        // Set up our initial questions.
        var tree = new Tree();
        var setup = true;
        while (setup) {
            System.out.print("Do you have a save file to load? (y/n/q): ");
            var response = scanner.next().toLowerCase().charAt(0);
            switch (response) {
                // The user has a save file. Load and import it, and go on
                // to the next loop.
                case 'y' -> {
                    System.out.print("Enter the filename (no whitespace): ");
                    var filename = scanner.next();
                    outFile = filename.trim();

                    tree.root = importTree();
                    setup = false;
                }
                // The user does not have a save file. Set up a minimal game
                // and go on to the next loop.
                case 'n' -> {
                    tree.root = new Node("Are you a mammal?");
                    tree.root.no = new Node("chicken");
                    tree.root.yes = new Node("elephant");
                    setup = false;
                }
                // Exit the game.
                case 'q' -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                // Invalid choice; repeat menu.
                default -> {
                    break;
                }
            }
        } // End setup loop.

        var currentNode = tree.root; // Begin at the root.

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
                            System.out.print("Wanna play again? (y/n): ");
                            var cont = scanner.next().toLowerCase().charAt(0);
                            if (cont == 'n') {
                                playing = false;
                            }

                            currentNode = tree.root;
                        }
                        case 'n' -> {
                            // Our guess was incorrect, and we need to ask the
                            // player what they were.
                            scanner.nextLine();
                            System.out.print("I give up; what are you? (Enter your animal's name): ");
                            var newAnimal = scanner.nextLine().replace(',', ' ');
                            
                            System.out.print("What is a question that distinguishes you from a(n) "
                                    + currentNode.question + "? (Enter a question ending with '?'.): "
                            );
                            var newQuestion = scanner.nextLine().replace(',', ' ');
                            
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
                                    System.out.println("Not a valid response!");
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
                        default -> {
                            System.out.println("Not a valid response!");
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
                        default -> {
                            System.out.println("Not a valid response!");
                        }
                    }
                }
            }
        }

        exportTree(tree);
        System.out.println("Goodbye!");
    }

    public static Node importTree() {
        var list = new ArrayList<Node>();
        
        // Initialize ArrayList before inserting to random indices.
        for (int i = 0; i < 1_000; i++) {
            list.add(null);
        }

        try {
            var freader = new FileReader(outFile);
            var breader = new BufferedReader(freader);

            var line = breader.readLine();
            while (line != null) {
                var data = line.split(",");
                var node = new Node(data[1]);
                list.set(Integer.parseInt(data[0]), node);

                line = breader.readLine();
            }

            breader.close();
            freader.close();
        } catch (IOException e) {
            System.out.println("Failed to import from: " + outFile);
            System.out.println("Attempted in directory: " + System.getProperty("user.dir"));
            System.out.println("\tError follows: ");
            System.out.println(e.toString());
            System.out.println("");
            System.out.println("Terminating program.");
            System.exit(1);
        }

        var node = list.get(1);
        buildTree(1, node, list);

        return node;
    }

    public static void buildTree(int idx, Node node, ArrayList<Node> list) {
        if (idx * 2 + 1 <= list.size()) {
            if (list.get(idx * 2) != null) {
                node.yes = list.get(idx * 2);
                buildTree(idx * 2, node.yes, list);
            }
            if (list.get(idx * 2 + 1) != null) {
                node.no = list.get(idx * 2 + 1);
                buildTree(idx * 2 + 1, node.no, list);
            }
        }
    }

    public static void exportTree(Tree tree) {
        try {
            var fwriter = new FileWriter(outFile);
            writeTree(tree.root, fwriter, 1);
            System.out.println("Exported file: " + outFile);
            fwriter.close();
        } catch (IOException e) {
            System.out.println("Failed to export to: " + outFile);
            System.out.println("Attempted in directory: " + System.getProperty("user.dir"));
            System.out.println("\tError follows: ");
            System.out.println(e.toString());
        }
    }

    private static void writeTree(Node root, FileWriter fwriter, int i) {
        if (!root.isLeaf()) {
            writeTree(root.yes, fwriter, i * 2);
            writeTree(root.no, fwriter, i * 2 + 1);
        }

        try {
            var out = i + "," + root.question + "\n";
            fwriter.write(out);
        } catch (IOException e) {
            System.out.println("Unexpected file error mid-execution!");
            System.out.println("Something is broken here.");
        }
    }

}
