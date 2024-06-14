package com.cartoonbbs.cartoonbbs.web.admin;

import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.UserService;
import com.cartoonbbs.cartoonbbs.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class    RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/register")
    public  String registerPage(){
        return "admin/register";
    }

    @PostMapping("/admin/register")
    public String register(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String email,
                        @RequestParam String nickname,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model
                       ){
        String usernameRegExp = "^[a-zA-Z0-9_-]{4,16}$";
        String passwordRegExp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\W]{6,18}$";
        String emailRegExp = "^\\w{3,12}@\\w{1,5}\\.[a-z]{2,3}$";
        boolean un=username.matches(usernameRegExp);
        boolean pwd=password.matches(passwordRegExp);
        boolean e=email.matches(emailRegExp);
        System.out.println(un+" "+pwd+" "+e);

        User user=userService.checkName(username);
        if(user!=null){
            attributes.addFlashAttribute("message","用户名重复");
            return "redirect:/admin/register";
        }else if(un&&pwd&&e&&user==null){
            String paw=Md5Utils.code(password);
            User auser=new User();
            auser.setPassword(paw);
            auser.setUsername(username);
            auser.setEmail(email);
            auser.setNickname(nickname);
            userService.saveUser(auser);
            attributes.addFlashAttribute("message","成功注册!");
            return "redirect:/admin";
        }
        else {
            attributes.addFlashAttribute("message","注册失败");
            return "redirect:/admin/register";
        }
    }
    @GetMapping("/logout")
    public  String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
