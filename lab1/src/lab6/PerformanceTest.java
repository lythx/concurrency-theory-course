package lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PerformanceTest {

    private static final int WARMUP_ROUNDS = 2; // Rundy rozgrzewkowe dla JIT
    private static final int TEST_DURATION_SECONDS = 5; // Czas trwania każdego pomiaru

    public static void main(String[] args) throws InterruptedException {
        // Zakresy zgodne z Twoim żądaniem
        int[] readerCounts = {10, 20, 50, 75, 100};
        int[] writerCounts = {1, 2, 5, 8, 10};

        System.out.println("### Testowanie wydajności SynchronizedList ###");
        System.out.println("Czas testu na konfigurację: " + TEST_DURATION_SECONDS + " sekund.");
        System.out.println("----------------------------------------------");
        System.out.printf("%-10s %-10s %-20s %-20s %n", "Czytelnicy", "Pisarze", "Czas odczytu (ms)", "Czas zapisu (ms)");
        System.out.println("----------------------------------------------");

        // Rozgrzewka (Warmup)
        System.out.println("(Trwa rozgrzewka JVM...)");
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            measurePerformance(10, 1);
        }

        // Pomiary
        for (int R : readerCounts) {
            for (int W : writerCounts) {
                var result = measurePerformance(R, W);
                System.out.printf("%-10d %-10d %-20f %-20f%n", R, W, result.get(0), result.get(1));
            }
        }
    }

    private static List<Double> measurePerformance(int numReaders, int numWriters) throws InterruptedException {
        SynchronizedList list = new SynchronizedList();
        // Wypełnienie listy początkowymi danymi (0 do 499)
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        List<Reader> readers = new ArrayList<>();
        for (int i = 0; i < numReaders; i++) {
            Reader r = new Reader(list);
            readers.add(r);
        }

        List<Writer> writers = new ArrayList<>();
        for (int i = 0; i < numWriters; i++) {
            Writer w = new Writer(list);
            writers.add(w);
        }

        // Uruchomienie wątków
        long startTime = System.nanoTime();
        readers.forEach(Thread::start);
        writers.forEach(Thread::start);
        // Oczekiwanie na zakończenie testu
        TimeUnit.SECONDS.sleep(TEST_DURATION_SECONDS);

        // Zatrzymanie wątków
        readers.forEach(Thread::interrupt);
        writers.forEach(Thread::interrupt);

        // Czekanie na wątki
        for (Thread t : readers) t.join();
        for (Thread t : writers) t.join();
        long endTime = System.nanoTime();

        // Zbieranie wyników
        long totalOps = 0;
        var averageReadTimeMilliseconds = readers.stream()
            .map(Reader::getAverageWaitTimeMilliseconds)
            .mapToLong(Long::longValue)
            .average()
            .orElse(-1);
        var averageWriteTimeMilliseconds = writers.stream()
            .map(Writer::getAverageWaitTimeMilliseconds)
            .mapToLong(Long::longValue)
            .average()
            .orElse(-1);

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

        // Zwraca łączną liczbę operacji na sekundę
        return List.of(averageReadTimeMilliseconds, averageWriteTimeMilliseconds);
    }
}