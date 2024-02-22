package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.slideReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping(value = "admin")
public class slideQcController {
    @Autowired
    slideReporsitory slideReporsitory;
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
    @GetMapping(value = "viewSlide")
    public String getSlide(ModelMap modelMap){
        Iterable<slide> slides = slideReporsitory.findAll();
        modelMap.addAttribute("slides",slides);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/slideQc/viewSlide";
    }
    @RequestMapping(value = "newSlide",method = RequestMethod.GET)
    public String newSlide(ModelMap modelMap){
        slide slide = new slide();
        Iterable<product> products = productReporsitory.findAll();
        modelMap.addAttribute("slides",slide);
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/slideQc/newSlide";
    }
    @RequestMapping(value = "newSlide",method = RequestMethod.POST)
    public String newSlide(ModelMap modelMap, @RequestParam("imageFile") MultipartFile imageFile,
                           @Valid @ModelAttribute("slides") slide slide, BindingResult result)throws IOException {
        Iterable<product> products = productReporsitory.findAll();
        if(result.hasErrors() ) {
            return "admin/slideQc/newSlide";
        }
        try{
            if(!imageFile.isEmpty()) {

                String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);
                slide.setImages(randomFileName);
            }else {
                modelMap.addAttribute("account",account());
                modelMap.addAttribute("Total",Total());
                modelMap.addAttribute("products",products);
                modelMap.addAttribute("imageError","Catalog photo may not be blank!");
                return "admin/slideQc/newSlide";
            }

            slideReporsitory.save(slide);
        }catch (Exception e){
            modelMap.addAttribute("account",account());
            modelMap.addAttribute("Total",Total());
            modelMap.addAttribute("products",products);
            return "admin/slideQc/newSlide";
        }

        return "redirect:/admin/viewSlide";
    }
    @RequestMapping(value = "/editSlide/{IdSlide}",method = RequestMethod.GET)
    public String getSlide(ModelMap modelMap, @PathVariable Integer IdSlide){
        Iterable<product> products = productReporsitory.findAll();
        slide slides = slideReporsitory.findById(IdSlide).get();
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("slide",slides);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/slideQc/editSlide";
    }
    @PostMapping(value = "/editSlide/{IdSlide}")
    public String saveOrUpdate(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model,@PathVariable Integer IdSlide,
                               @Valid @ModelAttribute("news")slide slide, BindingResult result)  {
        slide entity = slide;
        Iterable<product> products = productReporsitory.findAll();
        slide newsItem = slideReporsitory.findById(IdSlide).get();
        String ImagePr = newsItem.getImages();

        if(result.hasErrors()) {

            model.addAttribute("products",products);
            return "admin/slideQc/editSlide";
        }
        String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
        try {
            if(!imageFile.isEmpty()) {
                Path paths = Paths.get(uploadsDir+ File.separator + ImagePr);
                try {
                    Files.deleteIfExists(paths);

                } catch (Exception e) {
                    System.out.println(e);
                    return "redirect:/admin/viewSlide/";
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
                    model.addAttribute("account",account());
                    model.addAttribute("Total",Total());
                    model.addAttribute("imageError","News photos cannot be blank!");
                    model.addAttribute("products",products);
                    return "admin/slideQc/editSlide";
                }

            }

            int idP = entity.getProduct().getIdProduct();
//            entity.setIdCategory(idcategory);
            entity.setIdSlide(IdSlide);
            slideReporsitory.save(entity);

        }catch (Exception e){
            model.addAttribute("account",account());
            model.addAttribute("Total",Total());
            model.addAttribute("products",products);
            model.addAttribute("error",e.toString());

            return "admin/slideQc/editSlide";
        }

        return "redirect:/admin/viewSlide";
    }
    @RequestMapping(value = "/deleteSlide/{IdSlide}",method = RequestMethod.GET)
    public String deleteProducts(ModelMap modelMap,@PathVariable Integer IdSlide){
        slide slide = slideReporsitory.findById(IdSlide).get();
        String image = slide.getImages();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return  "redirect:/admin/viewSlide";
            }
            slideReporsitory.deleteById(IdSlide);
        }catch (Exception e){
            return  "redirect:/admin/viewSlide";
        }

        return  "redirect:/admin/viewSlide";
    }
}
