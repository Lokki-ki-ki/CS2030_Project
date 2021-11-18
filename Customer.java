package cs2030.simulator;

class Customer {
    private final int id;
    private final double arrivalTime;
    private final double timeNeed;
    //private final double timeServed;

    Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = 1.0;
        //this.timeServed = 0.0;
    }

    Customer(int id, double arrivalTime, double timeNeed) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.timeNeed = timeNeed;
        //this.timeServed = 0.0;
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

    // Customer beServed(double time) {
    //     return new Customer(this.id, this.arrivalTime, this.timeNeed, time)
    // }

}