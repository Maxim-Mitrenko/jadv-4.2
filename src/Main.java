import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final int ARRAY_SIZE = 1000000;
    private static Random random = new Random();

    public static void main(String[] args) {
        int[] ints = random.ints(ARRAY_SIZE).toArray();
        System.out.println("ConcurrentHashMap - " + countTime(new ConcurrentHashMap<>(), ints) + " миллисекунд");
        System.out.println("Collections.synchronizedMap - " + countTime(Collections.synchronizedMap(new HashMap<>()), ints) + " миллисекунд");
    }

    public static void add(Map<Integer, Integer> map, int[] ints) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            map.put(ints[random.nextInt(ARRAY_SIZE - 1)], ints[random.nextInt(ARRAY_SIZE - 1)]);
        }
    }

    public static void read(Map<Integer, Integer> map, int[] ints) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            map.get(ints[random.nextInt(ARRAY_SIZE - 1)]);
        }
    }

    public static long countTime(Map<Integer, Integer> map, int[] ints) {
        long first = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> add(map, ints));
        Thread thread2 = new Thread(() -> add(map, ints));
        Thread thread3 = new Thread(() -> read(map, ints));
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread3.join();
            thread2.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long second = System.currentTimeMillis();
        return second - first;
    }
}
