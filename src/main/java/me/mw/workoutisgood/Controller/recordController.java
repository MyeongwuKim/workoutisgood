package me.mw.workoutisgood.Controller;

import me.mw.workoutisgood.service.loginService;
import me.mw.workoutisgood.service.jdbcService;
import me.mw.workoutisgood.VO.calendarVO;
import me.mw.workoutisgood.VO.workoutInfoVO;
import me.mw.workoutisgood.service.CalendarService;
import me.mw.workoutisgood.VO.historyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/workoutRecord",method ={RequestMethod.POST,RequestMethod.GET})
public class recordController {

    @Autowired
    private CalendarService cal;
    @Autowired
    jdbcService jdbcService;
    @Autowired
    loginService loginService;
    @Autowired
    ServletContext servletContext;

    @GetMapping("/saveProgress")
    public String saveProgress(String jsonData,@ModelAttribute calendarVO calVO,int date){
        jdbcService.saveProgressData(jsonData,calVO,date,loginService.getId());
        return "redirect:/workoutRecord/schedule";
    }
    @PostMapping("/workoutProgress")
    public String progress(Model model,String jsonData,@ModelAttribute calendarVO calVO,String date){
/*        Gson gson = new Gson();
        List<workoutInfoVO> list_workoutVO = gson.fromJson(jsonData,
                new TypeToken<List<workoutInfoVO>>(){}.getType());
        model.addAttribute("list_workoutVO",list_workoutVO);*/
        List<calendarVO> cal_list=cal.getMaxDay();
        model.addAttribute("cal_list",cal_list);
        model.addAttribute("jsonData",jsonData);
        model.addAttribute("calVO",calVO);
        model.addAttribute("date",date);
        return "workoutRecord/progress";
    }
    @PostMapping("/addWorkoutList")
    public String addList(Model model,String jsonData,@ModelAttribute calendarVO calVO,String date){
        model.addAttribute("jsonData",jsonData);
        model.addAttribute("calVO",calVO);
        model.addAttribute("date",date);
        return "workoutRecord/addList";
    }

    @PostMapping("/workoutInfo")
    public String info(Model model ,@ModelAttribute workoutInfoVO VO){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy");
        Date time = new Date();
        String curYear = format.format(time);
        List<historyVO> listVO=jdbcService.getLastDayInfo(Integer.parseInt(curYear),loginService.getId(),VO.getName());
        String[] jsonData= new String[listVO.size()];
        String[] day= new String[listVO.size()];
        for(int i=0; i<listVO.size();i++){
            jsonData[i]= listVO.get(i).getJsonData();
            day[i]=listVO.get(i).getDay();
        }
        int[] totalAndTarget=jdbcService.getAverageInfo(loginService.getId(),VO.getName(),VO.getEvent());

        model.addAttribute("totalAndTarget",totalAndTarget);
        model.addAttribute("arrDay",day);
        model.addAttribute("arrJsonData",jsonData);
        model.addAttribute("workoutInfo",VO);
        return "workoutRecord/info";
    }
    @PostMapping("/workoutListMain")
    public String listMain(Model model, String event){
        model.addAttribute("event",event);
        return "workoutRecord/listMain";
    }
    @PostMapping("/myWorkout")
    public String list(String jsonData,Model model,@ModelAttribute calendarVO calVO,String date){
        model.addAttribute("jsonData",jsonData);
        model.addAttribute("calVO",calVO);
        model.addAttribute("date",date);
        return "workoutRecord/list";
    }
    @PostMapping("/schedule")
    public String schedule(Model model, @Valid @ModelAttribute calendarVO calVO,
                           Errors errors){
        String id=loginService.getId();
        if(errors.hasErrors()){
            cal.setCalDefault();
            calVO.setYear(cal.getYear());
            calVO.setMonth(cal.getMonth());
        }
        else{
            cal.setCal(calVO.getYear(),calVO.getMonth());
        }

        if(jdbcService.getProgressData(calVO,loginService.getId()).length>0){
            String[] arrJsonData=jdbcService.getProgressData(calVO,loginService.getId());
            model.addAttribute("arrJsonData",arrJsonData);
        }

        List<calendarVO> cal_list=cal.getMaxDay();
        model.addAttribute("cal_list",cal_list);
        model.addAttribute("calVO",calVO);
        model.addAttribute("id",id);
        return "workoutRecord/schedule";
    }
}
