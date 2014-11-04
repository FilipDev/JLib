package me.pauzen.jlib.random;

import me.pauzen.jlib.http.Result;
import me.pauzen.jlib.http.get.HttpGetRequest;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class RandomRandom {

    private final Queue<Integer> generated = new PriorityQueue<>();
    private int max;

    /**
     * Requires internet connection to function. Use SecureRandom if internet unavailable.
     *
     * @param max Max number that can be generated exclusive.
     */
    public RandomRandom(int max) {
        this.max = max;
        gatherRandom();
    }

    /**
     * Checks if the queue has more elements, and if false will gather more.
     *
     * @return Returns the next int in the queue.
     */
    public int nextInt() {
        if (generated.peek() == null) gatherRandom();
        return generated.poll();
    }

    /**
     * Generates 200 random numbers and puts them into the priority queue.
     */
    private void gatherRandom() {
        if (max < 1000000000)
            try {
                HttpGetRequest get = new HttpGetRequest("http://www.random.org/integers/");
                get.addPart("num", "200")
                    .addPart("min", "10")
                    .addPart("max", String.valueOf(max))
                    .addPart("col", "1")
                    .addPart("base", "10")
                    .addPart("format", "plain")
                    .addPart("rdn", "new")
                    .send();
                Result result = get.getResult();
                for (String line : result.getResult()) {
                    System.out.println(line.replace("\n", ""));
                    generated.offer(Integer.parseInt(line.replace("\n", "")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}