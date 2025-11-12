package lab6;

public class MainLab6 {

    public static void main(String[] args) {
        var list = new SynchronizedList();
        try {


            list.add(1);
            list.add(2);
            list.add(3);

            System.out.println("1: " + list.contains(2));
            System.out.println("2: " + list.contains(4));
            System.out.println("3: " + list.remove(2));
            System.out.println("4: " + list.contains(2));
            System.out.println("5: " + list.contains(3));
            System.out.println("6: " + list.remove(1));
            System.out.println("7: " + list.contains(1));
            System.out.println("8: " + list.contains(3));
            System.out.println("9: " + list.add(null));
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted: " + e);
        }
    }

}
