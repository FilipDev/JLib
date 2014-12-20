package me.pauzen.jlib.random;

import me.pauzen.jlib.http.request.get.HttpGetRequest;
import me.pauzen.jlib.http.result.Result;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class RandomRandom {

    private final Deque<Integer> generated = new ArrayDeque<>(200);
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
                get.field("num", "200")
                   .field("min", "10")
                   .field("max", String.valueOf(max))
                   .field("col", "1")
                   .field("base", "10")
                   .field("format", "plain")
                   .field("rdn", "new")
                   .send();
                Result result = get.getResult();
                for (String line : result.getFile()) generated.offer(Integer.parseInt(line.replace("\n", "")));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}