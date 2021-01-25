
public class HomeWork4 {

    public static void main(String[] args) {

        Object mon = new Object();
        CustomThread[] threads = new CustomThread[3];
        threads[0] = new CustomThread("A", 0, mon);
        threads[1] = new CustomThread("B", 1, mon);
        threads[2] = new CustomThread("C", 2, mon);

        for (CustomThread thread : threads) {
            thread.start();
        }
    }

}
