package me.mw.workoutisgood.Controller;

import me.mw.workoutisgood.service.loginService;
import me.mw.workoutisgood.service.jdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

@Controller
public class menuController {
    @Autowired
    loginService loginService;
    @Autowired
    jdbcService jdbcService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("id",loginService.getId());
        return "home";
    }
    @GetMapping("/signUp")
    public String signUp(Model model){
        String [] arrId=jdbcService.getAllId();
        model.addAttribute("arrId",arrId);
        return "signUp";
    }
    @PostMapping("/infoSubmit")
    public String infoSubmit(String id,String password){
        jdbcService.createAccount(id,password);
        return "redirect:/";
    }
    @PostMapping("/loginCheck")
    public String loginCheck(String id,String password,Model model){
        String msg="";
        String URL="";
        boolean loginCheck=jdbcService.checkAccount(id,password);
        if(loginCheck){
            loginService.setId(id);
            msg="로그인에 성공 했습니다!";
            URL="/workoutRecord/schedule";
        }
        else{
            msg="로그인에 실패 했습니다. 다시 시도 해주세요!";
            URL="/";
        }
        model.addAttribute("msg",msg);
        model.addAttribute("URL",URL);
        return "loginMessage";
    }
    @GetMapping("/logOut")
    public String logOut(){
        loginService.setId(null);
        return "logOut";
    }
}
