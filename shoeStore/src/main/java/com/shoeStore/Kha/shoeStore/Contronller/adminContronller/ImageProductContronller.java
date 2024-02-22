package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.imageproduct;
import com.shoeStore.Kha.shoeStore.Model.product;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.imageProductReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping(value = "admin/")
public class ImageProductContronller {
    @Autowired
    productReporsitory productReporsitory;
    @Autowired
    imageProductReporsitory imageProductReporsitory;
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
    @RequestMapping(value = "addImageView/{IdProduct}",method = RequestMethod.GET)
    public String setViewImageProduct(ModelMap modelMap, @PathVariable Integer IdProduct){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        imageproduct imageproduct = new imageproduct();
        product product = productReporsitory.findById(IdProduct).get();
        modelMap.addAttribute("product",product);
        modelMap.addAttribute("imageProduct",imageproduct);
        Iterable<imageproduct> imageproducts = imageProductReporsitory.findAll(IdProduct);
        modelMap.addAttribute("imageProducts",imageproducts);
        modelMap.addAttribute("Id",IdProduct);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/viewImageProduct";
    }
    @RequestMapping(value = "addImageView/{IdProduct}",method = RequestMethod.POST)
    public String setImageProduct(ModelMap modelMap,@PathVariable Integer IdProduct,
                                  @RequestParam("imageFile") MultipartFile imageFile,
                                  @ModelAttribute("imageProduct") imageproduct imageProduct){


        try {
            if(!imageFile.isEmpty()) {

                String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);
                imageProduct.setImage(randomFileName);
            }else {
                return "redirect:/admin/addImageView/"+IdProduct;
            }


            imageProduct.setIdProduct(IdProduct);
            imageProductReporsitory.save(imageProduct);

        }catch (Exception e){

            return "admin/product/viewImageProduct";
        }

        return "redirect:/admin/addImageView/" + IdProduct;
    }
    @RequestMapping(value = "deleteImageView/{IdImage}",method = RequestMethod.GET)
    public String delete(ModelMap modelMap,@PathVariable Integer IdImage){
        imageproduct entity = imageProductReporsitory.findById(IdImage).get();
        String image = entity.getImage();
        int ID = entity.getIdProduct();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return "redirect:/admin/addImageView/"+ID;
            }
            imageProductReporsitory.deleteById(IdImage);
        }catch (Exception e){
            return "redirect:/admin/addImageView/"+ID;
        }

        return "redirect:/admin/addImageView/"+ID;
    }

}
