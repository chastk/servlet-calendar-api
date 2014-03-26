package edu.rpi.tw.escience;
/**
 * The Event interface provides a contract for accessing calendar event data.
 * An Event includes at a minimum a title and at least one instance that has
 * a specified start/end time (or is all day, see {@link #isAllDay}).
 *
 */

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.Instance;


public class ServletEvent implements Event {
	public static final String[] EVENT_PROJECTION = {
        Events._ID,
        Events.ORGANIZER,
        Events.TITLE,
        Events.DESCRIPTION,
        Events.EVENT_LOCATION,
        Events.ALL_DAY,
        Events.DTSTART,
        Events.DTEND,
        Events.DURATION,
        Events.RRULE,
        Events.RDATE,
        Events.AVAILABILITY
    };
    private static final int _ID = 0;
    private static final int ORGANIZER = 1;
    private static final int TITLE = 2;
    private static final int DESCRIPTION = 3;
    private static final int EVENT_LOCATION = 4;
    private static final int ALL_DAY = 5;
    private static final int DTSTART = 6;
    private static final int DTEND = 7;
    private static final int DURATION = 8;
    private static final int RRULE = 9;
    private static final int RDATE = 10;
    private static final int AVAILABILITY = 11;
    private static final long DAY_LENGTH = 86400000;
    
    /**
     * Availability is an enumeration used to identify the availability of an
     * individual based on the information present in the calendar.
     * @author ewpatton
     *
     */
    public static enum Availability {
        BUSY,
        FREE,
        TENTATIVE
    }

	ServletEvent( Calendar owner ) {
        this._id = cursor.getLong( _ID );
        this.organizer = cursor.getString( ORGANIZER );
        this.title = cursor.getString( TITLE );
        this.description = cursor.getString( DESCRIPTION );
        this.location = cursor.getString( EVENT_LOCATION );
        this.allDay = cursor.getInt( ALL_DAY ) == 1;
        //long dt = cursor.getLong( DTSTART );
        this.start = java.util.Calendar.getInstance();
        this.start.setTimeInMillis( dt );
        //dt = cursor.getLong( DTEND );
        this.end = java.util.Calendar.getInstance();
        this.end.setTimeInMillis( dt );
        this.duration = cursor.getString( DURATION );
        this.rrule = cursor.getString( RRULE );
        this.rdate = cursor.getString( RDATE );
        //int a = cursor.getInt( AVAILABILITY );
        if ( a == Events.AVAILABILITY_BUSY ) {
            this.availability = Availability.BUSY;
        } else if ( a == Events.AVAILABILITY_FREE ) {
            this.availability = Availability.FREE;
        } else {
            this.availability = Availability.TENTATIVE;
        }
    }
	
    /**
     * Gets a unique identifier for this event that can be used to reference
     * it in future calls to other functions, such as
     * {@link Calendar#getEventById(long)}.
     * @return
     */
	@Override
    public long getId(){
		return this._id;
    }// /getId()

    /**
     * Gets the parent calendar for this event. Note: implementations SHOULD
     * make use of {@link java.lang.ref.WeakReference WeakReferences}s to
     * maintain parent references and keep the calendar structure tree-like for
     * garbage collection.
     * @return
     */
	@Override
    public Calendar getCalendar(){
		return owner.get();
    }// /getCalendar

    /**
     * Gets a string naming the organizer of the calendar. The information
     * content is implementation-defined. It may be, for example, an email
     * address or the name of the individual holding the calendar account.
     * @return
     */
	@Override
    public String getOrganizer(){
    	return this.organizer;
    }// /getOrganizer()

    /**
     * Gets the title of the event in the calendar.
     * @return
     */
	@Override
    public String getTitle(){
    	return this.title;
    }// /getTitle()

    /**
     * Gets a description of the event. If no description is provided by the
     * implementationm, this function MAY return null.
     * @return
     */
	@Override
    public String getDescription(){
    	return this.description;
    }// /getDescription()

    /**
     * Gets the location description of the event. If no location information
     * is provided by the implementation, this function MAY return null.
     * @return
     */
	@Override
    public String getLocation(){
    	return this.location;
    }// /getLocation()

    /**
     * Indicates if the event is an all-day event or not.
     * @return
     */
	@Override
    public boolean isAllDay(){
    	return this.allDay;
    }// /isAllDay()

    /**
     * Obtains a {@link java.util.Calendar} object representing the start time
     * of this calendar event. If this event is recurring, it is implementation-
     * defined as to which specific instance the calendar object represents.
     * @return
     */
	@Override
    public java.util.Calendar getStart(){
    	return this.start;
    }// /getStart()

    /**
     * Obtains a {@link java.util.Calendar} object representing the end time
     * of this calendar event. If this event is recurring, it is implementation-
     * defined as to which specific instance the calendar object represents.
     * @return
     */
	@Override
    public java.util.Calendar getEnd(){
		return this.end;
    }// /getEnd

    /**
     * Gets the duration of the event in the format specified by
     * {@link http://tools.ietf.org/html/rfc5545#section-3.8.2.5 IETF RFC 5545}.
     * @return
     */
	@Override
    public String getDuration(){
    	return this.duration;
    }// /getDuration()

    /**
     * Gets the recurrence rule for this event if the user has set one.
     * @return
     */
	@Override
    public String getRRule(){
    	return this.rrule;
    }// /getRRule()

    /**
     * Gets the recurrence dates for this event if the user has set any.
     * @return
     */
	@Override
    public String getRDate(){
    	return this.rdate;
    }// /getRDate()

    /**
     * Gets the individual's availability for this event as specified in the
     * underlying calendar data.
     * @return
     */
	@Override
    public Availability getAvailability(){
    	return this.availability;
    }// /getAvailability

    /**
     * Gets a {@link java.util.List List} of {@link Instance}s for this event.
     * Note: an implementation MAY throw an
     * {@link java.lang.UnsupportedOperationException UnsupportedOperationException}
     * in the event that it cannot generate a finite list of instances. It MAY
     * elect to return a subset of valid instances.
     * @return
     */
	@Override
    public List<Instance> getInstances(){
		String pageToken = null;
		List<Instance> instances = new ArrayList<Instance>();
		do {
			events = service.events().instances('primary', 'eventId').setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				instances.add(event);
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);
		return instances;
    }// /getInstances()

    /**
     * Gets a {@link java.util.List List} of {@link Instances}s for this event
     * that will occur within the specified number of milliseconds from the
     * current time.
     * @param millisInFuture
     * @return
     */
	@Override
    public List<Instance> getInstances(long millisInFuture){
		// fix for only instances before millisInFuture
		String pageToken = null;
		List<Instance> instances = new ArrayList<Instance>();
		do {
			events = service.events().instances('primary', '{eventId}').setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				instances.add(event);
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);
		return instances;
    	
    }// /getInstances()
}// /ServletEvent
