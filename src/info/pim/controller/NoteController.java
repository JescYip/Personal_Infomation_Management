package info.pim.controller;

import info.pim.model.Note;
import info.pim.model.Task;
import info.pim.service.PimDataService;

import java.util.List;
import java.util.Scanner;

/**
 * Note Controller
 */
public class NoteController {
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
        List<Note> notes = PimDataService.notes;
        System.out.println("note list:");
        for (Note note : notes) {
            System.out.println(note);
        }
    }

    /**
     * Add
     */
    private static void add(Scanner sc) {
        Note note = new Note();
        note.setId(PimDataService.idWorker.nextId());

        updateData(sc, note);
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
        Note note = PimDataService.getNote(id);
        if (note == null) {
            System.out.println("The note does not exist！");
            return;
        }

        updateData(sc, note);
    }

    private static void updateData(Scanner sc, Note note) {
        System.out.println("Please input content(When input equals to [:quit] Quit the system)：");
        StringBuffer sbContent = new StringBuffer();
        while (true) {
            String content = sc.nextLine();
            if (content.equals(":quit")) {
                break;
            }
            sbContent.append(content);
        }
        note.setContent(sbContent.toString());
        PimDataService.saveNote(note);
        System.out.println("save successfully！");
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
        PimDataService.deleteNote(id);
    }
}
