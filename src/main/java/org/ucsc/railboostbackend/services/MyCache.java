package org.ucsc.railboostbackend.services;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.ucsc.railboostbackend.repositories.StaffRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyCache implements Job {

    private static final Map<String, String> staffCache;

    static {
        staffCache = new HashMap<>();
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        String tempUID = dataMap.getString("tempUID");
        
        clearCache(tempUID, true);
    }


    public void add(String staffId, String tempUID) {
        staffCache.put(tempUID, staffId);

        // add automatic delete trigger to cron scheduler.
        JobDetail job = JobBuilder.newJob(MyCache.class)
                .withIdentity(tempUID, "staff-cache")
                .usingJobData("tempUID", tempUID)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(tempUID)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.HOUR))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .build();

        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            System.out.println("Error: Couldn't create Scheduler to delete staff cache");
            System.out.println(e.getMessage());
        }
    }


    public String get(String tempUID) throws Exception {
        if (staffCache.containsKey(tempUID))
            return staffCache.get(tempUID);
        else throw new RuntimeException("signup-expired");
    }


    public void clearCache(String tempUID, boolean isExpired) {
        String staffId = staffCache.get(tempUID);
        staffCache.remove(tempUID);

        if (isExpired) {
            // remove entry from DB
            if (staffId!=null){
                StaffRepo staffRepo = new StaffRepo();
                staffRepo.deleteMember(staffId);
            }
            System.out.println(new Date() +"Staff cache cleared from expiration: " + staffId);
        }
        else {
            try {
                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                JobKey jobKey = new JobKey(tempUID, "staff-cache");
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e) {
                System.out.println("Cannot get scheduler: clearCache() : MyCache.java");
                System.out.println(e.getMessage());
            }
            System.out.println(new Date() +"Staff signup completed and cache cleared: " + staffId);
        }
    }


    public String createTempUID(String staffDetails) {
        MessageDigest instance = null;
        StringBuilder hexString = new StringBuilder();
        try {
            instance = MessageDigest.getInstance("MD5");
            byte[] messageDigest = instance.digest((String.valueOf(System.nanoTime())+staffDetails).getBytes());
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in MessageDigest in MyCache class:\n"+e.getMessage());
        }
        
        return hexString.toString();
    }
}
