package me.pauzen.jlib.random;

import me.pauzen.jlib.http.request.get.HttpGetRequestBuilder;
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
                HttpGetRequestBuilder get = new HttpGetRequestBuilder("http://www.random.org/integers/");
                get.addPart("num", "200")
                   .addPart("min", "10")
                   .addPart("max", String.valueOf(max))
                   .addPart("col", "1")
                   .addPart("base", "10")
                   .addPart("format", "plain")
                   .addPart("rdn", "new")
                   .send();
                Result result = get.getResult();
                for (String line : result.getResult()) generated.offer(Integer.parseInt(line.replace("\n", "")));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}