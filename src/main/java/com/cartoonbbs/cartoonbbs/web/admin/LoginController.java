package com.cartoonbbs.cartoonbbs.web.admin;

import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public  String loginPage(){
        return "admin/login";
    }

    @PostMapping("login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model
    ){
        User user=userService.checkUse(username,password);
        String kaptchaCode=session.getAttribute("verifyCode")+"";
        if(user!=null&&verifyCode.toLowerCase().equals(kaptchaCode)){
            userService.changestatementOnline(user);
            session.setAttribute("user",user);
            model.addAttribute("user",user);
        }else {
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/admin";
        }
        if(user.getType()==1)
        {
            return "admin/index";
        }
        else
        {
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public  String logout(HttpSession session,Model model) {
        User user=(User) session.getAttribute("user");
        session.removeAttribute("user");
        userService.changestatementOffline(user);
        return "redirect:/admin";
    }
    @GetMapping("/background")
    public  String loginPage1(){
        return "admin/background";
    }
}
