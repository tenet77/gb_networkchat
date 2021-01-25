import java.util.concurrent.TimeUnit;

public class CustomThread extends Thread {

    private static int currentPos = 0;

    private final String symbol;
    private int counter;
    private final int pos;
    private final Object mon;

    public CustomThread(String symbol, int pos, Object mon) {
        this.symbol = symbol;
        this.counter = 0;
        this.pos = pos;
        this.mon = mon;
    }

    @Override
    public void run() {
        while (counter < 5) {
            try {
                synchronized (mon) {
                    if (currentPos != pos) {
                        mon.wait();
                    } else {
                        currentPos = (currentPos + 1) % 3;
                        mon.notifyAll();
                        TimeUnit.MICROSECONDS.sleep(300);
                        System.out.print(symbol);
                        counter++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
