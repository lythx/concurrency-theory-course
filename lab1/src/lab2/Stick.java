package lab2;

import java.util.concurrent.Semaphore;

public class Stick {

    private final Semaphore semaphore = new Semaphore(1);

    public boolean isTaken() {
        return semaphore.availablePermits() == 0;
    }

    public void take() {
        semaphore.acquireUninterruptibly();
    }

    public void release() {
        semaphore.release();
    }

}
