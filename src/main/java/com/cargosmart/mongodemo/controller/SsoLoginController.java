package com.cargosmart.mongodemo.controller;

import com.cargosmart.mongodemo.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class SsoLoginController {

    @Autowired
    TokenUtil tokenUtil;

    @GetMapping("redirect")
    public String redirect(HttpServletRequest request, HttpServletResponse response) {
        String token = tokenUtil.createJWT(request.getParameter("userId"));
        response.addCookie(new Cookie("token", token));
        return "redirect:http://localhost:9000";
    }

    @GetMapping("createToken")
    @ResponseBody
    public Map createToken() {
        Map<String, String> resp = new HashMap<>(5);
        resp.put("token", "token-38217372");
        return resp;
    }

    @GetMapping("validToken")
    @ResponseBody
    public ResponseEntity validTokne(@RequestParam(value = "token") String token) {
        System.out.println("enter validate Token....");
        Claims claims = tokenUtil.parseJWT(token);
        if (ObjectUtils.isNotEmpty(claims)) {
            System.out.println(claims.getIssuer());
            System.out.println(claims.getAudience());
            System.out.println(claims.get("base64UserId"));
            return ResponseEntity.ok("pass");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("token incorrectly");
    }

}
