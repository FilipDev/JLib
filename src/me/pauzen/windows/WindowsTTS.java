package me.pauzen.windows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public final class WindowsTTS {

    /**
     * Prevents instantiation.
     */
    private WindowsTTS() {
    }

    /**
     * Automatically makes the volume 100 and sets the speed at which to say the message 1.
     *
     * @param message The message to say.
     * @return Whether or not saying the message was successful.
     */
    public static boolean say(String message) {
        return say(message, 100, 1);
    }

    /**
     * Automatically makes the speed at which to say the message to 1.
     *
     * @param message The message to say.
     * @param volume  The volume at which to say the message.
     * @return Whether or not saying the message was successful.
     */
    public static boolean say(String message, float volume) {
        return say(message, volume, 1);
    }

    /**
     * Creates a VBS file containing the message, the volume and the speed. Then runs wscript.exe with the file location as the argument.
     *
     * @param message The message to say.
     * @param volume  The volume at which to say the message.
     * @param rate    The speed or rate the message is said.
     * @return Whether or not saying the message was successful. Not having windows will automatically return false.
     */
    public static boolean say(String message, float volume, float rate) {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                if (volume > 100)
                    throw new IllegalArgumentException("Volume must be <= 100");
                File tempFile = File.createTempFile("SPEAKER", ".VBS", new File(""));
                ArrayList<String> lines = new ArrayList<>(4);
                lines.set(0, addQuotes("Set voice = CreateObject(**SAPI.Spvoice**)"));
                lines.add(1, "voice.Volume = " + volume);
                lines.add(2, "voice.Rate = " + rate);
                lines.add(3, addQuotes("voice.Speak(**" + message + "**)"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                for (String line : lines)
                    writer.write(line + System.getProperty("line.separator"));
                writer.close();
                Process process = Runtime.getRuntime().exec(String.format("wscript %s" + tempFile.getCanonicalPath() + "%s", "\"", "\""));
                process.waitFor();
                tempFile.deleteOnExit();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Replaces '**' in a String with quotation marks.
     *
     * @param string The string to replace the '**' in.
     * @return The processed string.
     */
    private static String addQuotes(String string) {
        return string.replace("**", "\"");
    }
}