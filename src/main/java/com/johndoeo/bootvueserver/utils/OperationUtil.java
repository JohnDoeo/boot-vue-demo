package com.johndoeo.bootvueserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.johndoeo.bootvueserver.constant.OperationCode;

import java.util.Calendar;
import java.util.Date;

public class OperationUtil {

    public static JSON toJSON(Integer operationCode, Object result, String... message) {
        JSONObject json = new JSONObject();
        json.put(OperationCode.CODE, operationCode);
        json.put(OperationCode.RESULT, result);
        if(message == null || message.length == 0){
            if(operationCode == OperationCode.GET){
                json.put(OperationCode.MESSAGE, "查询成功");
            }else if(operationCode == OperationCode.ADD){
                json.put(OperationCode.MESSAGE, "新增成功");
            }else if(operationCode == OperationCode.UPDATE){
                json.put(OperationCode.MESSAGE, "修改成功");
            }else if(operationCode == OperationCode.SET){
                json.put(OperationCode.MESSAGE, "设置成功");
            }else if(operationCode == OperationCode.DELETE){
                json.put(OperationCode.MESSAGE, "删除成功");
            }
        }else{
            json.put(OperationCode.MESSAGE, message);
        }

        return json;
    }

    public static Date getWeekStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    public static Date getLastWeekStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR,-7);
        Date date = cal.getTime();
        return date;
    }
}
