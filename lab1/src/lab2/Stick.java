package lab2;

public class Stick {

    private final BinarySemaphore semaphore = new BinarySemaphore();

    public boolean isTaken() {
        return semaphore.isLocked();
    }

    public void take() {
        semaphore.lock();
    }

    public void release() {
        semaphore.unlock();
    }

}
