
import java.util.ArrayList;

public class EVChargingSimulation {

    static final int MAX_VEHICLES = 5;
    static final int MAX_QUEUE = 10;
    static final int MAX_PATIENCE = 10;

    static int[] strategyServed = new int[5];
    static int[] strategyLost = new int[5];
    static double[] strategyAverageWait = new double[5];
    static int[] strategyMaxWait = new int[5];
    static double[] strategyUtilization = new double[5];

    /* ================================================
       VEHICLE CLASS
       ================================================ */

    static class Vehicle {
        int id, arrivalTime, currentBattery, targetBattery;
        int chargeDuration, emergency, startTime, waitTime;

        Vehicle(int id, int arrivalTime, int currentBattery,
                int targetBattery, int chargeDuration,
                int emergency, int startTime, int waitTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.currentBattery = currentBattery;
            this.targetBattery = targetBattery;
            this.chargeDuration = chargeDuration;
            this.emergency = emergency;
            this.startTime = startTime;
            this.waitTime = waitTime;
        }

        Vehicle copy() {
            return new Vehicle(id, arrivalTime, currentBattery,
                    targetBattery, chargeDuration, emergency,
                    startTime, waitTime);
        }
    }

    /* ================================================
       CHARGER CLASS
       ================================================ */

    static class Charger {
        int id, power, busy, vehicleId, finishTime;

        Charger(int id, int power) {
            this.id = id;
            this.power = power;
        }
    }

    /* ================================================
       DATA
       ================================================ */

    static Vehicle[] vehicles = {
        new Vehicle(1, 0, 20, 80, 0, 0, -1, 0),
        new Vehicle(2, 0, 40, 80, 0, 0, -1, 0),
        new Vehicle(3, 1, 30, 80, 0, 0, -1, 0),
        new Vehicle(4, 2, 50, 80, 0, 1, -1, 0),
        new Vehicle(5, 2, 60, 80, 0, 0, -1, 0)
    };

    static Charger c1 = new Charger(1, 50);
    static Charger c2 = new Charger(2, 50);

    /* ================================================
       CHARGING DURATION
       ================================================ */

    static int calculateChargeDuration(
            int currentBattery, int targetBattery, int power) {
        if (power <= 0) return 0;
        return ((targetBattery - currentBattery) * 10) / power;
    }

    static void calculateDurations() {
        for (Vehicle v : vehicles) {
            v.chargeDuration = calculateChargeDuration(
                    v.currentBattery, v.targetBattery, c1.power);
        }
    }

    /* ================================================
       FIFO QUEUE
       ================================================ */

    static class WaitingQueue {
        ArrayList<Vehicle> queue = new ArrayList<>();

        void enqueue(Vehicle v) {
            if (queue.size() >= MAX_QUEUE) {
                System.out.println("Waiting queue is full!");
                return;
            }
            queue.add(v.copy());
        }

        Vehicle dequeue() {
            return queue.isEmpty() ? null : queue.remove(0);
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    /* ================================================
       PRIORITY HEAP
       ================================================ */

    static class VehicleHeap {
        Vehicle[] heap = new Vehicle[20];
        int size = 0;
        int rule = 1;

        void setHeapRule(int rule) {
            this.rule = rule;
        }

        boolean higherPriority(Vehicle a, Vehicle b) {
            if (rule == 1) {
                if (a.chargeDuration != b.chargeDuration)
                    return a.chargeDuration < b.chargeDuration;
                return a.id < b.id;
            }

            if (rule == 2) {
                if (a.currentBattery != b.currentBattery)
                    return a.currentBattery < b.currentBattery;
                if (a.arrivalTime != b.arrivalTime)
                    return a.arrivalTime < b.arrivalTime;
                return a.id < b.id;
            }

            if (rule == 3) {
                if (a.emergency != b.emergency)
                    return a.emergency > b.emergency;
                if (a.waitTime != b.waitTime)
                    return a.waitTime > b.waitTime;
                return a.id < b.id;
            }

            return false;
        }

        void swap(int i, int j) {
            Vehicle temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        void heapifyUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (!higherPriority(heap[i], heap[parent])) break;
                swap(i, parent);
                i = parent;
            }
        }

        void heapifyDown(int i) {
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int best = i;

                if (left < size &&
                        higherPriority(heap[left], heap[best]))
                    best = left;

                if (right < size &&
                        higherPriority(heap[right], heap[best]))
                    best = right;

                if (best == i) break;

                swap(i, best);
                i = best;
            }
        }

        void insertHeap(Vehicle v) {
            if (size == heap.length) {
                System.out.println("Heap is full!");
                return;
            }

            heap[size] = v.copy();
            heapifyUp(size++);
        }

        Vehicle extractMin() {
            if (size == 0) return null;

            Vehicle result = heap[0];
            size--;

            if (size > 0) {
                heap[0] = heap[size];
                heapifyDown(0);
            }

            heap[size] = null;
            return result;
        }

        boolean isHeapEmpty() {
            return size == 0;
        }

        void resetHeap() {
            for (int i = 0; i < size; i++) heap[i] = null;
            size = 0;
        }
    }

    static VehicleHeap heap = new VehicleHeap();

    /* ================================================
       CHARGER MANAGEMENT
       ================================================ */

    static void resetChargers() {
        c1.busy = c2.busy = 0;
        c1.vehicleId = c2.vehicleId = 0;
        c1.finishTime = c2.finishTime = 0;
    }

    static int getNextEventTime() {
        int next = -1;

        if (c1.busy == 1) next = c1.finishTime;

        if (c2.busy == 1 &&
                (next == -1 || c2.finishTime < next))
            next = c2.finishTime;

        return next;
    }

    static void finishCharging(int time) {
        if (c1.busy == 1 && c1.finishTime == time) {
            System.out.printf("V%d finished charging on Charger %d%n",
                    c1.vehicleId, c1.id);
            c1.busy = c1.vehicleId = c1.finishTime = 0;
        }

        if (c2.busy == 1 && c2.finishTime == time) {
            System.out.printf("V%d finished charging on Charger %d%n",
                    c2.vehicleId, c2.id);
            c2.busy = c2.vehicleId = c2.finishTime = 0;
        }
    }

    static void assignVehicle(Charger charger, Vehicle v, int time) {
        charger.busy = 1;
        charger.vehicleId = v.id;
        charger.finishTime = time + v.chargeDuration;

        System.out.printf("V%d starts charging on Charger %d at time %d%n",
                v.id, charger.id, time);
        System.out.println("Expected finish time: " + charger.finishTime);
    }

    /* ================================================
       SELECT VEHICLES
       ================================================ */

    static Vehicle getFIFO(WaitingQueue q, int time, int[] lost) {
        while (!q.isEmpty()) {
            Vehicle v = q.dequeue();
            v.waitTime = time - v.arrivalTime;

            if (v.waitTime > MAX_PATIENCE) {
                System.out.println("V" + v.id + " waited too long and left.");
                lost[0]++;
            } else {
                return v;
            }
        }
        return null;
    }

    static Vehicle getPriorityVehicle(int time, int[] lost) {
        while (!heap.isHeapEmpty()) {
            Vehicle v = heap.extractMin();
            v.waitTime = time - v.arrivalTime;

            if (v.waitTime > MAX_PATIENCE) {
                System.out.println("V" + v.id + " waited too long and left.");
                lost[0]++;
            } else {
                return v;
            }
        }
        return null;
    }

    /* ================================================
       STRATEGY NAME
       ================================================ */

    static void printStrategyName(int strategy) {
        String[] names = {
            "", "FIFO SIMULATION", "SHORTEST CHARGE FIRST",
            "LOWEST BATTERY FIRST", "EMERGENCY + AGING"
        };

        System.out.println("\n==============================");
        System.out.println(names[strategy]);
        System.out.println("==============================");
    }

    /* ================================================
       RUN STRATEGY
       ================================================ */

    static void runStrategy(int strategy) {
        WaitingQueue queue = new WaitingQueue();

        int time = 0;
        int nextArrival = 0;
        int served = 0;
        int[] lost = {0};
        int totalWait = 0;
        int maxWait = 0;
        int totalChargeTime = 0;

        boolean useHeap = strategy != 1;

        calculateDurations();
        resetChargers();
        heap.resetHeap();

        if (useHeap) heap.setHeapRule(strategy - 1);

        printStrategyName(strategy);

        while (nextArrival < MAX_VEHICLES ||
                !queue.isEmpty() || !heap.isHeapEmpty() ||
                c1.busy == 1 || c2.busy == 1) {

            while (nextArrival < MAX_VEHICLES &&
                    vehicles[nextArrival].arrivalTime <= time) {

                Vehicle v = vehicles[nextArrival];

                System.out.printf("%nTime %d: V%d arrived",
                        time, v.id);

                if (v.emergency == 1) System.out.print(" [EMERGENCY]");
                System.out.println();

                if (useHeap) heap.insertHeap(v);
                else queue.enqueue(v);

                nextArrival++;
            }

            finishCharging(time);

            for (Charger charger : new Charger[]{c1, c2}) {
                if (charger.busy != 0) continue;

                Vehicle v = useHeap
                        ? getPriorityVehicle(time, lost)
                        : getFIFO(queue, time, lost);

                if (v != null) {
                    v.startTime = time;
                    v.waitTime = time - v.arrivalTime;

                    System.out.printf("V%d waited %d time units%n",
                            v.id, v.waitTime);

                    assignVehicle(charger, v, time);

                    served++;
                    totalWait += v.waitTime;
                    maxWait = Math.max(maxWait, v.waitTime);
                    totalChargeTime += v.chargeDuration;
                }
            }

            int finish = getNextEventTime();
            int arrival = nextArrival < MAX_VEHICLES
                    ? vehicles[nextArrival].arrivalTime : -1;

            if (arrival != -1 && finish != -1)
                time = Math.min(arrival, finish);
            else if (arrival != -1)
                time = arrival;
            else if (finish != -1)
                time = finish;
            else
                break;
        }

        double avgWait = served > 0
                ? (double) totalWait / served : 0.0;

        double utilization = time > 0
                ? (double) totalChargeTime / (2 * time) * 100 : 0.0;

        utilization = Math.min(utilization, 100.0);

        System.out.println("\n------------------------------");
        System.out.println("RESULTS");
        System.out.println("------------------------------");
        System.out.println("Vehicles Served   : " + served);
        System.out.println("Vehicles Lost     : " + lost[0]);
        System.out.printf("Average Wait      : %.2f%n", avgWait);
        System.out.println("Maximum Wait      : " + maxWait);
        System.out.printf("Charger Utilization: %.2f%%%n", utilization);

        strategyServed[strategy] = served;
        strategyLost[strategy] = lost[0];
        strategyAverageWait[strategy] = avgWait;
        strategyMaxWait[strategy] = maxWait;
        strategyUtilization[strategy] = utilization;

        System.out.println("==============================");
        System.out.println("SIMULATION COMPLETE");
        System.out.println("==============================");
    }

    /* ================================================
       PUBLIC STRATEGY METHODS
       ================================================ */

    static void runSimulation() {
        runStrategy(1);
    }

    static void runShortestFirst() {
        runStrategy(2);
    }

    static void runLowestBatteryFirst() {
        runStrategy(3);
    }

    static void runEmergencyAging() {
        runStrategy(4);
    }

    /* ================================================
       COMPARE STRATEGIES
       ================================================ */

    static void compareStrategies() {
        System.out.println("\n========================================");
        System.out.println("        STRATEGY COMPARISON");
        System.out.println("========================================");

        System.out.printf("%-24s %-8s %-7s %-11s %-10s %-13s%n",
                "Strategy", "Served", "Lost", "Avg Wait",
                "Max Wait", "Utilization");

        System.out.println(
                "--------------------------------------------------------------------------");

        String[] names = {
            "", "FIFO", "Shortest Charge First",
            "Lowest Battery First", "Emergency + Aging"
        };

        for (int i = 1; i <= 4; i++) {
            System.out.printf("%-24s %-8d %-7d %-11.2f %-10d %.2f%%%n",
                    names[i], strategyServed[i], strategyLost[i],
                    strategyAverageWait[i], strategyMaxWait[i],
                    strategyUtilization[i]);
        }

        System.out.println("========================================");
    }

    public static void main(String[] args) {
        runSimulation();
        runShortestFirst();
        runLowestBatteryFirst();
        runEmergencyAging();
        compareStrategies();
    }
}
