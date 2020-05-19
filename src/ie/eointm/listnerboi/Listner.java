package ie.eointm.listnerboi;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listner {

    //private Timer timer;
    private Config config;

    public Listner(Config config) {
        //timer = new Timer();
        this.config = config;
        addTimers();
    }

    public void addTimers() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(config.getSchedule().size());
        for(int i=0; i<config.getSchedule().size(); i++) {
            Show s = config.getSchedule().get(i);
            Calendar d = Calendar.getInstance();

            if (s.getDay() == -1) { // To allow for easier debugging
                d.add(Calendar.SECOND, 3);
            } else {
                d.set(Calendar.DAY_OF_WEEK, s.getDay());
                d.set(Calendar.HOUR_OF_DAY, s.getStartHr());
                d.set(Calendar.MINUTE, s.getStartMin());
                d.set(Calendar.SECOND, s.getStartSec());
                d.set(Calendar.MILLISECOND, 0);
                d.add(Calendar.MINUTE, -1);
            }

            // Code to ensure correct scheduling if the scheduled time has already been missed this week
            if(d.getTimeInMillis() - System.currentTimeMillis() < 0)
                d.add(Calendar.DATE, 7);

            ses.scheduleAtFixedRate(new ListnerThread(s, config), (d.getTimeInMillis() - System.currentTimeMillis())/1000, 60*60*24*7, TimeUnit.SECONDS);
            System.out.println("Scheduled timer for "+s.getShowName() + "\nFirst time at " + d.getTimeInMillis());
        }
    }

    class ListnerThread implements Runnable {
        private String outputLocation;
        private String filename;
        private Show s;
        private Config c;

        public ListnerThread(Show s, Config c) {
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
            try {
                p = new ProcessBuilder(command).redirectInput(new File("/dev/null")).inheritIO().start();
                p.waitFor();

                File f = new File(outputLocation);
                postStream(f.isFile());
            } catch (IOException e) {
                e.printStackTrace();
                postStream(false);
            } catch (InterruptedException e) {
                p.destroy();
                e.printStackTrace();
            }
        }

        private void postStream(boolean succeeded) {
            if(succeeded) {
                System.out.println(getTimestamp() + "Stream successfully recorded to location " + outputLocation);
                new PostRecordingEmail(c).sendPostRecordingEmail(s, filename);
            } else {
                System.out.println(getTimestamp() + "Stream" + s.getShowName() + " failed to record.");
                new PostRecordingEmail(c).sendFailureEmail(s, new Exception("File was not found post-recording"));
            }
        }

        private String getTimestamp() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            return '[' + dtf.format(now) + "] ";
        }
    }
}
