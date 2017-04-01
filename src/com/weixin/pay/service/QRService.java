package com.weixin.pay.service;

import com.weixin.pay.dao.TransationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luc on 16/11/20.
 * 通过扫二维码将油站Id直接传进页面中
 * 不用再通过定位
 */
@Service
public class QRService {

    @Autowired
    private TransationDao transationDao;

    public Map<String, String> queryForStationNameById(String stationId){
        Map<String, String> result = new LinkedHashMap<>();
        String stationName = transationDao.queryForStationNameById(stationId);

        if(stationName.equals("NA")){
            result.put("code", "0");
            result.put("station_id", "NA");
        }
        else{
            result.put("code", "1");
            result.put("station_id", stationId);
        }

        result.put("station_name", stationName);
        return result;
    }
}
