package test;


import info.pim.model.Contact;
import info.pim.model.Event;
import info.pim.model.Note;
import info.pim.model.Task;
import info.pim.service.PimDataService;
import info.pim.util.DateUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test
 */
public class MainUnitTest {
    @BeforeClass
    public static void initTestData() {
        String imDataFilePath = "data/unit_test_data.pim";
        File pimTestFile = new File(imDataFilePath);
        if (pimTestFile.exists()) {
            pimTestFile.delete();
        }
        PimDataService.loadPimData(imDataFilePath);
    }

    @Test
    public void testNoteAdd() {
        Note note = new Note();
        String id = PimDataService.idWorker.nextId();
        note.setId(id);
        note.setContent("content add");
        // add
        PimDataService.saveNote(note);

        // get add data
        Note noteSaved = PimDataService.getNote(id);
        assertSame(note, noteSaved);

        // test print data
        String dataStr = "Note{" +
                "id='" + id + '\'' +
                ", content='" + noteSaved.getContent() + '\'' +
                '}';
        assertEquals(note.toString(), dataStr);
    }

    @Test
    public void testNoteEdit() {
        Note note = PimDataService.notes.get(0);
        String id = note.getId();
        note.setContent("content edit");
        // edit
        PimDataService.saveNote(note);

        // get edit data
        Note noteSaved = PimDataService.getNote(id);
        assertSame(note, noteSaved);
    }

    @Test
    public void testTaskAdd() {
        Task task = new Task();
        String id = PimDataService.idWorker.nextId();
        task.setId(id);
        task.setDeadline(DateUtil.date());
        task.setDescription("description add");
        // add
        PimDataService.saveTask(task);

        // get add data
        Task taskSaved = PimDataService.getTask(id);
        assertSame(task, taskSaved);

        // test print data
        String dataStr = "Task{" +
                "id='" + id + '\'' +
                ", deadline='" + taskSaved.getDeadline() + '\'' +
                ", description='" + taskSaved.getDescription() + '\'' +
                '}';
        assertEquals(task.toString(), dataStr);
    }

    @Test
    public void testTaskEdit() {
        Task task = PimDataService.tasks.get(0);
        String id = task.getId();
        task.setDeadline(DateUtil.date());
        task.setDescription("description edit");
        // edit
        PimDataService.saveTask(task);

        // get edit data
        Task taskSaved = PimDataService.getTask(id);
        assertSame(task, taskSaved);
    }

    @Test
    public void testEventAdd() {
        Event event = new Event();
        String id = PimDataService.idWorker.nextId();
        event.setId(id);
        event.setStartTime(DateUtil.date());
        event.setAlarm(DateUtil.date());
        event.setDescription("description add");
        // add
        PimDataService.saveEvent(event);

        // get add data
        Event eventSaved = PimDataService.getEvent(id);
        assertSame(event, eventSaved);

        // test print data
        String dataStr = "Event{" +
                "id='" + id + '\'' +
                ", startTime='" + eventSaved.getStartTime() + '\'' +
                ", alarm='" + eventSaved.getAlarm() + '\'' +
                ", description='" + eventSaved.getDescription() + '\'' +
                '}';
        assertEquals(event.toString(), dataStr);
    }

    @Test
    public void testEventEdit() {
        Event event = PimDataService.events.get(0);
        String id = event.getId();
        event.setStartTime(DateUtil.date());
        event.setAlarm(DateUtil.date());
        event.setDescription("description edit");
        // edit
        PimDataService.saveEvent(event);

        // get edit data
        Event eventSaved = PimDataService.getEvent(id);
        assertSame(event, eventSaved);
    }

    @Test
    public void testContactAdd() {
        Contact contact = new Contact();
        String id = PimDataService.idWorker.nextId();
        contact.setId(id);
        contact.setName("name add");
        contact.setMobile("mobile add");
        contact.setAddress("address add");
        // add
        PimDataService.saveContact(contact);

        // get add data
        Contact contactSaved = PimDataService.getContact(id);
        assertSame(contact, contactSaved);

        // test print data
        String dataStr = "Contact{" +
                "id='" + id + '\'' +
                ", name='" + contactSaved.getName() + '\'' +
                ", address='" + contactSaved.getAddress() + '\'' +
                ", mobile='" + contactSaved.getMobile() + '\'' +
                '}';
        assertEquals(contact.toString(), dataStr);
    }

    @Test
    public void testContactEdit() {
        Contact contact = PimDataService.contacts.get(0);
        String id = contact.getId();
        contact.setName("name edit");
        contact.setMobile("mobile edit");
        contact.setAddress("address edit");
        // edit
        PimDataService.saveContact(contact);

        // get edit data
        Contact contactSaved = PimDataService.getContact(id);
        assertSame(contact, contactSaved);
    }

    @Test
    public void testAllPirData() {
        List<Object> allPir = PimDataService.getAllPir();
        assertEquals(4, allPir.size());
    }

    @Test
    public void testGetPirById() {
        String noteId = PimDataService.notes.get(0).getId();
        Note note = (Note) PimDataService.getPirById(noteId);
        assertTrue(note != null && note.getId().equals(noteId));

        String taskId = PimDataService.tasks.get(0).getId();
        Task task = (Task) PimDataService.getPirById(taskId);
        assertTrue(task != null && task.getId().equals(taskId));

        String eventId = PimDataService.events.get(0).getId();
        Event event = (Event) PimDataService.getPirById(eventId);
        assertTrue(event != null && event.getId().equals(eventId));

        String contactId = PimDataService.contacts.get(0).getId();
        Contact contact = (Contact) PimDataService.getPirById(contactId);
        assertTrue(contact != null && contact.getId().equals(contactId));
    }

    @Test
    public void testNoteDelete() {
        Note note = PimDataService.notes.get(0);
        String id = note.getId();
        // delete
        PimDataService.deleteNote(id);

        // check delete data
        Note noteDeleted = PimDataService.getNote(id);
        assertSame(null, noteDeleted);
    }

    @Test
    public void testTaskDelete() {
        Task task = PimDataService.tasks.get(0);
        String id = task.getId();
        // delete
        PimDataService.deleteTask(id);

        // check delete data
        Task taskDeleted = PimDataService.getTask(id);
        assertSame(null, taskDeleted);
    }

    @Test
    public void testEventDelete() {
        Event event = PimDataService.events.get(0);
        String id = event.getId();
        // delete
        PimDataService.deleteEvent(id);

        // check delete data
        Event eventDeleted = PimDataService.getEvent(id);
        assertSame(null, eventDeleted);
    }

    @Test
    public void testContactDelete() {
        Contact contact = PimDataService.contacts.get(0);
        String id = contact.getId();
        // delete
        PimDataService.deleteContact(id);

        // check delete data
        Contact contactDeleted = PimDataService.getContact(id);
        assertSame(null, contactDeleted);
    }

    @Test
    public void testEmptyPirData() {
        String imDataFilePath = "data/unit_test_data.pim";
        PimDataService.loadPimData(imDataFilePath);

        assertEquals(0, PimDataService.notes.size());
        assertEquals(0, PimDataService.tasks.size());
        assertEquals(0, PimDataService.events.size());
        assertEquals(0, PimDataService.contacts.size());
    }
}
