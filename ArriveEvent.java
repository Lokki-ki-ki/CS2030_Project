package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;


class ArriveEvent extends Event {
    private final int type;
    private final List<Server> servers;
    
    ArriveEvent(Customer customer, double time) {
        super(customer, time);
        this.type = 1;
        this.servers = new ArrayList<Server>();
    }

    @Override
    int getType() {
        return this.type;
    }

    @Override
    int getServerId() {
        return 0;
    }

    @Override
    public String toString() {
        if (super.getCustomer().isGreedy()) {
            return String.format("%.3f", super.getTime()) + " " + super.getCustomerId() 
                + "(greedy) arrives";
        }
        return String.format("%.3f", super.getTime()) + " " + super.getCustomerId() + " arrives";
    }
}