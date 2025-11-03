package lab1;

public class CountingSemaphore {

    private final int maxCount;
    private final BinarySemaphore binarySemaphore = new BinarySemaphore();
    private int count = 0;
    private int waiting = 0;

    public CountingSemaphore(int maxCount) {
        this.maxCount = maxCount;
    }

    public void lock() {
        binarySemaphore.lock();

        waiting += 1;
        while (count == maxCount) {
            binarySemaphore.unlock();
            binarySemaphore.lock();
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
