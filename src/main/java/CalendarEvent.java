
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;

public class CalendarEvent implements Serializable {
    protected int DEFAULT_PREPARE_MIN = 30;
    protected String addressFrom;
    protected String addressTo;
    protected String eventName;
    protected String originName;
    protected String destName;
    protected GregorianCalendar arrivalDateTime;
    protected GregorianCalendar alarmTime;
    protected Transportation transport;
    protected double importantScale;
    protected int recommendedReadyMin;
    protected int preparingTime;
    protected int travelTime;
    protected SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM dd yyyy - HH:mm");


    public CalendarEvent(String addressFrom, String addressTo, String eventName,
                         String originName, String destName, GregorianCalendar arrivalDateTime,
                         Transportation transport, double importantScale) {
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.eventName = eventName;
        this.originName = originName;
        this.destName = destName;
        this.arrivalDateTime = arrivalDateTime;
        this.transport = transport;
        this.importantScale = importantScale;
        this.travelTime = transport.getTotalMinTravel();
        calPrepareTime();
        setTotalTime();

    }

    public String toString() {
//        return String.format("Date and Time:\t%s\nOrigin:\t%s\nDestination:\t%s\n" +
//                        "Travel by:\t%s\n%s\n\n",
//                        getArrivalFormatTime(), addressFrom, addressTo, transport.toString(), getEventInfo());

        return String.format("---%s---\n%s  -->  %s\n" +
                        "Alarm at:%s  ",
                getArrivalFormatTime(), originName, destName, getAlarmFormatTime());

    }

    //TODO: calculate and format the alarm time by adding it to the event time
    public String getAlarmFormatTime() {
        return dateTimeFormat.format(alarmTime.getTime());
    }

    public String getArrivalFormatTime() {
        return dateTimeFormat.format(arrivalDateTime.getTime());
    }

    protected void calPrepareTime() {
        preparingTime = DEFAULT_PREPARE_MIN + (int) importantScale * 5;
    }

    protected void setTotalTime() {
        recommendedReadyMin = preparingTime + travelTime;
        alarmTime = (GregorianCalendar) arrivalDateTime.clone();
        alarmTime.add(Calendar.MINUTE, -recommendedReadyMin);
    }

    public void editReadyTime(double adjustMin) {
        recommendedReadyMin += adjustMin;
        alarmTime.add(Calendar.MINUTE, -(int) adjustMin);
        System.out.println(getAlarmFormatTime());
    }

    public int getTravelTime() {
        return travelTime;
    }

    public String getEventInfo(){
        return String.format("Travel Duration: %d %s" +
                        "\nAlarm Time: %s \n%d minutes %s the event",
                travelTime, travelTime > 1? "minutes" : "minute",
                getAlarmFormatTime(), Math.abs(recommendedReadyMin), (recommendedReadyMin < 0) ? "after" : "before");
    }

    public boolean equals(Object other) {
        if (!(other instanceof CalendarEvent)) {
            return false;
        }
        CalendarEvent comparingEvent = (CalendarEvent) other;
        return (addressFrom.equals(comparingEvent.addressFrom) &&
                addressTo.equals(comparingEvent.addressTo) &&
                arrivalDateTime.equals(comparingEvent.arrivalDateTime) &&
                transport.equals(comparingEvent.transport));
    }

}
