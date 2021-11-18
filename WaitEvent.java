package cs2030.simulator;


class WaitEvent extends Event {
    private final int type;
    private final int serverId;
    private final boolean machine;
    
    WaitEvent(Customer customer, double time, int serverId) {
        super(customer, time);
        this.type = 2;
        this.serverId = serverId;
        this.machine = false;
    }

    WaitEvent(Customer customer, double time, int serverId, boolean machine) {
        super(customer, time);
        this.type = 2;
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
            if (super.getCustomer().isGreedy()) {
                return String.format("%.3f", super.getTime()) + " " + this.getCustomerId() 
                    + "(greedy) waits at server " + this.serverId;
            } else {
                return String.format("%.3f", super.getTime()) + " " + this.getCustomerId() 
                    + " waits at server " + this.serverId;
            }
        } else {
            if (super.getCustomer().isGreedy()) {
                return String.format("%.3f", super.getTime()) + " " + this.getCustomerId() 
                    + "(greedy) waits at self-check " + this.serverId;
            } else {
                return String.format("%.3f", super.getTime()) + " " + this.getCustomerId() 
                    + " waits at self-check " + this.serverId;
            }
        }
        
    }
}