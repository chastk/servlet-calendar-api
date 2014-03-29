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
	
    public java.util.Calendar getStart(){
		return (Calendar) this.start;
    }// /getStart()

    public java.util.Calendar getEnd(){
    	return (Calendar) this.end;
    }// /getEnd()

    public Event getEvent(){
    	return this.event.get();
    }// /getEvent()
}
