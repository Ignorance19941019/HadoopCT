package cn.edu.pku.ss.ct.web.controller;


import cn.edu.pku.ss.ct.web.bean.Calllog;
import cn.edu.pku.ss.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CalllogController {
    @Autowired
    private CalllogService callLogService;

    @RequestMapping("/query")
    public String query() {
        return "query";
    }

    @RequestMapping("/view")
    public String view(String tel, String calltime, Model model) {

        List<Calllog> logs = callLogService.queryMonthDatas(tel, calltime);

        model.addAttribute("calllogs", logs);

        return "view";
    }
}
