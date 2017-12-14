package Utillities;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

import datamodels.MilestoneDataModel;

/**
 * Created by parti on 11-12-2017.
 */

public class MilestoneData {

    public MilestoneData()
    {

    }

    private DateTime quitTime;

    public List<MilestoneDataModel> Build(DateTime time){

        quitTime = time;

        List<MilestoneDataModel> milestoneList = new ArrayList<>();

        MilestoneDataModel model = new MilestoneDataModel();
        model.totalhours = 2;
        model.period = 24;
        model.goal = "2 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(2);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 4;
        model.period = 24;
        model.goal = "4 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(4);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 8;
        model.period = 24;
        model.goal = "8 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(8);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 12;
        model.period = 24;
        model.goal = "Last the first 12 hours.";
        model.timeunit = "hours";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(12);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 24;
        model.period = 24;
        model.goal = "day 1";
        model.timeunit = "day";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(24);

        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 48;
        model.period = 24;
        model.goal = "Day 2";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(48);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 72;
        model.period = 24;
        model.goal = "Day 3";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(72);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 96;
        model.period = 24;
        model.goal = "Day 4";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(96);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 120;
        model.period = 24;
        model.goal = "Day 5";
        model.timeunit = "days";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(120);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 144;
        model.period = 24;
        model.goal = "Day 6";
        model.quote = "The first step is always the hardest";
        model.completed = isMilestoneCompleted(144);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 168;
        model.period = 24;
        model.goal = "Week 1";
        model.quote = "Almost a full week now!";
        model.completed = isMilestoneCompleted(168);
        milestoneList.add(model);

        model = new MilestoneDataModel();
        model.totalhours = 336;
        model.period = 168;
        model.goal = "Week 2";
        model.quote = "Almost a full week now!";
        model.completed = isMilestoneCompleted(336);
        milestoneList.add(model);

        return milestoneList;
    }

    private Boolean isMilestoneCompleted(long hours)
    {
        DateTime now = new DateTime();
        Duration duration = new Duration(quitTime, now);

        long durationHours = duration.getStandardHours();

        if(hours <= durationHours)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
