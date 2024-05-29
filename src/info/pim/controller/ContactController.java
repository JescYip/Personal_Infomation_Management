package info.pim.controller;

import info.pim.model.Contact;
import info.pim.model.Task;
import info.pim.service.PimDataService;

import java.util.List;
import java.util.Scanner;

/**
 * Contact Controller
 */
public class ContactController {
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
     * search
     */
    private static void list() {
        List<Contact> contacts = PimDataService.contacts;
        System.out.println("contact list:");
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
    }

    /**
     * Add
     */
    private static void add(Scanner sc) {
        Contact contact = new Contact();
        contact.setId(PimDataService.idWorker.nextId());

        updateData(sc, contact);
    }

    /**
     * Edit
     */
    private static void edit(Scanner sc, String[] commands) {
        if (commands.length < 3) {
            System.out.println("When executing the Edit command, please attach a unique id.");
            return;
        }
        String id = commands[2];
        Contact contact = PimDataService.getContact(id);
        if (contact == null) {
            System.out.println("Contact does not exist！");
            return;
        }

        updateData(sc, contact);
    }

    private static void updateData(Scanner sc, Contact contact) {
        System.out.println("Please input name：");
        String name = sc.nextLine();
        contact.setName(name);
        System.out.println("Please input address：");
        String address = sc.nextLine();
        contact.setAddress(address);
        System.out.println("Please input mobile：");
        String mobile = sc.nextLine();
        contact.setMobile(mobile);
        PimDataService.saveContact(contact);
        System.out.println("Save Successfully！");
    }

    /**
     * delete
     */
    private static void delete(String[] commands) {
        if (commands.length < 3) {
            System.out.println("When executing the Delete command, please attach a unique id.");
            return;
        }
        String id = commands[2];
        PimDataService.deleteContact(id);
    }
}
