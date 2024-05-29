package info.pim.controller;

import info.pim.model.Contact;
import info.pim.model.Event;
import info.pim.model.Note;
import info.pim.model.Task;
import info.pim.service.PimDataService;
import info.pim.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Search Controller
 * Logical processing of query conditions
 * Query Command Format: search + space + condition
 * condition: type | text | time，and the conditions of the combination must be in this order
 * The value of the condition can be preceded by '!', which represents the inverse operation.
 * Multiple conditions can be used by '&&' or '||'
 */
public class SearchController {
    public static void executeCommand(Scanner sc, String command) {
        List<Object> result = new ArrayList<>();
        String[] commands = command.split(" ");
        if (commands.length >= 2) {
            if (!commands[1].startsWith("type=")  && !commands[1].startsWith("text=") && !commands[1].startsWith("time")) {
                return;
            }
            // Data type criteria for current search
            String typeValue = "";
            // Marks if type is an inverse query
            boolean typeNegation = false;
            // Currently searched text conditions
            String textValue = "";
            // Marks if text is an inverse query
            boolean textNegation = false;
            // Mark whether the text condition and the preorder condition (if any is type) are AND relations
            boolean textAnd = false;
            // Mark whether the text condition and the preorder condition (if any is type) are OR relations
            boolean textOr = false;
            // Currently searching for time conditions
            String timeValue = "";
            // Marks whether time is an inverse query
            boolean timeNegation = false;
            // Mark whether the text condition and the preorder condition (if any is type or text) are AND relations
            boolean timeAnd = false;
            // Mark whether the text condition and the preorder condition (if any is type or text) are OR relations
            boolean timeOr = false;
            for (int i = 1; i < commands.length; i++) {
                String condition = commands[i];
                boolean negation = false;
                if (condition.startsWith("type=")) {
                    condition = condition.replace("type=", "").trim();
                    if (condition.startsWith("!")) {
                        negation = true;
                        condition = condition.substring(1);
                    }
                    if (StringUtils.isEmpty(condition)) {
                        System.out.println("The type query condition entered does not meet the requirements, please re-enter it!");
                        return;
                    }
                    typeValue = condition;
                    typeNegation = negation;
                } else if (condition.startsWith("text=")) {
                    condition = condition.replace("text=", "").trim();
                    if (condition.startsWith("!")) {
                        negation = true;
                        condition = condition.substring(1);
                    }
                    if (StringUtils.isEmpty(condition)) {
                        System.out.println("The text query condition entered does not meet the requirements, please re-enter it!");
                        return;
                    }
                    textValue = condition;
                    textNegation = negation;
                } else if (condition.startsWith("time")) {
                    condition = condition.replace("time", "").trim();
                    if (condition.startsWith("!")) {
                        negation = true;
                        condition = condition.substring(1);
                    }
                    if (StringUtils.isEmpty(condition)) {
                        System.out.println("The time query condition entered does not meet the requirements, please re-enter it!");
                        return;
                    }
                    timeValue = condition;
                    timeNegation = negation;
                } else if (condition.equals("&&")) {
                    int nextIndex = i + 1;
                    if (nextIndex < commands.length) {
                        String nextCondition = commands[nextIndex];
                        if (nextCondition.startsWith("text=")) {
                            textAnd = true;
                        } else if (nextCondition.startsWith("time")) {
                            timeAnd = true;
                        }
                    }
                } else if (condition.equals("||")) {
                    int nextIndex = i + 1;
                    if (nextIndex < commands.length) {
                        String nextCondition = commands[nextIndex];
                        if (nextCondition.startsWith("text=")) {
                            textOr = true;
                        } else if (nextCondition.startsWith("time")) {
                            timeOr = true;
                        }
                    }
                }
            }
            List<Note> noteList = searchNote(typeValue, typeNegation,
                    textValue, textNegation, textAnd, textOr);
            result.addAll(noteList);
            List<Task> taskList = searchTask(typeValue, typeNegation,
                    textValue, textNegation, textAnd, textOr,
                    timeValue, timeNegation, timeAnd, timeOr);
            result.addAll(taskList);
            List<Event> eventList = searchEvent(typeValue, typeNegation,
                    textValue, textNegation, textAnd, textOr,
                    timeValue, timeNegation, timeAnd, timeOr);
            result.addAll(eventList);
            List<Contact> contactList = searchContact(typeValue, typeNegation,
                    textValue, textNegation, textAnd, textOr);
            result.addAll(contactList);
        } else {
            result = PimDataService.getAllPir();
        }
        for (Object pir : result) {
            System.out.println(pir);
        }
    }

    private static boolean checkType(String typeValue, boolean typeNegation, String typeKey) {
        if (StringUtils.isEmpty(typeValue)) {
            // Returns true if no condition type is entered.
            return true;
        }
        // inverse operation
        if (typeNegation) {
            return !typeValue.equals(typeKey);
        } else {
            return typeValue.equals(typeKey);
        }
    }

    private static List<Note> searchNote(String typeValue, boolean typeNegation,
                               String textValue, boolean textNegation,
                               boolean textAnd, boolean textOr) {
        List<Note> result = new ArrayList<>();
        if (checkType(typeValue, typeNegation, "note")) {
            List<Note> noteList = searchNoteDetail(textValue, textNegation);
            result.addAll(noteList);
        } else {
            if (textOr) {
                List<Note> noteList = searchNoteDetail(textValue, textNegation);
                result.addAll(noteList);
            }
        }

        return result;
    }

    private static List<Note> searchNoteDetail(String textValue, boolean textNegation) {
        List<Note> matchList = new ArrayList<>();
        if (StringUtils.isEmpty(textValue)) {
            return PimDataService.notes;
            //return matchList;
        }
        if (textNegation) {
            matchList = PimDataService.notes.stream().filter(f -> !f.getContent().contains(textValue)).collect(Collectors.toList());
        } else {
            matchList = PimDataService.notes.stream().filter(f -> f.getContent().contains(textValue)).collect(Collectors.toList());
        }
        return matchList;
    }

    private static List<Task> searchTask(String typeValue, boolean typeNegation,
                                         String textValue, boolean textNegation,
                                         boolean textAnd, boolean textOr,
                                         String timeValue, boolean timeNegation,
                                         boolean timeAnd, boolean timeOr) {
        List<Task> result = new ArrayList<>();
        if (checkType(typeValue, typeNegation, "task")) {
            List<Task> taskList = searchTaskDetail(textValue, textNegation, timeValue, timeNegation, timeAnd, timeOr);
            result.addAll(taskList);
        } else {
            if (textOr) {
                List<Task> taskList = searchTaskDetail(textValue, textNegation, timeValue, timeNegation, timeAnd, timeOr);
                result.addAll(taskList);
            }
        }

        return result;
    }

    private static List<Task> searchTaskDetail(String textValue, boolean textNegation,
                                         String timeValue, boolean timeNegation,
                                         boolean timeAnd, boolean timeOr) {
        List<Task> matchList = new ArrayList<>();

        for (Task task : PimDataService.tasks) {
            boolean isMatch = false;
            if (StringUtils.isEmpty(textValue)) {
                if (StringUtils.isEmpty(timeValue)) {
                    isMatch = true;
                } else {
                    if (timeNegation) {
                        if (timeValue.startsWith(">")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) <= 0) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("=")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) != 0) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("<")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) >= 0) {
                                isMatch = true;
                            }
                        }
                    } else {
                        if (timeValue.startsWith(">")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) > 0) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("=")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) == 0) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("<")) {
                            if (task.getDeadline().compareTo(timeValue.substring(1)) < 0) {
                                isMatch = true;
                            }
                        }
                    }
                }
            } else {
                if (StringUtils.isEmpty(timeValue)) {
                    if (textNegation) {
                        if (!task.getDescription().contains(textValue)) {
                            isMatch = true;
                        }
                    } else {
                        if (task.getDescription().contains(textValue)) {
                            isMatch = true;
                        }
                    }
                } else {
                    if (textNegation) {
                        boolean partMath = !task.getDescription().contains(textValue);
                        if (timeNegation) {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) <= 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) != 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) >= 0) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) <= 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) != 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) >= 0) {
                                        isMatch = true;
                                    }
                                }
                            }

                        } else {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) > 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) == 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) < 0) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) > 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) == 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) < 0) {
                                        isMatch = true;
                                    }
                                }
                            }
                        }
                    } else {
                        boolean partMath = task.getDescription().contains(textValue);
                        if (timeNegation) {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) <= 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) != 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) >= 0) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) <= 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) != 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) >= 0) {
                                        isMatch = true;
                                    }
                                }
                            }

                        } else {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) > 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) == 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && task.getDeadline().compareTo(timeValue.substring(1)) < 0) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) > 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) == 0) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || task.getDeadline().compareTo(timeValue.substring(1)) < 0) {
                                        isMatch = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isMatch) {
                matchList.add(task);
            }
        }

        return matchList;
    }


    private static List<Event> searchEvent(String typeValue, boolean typeNegation,
                                           String textValue, boolean textNegation,
                                           boolean textAnd, boolean textOr,
                                           String timeValue, boolean timeNegation,
                                           boolean timeAnd, boolean timeOr) {
        List<Event> result = new ArrayList<>();
        if (checkType(typeValue, typeNegation, "event")) {
            List<Event> eventList = searchEventDetail(textValue, textNegation, timeValue, timeNegation, timeAnd, timeOr);
            result.addAll(eventList);
        } else {
            if (textOr) {
                List<Event> eventList = searchEventDetail(textValue, textNegation, timeValue, timeNegation, timeAnd, timeOr);
                result.addAll(eventList);
            }
        }

        return result;
    }

    private static List<Event> searchEventDetail(String textValue, boolean textNegation,
                                               String timeValue, boolean timeNegation,
                                               boolean timeAnd, boolean timeOr) {
        List<Event> matchList = new ArrayList<>();

        for (Event event : PimDataService.events) {
            boolean isMatch = false;
            if (StringUtils.isEmpty(textValue)) {
                if (StringUtils.isEmpty(timeValue)) {
                    isMatch = true;
                } else {
                    if (timeNegation) {
                        if (timeValue.startsWith(">")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) <= 0 || event.getAlarm().compareTo(timeValue.substring(1)) <= 0)) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("=")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) != 0 || event.getAlarm().compareTo(timeValue.substring(1)) != 0)) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("<")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) >= 0 || event.getAlarm().compareTo(timeValue.substring(1)) >= 0)) {
                                isMatch = true;
                            }
                        }
                    } else {
                        if (timeValue.startsWith(">")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) > 0 || event.getAlarm().compareTo(timeValue.substring(1)) > 0)) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("=")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) == 0 || event.getAlarm().compareTo(timeValue.substring(1)) == 0)) {
                                isMatch = true;
                            }
                        } else if (timeValue.startsWith("<")) {
                            if ((event.getStartTime().compareTo(timeValue.substring(1)) < 0 || event.getAlarm().compareTo(timeValue.substring(1)) < 0)) {
                                isMatch = true;
                            }
                        }
                    }
                }
            } else {
                if (StringUtils.isEmpty(timeValue)) {
                    if (textNegation) {
                        if (!event.getDescription().contains(textValue)) {
                            isMatch = true;
                        }
                    } else {
                        if (event.getDescription().contains(textValue)) {
                            isMatch = true;
                        }
                    }
                } else {
                    if (textNegation) {
                        boolean partMath = !event.getDescription().contains(textValue);
                        if (timeNegation) {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) <= 0 || event.getAlarm().compareTo(timeValue.substring(1)) <= 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) != 0 || event.getAlarm().compareTo(timeValue.substring(1)) != 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) >= 0 || event.getAlarm().compareTo(timeValue.substring(1)) >= 0)) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) <= 0 || event.getAlarm().compareTo(timeValue.substring(1)) <= 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) != 0 || event.getAlarm().compareTo(timeValue.substring(1)) != 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) >= 0 || event.getAlarm().compareTo(timeValue.substring(1)) >= 0)) {
                                        isMatch = true;
                                    }
                                }
                            }

                        } else {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) > 0 || event.getAlarm().compareTo(timeValue.substring(1)) > 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) == 0 || event.getAlarm().compareTo(timeValue.substring(1)) == 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) < 0 || event.getAlarm().compareTo(timeValue.substring(1)) < 0)) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) > 0 || event.getAlarm().compareTo(timeValue.substring(1)) > 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) == 0 || event.getAlarm().compareTo(timeValue.substring(1)) == 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) < 0 || event.getAlarm().compareTo(timeValue.substring(1)) < 0)) {
                                        isMatch = true;
                                    }
                                }
                            }
                        }
                    } else {
                        boolean partMath = event.getDescription().contains(textValue);
                        if (timeNegation) {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) <= 0 || event.getAlarm().compareTo(timeValue.substring(1)) <= 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) != 0 || event.getAlarm().compareTo(timeValue.substring(1)) != 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) >= 0 || event.getAlarm().compareTo(timeValue.substring(1)) >= 0)) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) <= 0 || event.getAlarm().compareTo(timeValue.substring(1)) <= 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) != 0 || event.getAlarm().compareTo(timeValue.substring(1)) != 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) >= 0 || event.getAlarm().compareTo(timeValue.substring(1)) >= 0)) {
                                        isMatch = true;
                                    }
                                }
                            }

                        } else {
                            if (timeAnd) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) > 0 || event.getAlarm().compareTo(timeValue.substring(1)) > 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) == 0 || event.getAlarm().compareTo(timeValue.substring(1)) == 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath && (event.getStartTime().compareTo(timeValue.substring(1)) < 0 || event.getAlarm().compareTo(timeValue.substring(1)) < 0)) {
                                        isMatch = true;
                                    }
                                }
                            } else if (timeOr) {
                                if (timeValue.startsWith(">")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) > 0 || event.getAlarm().compareTo(timeValue.substring(1)) > 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("=")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) == 0 || event.getAlarm().compareTo(timeValue.substring(1)) == 0)) {
                                        isMatch = true;
                                    }
                                } else if (timeValue.startsWith("<")) {
                                    if (partMath || (event.getStartTime().compareTo(timeValue.substring(1)) < 0 || event.getAlarm().compareTo(timeValue.substring(1)) < 0)) {
                                        isMatch = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isMatch) {
                matchList.add(event);
            }
        }

        return matchList;
    }

    private static List<Contact> searchContact(String typeValue, boolean typeNegation,
                                               String textValue, boolean textNegation,
                                               boolean textAnd, boolean textOr) {
        List<Contact> result = new ArrayList<>();
        if (checkType(typeValue, typeNegation, "contact")) {
            List<Contact> contactList = searchContactDetail(textValue, textNegation);
            result.addAll(contactList);
        } else {
            if (textOr) {
                List<Contact> contactList = searchContactDetail(textValue, textNegation);
                result.addAll(contactList);
            }
        }

        return result;
    }

    private static List<Contact> searchContactDetail(String textValue, boolean textNegation) {
        List<Contact> matchList = new ArrayList<>();
        if (StringUtils.isEmpty(textValue)) {
            return PimDataService.contacts;
            //return matchList;
        }
        if (textNegation) {
            matchList = PimDataService.contacts.stream().filter(f -> (!f.getName().contains(textValue) || !f.getMobile().contains(textValue) || !f.getAddress().contains(textValue))).collect(Collectors.toList());
        } else {
            matchList = PimDataService.contacts.stream().filter(f -> (f.getName().contains(textValue) || f.getMobile().contains(textValue) || f.getAddress().contains(textValue))).collect(Collectors.toList());
        }
        return matchList;
    }
}
