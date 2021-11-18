package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;

class Server {
    private final int id;
    private final int queueLength;
    private final List<Customer> customerInQueue;
    private final String status;
    private final double restTime;
    private final boolean machine;

    Server(int id) {
        this.id = id;
        this.queueLength = 1;
        this.customerInQueue = new ArrayList<Customer>();
        this.status = "idle";
        this.restTime = 0.0;
        this.machine = false;
    }

    Server(int id, int queueLength) {
        this.id = id;
        this.queueLength = queueLength;
        this.customerInQueue = new ArrayList<Customer>();
        this.status = "idle";
        this.restTime = 0.0;
        this.machine = false;
    }

    Server(int id, int queueLength, boolean machine) {
        this.id = id;
        this.queueLength = queueLength;
        this.customerInQueue = new ArrayList<Customer>();
        this.status = "idle";
        this.restTime = 0.0;
        this.machine = machine;
    }

    Server(int id, int queueLength, List<Customer> customerInQueue, 
        String status, double restTime, boolean machine) {
        this.id = id;
        this.queueLength = queueLength;
        this.customerInQueue = customerInQueue;
        this.status = status;
        this.restTime = restTime;
        this.machine = machine;
    }

    boolean canServe(Customer customer) {
        if (this.status.equals("idle")) {
            return true;
        }
        if (this.status.equals("rest")) {
            if (this.customerInQueue.size() == 0) {
                return true;
            }
        }
        return false;
    }

    boolean canQueue() {
        int numInQueue = this.customerInQueue.size();
        if (this.status.equals("work")) {
            if (numInQueue < this.queueLength) {
                return true;
            }
        }
        if (this.status.equals("rest")) {
            if (numInQueue < this.queueLength) {
                return true;
            }
        }
        return false;
    }

    void queue(Customer customer) {
        this.customerInQueue.add(customer);
    }

    boolean hasNext() {
        if (this.customerInQueue.isEmpty()) {
            return false;
        }
        return true;
    }

    String getStatus() {
        return this.status;
    }

    Customer getNext() {
        Customer result = this.customerInQueue.get(0);
        this.customerInQueue.remove(0);
        return result;
    }

    Server setStatus(String string) {
        return new Server(this.id, this.queueLength, this.customerInQueue, 
            string, this.restTime, this.machine);
    }

    Event serve(Customer customer, double time, boolean machine) {
        double lastTime = time;
        //double customerTime = customer.getTime();
        if (machine) {
            return new DoneEvent(customer, time + customer.getTimeNeed(), this.id, true);
        }

        return new DoneEvent(customer, time + customer.getTimeNeed(), this.id, false);
    }

    Event serve(Customer customer, double time, boolean machine, double serveTime) {
        double lastTime = time;
        //double customerTime = customer.getTime();
        if (machine) {
            return new DoneEvent(customer, time + serveTime, this.id, true);
        }

        return new DoneEvent(customer, time + serveTime, this.id, false);
    }

    Server setRestTime(double restTime) {
        return new Server(this.id, this.queueLength, this.customerInQueue, 
            this.status, restTime, this.machine);
    }

    double getRestTime() {
        return this.restTime;
    }

    int getId() {
        return this.id;
    }

    boolean isMachine() {
        return this.machine;
    }

    int getQueueLength() {
        return this.customerInQueue.size();
    }

    @Override
    public String toString() {
        if (this.machine) {
            return "self-check server";
        } else {
            return "server";
        }
    }
}