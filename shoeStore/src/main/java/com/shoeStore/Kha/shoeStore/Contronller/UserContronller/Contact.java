package com.shoeStore.Kha.shoeStore.Contronller.UserContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
import com.shoeStore.Kha.shoeStore.Model.product;
import com.shoeStore.Kha.shoeStore.Reporsitory.aboutResitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.contactReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class Contact {
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.slideReporsitory slideReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.categoryReporsitory categoryReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory productReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.imageProductReporsitory imageProductReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.commentRepository commentRepository;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory accountReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.newsReporsitory newsReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.aboutResitory aboutResitory;
    @Autowired
    contactReporsitory contactReporsitory;
    public account account(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        account account = new account();
        if (username.equals("anonymousUser")) {
            return account = null;
        } else {
            account = accountReporsitory.findById(username).get();
            return account;
        }

    }
    @RequestMapping(value = "Contact",method = RequestMethod.GET)
    public String getNews(ModelMap modelMap, @RequestParam(name = "title", required = false) String name,
                          HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill bills = (bill)session.getAttribute("bills");
        Iterable<product> products = productReporsitory.findAll();

        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        Iterable<contact> contacts=contactReporsitory.findAll();
        modelMap.addAttribute("billDetail",billDetails);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("about", contacts);
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("products",products);
        return "customer/Home/Contact";
    }
}
