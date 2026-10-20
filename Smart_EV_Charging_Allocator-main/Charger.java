
public class Charger {
    private int id;
    private int power;
    private boolean busy;
    private Vehicle vehicle;
    private int finishTime;

    public Charger(int id, int power) {
        this.id = id;
        this.power = power;
        reset();
    }

    public void reset() {
        busy = false;
        vehicle = null;
        finishTime = 0;
    }

    public void assignVehicle(Vehicle v, int time) {
        vehicle = v;
        busy = true;
        finishTime = time + v.getChargeDuration();

        System.out.printf(
            "V%d starts charging on Charger %d at time %d%n",
            v.getId(), id, time
        );
        System.out.println("Expected finish time: " + finishTime);
    }

    public void finishCharging() {
        if (vehicle != null) {
            System.out.printf(
                "V%d finished charging on Charger %d%n",
                vehicle.getId(), id
            );
        }
        reset();
    }

    public int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public boolean isBusy() {
        return busy;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
    public int getVehicleId() {
    return vehicle == null ? 0 : vehicle.getId();
}
}
