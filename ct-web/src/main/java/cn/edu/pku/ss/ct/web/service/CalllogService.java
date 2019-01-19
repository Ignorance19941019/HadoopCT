package cn.edu.pku.ss.ct.web.service;

import cn.edu.pku.ss.ct.web.bean.Calllog;

import java.util.List;

public interface CalllogService {
    List<Calllog> queryMonthDatas(String tel, String calltime);
}
