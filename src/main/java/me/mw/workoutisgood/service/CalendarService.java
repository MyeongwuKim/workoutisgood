package me.mw.workoutisgood.service;

import me.mw.workoutisgood.VO.calendarVO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CalendarService {

    private Calendar cal;

    public CalendarService(){
        cal=Calendar.getInstance();
    }

    public void setCalDefault(){
        int currentYear=Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth=Calendar.getInstance().get(Calendar.MONTH);
        cal.set(currentYear,currentMonth,1);
    }
    public Integer getMonth(){
        return cal.get(cal.MONTH);
    }
    public Integer getYear(){
        return cal.get(cal.YEAR);
    }
    //month start 0 index
    public void setCal(int year,int month){
        cal.set(year,month,1);
    }

    public List<calendarVO> getMaxDay(){
        int maxDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int plusDayByFirst=cal.get(Calendar.DAY_OF_WEEK)-1;
        maxDay+=plusDayByFirst;
        int maxDayFirst=maxDay;

        Calendar cal2=Calendar.getInstance();
        Integer currentYear=cal.get(cal.YEAR);
        Integer currentMonth=cal.get(cal.MONTH);
        int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal2.set(currentYear,currentMonth,lastDay);
        int dayOfWeek=cal2.get(cal2.DAY_OF_WEEK);
        int plusDayByLast=7-dayOfWeek;
        maxDay+=plusDayByLast;

        Integer dayInfo=1;
        HashMap<Integer,String> cal_map= new HashMap<>();
        for(int i=1; i<=maxDay; i++){
            if(i>=cal.get(Calendar.DAY_OF_WEEK) && i<=maxDay-plusDayByLast){
                cal_map.put(i,dayInfo.toString());
                dayInfo++;
            }
            else
                cal_map.put(i," ");
        }
        List<calendarVO> list=new ArrayList<>();
        for(int i=0; i<maxDay/7; i++){
            calendarVO vo=new calendarVO(cal_map.get(1+i*7),cal_map.get(2+i*7),cal_map.get(3+i*7),
                    cal_map.get(4+i*7),cal_map.get(5+i*7),cal_map.get(6+i*7),cal_map.get(7+i*7));
            vo.setYear(currentYear);
            vo.setMonth(currentMonth);
            list.add(i,vo);
        }
        return list;
    }
}
