package cs2030.simulator;
import java.util.List;

abstract class Event {
    private final Customer customer;
    private final double time;

    Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }

    double getTime() {
        return this.time;
    }

    int getCustomerId() {
        return this.customer.getId();
    }

    Customer getCustomer() {
        return this.customer;
    }

    abstract int getType();

    abstract int getServerId();

}