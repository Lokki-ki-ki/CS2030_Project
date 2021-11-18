import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.Simulator3;


class Main3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Double[]> arrivals = new ArrayList<>();
        List<Double> restList = new ArrayList<>();

        int numOfServers = sc.nextInt();
        int queueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();


        while (numOfCustomers > 0) {
            double are = sc.nextDouble();
            double ser = sc.nextDouble();
            arrivals.add(new Double[]{are, ser});
            numOfCustomers = numOfCustomers - 1;
        }

        while (sc.hasNextDouble() || sc.hasNextInt()) {
            if (sc.hasNextInt()) {
                int zero = sc.nextInt();
                restList.add(0.0);
            } else {
                double nonzero = sc.nextDouble();
                restList.add(nonzero);
            }
        } 
    
        Simulator3 s = new Simulator3(arrivals, new int[]{numOfServers, queueLength}, restList);
        s.simulate();
    }
}
