public class Main {
    public static void main(String[] args) throws InterruptedException {
        var iterations = 10_000_000;

        var counter = new Counter();
        var incrementThread = new IncrementThread(counter, iterations);
        var decrementThread = new DecrementThread(counter, iterations);
        var startTime = System.nanoTime();
        incrementThread.start();
        decrementThread.start();
        incrementThread.join();
        decrementThread.join();
        var endTime = System.nanoTime();
        System.out.println("Non sync result: " + counter.getCount());
        System.out.println("Non sync time (miliseconds): " + (endTime - startTime) / 1_000_000);

        counter = new SynchronizedCounter();
        incrementThread = new IncrementThread(counter, iterations);
        decrementThread = new DecrementThread(counter, iterations);
        startTime = System.nanoTime();
        incrementThread.start();
        decrementThread.start();
        incrementThread.join();
        decrementThread.join();
        endTime = System.nanoTime();
        System.out.println("Sync result: " + counter.getCount());
        System.out.println("Sync time (miliseconds): " + (endTime - startTime) / 1_000_000);

//        for (int i = 0; ; i++) {
//            new Thread(() -> {
//                var a = 1;
//                var b = 2;
//                while (true) {
//                    a += b;
//                    b /= 2;
//                }
//            }).start();
//            System.out.println("Thread number: " + i);
//        }
//
//        for (int i = 0; ; i++) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000000000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }).start();
//            System.out.println("Thread number: " + i);
//        }
    }
}