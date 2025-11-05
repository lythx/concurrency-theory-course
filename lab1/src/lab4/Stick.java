package lab4;

import java.util.concurrent.Semaphore;

public class Stick {

    private final Semaphore semaphore = new Semaphore(1);

    public boolean isTaken() {
        return semaphore.availablePermits() == 0;
    }

    public void take() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

}
