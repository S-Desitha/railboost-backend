package org.ucsc.railboostbackend.services;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ucsc.railboostbackend.enums.Day;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.repositories.JourneyRepo;
import org.ucsc.railboostbackend.repositories.ScheduleRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JourneyCreator implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDate date = LocalDate.now();
        Day day = Day.valueOfInt(date.getDayOfWeek().getValue());
        ScheduleRepo scheduleRepo = new ScheduleRepo();

        assert day != null;
        List<Schedule> schedules = scheduleRepo.getScheduleByDay(day, date);

        createJourneys(schedules);

        System.out.println(LocalDateTime.now() + " : Job executed successfully!");
    }


    private void createJourneys(List<Schedule> schedules) {
        JourneyRepo journeyRepo = new JourneyRepo();
        schedules.forEach(journeyRepo::addJourney);
    }
}
