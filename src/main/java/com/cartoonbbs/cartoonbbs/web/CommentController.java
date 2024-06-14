package com.cartoonbbs.cartoonbbs.web;

import com.cartoonbbs.cartoonbbs.po.Comment;
import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.CommentService;
import com.cartoonbbs.cartoonbbs.servive.ControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;

    @Autowired
    private ControllerService controllerService;
    private static  final String REDIRECT_LIST="redirect:/admin/controller";

  @Value("${comment.avatar}")
    private String avatar;


    @GetMapping("/comments/{cartoonId}")
    public String comments( @PathVariable Long cartoonId, Model model) {
        model.addAttribute("comments", commentService.listCommentByCartoonId(cartoonId));

        return "details :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long cartoonId = comment.getCartoon().getId();
        comment.setCartoon(controllerService.getCartoon(cartoonId));
        User user= (User) session.getAttribute("user");
        if(user!=null){
            comment.setAvatar(user.getAvatar());
            boolean a=(user.getType()==1);
            comment.setAdminComment(a);
        }else {
            comment.setAvatar(avatar);
        }

        commentService.saveComment(comment);
        return "redirect:/comments/" + cartoonId;
    }

    @GetMapping("/comment/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        Comment a=commentService.getComment(id);
        Long Cartoonid=a.getCartoon().getId();
        commentService.deleteComment(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/controller/"+Cartoonid+"/input";
    }
}
