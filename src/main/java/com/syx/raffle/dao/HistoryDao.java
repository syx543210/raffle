package com.syx.raffle.dao;

import com.syx.raffle.Jpa.HistoryRepos;
import com.syx.raffle.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class HistoryDao {
    @PersistenceContext
    EntityManager em;

    @Autowired
    private HistoryRepos historyRepos;


    public List<History> selectAll() {
        return historyRepos.findAll();
    }

    public void saveHistory(History history) {
        historyRepos.save(history);
    }

    public void saveHistoryList(List<History> historyList) {
        historyRepos.saveAll(historyList);
    }


    public List<Integer> getExistTermList(List<Integer> termList) {
        return historyRepos.getExistTermList(termList);
    }

    public Integer getLastTerm(Date date) {
        History history = historyRepos.findByDate(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
        String year = sdf.format(date);
        String MonthAndDay = sdf1.format(date);
        if (history == null) {
            String term = "";
            if ("01-01".equals(MonthAndDay)) {
                term = year + "001";
                return Integer.parseInt(term);
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int day = c.get(Calendar.DATE);
                c.set(Calendar.DATE, day - 1);
                Date lastDate = c.getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf2.format(lastDate);
                Date parse = null;
                try {
                    parse = sdf2.parse(format);
                } catch (ParseException e) {

                }
                if (parse != null) {
                    History lastHistory = historyRepos.findByDate(parse);
                    if (lastHistory != null) {
                        Integer term1 = lastHistory.getTerm();
                        return term1 + 1;
                    }
                }
            }
        }
        return -1;
    }

    public List<History> getRangeTerm(Date startDate, Date endDate) {
        return historyRepos.getRangeTerm(startDate,endDate);
    }

    public List<History> getTableData(int limit, int offset) {
        Query query = em.createQuery("select p from History p order by id asc");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<History> resultList = query.getResultList();
        return resultList;
    }

    public long getCount(String condition) {
        Query query = em.createQuery("select count(p.id) from History p "+condition);
        Long singleResult = (Long) query.getSingleResult();
        return singleResult;
    }
}
