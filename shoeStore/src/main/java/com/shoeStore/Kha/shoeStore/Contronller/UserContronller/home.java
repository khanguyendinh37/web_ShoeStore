package com.shoeStore.Kha.shoeStore.Contronller.UserContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
import com.shoeStore.Kha.shoeStore.Model.product;
import com.shoeStore.Kha.shoeStore.Reporsitory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "")
public class home {
    @Autowired
    slideReporsitory slideReporsitory;
    @Autowired
    categoryReporsitory categoryReporsitory;
    @Autowired
    productReporsitory productReporsitory;
    @Autowired
    imageProductReporsitory imageProductReporsitory;
    @Autowired
    commentRepository commentRepository;
    @Autowired
    accountReporsitory accountReporsitory;

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


    @GetMapping(value = "home")
    public String getHome (ModelMap modelMap, @RequestParam(name = "titleProduct", required = false) String name,
                           @RequestParam("page") Optional<Integer> page, //trang hien tai
                           @RequestParam("size") Optional<Integer> size ,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");


        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        Iterable<category> categories = categoryReporsitory.findAll();
        Iterable<slide> slides = slideReporsitory.findAll();
        modelMap.addAttribute("slides",slides);
        modelMap.addAttribute("categories",categories);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(25);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleProduct"));
        Page<product> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = productReporsitory.findByNameContaining(name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = productReporsitory.findAll(pageable);
        }
        int totalPages = resultPage.getTotalPages(); // tinh toan so trang hien thi
        if(totalPages>0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);

            if(totalPages>5) {
                if(end == totalPages) start = end -25;
                else if(start == 1) end = start + 25;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("products", resultPage);
        modelMap.addAttribute("billDetail",billDetails);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("account",account());
        return "customer/Home/home";
    }

}
