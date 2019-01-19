package cn.edu.pku.ss.ct.web.dao;

import cn.edu.pku.ss.ct.web.bean.Calllog;

import java.util.List;
import java.util.Map;

public interface CalllogDao {
    List<Calllog> queryMonthDatas(Map<String, Object> paramMap);
}
