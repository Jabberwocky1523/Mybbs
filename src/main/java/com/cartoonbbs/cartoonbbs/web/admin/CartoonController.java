package com.cartoonbbs.cartoonbbs.web.admin;

import com.cartoonbbs.cartoonbbs.po.Cartoon;
import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.CommentService;
import com.cartoonbbs.cartoonbbs.servive.ControllerService;
import com.cartoonbbs.cartoonbbs.servive.TagService;
import com.cartoonbbs.cartoonbbs.servive.TypeService;
import com.cartoonbbs.cartoonbbs.vo.CartoonQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Statement;

@Controller
@RequestMapping("admin")
public class CartoonController {
    private static  final String INPUT="admin/controller-input";
    private static  final String LIST="admin/controller";
    private static  final String REDIRECT_LIST="redirect:/admin/controller";
    @Autowired
    private CommentService commentService;
    @Autowired
    private ControllerService controllerService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("controller")
    public String controller(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                             CartoonQuery cartoon, Model model,HttpSession session){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",controllerService.listCartoon(pageable,cartoon));
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return LIST;
    }


    @PostMapping("/controller/search")
    public String search(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         CartoonQuery cartoon, Model model,HttpSession session){
        model.addAttribute("page",controllerService.listCartoon(pageable,cartoon));
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return "admin/controller::cartoonList";
    }
    @GetMapping("/controller/input")
    public String input(Model model,HttpSession session){
       setTypeAndTag(model);
        model.addAttribute("cartoon",new Cartoon());
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return INPUT;
    }
    private  void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());

    }

    @GetMapping("/controller/{id}/input")
    public String editInput(@PathVariable  Long id, Model model,HttpSession session){
       setTypeAndTag(model);
        Cartoon cartoon = controllerService.getCartoon(id);
        cartoon.init();
        model.addAttribute("cartoon",cartoon);
        model.addAttribute("comments", commentService.listCommentByCartoonId(id));
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return INPUT;
    }
    @GetMapping("/controller/{id}/delete")
    public  String delete(RedirectAttributes attributes,@PathVariable Long id,HttpSession session,Model model){
        controllerService.deleteCartoon(id);
        attributes.addFlashAttribute("message","删除成功");
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return REDIRECT_LIST;
    }

    @PostMapping("/controller")
    public String post(Cartoon cartoon, RedirectAttributes attributes, HttpSession session,Model model){
        cartoon.setUser((User) session.getAttribute("user"));
        cartoon.setType(typeService.getType(cartoon.getType().getId()));
        cartoon.setTags(tagService.listTag(cartoon.getTagIds()));

        Cartoon c = controllerService.saveCartoon(cartoon);
       if (cartoon.getId() == null) {
            c =  controllerService.saveCartoon(cartoon);
        } else {
            c = controllerService.updateCartoon(cartoon.getId(),cartoon);
        }
        if (c == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return REDIRECT_LIST;

    }
}
