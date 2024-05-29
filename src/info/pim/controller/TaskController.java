package info.pim.controller;

import info.pim.model.Task;
import info.pim.service.PimDataService;

import java.util.List;
import java.util.Scanner;

/**
 * Task Controller
 */
public class TaskController {
    public static void executeCommand(Scanner sc, String command) {
        String[] commands = command.split(" ");
        if (commands.length >= 2) {
            if (commands[1].equals("list")) {
                list();
            } else if (commands[1].equals("add")) {
                add(sc);
            } else if (commands[1].equals("edit")) {
                edit(sc, commands);
            } else if (commands[1].equals("delete")) {
                delete(commands);
            }
        }
    }

    /**
     * Search
     */
    private static void list() {
        List<Task> tasks = PimDataService.tasks;
        System.out.println("task list:");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    /**
     * Add
     */
    private static void add(Scanner sc) {
        Task task = new Task();
        task.setId(PimDataService.idWorker.nextId());

        updateData(sc, task);
    }

    /**
     * Edit
     */
    private static void edit(Scanner sc, String[] commands) {
        if (commands.length < 3) {
            System.out.println("When executing the Edit command, please attach a unique id");
            return;
        }
        String id = commands[2];
        Task task = PimDataService.getTask(id);
        if (task == null) {
            System.out.println("The event does not exist！");
            return;
        }

        updateData(sc, task);
    }

    private static void updateData(Scanner sc, Task task) {
        System.out.println("Please input deadline(format as:yyyy-MM-dd，eg.2023-10-27)：");
        String deadline = sc.nextLine();
        task.setDeadline(deadline);
        System.out.println("Please input description(When input equals to [:quit] Quit the system)：");
        StringBuffer sbDescription = new StringBuffer();
        while (true) {
            String description = sc.nextLine();
            if (description.equals(":quit")) {
                break;
            }
            sbDescription.append(description);
        }
        task.setDescription(sbDescription.toString());
        PimDataService.saveTask(task);
        System.out.println("Save successfully！");
    }

    /**
     * Delete
     */
    private static void delete(String[] commands) {
        if (commands.length < 3) {
            System.out.println("When executing the Delete command, please attach a unique id");
            return;
        }
        String id = commands[2];
        PimDataService.deleteTask(id);
    }
}
