
public class Vehicle {
    private int id;
    private int arrivalTime;
    private int currentBattery;
    private int targetBattery;
    private int chargeDuration;
    private boolean emergency;
    private int startTime;
    private int waitTime;

    public Vehicle(int id, int arrivalTime, int currentBattery,
                   int targetBattery, boolean emergency) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.currentBattery = currentBattery;
        this.targetBattery = targetBattery;
        this.emergency = emergency;
        this.startTime = -1;
        this.waitTime = 0;
    }

    public int getId() { return id; }
    public int getArrivalTime() { return arrivalTime; }
    public int getCurrentBattery() { return currentBattery; }
    public int getTargetBattery() { return targetBattery; }
    public int getChargeDuration() { return chargeDuration; }
    public boolean isEmergency() { return emergency; }
    public int getStartTime() { return startTime; }
    public int getWaitTime() { return waitTime; }

    public void setChargeDuration(int duration) {
        chargeDuration = duration;
    }

    public void setStartTime(int time) {
        startTime = time;
    }

    public void setWaitTime(int time) {
        waitTime = time;
    }

    public Vehicle copy() {
        Vehicle v = new Vehicle(id, arrivalTime, currentBattery,
                targetBattery, emergency);
        v.setChargeDuration(chargeDuration);
        v.setStartTime(startTime);
        v.setWaitTime(waitTime);
        return v;
    }

    public void display() {
        System.out.println("Vehicle V" + id
                + " | Arrival: " + arrivalTime
                + " | Battery: " + currentBattery + "%"
                + " | Target: " + targetBattery + "%"
                + " | Emergency: " + emergency);
    }
}
