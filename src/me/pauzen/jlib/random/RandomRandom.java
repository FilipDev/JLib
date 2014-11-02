package me.pauzen.jlib.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
                String url = "http://www.random.org/integers/?num=200&min=0&max=maxValue&col=1&base=10&format=plain&rnd=new";
                URL url1 = new URL(url.replaceAll("maxValue", String.valueOf(max)));
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                int responseCode = connection.getResponseCode();
                if (responseCode != 404) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) generated.add(Integer.parseInt(line));
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}