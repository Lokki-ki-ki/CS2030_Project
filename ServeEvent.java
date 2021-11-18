package cs2030.simulator;

class ServeEvent extends Event {
    private final int type;
    private final int serverId;
    private final boolean machine;
    
    ServeEvent(Customer customer, double time, int serverId) {
        super(customer, time);
        this.type = 4;
        this.serverId = serverId;
        this.machine = false;
    }

    ServeEvent(Customer customer, double time, int serverId, boolean machine) {
        super(customer, time);
        this.type = 4;
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
        + this.getCustomerId() + " serves by server " + this.serverId;
        }
        return String.format("%.3f", super.getTime()) + " " 
        + this.getCustomerId() + " serves by self-check " + this.serverId;
    }

}