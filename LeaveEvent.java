package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;

class LeaveEvent extends Event {
    private static final int SIX = 6;
    private final int type;
    
    LeaveEvent(Customer customer, double time) {
        super(customer, time);
        this.type = SIX;
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
                + "(greedy) leaves";
        }
        return String.format("%.3f", super.getTime()) + " " + super.getCustomerId() + " leaves";
    }
}