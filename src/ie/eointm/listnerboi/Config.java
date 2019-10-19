package ie.eointm.listnerboi;

import org.json.*;

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

    public String getStreamURL() {
        return streamURL;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<Show> getSchedule() {
        return schedule;
    }

    public Config() {
        schedule = new ArrayList<>();

        try {
            JSONObject config = new JSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));

            this.streamURL = config.getString("streamUrl");
            this.outputDir = config.getString("outputDir");
            this.isActive = config.getBoolean("active");
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

        } catch (FileNotFoundException e) {
            System.out.println("Could not find config file");
            //TODO implement config file regeneration in this scenario
        } catch (IOException e) {
            System.out.println("IO Exception while opening config file");
        }
    }

    @Override
    public String toString() {
        String scheduleInfo = "";
        for(int i=0; i<schedule.size(); i++)
            scheduleInfo += "    " + schedule.get(i).toString() + '\n';

        return "URL: " + getStreamURL() + "\noutputDir: " + outputDir  + "\nisActive: " + (isActive ? "true" : "false") + "\nSchedule:\n" + scheduleInfo;
    }
}
