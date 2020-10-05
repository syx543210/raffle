package com.syx.raffle.scheduled;

import com.syx.raffle.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledUtil {
    @Autowired
    private HistoryService historyService;

    @Scheduled(cron = "30 10 * * * *")
    public void scheduled() {
        historyService.getRaffleInfoFormWeb("");
//        Calendar cal=Calendar.getInstance();
//        cal.add(Calendar.DATE,-1);
//        Date time=cal.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String format = sdf.format(time);
//        Date parse = null;
//        try {
//            parse = sdf.parse(format);
//        } catch (ParseException e) {
//
//        }
//        if(parse!=null){
//            Integer newTerm = historyService.getNewTerm(parse);
//            if(newTerm!=-1){
//                historyService.getRaffleInfoFormWeb(newTerm.toString());
//            }
//        }
    }
}
