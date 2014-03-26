package edu.rpi.tw.escience;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Calendars;
import com.google.api.services.calendar.model.*;

import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.CalendarFactory;

public class ServletCalendarFactory implements CalendarFactory {
    private WeakReference<Context> context;
    private Map<String, Calendar> calendars;
    private final String[] PROJECTION = new String[] {
        Calendars._ID,
        Calendars.ACCOUNT_NAME,
        Calendars.ACCOUNT_TYPE,
        Calendars.OWNER_ACCOUNT,
        Calendars.CALENDAR_DISPLAY_NAME
    };

    public ServletCalendarFactory( Context context ) {
        if ( context == null ) {
            throw new IllegalArgumentException( "Context cannot be null" );
        }
        this.context = new WeakReference<Context>( context );
    }
	
	/**
     * Obtains a collection of calendar names from the underlying model that
     * can be constructed by this factory.
     * @return
     */
    public Collection<String> getCalendarNames(){
    	
    }// /getCalendarNames()

    /**
     * Obtains a specific calendar given the calendar's name.
     * @param name
     * @return
     */
    public Calendar getCalendar(String name){
    	
    }// /getCalendar()
    

    /**
     * Obtains a collection of all calendars from the underlying model.
     * @return
     */
    public Collection<Calendar> getAllCalendars(){
    	
    }// /getAllCalendars()
}// /ServletCalendarFactory