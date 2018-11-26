import java.util.GregorianCalendar;

public class CalendarEventFactory {
    /**
     *
     * @param addressFrom
     * @param addressTo
     * @param eventName
     * @param originName
     * @param destName
     * @param arrivalDateTime
     * @param transport
     * @param importantScale
     * @param rating
     * @return
     */
    public static CalendarEvent createEventType(String addressFrom, String addressTo, String eventName,
                                                String originName, String destName, GregorianCalendar arrivalDateTime,
                                                Transportation transport, double importantScale, double rating) {
        if (rating <= 0.0) {
            return new CalendarEvent(addressFrom, addressTo, eventName,
                    originName, destName, arrivalDateTime, transport, importantScale);
        } else {
            return new EventWithPlaceInfo(addressFrom, addressTo, eventName,
                    originName, destName, arrivalDateTime, transport, importantScale, rating);
        }
    }
}
