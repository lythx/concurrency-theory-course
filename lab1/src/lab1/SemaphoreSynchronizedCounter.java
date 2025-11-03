package lab1;

public class SemaphoreSynchronizedCounter extends Counter {

    private final BinarySemaphore semaphore = new BinarySemaphore();

    @Override
    public void increment() {
        semaphore.lock();
        count++;
        semaphore.unlock();
    }

    @Override
    public void decrement() {
        semaphore.lock();
        count--;
        semaphore.unlock();
    }

}
