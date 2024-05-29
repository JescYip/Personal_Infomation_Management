package info.pim.controller;

import info.pim.service.PimDataService;

import java.util.List;
import java.util.Scanner;

/**
 * Print Controller
 */
public class PrintController {
    public static void executeCommand(Scanner sc, String command) {
        String[] commands = command.split(" ");
        if (commands.length >= 2) {
            // The second command is used as the id of the query
            String id = commands[1].trim();
            if (id.equals("all")) {
                List<Object> result = PimDataService.getAllPir();
                for (Object pir : result) {
                    System.out.println(pir);
                }
            } else {
                Object pir = PimDataService.getPirById(id);
                if (pir != null) {
                    System.out.println(pir);
                }
            }
        }
    }
}
