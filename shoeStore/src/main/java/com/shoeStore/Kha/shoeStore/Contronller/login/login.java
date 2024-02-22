package com.shoeStore.Kha.shoeStore.Contronller.login;

import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class login {
    @Autowired
    accountReporsitory accountRepository;
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String index(ModelMap modelMap, @RequestParam(value = "success", required = false) Boolean success) {
        if (success == null) {
            return "passlogin/login";
        }
        else
        {
            modelMap.addAttribute("success", success);
            return "passlogin/login";
        }
    }
    @GetMapping(value = "logout")
    public String logout(HttpServletRequest request, HttpSession session){
        // Xóa thông tin phiên làm việc của người dùng
        session.invalidate();
        return "redirect:/login";
    }
}
