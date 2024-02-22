package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.newsReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "admin")
public class NewsContrroller {
    @Autowired
    newsReporsitory newsReporsitory;
    @Autowired
    productReporsitory productReporsitory;
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

    @RequestMapping(value = "/viewNews",method = RequestMethod.GET)
    public String getNews(ModelMap modelMap, @RequestParam(name = "title", required = false) String name,
                          @RequestParam("page") Optional<Integer> page, //trang hien tai
                          @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleNews"));
        Page<news> resultPage = null;

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
        modelMap.addAttribute("accountPage", resultPage);
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("Total",Total());
        return "admin/news/viewNews";
    }
    @RequestMapping(value = "/viewNewsHistory",method = RequestMethod.GET)
    public String getNewsHhistory(ModelMap modelMap, @RequestParam(name = "title", required = false) String name,
                          @RequestParam("page") Optional<Integer> page, //trang hien tai
                          @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleNews"));
        Page<news> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = newsReporsitory.findByNameContainingHistory(name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = newsReporsitory.findAllHistopry(pageable);
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
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("accountPage", resultPage);
        return "admin/news/historyNews";
    }
    @RequestMapping(value="addNews",method = RequestMethod.GET)
    public String add(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        account account = accountReporsitory.findById(username).get();
        Iterable<product> products = productReporsitory.findAll();
        news news = new news();
        model.addAttribute("account", account());
        model.addAttribute("Total",Total());
        model.addAttribute("products",products);
        model.addAttribute("news",news);
        return "admin/news/addNews";
    }
    @PostMapping("/addNews")
    public String saveOrUpdate(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model,
                               @Valid @ModelAttribute("news") news news, BindingResult result)  {
        news entity = news;
        Iterable<product> products = productReporsitory.findAll();
        if(result.hasErrors()) {
            model.addAttribute("account", account());
            model.addAttribute("Total",Total());
            model.addAttribute("products",products);
            return "admin/news/addNews";
        }
        try {
            if(!imageFile.isEmpty()) {

                String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);
                entity.setImages(randomFileName);
            }else {
                model.addAttribute("account", account());
                model.addAttribute("Total",Total());
                model.addAttribute("products",products);
                model.addAttribute("imageError","News photos cannot be blank!");
                return "admin/news/addNews";
            }

            newsReporsitory.save(entity);

        }catch (Exception e){
            model.addAttribute("Total",Total());
            model.addAttribute("account", account());
            model.addAttribute("products",products);
            model.addAttribute("error",e.toString());
            return "admin/news/addNews";
        }

        return "redirect:/admin/viewNews";
    }
    @RequestMapping(value = "/deleteNews/{IdNews}",method = RequestMethod.GET)
    public String deleteNews(ModelMap modelMap,@PathVariable int IdNews){
        news newsdelete = newsReporsitory.findById(IdNews).get();
        newsdelete.setProperti(1);
        newsdelete.setIdNews(IdNews);
        newsReporsitory.save(newsdelete);
        return "redirect:/admin/viewNews";
    }
    @RequestMapping(value = "/ristoryNews/{IdNews}",method = RequestMethod.GET)
    public String ristoryNews(ModelMap modelMap,@PathVariable int IdNews){
        news newsdelete = newsReporsitory.findById(IdNews).get();
        newsdelete.setProperti(0);
        newsdelete.setIdNews(IdNews);
        newsReporsitory.save(newsdelete);
        return "redirect:/admin/viewNewsHistory";
    }
    @RequestMapping(value = "/deleteNEws/{IdNews}",method = RequestMethod.GET)
    public String deleteProducts(ModelMap modelMap,@PathVariable Integer IdNews){
        news News = newsReporsitory.findById(IdNews).get();
        String image = News.getImages();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return  "redirect:/admin/viewNews";
            }
            newsReporsitory.deleteById(IdNews);
        }catch (Exception e){
            return  "redirect:/admin/viewNews";
        }

        return  "redirect:/admin/viewNews";
    }
    @RequestMapping(value = "/deleteNEwsHistory/{IdNews}",method = RequestMethod.GET)
    public String deleteProductsHistory(ModelMap modelMap,@PathVariable Integer IdNews){
        news News = newsReporsitory.findById(IdNews).get();
        String image = News.getImages();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return  "redirect:/admin/viewNewsHistory";
            }
            newsReporsitory.deleteById(IdNews);
        }catch (Exception e){
            return  "redirect:/admin/viewNewsHistory";
        }

        return  "redirect:/admin/viewNewsHistory";
    }
    @RequestMapping(value = "/editNews/{IdNews}",method = RequestMethod.GET)
    public String getNews(ModelMap modelMap, @PathVariable Integer IdNews){
        Iterable<product> products = productReporsitory.findAll();
        news newsViews = newsReporsitory.findById(IdNews).get();
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("news",newsViews);
        return "admin/news/editNews";
    }
    @PostMapping("/editNews/{IdNews}")
    public String saveOrUpdate(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model,@PathVariable Integer IdNews,
                               @Valid @ModelAttribute("news")news news, BindingResult result)  {
        news entity = news;
        Iterable<product> products = productReporsitory.findAll();
        news newsItem = newsReporsitory.findById(IdNews).get();
        String ImagePr = newsItem.getImages();

        if(result.hasErrors()) {
            model.addAttribute("account", account());
            model.addAttribute("Total",Total());
            model.addAttribute("products",products);
            return "admin/news/editNews";
        }
        String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
        try {
            if(!imageFile.isEmpty()) {
                Path paths = Paths.get(uploadsDir+ File.separator + ImagePr);
                try {
                    Files.deleteIfExists(paths);

                } catch (Exception e) {
                    System.out.println(e);
                    return "redirect:/admin/viewNews/";
                }
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);
                entity.setImages(randomFileName);
            }else {
                if(!ImagePr.isEmpty()){
                    entity.setImages(ImagePr);
                }else {
                    model.addAttribute("account", account());
                    model.addAttribute("Total",Total());
                    model.addAttribute("imageError","News photos cannot be blank!");
                    model.addAttribute("products",products);
                    return "admin/news/editNews";
                }

            }

            int idP = entity.getProduct().getIdProduct();
//            entity.setIdCategory(idcategory);
            entity.setIdNews(IdNews);
            newsReporsitory.save(entity);

        }catch (Exception e){
            model.addAttribute("account", account());
            model.addAttribute("Total",Total());
            model.addAttribute("products",products);
            model.addAttribute("error",e.toString());

            return "admin/news/editNews";
        }

        return "redirect:/admin/viewNews/";
    }
    @RequestMapping(value ="ViewNews/{IdNews}",method = RequestMethod.GET)
    public String viewPage (ModelMap modelMap,@PathVariable Integer IdNews){
        news news = newsReporsitory.findById(IdNews).get();
        modelMap.addAttribute("News",news);
        modelMap.addAttribute("account", account());
        modelMap.addAttribute("Total",Total());
        return "admin/news/viewPageNews";
    }
}
