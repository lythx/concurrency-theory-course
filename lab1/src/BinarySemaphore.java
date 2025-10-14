public class BinarySemaphore {

    private boolean isLocked = false;
    private int waiting = 0;

    public synchronized void lock() {
        waiting += 1;
        while (isLocked) {
            try {
                wait();
            }
            catch (InterruptedException ex) {
                System.out.println("wait() interrupted: " + ex);
            }
        }
        isLocked = true;
        waiting -= 1;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }

    public int getWaiting() {
        return waiting;
    }

}
