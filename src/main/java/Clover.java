/**
 * Starts the Clover chatbot application.
 */

import java.util.Scanner;

public class Clover {
    public static void main(String[] args) {
        String banner = "  _____    _         ____    __      __   ______    _____\n"
                + " / ____|  | |       / __ \\   \\ \\    / /  |  ____|  |  __ \\\n"
                + "| |       | |      | |  | |   \\ \\  / /   | |__     | |__) |\n"
                + "| |       | |      | |  | |    \\ \\/ /    |  __|    |  _  /\n"
                + "| |____   | |____  | |  | |     \\  /     | |____   | | \\ \\\n"
                + " \\_____|  |______|  \\____/       \\/      |______|  |_|  \\_\\\n";
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];

        int taskIndex = 0;
        System.out.println("____________________________________________________________");
        System.out.println(banner);

        System.out.println("Hello! I'm Clover.");
        System.out.println("What can I do for you?\n");
        System.out.println("____________________________________________________________");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            // "list" command - outputs the list
            if (input.equals("list")) {
                System.out.println("""
                        ____________________________________________________________
                        Here are the tasks in your list:""");
                for (int taskCount = 0; taskCount < 100; taskCount++) {
                    if (tasks[taskCount] == null) {
                        break;
                    }
                    System.out.println( (taskCount + 1) + "." + getCheckBox(isDone[taskCount]) + " " + tasks[taskCount]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                isDone[index] = false;
                System.out.println("""
                        ____________________________________________________________
                        OK, I've marked this task as not done yet:""");
                System.out.println(" " + getCheckBox(isDone[index]) + " " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                isDone[index] = true;
                System.out.println("""
                        ____________________________________________________________
                        Nice! I've marked this task as done:""");
                System.out.println(" " + getCheckBox(isDone[index]) + " " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.equals("bye")) { // "bye" command - exits program
                System.out.println("""
                                Bye. Hope to see you again soon!
                                ____________________________________________________________""");
                break;
            } else { // add to list
                System.out.println("____________________________________________________________");
                tasks[taskIndex] = input;
                taskIndex++;
                System.out.println("added: " + input);
                System.out.println("____________________________________________________________");
            }
        }
    }

    private static String getCheckBox(boolean done) {
        return done ? "[X]" : "[ ]";
    }
}
