
import java.util.ArrayList;
import java.util.List;

public class EVChargingSimulation {
    private static final int MAX_PATIENCE = 10;

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final Charger charger1 = new Charger(1, 50);
    private final Charger charger2 = new Charger(2, 50);

    private final VehicleQueue queue = new VehicleQueue(10);
    private final VehicleHeap heap = new VehicleHeap();

    public EVChargingSimulation() {
        vehicles.add(new Vehicle(1, 0, 20, 80, false));
        vehicles.add(new Vehicle(2, 0, 40, 80, false));
        vehicles.add(new Vehicle(3, 1, 30, 80, false));
        vehicles.add(new Vehicle(4, 2, 50, 80, true));
        vehicles.add(new Vehicle(5, 2, 60, 80, false));

        calculateDurations();
    }

    private void calculateDurations() {
        for (Vehicle v : vehicles) {
            int difference =
                v.getTargetBattery() - v.getCurrentBattery();

            int duration = Math.max(0, difference * 10)
                / charger1.getPower();

            v.setChargeDuration(duration);
        }
    }

    public void displayVehicles() {
        for (Vehicle v : vehicles) {
            v.display();
        }
    }

    public void runStrategy(int strategy) {
        System.out.println("\nRunning strategy: " + strategy);

        queue.clear();
        heap.clear();
        charger1.reset();
        charger2.reset();

        if (strategy != 1) {
            heap.setRule(strategy - 1);
        }

        int time = 0;
        int nextArrival = 0;
        int served = 0;
        int lost = 0;
        int totalWait = 0;
        int maxWait = 0;
        int totalChargeTime = 0;

        while (nextArrival < vehicles.size()
                || !queue.isEmpty() || !heap.isEmpty()
                || charger1.isBusy() || charger2.isBusy()) {

            while (nextArrival < vehicles.size()
                    && vehicles.get(nextArrival).getArrivalTime() <= time) {
                Vehicle v = vehicles.get(nextArrival);

                System.out.printf("Time %d: V%d arrived%s%n",
                    time, v.getId(),
                    v.isEmergency() ? " [EMERGENCY]" : "");

                if (strategy == 1) {
                    queue.enqueue(v);
                } else {
                    heap.insert(v);
                }

                nextArrival++;
            }

            Charger[] chargers = {charger1, charger2};

            for (Charger charger : chargers) {
                if (charger.isBusy() && charger.getFinishTime() == time) {
                    System.out.printf(
                        "V%d finished charging on Charger %d%n",
                        charger.getVehicleId(), charger.getId());
                    charger.finishCharging();
                }
            }

            for (Charger charger : chargers) {
                if (charger.isBusy()) continue;

                Vehicle v;

                do {
                    v = strategy == 1
                        ? queue.dequeue()
                        : heap.extractMin();

                    if (v != null) {
                        v.setWaitTime(time - v.getArrivalTime());

                        if (v.getWaitTime() > MAX_PATIENCE) {
                            System.out.println(
                                "V" + v.getId()
                                + " waited too long and left.");
                            lost++;
                            v = null;
                        }
                    }
                } while (v == null &&
                    (strategy == 1 ? !queue.isEmpty() : !heap.isEmpty()));

                if (v != null) {
                    v.setStartTime(time);
                    charger.assignVehicle(v, time);

                    served++;
                    totalWait += v.getWaitTime();
                    maxWait = Math.max(maxWait, v.getWaitTime());
                    totalChargeTime += v.getChargeDuration();
                }
            }

            int finish = -1;
            for (Charger charger : chargers) {
                if (charger.isBusy() &&
                    (finish == -1 || charger.getFinishTime() < finish)) {
                    finish = charger.getFinishTime();
                }
            }

            int arrival = nextArrival < vehicles.size()
                ? vehicles.get(nextArrival).getArrivalTime() : -1;

            if (arrival != -1 && finish != -1)
                time = Math.min(arrival, finish);
            else if (arrival != -1)
                time = arrival;
            else if (finish != -1)
                time = finish;
            else
                break;
        }

        double averageWait = served == 0
            ? 0.0 : (double) totalWait / served;

        double utilization = time == 0
            ? 0.0 : (double) totalChargeTime / (2 * time) * 100;

        utilization = Math.min(utilization, 100.0);

        System.out.println("\nVehicles Served: " + served);
        System.out.println("Vehicles Lost: " + lost);
        System.out.printf("Average Wait: %.2f%n", averageWait);
        System.out.println("Maximum Wait: " + maxWait);
        System.out.printf("Charger Utilization: %.2f%%%n", utilization);
    }

    public void runFIFO() {
        runStrategy(1);
    }

    public void runShortestFirst() {
        runStrategy(2);
    }

    public void runLowestBatteryFirst() {
        runStrategy(3);
    }

    public void runEmergencyAging() {
        runStrategy(4);
    }
}
