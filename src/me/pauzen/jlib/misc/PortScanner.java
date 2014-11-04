package me.pauzen.jlib.misc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class PortScanner {

    final String host;
    final int    timeOut;
    final int    rangeStart;
    final int    rangeEnd;
    final HashMap<Integer, Runnable> threads = new HashMap<>();

    /**
     * @param timeOut    The milliseconds to wait before discarding the connection.
     * @param host       The URL or IP of the target.
     * @param rangeStart The starting port.
     * @param rangeEnd   The ending port.
     */
    public PortScanner(String host, int timeOut, int rangeStart, int rangeEnd) {
        this.host = host;
        this.timeOut = timeOut;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    /**
     * Automatically makes the connection time out 2000ms.
     *
     * @param host       The URL or IP of the target.
     * @param rangeStart The starting port.
     * @param rangeEnd   The ending port.
     */
    public PortScanner(String host, int rangeStart, int rangeEnd) {
        this.host = host;
        this.timeOut = 2000;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    /**
     * Scans all ports within the range specified.
     *
     * @return The open ports.
     */
    public ArrayList<Integer> scanPorts() {
        final ArrayList<Integer> workingPorts = new ArrayList<>();

        for (int port = rangeStart; port <= rangeEnd; port++) {
            final int portMirror = port;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(host, portMirror), timeOut);
                        workingPorts.add(portMirror);
                        socket.close();
                    } catch (IOException ignored) {
                        System.out.println(portMirror + " is closed");
                    }
                    synchronized (threads) {
                        threads.remove(portMirror);
                    }
                }
            });

            synchronized (threads) {
                threads.put(portMirror, thread);
            }

            thread.start();
        }
        while (threads.keySet().size() > 0) {
            synchronized (threads) {
                System.out.println(threads);
            }
        }

        threads.clear();
        return workingPorts;
    }
}
