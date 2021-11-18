package cs2030.simulator;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Simulator2 {
    private final List<Customer> customers;
    private final List<Server> servers;
    private final PriorityQueue<Event> queue;
    

    public Simulator2(List<Double[]> arrivalsList, int[] serInfo) {
        this.customers = new ArrayList<Customer>();
        this.servers = new ArrayList<Server>();
        for (int i = 0; i < arrivalsList.size(); i++) {
            Double[] pair = arrivalsList.get(i);
            customers.add(new Customer(i + 1, pair[0], pair[1]));
        }
        for (int j = 0; j < serInfo[0]; j++) {
            servers.add(new Server(j + 1, serInfo[1]));
        }
        this.queue = new PriorityQueue<Event>(new EventComparator());
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

    public void simulate() {

        int numOfLeave = 0;
        double waitTime = 0.0;

        for (Customer c : this.customers) {
            this.queue.add(new ArriveEvent(c, c.getTime()));
        }

        PriorityQueue<Event> newQueue = new PriorityQueue<Event>(new EventComparator());
        while(!this.queue.isEmpty()) {
            Event event = this.queue.poll();
            Customer customer = event.getCustomer();

            if (event.getType() == 1) {
                boolean execute = false;
                
                for (int i = 0; i < this.servers.size(); i++) {
                    Server s = this.servers.get(i);
                    if (s.canServe(customer)) {
                        execute = true;
                        Event nextEvent = new ServeEvent(customer, event.getTime(), i + 1);
                        s = s.setStatus("work");
                        this.updateServer(s);
                        this.queue.add(nextEvent);//add ServeEvent
                        newQueue.add(event);//print this arrive
                        break;
                    } 
                }

                if (execute == false) {
                    for (int i = 0; i < this.servers.size(); i++) {
                        Server s = this.servers.get(i);
                        if (s.canQueue()) {
                            execute = true;
                            s.queue(customer);
                            Event nextEvent = new WaitEvent(customer, event.getTime(), i + 1);
                            newQueue.add(event);//print ArriveEvent
                            newQueue.add(nextEvent);//print WaitEvent
                            break;
                        }
                    }
                }
                
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
                } else {
                    s = s.setStatus("idle");
                }
                this.updateServer(s);
            }

            if (event.getType() == 4) {//ServeEvent
                newQueue.add(event);//print ServeEvent
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));
                Event doneEvent = s.serve(customer, event.getTime(), false);
                this.queue.add(doneEvent);
                waitTime += event.getTime() - customer.getTime();
                
            }

            if (event.getType() == 5) {
                newQueue.add(event);//print DoneEvent
                
                int id = event.getServerId();
                Server s = this.servers.get(this.getIndex(id));

                s = s.setRestTime(0.0);
                this.updateServer(s);

                if (s.getRestTime() == 0.0) {
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
                
                //s = s.rest(this.restList.get(0));
                //this.restList.remove(0);
                
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


