package cs2030.simulator;

class Customer {
    private final int id;
    private final double arrivalTime;
    private final double timeNeed;
    private final boolean greedy;

    Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = 1.0;
        this.greedy = false;
    }

    Customer(int id, double arrivalTime, double timeNeed) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = timeNeed;
        this.greedy = false;
    }

    Customer(int id, double arrivalTime, boolean greedy) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = 1.0;
        this.greedy = greedy;
    }

    Customer(int id, double arrivalTime, double timeNeed, boolean greedy) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = timeNeed;
        this.greedy = greedy;
    }

    // Customer(int id, double arrivalTime, double timeNeed, double timeServed) {
    //     this.id = id;
    //     this.arrivalTime = arrivalTime;
    //     this.timeNeed = timeNeed;
    //     this.timeServed = timeServed;
    // }

    int getId() {
        return this.id;
    }

    double getTime() {
        return this.arrivalTime;
    }

    double getTimeNeed() {
        return this.timeNeed;
    }

    Customer updateTimeNeed(double time) {
        return new Customer(this.id, this.arrivalTime, time, this.greedy);
    }

    boolean isGreedy() {
        return this.greedy;
    }

    // Customer beServed(double time) {
    //     return new Customer(this.id, this.arrivalTime, this.timeNeed, time)
    // }

}