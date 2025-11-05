package lab4;

import java.util.concurrent.Semaphore;

public class Arbiter {

    private final Semaphore semaphore;

    public Arbiter(int numPhilosophers) {
        this.semaphore = new Semaphore(numPhilosophers - 1);
    }

    public void acquireSticksPermission() {
        semaphore.acquireUninterruptibly();
    }

    public void notifySticksReleased() {
        semaphore.release();
    }

}
