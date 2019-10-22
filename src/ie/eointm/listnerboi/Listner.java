package ie.eointm.listnerboi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            ListnerThread t = new ListnerThread(this, s, config);
            Calendar d = Calendar.getInstance();

            d.set(Calendar.DAY_OF_WEEK, s.getDay());
            d.set(Calendar.HOUR_OF_DAY, s.getStartHr());
            d.set(Calendar.MINUTE, s.getStartMin());
            d.set(Calendar.SECOND, s.getStartSec());
            d.set(Calendar.MILLISECOND, 0);
            d.add(Calendar.MINUTE, -1);

            timer.schedule(new TimedListner(t), d.getTime(), 1000 * 60 * 60 * 24 * 7);
            System.out.println("Scheduled timer for "+s.getShowName() + "\nFirst time at " + d.getTimeInMillis());
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
        private String outputLocation;
        private String filename;
        private Show s;
        private Config c;

        public ListnerThread(Listner listner, Show s, Config c) {
            this.listner = listner;
            this.s = s;
            this.c = c;

        }

        public void run() {
            this.filename = s.getShowName() + "_" + System.currentTimeMillis() + ".mp3";
            this.outputLocation = c.getOutputDir() + "/" + filename;

            System.out.println(getTimestamp() + s.getShowName() + " is being saved to " + outputLocation);
            streamToFile();

        }

        private void streamToFile() {
            String[] command = {"ffmpeg", "-t", Integer.toString(s.getRuntime()+120), "-i", c.getStreamURL(), "-acodec", "mp3", "-ab", "257k", outputLocation};
            Process p = null;
            synchronized (listner) {
                try {
                    p = new ProcessBuilder(command).inheritIO().start();
                    p.waitFor();

                    postStream(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    postStream(false);
                } catch (InterruptedException e) {
                    if(p != null)
                        p.destroy();
                    e.printStackTrace();
                }
            }
        }

        private void postStream(boolean succeeded) {
            System.out.println(getTimestamp() + "Stream successfully recorded to location " + outputLocation);

            new PostRecordingEmail(c, s, filename);
        }

        private String getTimestamp() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            return '[' + dtf.format(now) + "] ";
        }
    }
}
