import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A model keeps track all events' values
 */
public class EventModel{
    private ArrayList<Listener> listeners;
    private TreeMap<GregorianCalendar, CalendarEvent> events;
    private File file;
    private LinkedBlockingQueue<ChangedObject> eventsToProcess;

    /**
     * Constructor
     *
     * @param filePath file path that save old information
     */
    public EventModel(String filePath) {
        events = new TreeMap<GregorianCalendar, CalendarEvent>();
        listeners = new ArrayList<Listener>();
        eventsToProcess = new LinkedBlockingQueue<>();
        maybeCreateFile(filePath);
    }

    /**
     * adds a listener
     *
     * @param l adding listener
     */
    public void addListener(Listener l) {
        listeners.add(l);
    }

    /**
     * Adds event to the list
     *
     * @param s the adding event
     */
    public void addEvent(CalendarEvent s) {
        events.put(s.getArrivalDateTime(), s);
        notifyListener(s);
    }

    /**
     * @param ob
     */

    public void addEventToProcess(ChangedObject ob) {
        try {
            eventsToProcess.put(ob);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return
     */

    public ChangedObject getEventToProcess() {
        ChangedObject event = null;
        try {
            System.out.println("start run");
            event = eventsToProcess.take();
            System.out.println("Start to pull data request");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return event;
    }

    /**
     * Checks if the time is already in the the list
     *
     * @param dateTime the checking time
     * @return true or false
     */
    public boolean isTimeOccupied(GregorianCalendar dateTime) {
        return events.containsKey(dateTime);
    }

    /**
     * Gets list of events in string format
     *
     * @return a copy of list of events in string format
     */
    public ArrayList<String> getEvents() {
        CalendarEvent event;
        ArrayList<String> eventList = new ArrayList<>();
        Iterator<Map.Entry<GregorianCalendar, CalendarEvent>> iterator = events.entrySet().iterator();
        while (iterator.hasNext()) {
            event = iterator.next().getValue();
            eventList.add(event.toString());
        }
        return eventList;
    }


    public ArrayList<CalendarEvent> getEventsList() {
        CalendarEvent event;
        ArrayList<CalendarEvent> eventList = new ArrayList<>();
        Iterator<Map.Entry<GregorianCalendar, CalendarEvent>> iterator = events.entrySet().iterator();
        while (iterator.hasNext()) {
            event = iterator.next().getValue();
            eventList.add(event.copy());
        }
        Collections.sort(eventList);
        return eventList;
    }

    /**
     * notifies all listener
     *
     * @param object changed object
     */
    private void notifyListener(Object object) {
        for (Listener l : listeners) {
            l.update(object);
        }
    }

    /**
     *
     */
    public void saveEventsToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(file.getName());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(events);

            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
//TODO:  catch InvalidClassException

    /**
     *
     */

    public void restoreEventsFromFile() {
        try {
            FileInputStream fileInput = new FileInputStream(file.getName());
            ObjectInputStream inputStream = new ObjectInputStream(fileInput);
            while (true) {
                try {
                    events = (TreeMap<GregorianCalendar, CalendarEvent>) inputStream.readObject();
                } catch (EOFException eof) {
                    break;
                }
            }
            inputStream.close();
            fileInput.close();
        } catch (EOFException e) {
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Checks if the file exists, if not creates a file.
     *
     * @param fileName the file name
     */
    private void maybeCreateFile(String fileName) {
        this.file = new File(fileName);
        if (file.exists()) {
            restoreEventsFromFile();
        }
    }
}

