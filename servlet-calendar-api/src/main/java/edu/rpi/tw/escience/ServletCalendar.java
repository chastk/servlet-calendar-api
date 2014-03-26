package edu.rpi.tw.escience;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import edu.rpi.tw.calendar.Calendar;
import edu.rpi.tw.calendar.Event;

class ServletCalendar implements Calendar {
	private static final long DAY_IN_FUTURE = 86400000;
    private final long _id;
    private String accountName;
    private String displayName;
    private String calName;
    
	ServletCalendar( long _id, String calName, String accountName, String displayName ) {
        this._id = _id;
        this.calName = calName;
        this.accountName = accountName;
        this.displayName = displayName;
    }
	
	static class EventIterator implements Iterator<Event> {
		private WeakReference<Calendar> owner;
		private ListIterator iter;

    	EventIterator(Calendar owner) {
    		this.owner = new WeakReference<Calendar>(owner);
    		this.iter = new ListIterator();
    	}

    	@Override
    	public boolean hasNext() {
    		return iter.hasNext();
    	}

    	@Override
    	public Event next() {
    		if ( !iter.hasNext() ) {
    			throw new NoSuchElementException();
    		}
    		return new Event( iter.next() );
    	}

    	@Override
    	public void remove() {
    		throw new UnsupportedOperationException();
		}
	}// /EventIterator
    
    @Override
    public Iterator<Event> iterator() {
        return new EventIterator( this );
    }
    
	/**
     * Obtain a list of {@link Event}s from the calendar.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
	@Override
    public List<Event> getUpcomingEvents(){
		return getUpcomingEvents( DAY_IN_FUTURE );
	}// /getUpcomingEvents

    /**
     * Retrieves a list of {@link Event}s from the calendar that have instances
     * that occur within a specified interval in the future from the current
     * time.
     * @param millisInFuture Limit search to a certain number of milliseconds
     * into the future
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation, limited to events with instances
     * that will occur less than millisInFuture.
     */
	@Override
    public List<Event> getUpcomingEvents(long millisInFuture){
		final long now = java.util.Calendar.getInstance().getTimeInMillis();
		List<Event> upcoming = new ArrayList<Event>();
		//timeMin 	datetime
		Date today = new Date();
		//timeMax 	datetime 
		Date tomorrow = new Date((today.getTime()) + millisInFuture);
		
        String pageToken = null;
		do {
			events = service.events().list('primary').setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				upcoming.add(event);
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);
		
        return upcoming;
	}// /getUpcomingEvents

    /**
     * Obtains a list of {@link Events}s from the calendar that occurred before
     * the current time.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
	@Override
    public List<Event> getPastEvents(){
		return getPastEvents( 0 );
	}// /getPastEvents

    /**
     * Retrieves a list of {@link Event}s from the calendar that have instances
     * that occur within a specified interval in the past from the current
     * time.
     * @param millisInPast Limit search to a certain number of milliseconds in
     * the past
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation, limited to events with instances
     * that have occurred less than millisInFuture in the past.
     */
	@Override
    public List<Event> getPastEvents(long millisInPast){
		List<Event> past = new ArrayList<Event>();
		// timeMax
		Date today = new Date();
		// timeMin
		Date before = new Date(today.getTime() - millisInPast);
		
		do {
			events = service.events().list('primary').setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				System.out.println(event.getSummary());
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);
		
		return past;
	}// /getPastEvents

    /**
     * Obtains a list of all events in the calendar. If the underlying
     * implementation does not support this operation, it may elect to throw
     * a {@link java.lang.UnsupportedOperationException} runtime exception.
     * @return A {@link java.util.List List} of {@link Event} objects obtained
     * from the calendar API implementation.
     */
	@Override
    public List<Event> getEvents(){
		List<Event> allEvents = new ArrayList<Event>();
		do {
			events = service.events().list('primary').setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				allEvents.add(item);
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);
		
		return allEvents;
	}// /getEvents

    /**
     * Obtains an event in the calendar with a specific identifier.
     * @param id
     * @return An {@link Event} object if the identifier denotes an event in
     * the calendar, or null if the identifier is not valid or the event has
     * been deleted.
     */
	@Override
    public Event getEventById(long id){
		
	}// /getEventById

    /**
     * Gets the name for this calendar.
     * @return
     */
	@Override
    public String getName(){
		return calName;
	}// /getName

    /**
     * Returns the display name for the calendar as it should be displayed in
     * a user interface.
     * @return
     */
	@Override
    public String getDisplayName(){
		return displayName;
	}// /getDisplayName

    /**
     * Returns the account name, if any, that this calendar originates from.
     * @return A string containing an account name, otherwise null (e.g., if
     * the account is local)
     */
	@Override
    String getAccountName(){
		return accountName;
	}// /getAccountName

    /**
     * Returns whether the calendar is visible in the underlying calendar API.
     * Depending on the implementation, this may have further implications for
     * other methods.
     * @return true if the calendar is visible, otherwise false.
     */
	@Override
    public boolean isVisible(){
		/*final Uri uri = Calendars.CONTENT_URI;
		final String[] VISIBILITY = new String[] { Calendars.VISIBLE }; 
		final String sel = "(" + Calendars._ID + " = ?)";
		final String[] args = new String[] { Long.toString(_id) };
		Cursor cur = null;
		boolean visible = false;
		try {
			cur = resolver.query(uri, VISIBILITY, sel, args, null);
			while( cur.moveToNext() ) {
				if ( 1 == cur.getInt( 0 ) ) {
					visible = true;
				}
			}
		} finally {
			if ( cur != null ) {
				cur.close();
			}
		}*/
		return visible;
	}// /isVisible
	
	@Override
    public String toString() {
        return "calendar " + _id + " (" + accountName + "): " + displayName;
    }
}
