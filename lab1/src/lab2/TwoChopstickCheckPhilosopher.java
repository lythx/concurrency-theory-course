package lab2;

public class TwoChopstickCheckPhilosopher extends Philosopher {

    public TwoChopstickCheckPhilosopher(Stick leftStick, Stick rightStick) {
        super(leftStick, rightStick);
    }

    @Override
    protected void takeChopsticks() {
        while (true) {
            leftStick.take();
            if (rightStick.isTaken()) {
                leftStick.release();
            }
            else {
                rightStick.take();
                break;
            }
        }
    }

    @Override
    protected void releaseChopsticks() {
        leftStick.release();
        rightStick.release();
    }

}
