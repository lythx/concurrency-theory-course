package lab2;

public class AsymmetricPhilosopher extends Philosopher {

    private final boolean hasEvenNumber;

    public AsymmetricPhilosopher(Stick leftStick, Stick rightStick, int number) {
        super(leftStick, rightStick);
        hasEvenNumber = number % 2 == 0;
    }

    @Override
    protected void takeChopsticks() {
        if (hasEvenNumber) {
            rightStick.take();
            leftStick.take();
        }
        else {
            leftStick.take();
            rightStick.take();
        }
    }

    @Override
    protected void releaseChopsticks() {
        leftStick.release();
        rightStick.release();
    }

}
