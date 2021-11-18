package cs2030.simulator;

class RestEvent extends Event {
    private final int type;
    private final int serverId;

    RestEvent(Customer customer, double time, int serverId) {
        super(customer, time);
        this.type = 3;
        this.serverId = serverId;
    }

    @Override
    int getType() {
        return this.type;
    }

    @Override
    int getServerId() {
        return this.serverId;
    }

}