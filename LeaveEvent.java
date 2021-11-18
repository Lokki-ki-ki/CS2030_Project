package cs2030.simulator;
import java.util.List;
import java.util.ArrayList;

class LeaveEvent extends Event {
    private final int type;
    
    LeaveEvent(Customer customer, double time) {
        super(customer, time);
        this.type = 6;
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
        return String.format("%.3f", super.getTime()) + " " + super.getCustomerId() + " leaves";
    }
}