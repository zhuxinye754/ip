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
        Task[] tasks = new Task[100];
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
                    System.out.println((taskCount + 1)+ "." + tasks[taskCount]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[index].markAsUndone();
                System.out.println("""
                        ____________________________________________________________
                        OK, I've marked this task as not done yet:""");
                System.out.println(" " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[index].markAsDone();
                System.out.println("""
                        ____________________________________________________________
                        Nice! I've marked this task as done:""");
                System.out.println(" " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                tasks[taskIndex] = new ToDo(input.substring(5).trim());
                System.out.println("""
                        ____________________________________________________________
                        Got it. I've added this task:""");
                System.out.println(" " + tasks[taskIndex]);
                System.out.println("Now you have " + (taskIndex + 1) + " in the list.");
                System.out.println("____________________________________________________________");
                taskIndex++;
            } else if (input.startsWith("deadline ")) {
                String target = "/by";
                int index = input.indexOf(target);
                if (index == -1) {
                    System.out.println("Please use the format: deadline DESCRIPTION /by DUE DATE");
                    continue;
                }
                String endBy = input.substring(index + target.length());
                String desc = input.substring(9, index).trim();
                tasks[taskIndex] = new Deadline(desc, endBy);
                System.out.println("""
                        ____________________________________________________________
                        Got it. I've added this task:""");
                System.out.println(" " + tasks[taskIndex]);
                System.out.println("Now you have " + (taskIndex + 1) + " in the list.");
                System.out.println("____________________________________________________________");
                taskIndex++;
            } else if (input.startsWith("event ")) {
                int fromIndex = input.indexOf("/from");
                int toIndex = input.indexOf("/to");
                if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
                    System.out.println("Please use the format: event DESCRIPTION /from START /to END");
                    continue;
                }
                String fromDesc = input.substring(fromIndex + 5, toIndex).trim();
                String toDesc = input.substring(toIndex + 3).trim();
                String desc = input.substring(6, fromIndex).trim();
                tasks[taskIndex] = new Event(desc, fromDesc, toDesc);
                System.out.println("""
                        ____________________________________________________________
                        Got it. I've added this task:""");
                System.out.println(" " + tasks[taskIndex]);
                System.out.println("Now you have " + (taskIndex + 1) + " in the list.");
                System.out.println("____________________________________________________________");
                taskIndex++;
            } else if (input.equals("bye")) { // "bye" command - exits program
                System.out.println("""
                                Bye. Hope to see you again soon!
                                ____________________________________________________________""");
                break;
            } else { // add to list
                System.out.println("____________________________________________________________");
                Task t = new Task(input);
                tasks[taskIndex] = t;
                System.out.println(tasks[taskIndex]);
                taskIndex++;
                System.out.println("____________________________________________________________");
            }
        }
    }
}
