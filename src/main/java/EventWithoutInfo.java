import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class contain event which doesn't have valid info from API
 */
public class EventWithoutInfo extends CalendarEvent {
    protected int preparingTime;


    /**
     * Constructor for the class takes in string of transportation
     *
     * @param addressFrom     Starting address
     * @param addressTo       Ending address
     * @param eventName       Name of the event
     * @param arrivalDateTime Time the user wants to arrive by
     * @param transport       String of transportation
     * @param importantScale  Priority level of the event
     */
    public EventWithoutInfo(String addressFrom, String addressTo, String eventName,
                            GregorianCalendar arrivalDateTime, String transport, double importantScale) {
        super(addressFrom, addressTo, eventName,
                transport, 0, 0,arrivalDateTime, importantScale);
        setTotalTime();
        calPrepareTime();

    }

    /**
     * Constructor for the class takes in Transportation type.
     *
     * @param addressFrom     Starting address
     * @param addressTo       Ending address
     * @param eventName       Name of the event
     * @param arrivalDateTime Time the user wants to arrive by
     * @param transport       String of transportation
     * @param importantScale  Priority level of the event
     */
    public EventWithoutInfo(String addressFrom, String addressTo, String eventName,
                            GregorianCalendar arrivalDateTime, Transportation transport, double importantScale) {
        super(addressFrom, addressTo, eventName,
                transport, arrivalDateTime, importantScale);
        setTotalTime();
        calPrepareTime();
    }

    /**
     * Calculates the recommended time to prepare for an event based on all the user input.
     */
    protected void setTotalTime() {
        recommendedReadyMin = preparingTime;
        alarmTime = (GregorianCalendar) arrivalDateTime.clone();
        alarmTime.add(Calendar.MINUTE, -recommendedReadyMin);
    }

    /**
     * Calculates the estimated time the user needs to prepare for an event using the importance scale.
     * The more important the event is the more time will be adding to prepare.
     */
    protected void calPrepareTime() {
        preparingTime = DEFAULT_PREPARE_MIN + (int) (importantScale * 6);
    }

    /**
     * Gets string information for output frame
     *
     * @return String about information for output frame
     */
    @Override
    public String getSummaryInfo() {
        return String.format("\t%s\n*** %s ***\nEstimation Alarm Not Available\n" +
                "Alarm set at: %s\n", eventName, getArrivalTimeString(), getAlarmString());

    }

    /**
     * String of object summary
     *
     * @return information of the object
     */
    @Override
    public String getEventInfo() {
        return String.format("Alarm Time:\t%s\n\n%s %s the event", getAlarmString(),
                durationStringFormat(recommendedReadyMin), (recommendedReadyMin < 0) ? "after" : "before");
    }

    /**
     * Compares if the object is the same.
     *
     * @param other comparing object
     * @return false if it is not equal otherwise true
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EventWithoutInfo)) {
            return false;
        }
        EventWithoutInfo comparingEvent = (EventWithoutInfo) other;
        return (addressFrom.equals(comparingEvent.addressFrom) &&
                addressTo.equals(comparingEvent.addressTo) &&
                arrivalDateTime.equals(comparingEvent.arrivalDateTime));
    }

    /**
     * Copies the object
     *
     * @return A copy of the object
     * @throws CloneNotSupportedException
     */
    @Override
    public EventWithoutInfo clone() throws CloneNotSupportedException {
        EventWithoutInfo event = new EventWithoutInfo(addressFrom, addressTo, eventName,
                (GregorianCalendar) arrivalDateTime.clone(), (Transportation) transport.clone(), importantScale);
        event.recommendedReadyMin = recommendedReadyMin;
        event.alarmTime = alarmTime;
        return event;
    }

    /**
     * To string method
     *
     * @return a string of info.
     */
    @Override
    public String toString() {
        return String.format("Date & Time:\t%s\n\nOrigin:\t%s\n\n" +
                        "Destination:\t%s\n\nTravel by:\t%s\n\n%s",
                getArrivalTimeString(), addressFrom, addressTo,
                transport.toString(), getEventInfo());
    }

    /**
     * Creates a pop up frame
     *
     * @return a new create popUp
     */
    public PopUpFrame createPopUp() {
        return new OfflinePopUpFrame(this.getEventInfo(),true);
    }

}
