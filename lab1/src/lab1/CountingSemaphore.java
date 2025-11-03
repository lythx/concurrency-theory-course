package lab1;

public class CountingSemaphore {

    private final int maxCount;
    private int count = 0;
    private int waiting = 0;

    public CountingSemaphore(int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void lock() {
        waiting += 1;
        while (count == maxCount) {
            try {
                wait();
            }
            catch (InterruptedException ex) {
                System.out.println("wait() interrupted: " + ex);
            }
        }
        count += 1;
        waiting -= 1;
    }

    public synchronized void unlock() {
        count -= 1;
        notify();
    }

    public int getWaiting() {
        return waiting;
    }

}
