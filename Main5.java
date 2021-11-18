import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.Simulator5;

class Main5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int seed = sc.nextInt();
        int numOfServers = sc.nextInt();
        int numOfSelf = sc.nextInt();
        int queueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();
        double arrivalRate = sc.nextDouble();
        double serviceRate = sc.nextDouble();
        double restRate = sc.nextDouble();
        double restPro = sc.nextDouble();
        double greedyPro = sc.nextDouble(); 
    
        Simulator5 s = new Simulator5(seed, numOfServers, numOfSelf, queueLength, numOfCustomers, 
            arrivalRate, serviceRate, restRate, restPro, greedyPro);
        s.simulate();
    }
    
}
