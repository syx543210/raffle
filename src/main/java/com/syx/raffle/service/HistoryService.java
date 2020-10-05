package com.syx.raffle.service;

import com.syx.raffle.entity.History;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HistoryService {

    History selectByTerm(Integer term);


    List<History> selectAll();

    //获取彩票信息
    void getRaffleInfoFormWeb(String term);

    //查询当前期数是否插入返回最新期数
    Integer getNewTerm(Date nowDate);

    Map getRangeTerm(String startDate, String endDate);

    Map getTableData(int limit, int offset);

    Map getTermData(int limit, int offset);
}
