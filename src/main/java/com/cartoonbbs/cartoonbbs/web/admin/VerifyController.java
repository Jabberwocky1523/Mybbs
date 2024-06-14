package com.cartoonbbs.cartoonbbs.web.admin;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class VerifyController {


    @GetMapping("/admin/verify")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
//        byte[] captchaOutputStream = null;
//        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
//        try {
//            //生产验证码字符串并保存到session中
//            String verifyCode = captchaProducer.createText();
//            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
//            BufferedImage challenge = captchaProducer.createImage(verifyCode);
//            ImageIO.write(challenge, "jpg", imgOutputStream);
//        } catch (IllegalArgumentException e) {
//            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return ;
//        }
//        captchaOutputStream = imgOutputStream.toByteArray();
//        httpServletResponse.setHeader("Cache-Control", "no-store");
//        httpServletResponse.setHeader("Pragma", "no-cache");
//        httpServletResponse.setDateHeader("Expires", 0);
//        httpServletResponse.setContentType("image/jpeg");
//        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
//        responseOutputStream.write(captchaOutputStream);
//        responseOutputStream.flush();
//        responseOutputStream.close();
        CircleCaptcha captcha= CaptchaUtil.createCircleCaptcha(100,30,5,5);
        String code=captcha.getCode();
        httpServletRequest.getSession().setAttribute("verifyCode", code.toLowerCase());
        captcha.write(httpServletResponse.getOutputStream());
    }
}
