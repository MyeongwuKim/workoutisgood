package me.mw.workoutisgood.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.mw.workoutisgood.VO.calendarVO;
import me.mw.workoutisgood.VO.historyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class jdbcService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private Integer totalSet=0;
    private Integer targetSet=0;

    public void saveProgressData(String jsonData, calendarVO calVO, int date, String id) {
        String SQL = "SELECT jsonData FROM schedule where id=? and year=? and month =? and date=?";
        List<String> jsonDataList = jdbcTemplate.query(SQL, (rs, rowIndex) -> {
            String jsonData1 = rs.getString("jsonData");
            return jsonData1;
        }, id, calVO.getYear(), calVO.getMonth(), date);

        if (jsonDataList.size() > 0) {
            SQL = "UPDATE schedule set jsonData=? where id=? and year=? and month =? and date=?";
            jdbcTemplate.update(SQL, jsonData, id, calVO.getYear(), calVO.getMonth(), date);
        } else {
            SQL = "INSERT INTO schedule values(?,?,?,?,?)";
            jdbcTemplate.update(SQL, id, calVO.getYear(), calVO.getMonth(), date, jsonData);
        }
    }

    public String[] getProgressData(calendarVO calVO, String id) {
        String SQL = "SELECT jsonData FROM schedule where id=? and year=? and month =?";
        List<String> jsonDataList = jdbcTemplate.query(SQL, (rs, rowIndex) -> {
            String jsonData2 = rs.getString("jsonData");
            return jsonData2;
        }, id, calVO.getYear(), calVO.getMonth());

        String[] arrStr = new String[jsonDataList.size()];
        for (int i = 0; i < jsonDataList.size(); i++) {
            arrStr[i] = jsonDataList.get(i);
        }
        return arrStr;
    }
    public int[] getAverageInfo(String id,String targetName,String targetEvent){
        totalSet=0 ; targetSet=0;
        String SQL = "SELECT jsondata FROM schedule where id=?";
        List<String> jsonDataList = jdbcTemplate.query(SQL, (rs, rowIndex) -> {
            String json= rs.getString("jsondata");
            return json;
        }, id);
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = new JsonArray();
        for(int i=0; i<jsonDataList.size();i++){
            String jsonData=jsonDataList.get(i);
            jsonArray = (JsonArray) jsonParser.parse(jsonData);
            for(int j=0; j<jsonArray.size();j++){
                JsonObject object = (JsonObject) jsonArray.get(j);
                String event=object.get("event").getAsString();
                String name=object.get("name").getAsString();
                if(event.equals(targetEvent))
                    totalSet+=object.get("amount").getAsJsonArray().size();
                if(name.equals(targetName))
                    targetSet+=object.get("amount").getAsJsonArray().size();
            }
        }
        int[] totalAndTarget={totalSet,targetSet};
        return totalAndTarget;
    }
    public  List<historyVO> getLastDayInfo(int curYear, String id, String name) {
        String SQL = "SELECT jsondata, year,month,date FROM schedule where id=? and year=? ";
        List<historyVO> historyVOList = jdbcTemplate.query(SQL, (rs, rowIndex) -> {
            historyVO historyVO = new historyVO();
            historyVO.setYear((rs.getInt("year")));
            historyVO.setMonth((rs.getInt("month")));
            historyVO.setDate((rs.getInt("date")));
            historyVO.setJsonData((rs.getString("jsondata")));
            return historyVO;
        }, id,curYear);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = new JsonArray();
        List<historyVO> listVO=new ArrayList<historyVO>();
        for (int i = 0; i < historyVOList.size(); i++) {
            String jsonData = historyVOList.get(i).getJsonData();
             jsonArray = (JsonArray) jsonParser.parse(jsonData);
             for(int j=0; j<jsonArray.size();j++){
                 JsonObject object = (JsonObject) jsonArray.get(j);
                 String workoutNO = object.get("name").getAsString();
                 if(workoutNO.equals(name)){
                     historyVO vo= new historyVO();
                     String month=""; String date="";
                     Integer addMonth=historyVOList.get(i).getMonth()+1;
                     String json=jsonArray.get(j).toString();
                     if(historyVOList.get(i).getMonth().toString().length()>=2)
                         month=addMonth.toString();
                     else
                         month="0"+addMonth;

                     if(historyVOList.get(i).getDate().toString().length()>=2)
                       date=historyVOList.get(i).getDate().toString();
                     else
                         date="0"+historyVOList.get(i).getDate().toString();

                     String day=month+date;
                     vo.setDay(day);
                     vo.setJsonData(json);
                     listVO.add(vo);
                 }
             }
        };
        Collections.sort(listVO, new Comparator<historyVO>() {
            @Override
            public int compare(historyVO o1, historyVO o2) {
                return o1.getDay().compareTo(o2.getDay());
            }
        });
        return listVO;
    }
    public String[] getAllId(){
        String SQL = "SELECT id FROM account ";
        List<String> listId = jdbcTemplate.query(SQL, (rs, rowIndex) -> {
            String id=rs.getString("id");
            return id;
        });

        String[] arrId=new String[listId.size()];
        for(int i=0; i<listId.size();i++){
            arrId[i]=listId.get(i);
        }
        return arrId;
    }
    public void createAccount(String id,String password){
        String SQL = "INSERT INTO account  values(?,?)";
        jdbcTemplate.update(SQL, id, password);
    }
    public boolean checkAccount(String id,String password){
        String SQL = "SELECT Count(*) FROM account  where id=? and password=?";
        int index=jdbcTemplate.queryForObject(SQL,Integer.class,id,password);
        if(index==1)
            return true;
        else
            return false;
    }
}
