package info.pim.controller;

import info.pim.model.Event;
import info.pim.service.PimDataService;

import java.util.List;
import java.util.Scanner;

/**
 * Event Controller
 */
public class EventController {
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
        List<Event> events = PimDataService.events;
        System.out.println("event list:");
        for (Event event : events) {
            System.out.println(event);
        }
    }

    /**
     * Add
     */
    private static void add(Scanner sc) {
        Event event = new Event();
        event.setId(PimDataService.idWorker.nextId());

        updateData(sc, event);
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
        Event event = PimDataService.getEvent(id);
        if (event == null) {
            System.out.println("Event does not exist！");
            return;
        }

        updateData(sc, event);
    }

    private static void updateData(Scanner sc, Event event) {
        System.out.println("Please input startTime(format as:yyyy-MM-dd，eg.2023-10-27)：");
        String startTime = sc.nextLine();
        event.setStartTime(startTime);
        System.out.println("Please input alarm(format as:yyyy-MM-dd，eg.2023-10-27)：");
        String alarm = sc.nextLine();
        event.setAlarm(alarm);
        System.out.println("Please input description(When input equals to [:quit] Quit the system)：");
        StringBuffer sbDescription = new StringBuffer();
        while (true) {
            String description = sc.nextLine();
            if (description.equals(":quit")) {
                break;
            }
            sbDescription.append(description);
        }
        event.setDescription(sbDescription.toString());
        PimDataService.saveEvent(event);
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
        PimDataService.deleteEvent(id);
    }
}
