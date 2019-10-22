package ie.eointm.listnerboi;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Config {

    private String streamURL;
    private String outputDir;
    private boolean isActive;
    private ArrayList<Show> schedule;
    private String ccEmail;
    private String fromEmail;
    private String emailPass;
    private String smtpServer;
    private String baseDlUrl;
    private int smtpPort;

    public String getStreamURL() {
        return streamURL;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<Show> getSchedule() {
        return schedule;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getEmailPass() {
        return emailPass;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getBaseDlUrl() {
        return baseDlUrl;
    }

    public Config() {
        schedule = new ArrayList<>();

        try {
            JSONObject config = new JSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));

            this.streamURL = config.getString("streamUrl");
            this.outputDir = config.getString("outputDir");
            this.isActive = config.getBoolean("active");
            this.ccEmail = config.getString("ccEmail");
            this.fromEmail = config.getString("fromEmail");
            this.emailPass = config.getString("emailPass");
            this.smtpServer = config.getString("smtpServer");
            this.smtpPort = config.getInt("smtpPort");
            this.baseDlUrl = config.getString("baseDlUrl");
            JSONArray arr = config.getJSONArray("schedule");

            for(int i=0; i<arr.length(); i++) {
                JSONObject show = arr.getJSONObject(i);
                schedule.add(new Show(
                        show.getString("showName"),
                        show.getString("emailAddress"),
                        show.getInt("day"),
                        show.getInt("startTimeHrs"),
                        show.getInt("startTimeMins"),
                        show.getInt("startTimeSecs"),
                        show.getInt("runtime")

                ));
            }

            createOutputDirIfNotExists();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find config file");
            //TODO implement config file regeneration in this scenario
        } catch (IOException e) {
            System.out.println("IO Exception while opening config file");
        }
    }

    private void createOutputDirIfNotExists() {
        File directory = new File(outputDir);
        if(!directory.exists())
            directory.mkdir();
    }

    @Override
    public String toString() {
        String scheduleInfo = "";
        for(int i=0; i<schedule.size(); i++)
            scheduleInfo += "    " + schedule.get(i).toString() + '\n';

        return "URL: " + getStreamURL() + "\noutputDir: " + outputDir  + "\nisActive: " + (isActive ? "true" : "false") + "\nSchedule:\n" + scheduleInfo;
    }
}
