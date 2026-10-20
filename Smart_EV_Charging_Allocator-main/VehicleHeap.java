
import java.util.PriorityQueue;

public class VehicleHeap {
    private final PriorityQueue<Vehicle> heap =
        new PriorityQueue<>((a, b) -> compare(a, b));

    private int rule = 1;

    public void setRule(int rule) {
        this.rule = rule;
        heap.clear();
    }

    private int compare(Vehicle a, Vehicle b) {
        if (rule == 1) {
            int result = Integer.compare(
                a.getChargeDuration(), b.getChargeDuration());
            if (result != 0) return result;
        } else if (rule == 2) {
            int result = Integer.compare(
                a.getCurrentBattery(), b.getCurrentBattery());
            if (result != 0) return result;

            result = Integer.compare(
                a.getArrivalTime(), b.getArrivalTime());
            if (result != 0) return result;
        } else {
            int result = Boolean.compare(
                b.isEmergency(), a.isEmergency());
            if (result != 0) return result;

            result = Integer.compare(
                b.getWaitTime(), a.getWaitTime());
            if (result != 0) return result;
        }

        return Integer.compare(a.getId(), b.getId());
    }

    public void insert(Vehicle vehicle) {
        heap.offer(vehicle.copy());
    }

    public Vehicle extractMin() {
        return heap.poll();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }
}
