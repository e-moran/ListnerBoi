package ie.eointm.listnerboi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Listner {

    private Timer timer;
    private Config config;

    public Listner(Config config) {
        timer = new Timer();
        this.config = config;
        addTimers();
    }

    public void addTimers() {
        for(int i=0; i<config.getSchedule().size(); i++) {
            Show s = config.getSchedule().get(i);
            ListnerThread t = new ListnerThread(config.getStreamURL(), s.getRuntime(), config.getOutputDir(), s.getShowName(), this);
            Calendar d = Calendar.getInstance();

            d.set(Calendar.DAY_OF_WEEK, s.getDay());
            d.set(Calendar.HOUR, s.getStartHr());
            d.set(Calendar.MINUTE, s.getStartMin());
            d.set(Calendar.SECOND, s.getStartSec());
            d.set(Calendar.MILLISECOND, 0);

            timer.schedule(new TimedListner(t), d.getTime(), 1000L * 60L * 60L * 24L * 7L);
            System.out.println("Scheduled timer for "+s.getShowName());
        }
    }

    class TimedListner extends TimerTask {
        Thread listner;

        TimedListner (ListnerThread t) {
            this.listner = t;
        }

        public void run() {
            listner.start();
        }
    }

    class ListnerThread extends Thread {
        private Listner listner;
        private int timeToListen;
        private String streamUrl;
        private String outputLocation;

        public ListnerThread(String streamUrl, int timeToListen, String outputDir, String showName, Listner listner) {

            this.listner = listner;
            this.timeToListen = timeToListen;
            //this.outputLocation = outputDir + "/" + showName + "_" + System.currentTimeMillis() + ".mp3";
            this.outputLocation = showName + "_" + System.currentTimeMillis() + ".mp3";
            this.streamUrl = streamUrl;
        }

        public void run() {
            streamToFile();
        }

        private void streamToFile() {
            String[] command = {"ffmpeg", "-t", Integer.toString(timeToListen), "-i", streamUrl, "-acodec", "mp3", "-ab", "257k", outputLocation};
            synchronized (listner) {
                try {
                    String s = "";
                    Process p = new ProcessBuilder(command).start();

                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    StringBuffer start= new StringBuffer();
                    // read the output from the command
                    while ((s = stdInput.readLine()) != null)
                    {
                        System.out.println(s);
                    }
                    stdInput.close();
                    // read any errors from the attempted command
                    while ((s = stdError.readLine()) != null)
                    {
                        System.out.println(s);
                    }

                    postStream(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    postStream(false);
                }
            }
        }

        private void postStream(boolean succeeded) {
            System.out.println("Stream stored at " + outputLocation);
        }
    }
}
