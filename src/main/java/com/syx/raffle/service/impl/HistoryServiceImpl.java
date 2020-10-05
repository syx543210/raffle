package com.syx.raffle.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.syx.raffle.dao.HistoryDao;
import com.syx.raffle.entity.History;
import com.syx.raffle.service.HistoryService;
import com.syx.raffle.utils.DateUtils;
import com.syx.raffle.utils.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryDao historyDao;

    @Override
    public History selectByTerm(Integer term) {
        return null;
    }

    @Override
    public List<History> selectAll() {
        return historyDao.selectAll();
    }

    @Override
    public void getRaffleInfoFormWeb(String term) {
        if (term == null || "".equals(term)) {//抓取所有期数的数据
            URL url = null;
            try {
                String sendUrl = "https://kaijiang.500.com/shtml/sd/2004001.shtml";
                url = new URL(sendUrl);
                Document document = Jsoup.parse(url, 2000);
                Element body = document.body();
                Elements selectList = body.getElementsByClass("iSelectList");
                Elements children = selectList.get(0).children();
                int size = children.size();
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Integer text = Integer.parseInt(children.get(i).text());
                    list.add(text);
                }
                Collections.reverse(list);
                List<Integer> existTermList = historyDao.getExistTermList(list);
                list.removeAll(existTermList);

                List<History> historyList = new ArrayList<>();
                for (Integer nowTerm : list) {
                    History history = getBySelectTerm(nowTerm.toString());
                    if (history != null) {
                        historyList.add(history);
                    }
                }
                if (historyList != null && historyList.size() > 0) {
                    historyDao.saveHistoryList(historyList);
                }
            } catch (Exception e) {
                System.out.println("获取网站信息失败");
            }
        } else {//抓取指定期数的数据
            History history = getBySelectTerm(term);
            if (history != null) {
                historyDao.saveHistory(history);
            }
        }
    }

    @Override
    public Integer getNewTerm(Date nowDate) {
        return historyDao.getLastTerm(nowDate);
    }

    @Override
    public Map getRangeTerm(String startDate, String endDate) {
        Map dataMap = new HashMap();
        List<String> firstNumberList = new ArrayList<>();
        List<String> secondNumberList = new ArrayList<>();
        List<String> thirdNumberList = new ArrayList<>();
        List<String> dateTitleList = new ArrayList<>();
        List<String> numberTitle = new ArrayList<>();
        List<History> historyDataList = new ArrayList<>();
        numberTitle.add("第一个数");
        numberTitle.add("第二个数");
        numberTitle.add("第三个数");
        dateTitleList.add(startDate);
        for (int i = 1; i <= 6; i++) {
            String targetDay = DateUtils.getTargetDay(startDate, i);
            if (targetDay == null) {
                dataMap.put("success", false);
                dataMap.put("msg", "获取日期信息错误");
                return dataMap;
            }
            dateTitleList.add(targetDay);
        }


        Date startDay = DateUtils.getDate(startDate);
        Date endDay = DateUtils.getDate(endDate);
        if (startDay == null || endDay == null) {
            dataMap.put("success", false);
            dataMap.put("msg", "获取日期范围错误");
            return dataMap;
        }

        List<History> historyList = historyDao.getRangeTerm(startDay, endDay);
        if (historyList == null || historyList.size() == 0) {
            dataMap.put("success", false);
            dataMap.put("msg", "暂无相关信息");
            return dataMap;
        } else if (historyList.size() == 7) {//7天数据都存在
            for (History history : historyList) {
                firstNumberList.add(history.getFirstNum().toString());
                secondNumberList.add(history.getSecondNum().toString());
                thirdNumberList.add(history.getThirdNum().toString());
            }
            historyDataList = historyList;
        } else {//缺少数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (String date : dateTitleList) {
                int count = 0;
                for (History history : historyList) {
                    Date date1 = history.getDate();
                    String formatDate = sdf.format(date1);
                    if (date.equals(formatDate)) {
                        count = 1;
                        firstNumberList.add(history.getFirstNum().toString());
                        secondNumberList.add(history.getSecondNum().toString());
                        thirdNumberList.add(history.getThirdNum().toString());
                        historyDataList.add(history);
                        break;
                    }
                }
                if (count == 0) {
                    firstNumberList.add("");
                    secondNumberList.add("");
                    thirdNumberList.add("");
                    History emptyHistory = new History();
                    if(date.equals(endDate)){
                        emptyHistory.setDate(endDay);
                    }
                    historyDataList.add(emptyHistory);
                }
            }
        }

//        JSONObject jsonObject = JSONObject.parseObject("{normal:{ label:{show:true,position:'top',textStyle:{color:'black',fontSize:16 }}}}");

        dataMap.put("success", true);
        dataMap.put("dateTitleList", dateTitleList);
        dataMap.put("numberTitle", numberTitle);
        List<Map> dataList = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("name", "第一个数");
        map1.put("type", "bar");
        map1.put("data", firstNumberList);
//        map1.put("itemStyle", jsonObject);

        dataList.add(map1);

        Map map2 = new HashMap();
        map2.put("name", "第二个数");
        map2.put("type", "bar");
        map2.put("data", secondNumberList);
//        map2.put("itemStyle", jsonObject);
        dataList.add(map2);

        Map map3 = new HashMap();
        map3.put("name", "第三个数");
        map3.put("type", "bar");
        map3.put("data", thirdNumberList);
//        map3.put("itemStyle", jsonObject);
        dataList.add(map3);

        dataMap.put("dataList", dataList);
        dataMap.put("historyData",historyDataList);
        return dataMap;
    }

    @Override
    public Map getTableData(int limit, int offset) {
        Map map = new HashMap();
        List<History> tableData = historyDao.getTableData(limit, offset);
        String condition = " where 1=1 ";
        long count = historyDao.getCount(condition);
        map.put("total",count);
        map.put("rows",tableData);
        return map;
    }

    @Override
    public Map getTermData(int limit, int offset) {
        Map map = new HashMap();
        List<History> tableData = historyDao.getTableData(limit, offset);

        if(tableData!=null&&tableData.size()>0){
            History history = tableData.get(0);
            Integer firstNum = history.getFirstNum();
            Integer secondNum = history.getSecondNum();
            Integer thirdNum = history.getThirdNum();
            for(int i=0;i<10;i++){
                if(i==firstNum){

                }

            }
        }

        String condition = " where 1=1 ";
        long count = historyDao.getCount(condition);
        map.put("total",count);
        map.put("rows",tableData);
        return map;
    }

    public History getBySelectTerm(String term) {
        URL url = null;
        try {
            String sendUrl = "https://kaijiang.500.com/shtml/sd/" + term + ".shtml";
            url = new URL(sendUrl);
            Document document = Jsoup.parse(url, 2000);
            Element body = document.body();
            Elements ball_orang = body.getElementsByClass("ball_orange");
            String html = body.toString();
            int i = html.indexOf("开奖日期：");

            if (i != -1) {
                String dateStr = html.substring(i + 5, i + 16);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                Date date = sdf.parse(dateStr);
                if (ball_orang != null) {
                    int size = ball_orang.size();
                    if (size == 3) {
                        String firstNum = ball_orang.get(0).text();
                        String secondNum = ball_orang.get(1).text();
                        String thirdNum = ball_orang.get(2).text();
                        History history = new History();
                        history.setFirstNum(Integer.parseInt(firstNum));
                        history.setSecondNum(Integer.parseInt(secondNum));
                        history.setThirdNum(Integer.parseInt(thirdNum));
                        history.setDate(date);
                        history.setTerm(Integer.parseInt(term));
                        return history;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("获取网站信息失败");
        }
        return null;
    }
}
