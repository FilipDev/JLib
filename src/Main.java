import me.pauzen.jlib.http.Http;
import me.pauzen.jlib.http.headers.UserAgent;

public class Main {

    public Main() throws IllegalAccessException {
        System.out.println(Http.post("https://mostwantedhf.info/").header(UserAgent.CHROME_WINDOWS).field("skypeName", "heycallmethemezand").field("resolve", "Grab IP").getResult().getFile().toString());
    }

    public double compund(double initial, double rate, int times) {
        if (times == 0) return 1;
        if (times == 1) return initial;
        return compund(initial * (1 + rate), rate, times - 1);
    }

    private int times = 0;
    public double getTimesDoubled(double n) {
        times = 0;
        return timesDoubled(n);
    }
    public double timesDoubled(double n) {
        if (n <= 1) return times;
        times++;
        System.out.println(n);
        return timesDoubled(Math.sqrt(n));
    }

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public class Test<T> {
    }

}