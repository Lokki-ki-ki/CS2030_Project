package cs2030.simulator;

import java.util.List;
import java.beans.Customizer;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Stream;


public class Simulator5 {
    private final List<Customer> customers;
    private final List<Server> servers;
    private final PriorityQueue<Event> queue;
    private final int machineLimit;
    private final int machineNum;
    private final RandomGenerator generator;
    private final double restPro;
    private static final int RESTEVENT_TYPE = 3;
    private static final int SERVEEVENT_TYPE = 4;
    private static final int DONEEVENT_TYPE = 5;
    
    /**
     * This is the simulator for Main5.java.
     * @param seed seed.
     * @param numOfServers Number of servers. 
     * @param numOfSelf Number of self-check machines.
     * @param queueLength Length of queue.
     * @param numOfCustomers Number of Customers.
     * @param arrivalRate Arrival rate.
     * @param serviceRate Service rate.
     * @param restRate Rest rate.
     * @param restPro Rest Probability.
     * @param greedyPro The probability of greedy customer.
     */
    public Simulator5(int seed, int numOfServers, int numOfSelf, int queueLength, 
        int numOfCustomers, double arrivalRate, double serviceRate, double restRate, 
        double restPro, double greedyPro) {
        this.generator = new RandomGenerator(seed, arrivalRate, serviceRate, restRate);
        this.customers = new ArrayList<Customer>();
        this.servers = new ArrayList<Server>();
        double arrive = 0.0;
        for (int i = 0; i < numOfCustomers; i++) {
            boolean greedy = generator.genCustomerType() < greedyPro;
            if (i == 0) {
                customers.add(new Customer(i + 1, 0.0, greedy));
                arrive += generator.genInterArrivalTime(); 
            } else {
                customers.add(new Customer(i + 1, arrive, greedy));
                arrive += generator.genInterArrivalTime(); 
            }
        }

        for (int j = 0; j < numOfServers; j++) {
            servers.add(new Server(j + 1, queueLength));
        }
        for (int j = numOfServers; j < numOfServers + numOfSelf; j++) {
            servers.add(new Server(j + 1, queueLength, true));
        }
        this.queue = new PriorityQueue<Event>(new EventComparator());
        this.machineLimit = queueLength;
        this.machineNum = numOfSelf;
        this.restPro = restPro;
    }

    int getIndex(int serverId) {
        for (int i = 0; i < this.servers.size(); i++) {
            if (serverId == this.servers.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    void updateServer(Server server) {
        int index = this.getIndex(server.getId());
        this.servers.set(index, server);
    }

    /** Check there is machine in Servers Lists and at least one of them "idle". */
    boolean checkMachine() {
        int count = 0;
        int num = 0;
        for (Server s : this.servers) {
            if (s.isMachine()) {
                num++;
                if (s.getStatus().equals("idle")) {
                    count++;
                }
            }
        }
        return count > 0 && num > 0;
    }

    int getServerWithShortQueue() {
        int nowQueue = this.servers.get(0).getQueueLength();
        int index = 0;
        for (int i = 0; i < this.servers.size() - this.machineNum; i++) {
            Server s = this.servers.get(i);
            if (nowQueue > s.getQueueLength()) {
                nowQueue = s.getQueueLength();
                index = i;
            }
        }
        return index;
    }

    int getMachineNum() {
        int count = 0;
        for (Server s : this.servers) {
            if (s.isMachine()) {
                count++;
            }
        }
        return count;
    }

    /**
     * This is the method for simulation.
     */
    public void simulate() {

        int numOfLeave = 0;
        double waitTime = 0.0;

        for (Customer c : this.customers) {
            this.queue.add(new ArriveEvent(c, c.getTime()));
        }

        PriorityQueue<Event> newQueue = new PriorityQueue<Event>(new EventComparator());
        List<Customer> machineCustomers = new ArrayList<Customer>();
        

        while (!this.queue.isEmpty()) {
            Event event = this.queue.poll();
            Customer customer = event.getCustomer();

            if (event.getType() == 1) {
                boolean execute = false;
                
                /** Check if there is idle */
                d: for (int i = 0; i < this.servers.size() - this.machineNum; i++) {
                    Server s = this.servers.get(i);
                    if (s.canServe(customer) && s.getStatus().equals("idle")) {
                            Event nextEvent = new ServeEvent(customer, event.getTime(), i + 1);
                            s = s.setStatus("work");
                            this.updateServer(s);
                            this.queue.add(nextEvent);//add ServeEvent
                            execute = true;
                            newQueue.add(event);//print this arrive
                            break d;
                        } 
                }

                /** Check if there is available self */
                if (execute == false && this.checkMachine()) {
                    c: for (int i = this.servers.size() - this.machineNum; 
                        i < this.servers.size(); i++) {
                        Server s = this.servers.get(i);
                        if (s.getStatus().equals("idle")) {
                            Event nextEvent = new ServeEvent(customer, event.getTime(), 
                                i + 1, true);
                            s = s.setStatus("work");
                            this.updateServer(s);
                            this.queue.add(nextEvent);//add ServeEvent
                            execute = true;
                            newQueue.add(event);//print this arrive
                            break c;
                        }
                    }

                }

                if (execute == false && customer.isGreedy()) {
                    int index = this.getServerWithShortQueue();
                    Server s = this.servers.get(index);
                    if (s.canQueue() && s.getQueueLength() > machineCustomers.size() 
                        && this.machineNum != 0) {
                        execute = true;
                        machineCustomers.add(customer);
                        Event nextEvent = new WaitEvent(customer, event.getTime(), 
                            this.servers.size() - this.machineNum + 1, true);
                        newQueue.add(event);//print ArriveEvent
                        newQueue.add(nextEvent);//print WaitEvent
                    } else if (s.canQueue()) {
                        execute = true;
                        s.queue(customer);
                        Event nextEvent = new WaitEvent(customer, event.getTime(), 
                            index + 1, false);
                        newQueue.add(event);//print ArriveEvent
                        newQueue.add(nextEvent);//print WaitEvent
                    }
                }

                /** Check if can queue in work server */
                if (execute == false) {
                    b: for (int i = 0; i < this.servers.size() - this.machineNum; i++) {
                        Server s = this.servers.get(i);
                        if (s.canQueue()) {
                            execute = true;
                            s.queue(customer);
                            Event nextEvent = new WaitEvent(customer, event.getTime(), 
                                i + 1, false);
                            newQueue.add(event);//print ArriveEvent
                            newQueue.add(nextEvent);//print WaitEvent
                            break b;
                        }
                    }
                }


                /** Check if can queue in machine */
                if (execute == false) {
                    if (this.machineLimit > machineCustomers.size() && 
                        this.getMachineNum() > 0) {
                        execute = true;
                        machineCustomers.add(customer);
                        Event nextEvent = new WaitEvent(customer, event.getTime(), 
                            this.servers.size() - this.machineNum + 1, true);
                        newQueue.add(event);//print ArriveEvent
                        newQueue.add(nextEvent);//print WaitEvent
                    }
                }

                // /** Check if there is rest idle */
                // if (execute == false) {
                //     a: for (int i = 0; i < this.servers.size() - this.machineNum; i++) {
                //         Server s = this.servers.get(i);
                //         if (s.canServe(customer)) {
                //             execute = true;
                //             s.queue(customer);
                //             newQueue.add(event);
                //             break a;
                //         }
                //     }
                // }
                
                /** Else only Leave */
                if (execute == false) {
                    Event nextEvent = new LeaveEvent(customer, event.getTime());
                    numOfLeave++;
                    newQueue.add(nextEvent);//print LeaveEvent
                    newQueue.add(event);//print ArriveEvent
                }
                
            }

            if (event.getType() == RESTEVENT_TYPE) {
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));
                if (s.hasNext()) {
                    Customer nextcustomer = s.getNext();
                    Event nextEvent = new ServeEvent(nextcustomer, event.getTime(), id);
                    this.queue.add(nextEvent);
                    s = s.setStatus("work");
                    this.updateServer(s);
                } else {
                    s = s.setStatus("idle");
                    this.updateServer(s);
                }
            }

            if (event.getType() == SERVEEVENT_TYPE) { //ServeEvent
                newQueue.add(event);//print ServeEvent
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));
                double ti = generator.genServiceTime();
                Event doneEvent = s.serve(customer, event.getTime(), s.isMachine(), ti);
                this.queue.add(doneEvent);
                waitTime += event.getTime() - customer.getTime();
                
            }

            if (event.getType() == DONEEVENT_TYPE) { //DoneEvent
                newQueue.add(event);//print DoneEvent
                
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));

                if (!s.isMachine()) {
                    if (generator.genRandomRest() < this.restPro) {
                        s = s.setRestTime(generator.genRestPeriod());
                        this.updateServer(s);
                    } else {
                        s = s.setRestTime(0.0);
                        this.updateServer(s);
                    }
                }
                
                if (s.isMachine()) {
                    if (machineCustomers.size() > 0) {
                        Customer nextcustomer = machineCustomers.get(0);
                        machineCustomers.remove(0);
                        Event nextEvent = new ServeEvent(nextcustomer, event.getTime(), 
                            id, true);
                        this.queue.add(nextEvent);
                    } else {
                        s = s.setStatus("idle");
                        this.updateServer(s);
                    }
                } else if (s.getRestTime() == 0.0) {
                    if (s.hasNext()) {
                        Customer nextcustomer = s.getNext();
                        Event nextEvent = new ServeEvent(nextcustomer, event.getTime(), id);
                        this.queue.add(nextEvent);
                    } else {
                        s = s.setStatus("idle");
                        this.updateServer(s);
                    }
                } else {
                    s = s.setStatus("rest");
                    this.updateServer(s);
                    Event nextEvent = new RestEvent(customer, event.getTime() 
                        + s.getRestTime(), id);
                    this.queue.add(nextEvent);
                }
            }
        }
 
            

        while (!newQueue.isEmpty()) {
            Event event = newQueue.poll();
            System.out.println(event);
        }

        int numOfServed = this.customers.size() - numOfLeave;
        System.out.println("[" + String.format("%.3f", waitTime / numOfServed) 
            + " " + numOfServed + " " + numOfLeave + "]");
    }
}
