package com.shoeStore.Kha.shoeStore.Contronller.UserContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Controller
@RequestMapping("/")
public class product {
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
    @GetMapping(value = "product")
    public String getHome (ModelMap modelMap, @RequestParam(name = "titleProduct", required = false) String name,
                           @RequestParam("page") Optional<Integer> page, //trang hien tai
                           @RequestParam("size") Optional<Integer> size, @RequestParam("IdProudct") Optional<Integer> IdProudct ,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");

        Iterable<category> categories = categoryReporsitory.findAll();
        Iterable<slide> slides = slideReporsitory.findAll();
        modelMap.addAttribute("slides",slides);
        modelMap.addAttribute("categories",categories);
        modelMap.addAttribute("account",account());
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(25);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleProduct"));
        Page<com.shoeStore.Kha.shoeStore.Model.product> resultPage = null;

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
        return "customer/Home/product";
    }
    @GetMapping(value = "productCategory/{IdCategory}")
    public String getCategoryProduct (ModelMap modelMap, @PathVariable Integer IdCategory, @RequestParam(name = "titleProduct", required = false) String name,
                                      @RequestParam("page") Optional<Integer> page, //trang hien tai
                                      @RequestParam("size") Optional<Integer> size,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");

        Iterable<category> categories = categoryReporsitory.findAll();
        Iterable<slide> slides = slideReporsitory.findAll();
        Iterable<com.shoeStore.Kha.shoeStore.Model.product> productReporsitoryAll = productReporsitory.findAll();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(15);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("IdCategory"));
        Page<com.shoeStore.Kha.shoeStore.Model.product> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = productReporsitory.findByNameContainingCategory(IdCategory,name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = productReporsitory.findAllCategory(IdCategory,pageable);
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
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("slides",slides);
        modelMap.addAttribute("categories",categories);
        category category = categoryReporsitory.findById(IdCategory).get();
        modelMap.addAttribute("products", resultPage);

        modelMap.addAttribute("billDetail",billDetails);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("category",category);
        modelMap.addAttribute("product",productReporsitoryAll);
        return "customer/Home/producCategory";
    }

}
