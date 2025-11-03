package lab2;

public class BinarySemaphore {

    private boolean locked = false;

    public synchronized void lock() {
        while (locked) {
            try {
                wait();
            }
            catch (InterruptedException ex) {
                System.out.println("lock() interrupted: " + ex);
            }
        }
        locked = true;
    }

    public synchronized void unlock() {
        locked = false;
        notify();
    }

    public boolean isLocked() {
        return locked;
    }

}
