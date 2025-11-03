package lab2;

public class ArbiterPhilosopher extends Philosopher {

    private final Arbiter arbiter;

    public ArbiterPhilosopher(Stick leftStick, Stick rightStick, Arbiter arbiter) {
        super(leftStick, rightStick);
        this.arbiter = arbiter;
    }

    @Override
    protected void takeChopsticks() {
        arbiter.acquireSticksPermission();
        leftStick.take();
        rightStick.take();
    }

    @Override
    protected void releaseChopsticks() {
        leftStick.release();
        rightStick.release();
        arbiter.notifySticksReleased();
    }

}
