package com.syx.raffle.controller;

import com.syx.raffle.entity.History;
import com.syx.raffle.utils.DateUtils;
import com.syx.raffle.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @RequestMapping("/selectByTerm")
    @ResponseBody
    public Object selectByTerm(@RequestParam(value = "term") Integer term) {
        return historyService.selectByTerm(term);
    }


    @RequestMapping("/index")
    public String toindex(Map<String, Object> map) {
        map.put("name", "hello");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");

        map.put("list", list);
        return "index";
    }


    @RequestMapping("/selectAll")
    public String toindex(Model model) {
        List<History> historyList = historyService.selectAll();
        model.addAttribute("list", historyList);
        return "page/chart";
    }


    @RequestMapping("/goToEChart")
    public String goToEChart() {

        return "page/echart";
    }

    @RequestMapping("/showBack")
    @ResponseBody
    public Map showBack(@RequestParam(value = "type") String type, @RequestParam(value = "startDate", required = false) String startDate,
                        @RequestParam(value = "endDate", required = false) String endDate, @RequestParam(value = "number", required = false) Integer number) {
        Map resultMap = new HashMap();
        if(type == null||"".equals(type)){
            resultMap.put("success",false);
            resultMap.put("msg","获取类型错误");
        }else if("1".equals(type)){//近七天
            endDate= DateUtils.getBeforeDay(-1);//昨天
            startDate = DateUtils.getBeforeDay(-7);
            Map dataMap = historyService.getRangeTerm(startDate,endDate);
            dataMap.put("title","近7天数据1");
            return dataMap;
        }

        return resultMap;
    }



    @RequestMapping("/getTableData")
    @ResponseBody
    public Map getTableData(@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset) {
        return historyService.getTableData(limit,offset);
    }


    @RequestMapping("/getTermData")
    @ResponseBody
    public Map getTermData(@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset) {
        return historyService.getTermData(limit,offset);
    }

    @RequestMapping(value = "/getPostData")
    @ResponseBody
    public Map getPostData(@RequestBody Map params) {
        System.out.println(params.get("offset"));
        System.out.println(params.get("limit"));
        Map map = new HashMap();
        map.put("1","2");
        return map;
    }


}
