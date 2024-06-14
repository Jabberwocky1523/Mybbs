package com.cartoonbbs.cartoonbbs.web.admin;

import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.UserService;
import com.cartoonbbs.cartoonbbs.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class  UserOnlineController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/useronlinecheck")
    public String UserOnline(
                           HttpSession session,
                           RedirectAttributes attributes,
                           Model model
    ){
        List<User> UserOnlineList=userService.checkState();
        model.addAttribute("useronlinelist",UserOnlineList);
        return "/admin/useronlinecheck";
    }
    @GetMapping("/admin/updateuser")
    public String update(HttpSession session, RedirectAttributes attributes, Model model)
    {   User user=(User)session.getAttribute("user");
        model.addAttribute("user",user);
        return "admin/updateuser";}

    @GetMapping("/admin/updateuser/{id}")
    public  String updateUser(Model model, @PathVariable Long id,@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String email,
                              @RequestParam String nickname)
    {
        User a=userService.findByid(id);
        a.setUsername(username);
        a.setPassword(Md5Utils.code(password));
        a.setEmail(email);
        a.setNickname(nickname);
        userService.updateUser(a);
        model.addAttribute("user",a);
        return "admin/login";
    }

}
