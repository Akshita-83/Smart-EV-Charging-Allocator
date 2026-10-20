
import java.util.ArrayDeque;
import java.util.Queue;

public class VehicleQueue {
    private final Queue<Vehicle> queue = new ArrayDeque<>();
    private final int capacity;

    public VehicleQueue(int capacity) {
        this.capacity = capacity;
    }

    public boolean enqueue(Vehicle vehicle) {
        if (queue.size() >= capacity) {
            System.out.println("Waiting queue is full!");
            return false;
        }

        queue.offer(vehicle.copy());
        return true;
    }

    public Vehicle dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}
