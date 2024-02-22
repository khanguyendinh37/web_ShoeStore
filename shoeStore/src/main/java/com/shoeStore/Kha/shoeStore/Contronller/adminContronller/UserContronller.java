package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.news;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "admin")
public class UserContronller {
    @Autowired
    accountReporsitory accountReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory billReporsitory;
    public int Total(){
        ArrayList<bill> bills = billReporsitory.findByAll();
        int count = bills.size();
        return count;
    }

    public account account(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        account account = new account();
        if (username.equals("anonymousUser")) {
            return account = null;
        } else {
            account =  accountReporsitory.findById(username).get();
            return account;
        }

    }
    @RequestMapping(value = "profile/{username}",method = RequestMethod.GET)
    public String profile (ModelMap modelMap,@PathVariable String username){
        account account = accountReporsitory.findById(username).get();
        modelMap.addAttribute("user",account);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/user/profile";
    }

    @RequestMapping(value = "/profile",method = RequestMethod.GET)
    public String getNews(ModelMap modelMap, @RequestParam(name = "fullName", required = false) String name,
                          @RequestParam("page") Optional<Integer> page, //trang hien tai
                          @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("fullName"));
        Page<account> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = accountReporsitory.findByNameContaining(name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = accountReporsitory.findAll(pageable);
        }
        int totalPages = resultPage.getTotalPages(); // tinh toan so trang hien thi
        if(totalPages>0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);

            if(totalPages>5) {
                if(end == totalPages) start = end -5;
                else if(start == 1) end = start + 5;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);

        }
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("accountPage", resultPage);
        modelMap.addAttribute("account",account());
        return "admin/user/viewProfile";
    }
}
