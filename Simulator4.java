package cs2030.simulator;
import java.util.List;
import java.beans.Customizer;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Stream;


public class Simulator4 {
    private final List<Customer> customers;
    private final List<Server> servers;
    private final PriorityQueue<Event> queue;
    private final List<Double> restList;
    private final int machineLimit;
    

    public Simulator4(List<Double[]> arrivalsList, int[] serInfo, List<Double> restList) {
        this.customers = new ArrayList<Customer>();
        this.servers = new ArrayList<Server>();
        for (int i = 0; i < arrivalsList.size(); i++) {
            Double[] pair = arrivalsList.get(i);
            customers.add(new Customer(i + 1, pair[0], pair[1]));
        }
        for (int j = 0; j < serInfo[0]; j++) {
            servers.add(new Server(j + 1, serInfo[1]));
        }
        for (int j = serInfo[0]; j < serInfo[0] + serInfo[2]; j++) {
            servers.add(new Server(j + 1, serInfo[1], true));
        }
        this.queue = new PriorityQueue<Event>(new EventComparator());
        this.restList = restList;
        this.machineLimit = serInfo[1];
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

    public boolean checkMachine() {
        int count = 0;
        for (Server s : this.servers) {
            if (s.isMachine()) {
                if (s.getStatus().equals("idle")) {
                    count++;
                }
            }
        }
        return count > 0;
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

    public void simulate() {

        int numOfLeave = 0;
        double waitTime = 0.0;

        for (Customer c : this.customers) {
            this.queue.add(new ArriveEvent(c, c.getTime()));
        }

        PriorityQueue<Event> newQueue = new PriorityQueue<Event>(new EventComparator());
        List<Customer> machineCustomers = new ArrayList<Customer>();
        

        while(!this.queue.isEmpty()) {
            Event event = this.queue.poll();
            Customer customer = event.getCustomer();

            if (event.getType() == 1) {
                boolean execute = false;
                
                /** Check if there is idle */
                d: for (int i = 0; i < this.servers.size() - this.getMachineNum(); i++) {
                    Server s = this.servers.get(i);
                    if (s.canServe(customer)) {
                        if (s.getStatus().equals("idle")) {
                            Event nextEvent = new ServeEvent(customer, event.getTime(), i + 1);
                            s = s.setStatus("work");
                            this.updateServer(s);
                            this.queue.add(nextEvent);//add ServeEvent
                            execute = true;
                            newQueue.add(event);//print this arrive
                            break d;
                        } 
                    } 
                }

                /** Check if there is available self */
                if (execute == false && this.checkMachine()) {
                    c: for (int i = this.getMachineNum() - 1; i < this.servers.size(); i++) {
                        Server s = this.servers.get(i);
                        if (s.getStatus().equals("idle")) {
                            Event nextEvent = new ServeEvent(customer, event.getTime(), i + 1, true);
                            s = s.setStatus("work");
                            this.updateServer(s);
                            this.queue.add(nextEvent);//add ServeEvent
                            execute = true;
                            newQueue.add(event);//print this arrive
                            break c;
                        }
                    }

                }

                /** Check if there is rest idle */
                if (execute == false) {
                    a: for (int i = 0; i < this.servers.size() - this.getMachineNum(); i++) {
                        Server s = this.servers.get(i);
                        if (s.canServe(customer)) {
                            execute = true;
                            s.queue(customer);
                            newQueue.add(event);
                            break a;
                        }
                    }
                }

                /** Check if can queue in work server */
                if (execute == false) {
                    b: for (int i = 0; i < this.servers.size() - this.getMachineNum(); i++) {
                        Server s = this.servers.get(i);
                        if (s.canQueue()) {
                            execute = true;
                            s.queue(customer);
                            Event nextEvent = new WaitEvent(customer, event.getTime(), i + 1);
                            newQueue.add(event);//print ArriveEvent
                            newQueue.add(nextEvent);//print WaitEvent
                            break b;
                        }
                    }
                }

                /** Check if can queue in machine */
                if (execute == false) {
                    if (this.machineLimit > machineCustomers.size() && this.getMachineNum() > 0) {
                        execute = true;
                        machineCustomers.add(customer);
                        Event nextEvent = new WaitEvent(customer, event.getTime(), this.servers.size() - this.getMachineNum(), true);
                        newQueue.add(event);//print ArriveEvent
                        newQueue.add(nextEvent);//print WaitEvent
                    }
                }
                
                /** Else only Leave */
                if (execute == false) {
                    Event nextEvent = new LeaveEvent(customer, event.getTime());
                    numOfLeave++;
                    newQueue.add(nextEvent);//print LeaveEvent
                    newQueue.add(event);//print ArriveEvent
                }
                
            }

            if (event.getType() == 3) {
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

            if (event.getType() == 4) {//ServeEvent
                newQueue.add(event);//print ServeEvent
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));
                Event doneEvent = s.serve(customer, event.getTime(), s.isMachine());
                this.queue.add(doneEvent);
                waitTime += event.getTime() - customer.getTime();
                
            }

            if (event.getType() == 5) {//DoneEvent
                newQueue.add(event);//print DoneEvent
                
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));

                if (!s.isMachine()) {
                    s = s.setRestTime(this.restList.get(0));
                    this.updateServer(s);
                    this.restList.remove(0);
                }
                

                if (s.isMachine()) {
                    if (machineCustomers.size() > 0) {
                        Customer nextcustomer = machineCustomers.get(0);
                        machineCustomers.remove(0);
                        Event nextEvent = new ServeEvent(nextcustomer, event.getTime(), id, true);
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
                    Event nextEvent = new RestEvent(customer, event.getTime() + s.getRestTime(), id);
                    this.queue.add(nextEvent);
                }
            }
        }
 
            


        while(!newQueue.isEmpty()) {
            Event event = newQueue.poll();
            System.out.println(event);
        }

        int numOfServed = this.customers.size() - numOfLeave;
        System.out.println("[" + String.format("%.3f", waitTime/numOfServed) + " " + numOfServed + " " + numOfLeave + "]");
    }
}
