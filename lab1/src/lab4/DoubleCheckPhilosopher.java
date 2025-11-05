package lab4;

public class DoubleCheckPhilosopher extends Philosopher {

    public DoubleCheckPhilosopher(Stick leftStick, Stick rightStick) {
        super(leftStick, rightStick);
    }

    @Override
    protected void takeChopsticks() throws InterruptedException {
        while (true) {
            leftStick.take();
            if (rightStick.isTaken()) {
                leftStick.release();
            }
            else {
                rightStick.take();
                break;
            }

            rightStick.take();
            if (leftStick.isTaken()) {
                rightStick.release();
            }
            else {
                leftStick.take();
                break;
            }
        }
    }

}
