package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.category;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.categoryReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;


@Controller
@RequestMapping(value = "admin/viewCategory")
public class CategoryContronller {
    @Autowired
    categoryReporsitory categoryReporsitory;
    @Autowired
    productReporsitory productReporsitory;
    @Autowired
    accountReporsitory accountReporsitory;
    @Autowired
    billReporsitory billReporsitory;
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


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String getCategory(ModelMap modelMap){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        Iterable<category> categories = categoryReporsitory.findAll();
        modelMap.addAttribute("categories",categories);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());

        return "admin/category/viewsCategory";
    }
    @RequestMapping(value = "save",method = RequestMethod.GET)
    public String categoryAdd(ModelMap modelMap){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        category categories = new category();
        modelMap.addAttribute("category",categories);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/category/addCategory";
    }
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public String categoryAdd(ModelMap modelMap,@RequestParam("imageFile") MultipartFile imageFile,
                              @Valid @ModelAttribute("category") category category, BindingResult result
    )throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        if(result.hasErrors() ) {
            return "admin/category/addCategory";
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
                category.setImageCategory(randomFileName);
            }else {
                modelMap.addAttribute("account",account());
                modelMap.addAttribute("Total",Total());
                modelMap.addAttribute("imageError","Catalog photo may not be blank!");
                return "admin/category/addCategory";
            }

            categoryReporsitory.save(category);
        }catch (Exception e){
            return "admin/category/addCategory";
        }

        return "redirect:/admin/viewCategory/";
    }
    @RequestMapping(value = "edit/{categoryID}",method = RequestMethod.GET)
    public String chancategory(ModelMap modelMap, @PathVariable Integer categoryID){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        category category= categoryReporsitory.findById(categoryID).get();
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("category",category);
        return "admin/category/editCategory";
    }
    @RequestMapping(value = "edit/{categoryID}",method = RequestMethod.POST)
    public String chancategory(ModelMap modelMap, @PathVariable Integer categoryID,@RequestParam("imageFile") MultipartFile imageFile,
                               @Valid @ModelAttribute("category") category category, BindingResult result){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        if(result.hasErrors() ) {
            return "admin/category/editCategory";
        }
        category entity = categoryReporsitory.findById(categoryID).get();
        String image = entity.getImageCategory();
        String uploadsDir = "uploads/images/";
        try{
            if(!imageFile.isEmpty()) {
                Path path = Paths.get(uploadsDir+ File.separator + image);
                try {
                    Files.deleteIfExists(path);

                } catch (Exception e) {
                    System.out.println(e);
                    return "redirect:/admin/viewCategory/";
                }
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path1 = Paths.get(filePath);
                Files.write(path1, bytes);
                category.setImageCategory(randomFileName);
            }else {
                if(!image.isEmpty()){
                    category.setImageCategory(image);
                }else {
                    modelMap.addAttribute("account",account());
                    modelMap.addAttribute("Total",Total());
                    modelMap.addAttribute("imageError","Catalog photo may not be blank!");
                    return "admin/category/editCategory";
                }

            }
            category.setIdCategory(categoryID);
            categoryReporsitory.save(category);
        }catch (Exception e){
            return "admin/category/editCategory";
        }

        return "redirect:/admin/viewCategory/";
    }
    @RequestMapping(value = "delete/{categoryID}",method = RequestMethod.GET)
    public String delete(ModelMap modelMap,@PathVariable Integer categoryID){
        category entity = categoryReporsitory.findById(categoryID).get();
        String image = entity.getImageCategory();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return "redirect:/admin/viewCategory/";
            }
            categoryReporsitory.deleteById(categoryID);
        }catch (Exception e){
            return "redirect:/admin/viewCategory/";
        }

        return "redirect:/admin/viewCategory/";
    }
}
