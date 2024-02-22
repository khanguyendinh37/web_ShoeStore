package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.category;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("admin")
public class myprofile {
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
    @RequestMapping(value = "myProfile",method = RequestMethod.GET)
    public String myProfile (ModelMap modelMap){
        account account = accountReporsitory.findById(account().getUserName()).get();
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("admin",account);
        return "admin/user/myProfile";
    }
    @RequestMapping(value = "editProfile/{userName}",method = RequestMethod.GET)
    public String editMyProfile (ModelMap modelMap, @PathVariable String userName){
        account account = accountReporsitory.findById(userName).get();
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        modelMap.addAttribute("admin",account);
        return "admin/user/editProfile";
    }
    @RequestMapping(value = "editProfile/{userName}",method = RequestMethod.POST)
    public String editMyProfile (ModelMap modelMap, @PathVariable String userName, @RequestParam("imageFile") MultipartFile imageFile, @Valid @ModelAttribute("account") account account, BindingResult result){
        account entity = accountReporsitory.findById(userName).get();
        String pass = entity.getPassword();

        account.setPassword(pass);
        account.setUserName(entity.getUserName());
        account.setPostion(entity.getPostion());
        if(result.hasErrors()  ) {

                account.setPassword(pass);
                modelMap.addAttribute("account",account());
                modelMap.addAttribute("Total",Total());
                modelMap.addAttribute("admin",account);
                String image = entity.getAvatar();

                String uploadsDir = "uploads/images/";
                try{
                    if(!imageFile.isEmpty()) {
                        if(!image.isEmpty()){
                            Path path = Paths.get(uploadsDir+ File.separator + image);
                            try {
                                Files.deleteIfExists(path);

                            } catch (Exception e) {
                                System.out.println(e);
                                return "redirect:/admin/myProfile";
                            }
                        }

                        String originalFileName = imageFile.getOriginalFilename();
                        String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                        String filePath = uploadsDir + randomFileName;
                        byte[] bytes = imageFile.getBytes();
                        Path path1 = Paths.get(filePath);
                        Files.write(path1, bytes);
                        account.setAvatar(randomFileName);
                    }else {
                        if(!image.isEmpty()){
                            account.setAvatar(image);
                        }

                    }


                    accountReporsitory.save(account);
                }catch (Exception e){
                    modelMap.addAttribute("account",account());
                    modelMap.addAttribute("Total",Total());
                    modelMap.addAttribute("admin",account);
                    return "admin/user/editProfile";
                }
                return "redirect:/admin/myProfile";


        }

        String image = entity.getAvatar();

        String uploadsDir = "uploads/images/";
        try{
            if(!imageFile.isEmpty()) {
                if(!image.isEmpty()){
                    Path path = Paths.get(uploadsDir+ File.separator + image);
                    try {
                        Files.deleteIfExists(path);

                    } catch (Exception e) {
                        System.out.println(e);
                        return "redirect:/admin/myProfile";
                    }
                }

                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path1 = Paths.get(filePath);
                Files.write(path1, bytes);
                account.setAvatar(randomFileName);
            }else {
                if(!image.isEmpty()){
                    account.setAvatar(image);
                }

            }


            accountReporsitory.save(account);
        }catch (Exception e){
            modelMap.addAttribute("account",account());
            modelMap.addAttribute("Total",Total());
            modelMap.addAttribute("admin",account);
            return "admin/user/editProfile";
        }
        return "redirect:/admin/myProfile";
    }
}
