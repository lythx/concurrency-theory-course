package lab4;

public class ArbiterPhilosopher extends Philosopher {

    private final Arbiter arbiter;

    public ArbiterPhilosopher(Stick leftStick, Stick rightStick, Arbiter arbiter) {
        super(leftStick, rightStick);
        this.arbiter = arbiter;
    }

    @Override
    protected void takeChopsticks() throws InterruptedException {
        arbiter.acquireSticksPermission();
        leftStick.take();
        rightStick.take();
    }

    @Override
    protected void releaseChopsticks() {
        super.releaseChopsticks();
        arbiter.notifySticksReleased();
    }

}
