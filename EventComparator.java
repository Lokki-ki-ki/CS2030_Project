package cs2030.simulator;

import java.util.Comparator;

class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event a, Event b) {

        if (a.getTime() > b.getTime()) {
            return 1;
        } else if (a.getTime() < b.getTime()) {
            return -1;
        }

        if (a.getCustomerId() > b.getCustomerId()) {
            return 1;
        } else if (a.getCustomerId() < b.getCustomerId()) {
            return -1;
        }

            
        if (a.getType() > b.getType()) {
            return 1;
        } else if (a.getType() < b.getType()) {
            return -1;
        }
                
        return 0;
            
    }
}
