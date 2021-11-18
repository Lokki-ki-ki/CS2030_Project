import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.Simulator2;

class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Double[]> arrivals = new ArrayList<>();

        int numOfServers = sc.nextInt();
        int queueLength = sc.nextInt();
        

        while (sc.hasNextDouble()) {
            double are = sc.nextDouble();
            double ser = sc.nextDouble();
            arrivals.add(new Double[]{are, ser});
        }


        Simulator2 s = new Simulator2(arrivals, new int[]{numOfServers, queueLength});
        s.simulate();
    }
}
