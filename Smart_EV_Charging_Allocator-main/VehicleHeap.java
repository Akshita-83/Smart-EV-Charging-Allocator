
public class VehicleMinHeap {

    /* =====================================================
       VEHICLE CLASS
       ===================================================== */

    static class Vehicle {

        int id;
        int arrivalTime;
        int currentBattery;
        int targetBattery;
        int chargeDuration;
        int emergency;
        int startTime;
        int waitTime;

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
    }

    /* =====================================================
       HEAP
       ===================================================== */

    static final int MAX_HEAP_SIZE = 20;

    static Vehicle[] minHeap = new Vehicle[MAX_HEAP_SIZE];

    static int heapSize = 0;

    /*
       Priority rules:
       1 = Shortest Charge First
       2 = Lowest Battery First
       3 = Emergency + Aging
    */

    static int heapRule = 1;

    /* =====================================================
       SET HEAP RULE
       ===================================================== */

    static void setHeapRule(int rule) {

        if (rule >= 1 && rule <= 3) {
            heapRule = rule;
        } else {
            System.out.println("Invalid heap rule!");
        }
    }

    /* =====================================================
       COMPARE PRIORITY
       ===================================================== */

    static boolean hasHigherPriority(Vehicle a, Vehicle b) {

        // RULE 1: SHORTEST CHARGE FIRST
        if (heapRule == 1) {

            if (a.chargeDuration != b.chargeDuration) {
                return a.chargeDuration < b.chargeDuration;
            }

            // Tie: smaller vehicle ID
            return a.id < b.id;
        }

        // RULE 2: LOWEST BATTERY FIRST
        if (heapRule == 2) {

            if (a.currentBattery != b.currentBattery) {
                return a.currentBattery < b.currentBattery;
            }

            if (a.arrivalTime != b.arrivalTime) {
                return a.arrivalTime < b.arrivalTime;
            }

            // Final tie: smaller ID
            return a.id < b.id;
        }

        // RULE 3: EMERGENCY + AGING
        if (heapRule == 3) {

            if (a.emergency != b.emergency) {
                return a.emergency > b.emergency;
            }

            if (a.waitTime != b.waitTime) {
                return a.waitTime > b.waitTime;
            }

            // Final tie: smaller ID
            return a.id < b.id;
        }

        return false;
    }

    /* =====================================================
       SWAP TWO VEHICLES
       ===================================================== */

    static void swapVehicles(int i, int j) {

        Vehicle temp = minHeap[i];

        minHeap[i] = minHeap[j];

        minHeap[j] = temp;
    }

    /* =====================================================
       HEAPIFY UP
       ===================================================== */

    static void heapifyUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            if (!hasHigherPriority(
                    minHeap[index],
                    minHeap[parent])) {
                break;
            }

            swapVehicles(index, parent);

            index = parent;
        }
    }

    /* =====================================================
       HEAPIFY DOWN
       ===================================================== */

    static void heapifyDown(int index) {

        while (true) {

            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int highest = index;

            if (left < heapSize &&
                    hasHigherPriority(
                            minHeap[left],
                            minHeap[highest])) {

                highest = left;
            }

            if (right < heapSize &&
                    hasHigherPriority(
                            minHeap[right],
                            minHeap[highest])) {

                highest = right;
            }

            if (highest == index) {
                break;
            }

            swapVehicles(index, highest);

            index = highest;
        }
    }

    /* =====================================================
       INSERT INTO HEAP
       ===================================================== */

    static void insertHeap(Vehicle vehicle) {

        if (heapSize >= MAX_HEAP_SIZE) {
            System.out.println("Heap is full!");
            return;
        }

        minHeap[heapSize] = vehicle;

        heapSize++;

        heapifyUp(heapSize - 1);
    }

    /* =====================================================
       EXTRACT HIGHEST PRIORITY
       ===================================================== */

    static Vehicle extractMin() {

        if (isHeapEmpty()) {
            System.out.println("Heap is empty!");
            return null;
        }

        Vehicle selected = minHeap[0];

        heapSize--;

        if (heapSize > 0) {
            minHeap[0] = minHeap[heapSize];
            heapifyDown(0);
        }

        minHeap[heapSize] = null;

        return selected;
    }

    /* =====================================================
       CHECK EMPTY
       ===================================================== */

    static boolean isHeapEmpty() {

        return heapSize == 0;
    }

    /* =====================================================
       RESET HEAP
       ===================================================== */

    static void resetHeap() {

        for (int i = 0; i < heapSize; i++) {
            minHeap[i] = null;
        }

        heapSize = 0;
    }

    /* =====================================================
       DISPLAY HEAP
       ===================================================== */

    static void displayHeap() {

        System.out.println("\nHeap:");

        for (int i = 0; i < heapSize; i++) {
            System.out.print("V" + minHeap[i].id + " ");
        }

        System.out.println();
    }

    /* =====================================================
       MAIN METHOD - TESTING
       ===================================================== */

    public static void main(String[] args) {

        Vehicle v1 = new Vehicle(1, 0, 20, 80, 12, 0, -1, 0);
        Vehicle v2 = new Vehicle(2, 0, 40, 80, 8, 0, -1, 0);
        Vehicle v3 = new Vehicle(3, 1, 30, 80, 10, 0, -1, 0);
        Vehicle v4 = new Vehicle(4, 2, 50, 80, 6, 1, -1, 0);
        Vehicle v5 = new Vehicle(5, 2, 60, 80, 4, 0, -1, 0);

        // Rule 1: Shortest Charge First
        setHeapRule(1);

        insertHeap(v1);
        insertHeap(v2);
        insertHeap(v3);
        insertHeap(v4);
        insertHeap(v5);

        System.out.println("Priority Rule: Shortest Charge First");
        displayHeap();

        Vehicle selected = extractMin();

        if (selected != null) {
            System.out.println(
                    "Selected Vehicle: V" + selected.id);
        }

        displayHeap();

        // Reset and test Rule 2
        resetHeap();

        setHeapRule(2);

        insertHeap(v1);
        insertHeap(v2);
        insertHeap(v3);
        insertHeap(v4);
        insertHeap(v5);

        System.out.println("\nPriority Rule: Lowest Battery First");
        displayHeap();

        // Reset and test Rule 3
        resetHeap();

        setHeapRule(3);

        insertHeap(v1);
        insertHeap(v2);
        insertHeap(v3);
        insertHeap(v4);
        insertHeap(v5);

        System.out.println("\nPriority Rule: Emergency + Aging");
        displayHeap();

        selected = extractMin();

        if (selected != null) {
            System.out.println(
                    "Selected Vehicle: V" + selected.id);
        }

        displayHeap();
    }
}
