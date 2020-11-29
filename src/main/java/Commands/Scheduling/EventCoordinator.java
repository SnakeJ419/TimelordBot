package Commands.Scheduling;

import java.util.ArrayList;

public class EventCoordinator {
	private final static ArrayList<Event> events = new ArrayList<>();

	protected static void addEvent(Event event){
		events.add(event);
	}

	protected static void removeEvent(Event event){
		events.remove(event);
	}
}
