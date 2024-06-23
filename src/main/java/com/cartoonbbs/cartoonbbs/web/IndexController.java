package com.cartoonbbs.cartoonbbs.web;

import com.cartoonbbs.cartoonbbs.po.Cartoon;
import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.servive.ControllerService;
import com.cartoonbbs.cartoonbbs.servive.TagService;
import com.cartoonbbs.cartoonbbs.servive.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class IndexController {
    @Autowired
    private ControllerService controllerService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    private static  final String INPUT="/input";
    private static  final String RESULT="/";
    private  void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());

    }
    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model,HttpSession session) {
        model.addAttribute("page",controllerService.listCartoon(pageable));
//        model.addAttribute("types",typeService.listTypeTop(6));
//        model.addAttribute("tags",tagService.listTagTop(10));
//        model.addAttribute("recommendCartoon",controllerService.listRecommendCartoonTop(8));
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return "index";
    }
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page",controllerService.listCartoon("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";

    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        //model.addAttribute("cartoon",controllerService.getCartoon(id));
        model.addAttribute("cartoon",controllerService.getAndConvert(id));
        return "details";
    }

    @GetMapping("/input")
    public String input(Model model, HttpSession session){
        setTypeAndTag(model);
        model.addAttribute("cartoon",new Cartoon());
        Object a=session.getAttribute("user");
        model.addAttribute("user",a);
        return INPUT;
    }
    @PostMapping("/controller")
    public String post(Cartoon cartoon, RedirectAttributes attributes, HttpSession session, Model model){
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
        return "redirect:/";

    }
}
