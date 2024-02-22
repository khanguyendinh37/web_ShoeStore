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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "")
public class news {
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
    @Autowired
    newsReporsitory newsReporsitory;
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
    @RequestMapping(value = "News",method = RequestMethod.GET)
    public String getNews(ModelMap modelMap, @RequestParam(name = "title", required = false) String name,
                          @RequestParam("page") Optional<Integer> page, //trang hien tai
                          @RequestParam("size") Optional<Integer> size,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        Iterable<product> products = productReporsitory.findAll();

        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleNews"));
        Page<com.shoeStore.Kha.shoeStore.Model.news> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = newsReporsitory.findByNameContaining(name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = newsReporsitory.findAll(pageable);
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
        modelMap.addAttribute("billDetail",billDetails);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("viewNews", resultPage);
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("products",products);
        return "customer/Home/news";
    }

//    @RequestMapping(value = "News/{IdNews}",method = RequestMethod.GET)
//    public String getNewsDettail(ModelMap modelMap,@PathVariable Integer IdNews){
//        news news = newsReporsitory.findById(IdNews).get();
//        modelMap.addAttribute("newDetail",news);
//        return "customer/Home/newsDetail";
//    }
}
