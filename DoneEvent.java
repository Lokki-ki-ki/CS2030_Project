package cs2030.simulator;
import java.util.List;
import java.util.ArrayList;

class DoneEvent extends Event {
    private final int type;
    private final int serverId;
    private final boolean machine;
    
    DoneEvent(Customer customer, double time, int serverId) {
        super(customer, time);
        this.type = 5;
        this.serverId = serverId;
        this.machine = false;
    }

    DoneEvent(Customer customer, double time, int serverId, boolean machine) {
        super(customer, time);
        this.type = 5;
        this.serverId = serverId;
        this.machine = machine;
    }

    @Override
    int getType() {
        return this.type;
    }

    @Override
    int getServerId() {
        return this.serverId;
    }

    @Override
    public String toString() {
        if (!this.machine) {
            return String.format("%.3f", super.getTime()) + " " 
        + this.getCustomerId() + " done serving by server " + this.serverId;
        }
        return String.format("%.3f", super.getTime()) + " " 
        + this.getCustomerId() + " done serving by self-check " + this.serverId;
    }
}