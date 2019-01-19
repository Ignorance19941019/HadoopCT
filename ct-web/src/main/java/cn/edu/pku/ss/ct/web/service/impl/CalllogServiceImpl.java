package cn.edu.pku.ss.ct.web.service.impl;

import cn.edu.pku.ss.ct.web.bean.Calllog;
import cn.edu.pku.ss.ct.web.dao.CalllogDao;
import cn.edu.pku.ss.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalllogServiceImpl implements CalllogService {

    @Autowired
    private CalllogDao callLogDao;

    @Override
    public List<Calllog> queryMonthDatas(String tel, String calltime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("tel", tel);
        if (calltime.length() > 4)
            calltime = calltime.substring(0, 4);
        paramMap.put("year", calltime);

        return callLogDao.queryMonthDatas(paramMap);
    }
}
