package edu.rpi.tw.escience;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Instances;
import com.google.api.services.calendar.model.*;

import edu.rpi.tw.calendar.Event;
import edu.rpi.tw.calendar.Instance;

/**
 * The Instance interface provides a wrapper around implementation-dependent
 * details of calendar event instances. It is meant to support accessing
 * the start/end times for a specific event occurrence. Instances may not
 * actually be stored anywhere in the implementation but instead be generated
 * at runtime in the event a recurrence rule exists in the parent event.
 * @author ewpatton
 *
 */
public class ServletInstance implements Instance {
	
	public static final String[] INSTANCE_PROJECTION = {
        Instances._ID,
        Instances.BEGIN,
        Instances.END,
        Instances.EVENT_ID
    };
    private static final int BEGIN = 1;
    private static final int END = 2;

    private final WeakReference<Event> event;
    private Calendar start;
    private Calendar end;

	ServletInstance( Event event ) {
        this.event = new WeakReference<Event>(event);
        this.start = Calendar.getInstance().start.dateTime();
        this.end = Calendar.getInstance().end.dateTime();
    }
	
    /**
     * Obtains a {@link java.util.Calendar Calendar} representing the starting
     * date and time of this instance.
     * @return
     */
    public java.util.Calendar getStart(){
		return this.start;
    }// /getStart()

    /**
     * Obtains a {@link java.util.Calendar Calendar} representing the ending
     * date and time of this instance.
     * @return
     */
    public java.util.Calendar getEnd(){
    	return this.end;
    }// /getEnd()

    /**
     * Gets the parent {@link Event} for this instance. Note: Implementations
     * SHOULD use {@link java.lang.ref.WeakReference} to maintain parent links
     * to keep the calendar structure tree-like for garbage collection.
     * @return
     */
    public Event getEvent(){
    	return this.event;
    }// /getEvent()
}
