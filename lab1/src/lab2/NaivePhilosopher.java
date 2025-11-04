package lab2;

public class NaivePhilosopher extends Philosopher {

    public NaivePhilosopher(Stick leftStick, Stick rightStick) {
        super(leftStick, rightStick);
    }

    @Override
    protected void takeChopsticks() throws InterruptedException {
        leftStick.take();
        rightStick.take();
    }

    @Override
    protected void releaseChopsticks() {
        leftStick.release();
        rightStick.release();
    }

}
