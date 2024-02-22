package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "admin")
public class ProductContronller {
    @Autowired
    productReporsitory productReporsitory;
    @Autowired
    categoryReporsitory categoryReporsitory;
    @Autowired
    imageProductReporsitory imageProductReporsitory;
    @Autowired
    commentRepository commentRepository;
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

    @RequestMapping(value = "viewProduct",method = RequestMethod.GET)
    public String getProduct(ModelMap modelMap, @RequestParam(name = "titleProduct", required = false) String name,
                             @RequestParam("page") Optional<Integer> page, //trang hien tai
                             @RequestParam("size") Optional<Integer> size) { //kich htuoc trang
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

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
                if(end == totalPages) start = end -5;
                else if(start == 1) end = start + 5;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("accountPage", resultPage);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/viewProduct";
    }
    @RequestMapping(value = "viewProductHistory",method = RequestMethod.GET)
    public String getProductHistory(ModelMap modelMap, @RequestParam(name = "titleProduct", required = false) String name,
                             @RequestParam("page") Optional<Integer> page, //trang hien tai
                             @RequestParam("size") Optional<Integer> size) { //kich htuoc trang
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        PageRequest pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("titleProduct"));
        Page<product> resultPage = null;

        if(StringUtils.hasText(name)) { //ten ton tai
            resultPage = productReporsitory.findByNameContainingHistory(name.trim(),pageable);
            modelMap.addAttribute("name", name);
        }
        else {
            resultPage = productReporsitory.findAllHistopry(pageable);
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
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/historyPoduct";
    }
    @RequestMapping(value="addProduct",method = RequestMethod.GET)
    public String add(ModelMap model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        product products = new product();
        Iterable<category> category =categoryReporsitory.findAll();
        model.addAttribute("products",products);
        model.addAttribute("category",category);
        model.addAttribute("account",account());
        model.addAttribute("Total",Total());
        return "admin/product/addProduct";
    }
    @PostMapping("/addProduct")
    public String saveOrUpdate(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model,
                               @Valid @ModelAttribute("products") product product, BindingResult result)  {
        product entity = product;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        Iterable<category> category =categoryReporsitory.findAll();
        if(result.hasErrors()) {
            model.addAttribute("account",account());
            model.addAttribute("Total",Total());
            model.addAttribute("category",category);
            return "admin/product/addProduct";
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
                entity.setImageProduct(randomFileName);
            }else {
                model.addAttribute("account",account());
                model.addAttribute("Total",Total());
                model.addAttribute("category",category);
                model.addAttribute("imageError","Product photos cannot be blank!");
                return "admin/product/addProduct";
            }

            int idcategory = entity.getCategory().getIdCategory();

            entity.setIdCategory(idcategory);
            productReporsitory.save(entity);

        }catch (Exception e){
            model.addAttribute("Total",Total());
            model.addAttribute("account",account());
            model.addAttribute("category",category);
            model.addAttribute("error",e.toString());

            return "admin/product/addProduct";
        }
        int id = product.getIdProduct();

        return "redirect:/admin/addImage/"+id;
    }
    @RequestMapping(value = "addImage/{IdProduct}",method = RequestMethod.GET)
    public String setImageProduct(ModelMap modelMap,@PathVariable Integer IdProduct){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        imageproduct imageproduct = new imageproduct();
        modelMap.addAttribute("imageProduct",imageproduct);
        Iterable<imageproduct> imageproducts = imageProductReporsitory.findAll(IdProduct);
        modelMap.addAttribute("imageProducts",imageproducts);
        modelMap.addAttribute("Id",IdProduct);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/imageproduct";
    }
    @RequestMapping(value = "addImage/{IdProduct}",method = RequestMethod.POST)
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
            }


            imageProduct.setIdProduct(IdProduct);
            imageProductReporsitory.save(imageProduct);

        }catch (Exception e){

            return "redirect:/admin/addImage/" + IdProduct;
        }

        return "redirect:/admin/addImage/" + IdProduct;
    }
    @RequestMapping(value = "deleteImage/{IdImage}",method = RequestMethod.GET)
    public String delete(ModelMap modelMap,@PathVariable Integer IdImage){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
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
                return "redirect:/admin/addImage/"+ID;
            }
            imageProductReporsitory.deleteById(IdImage);
        }catch (Exception e){
            return "redirect:/admin/addImage/"+ID;
        }

        return "redirect:/admin/addImage/"+ID;
    }
    @RequestMapping(value = "editProduct/{IdProduct}",method = RequestMethod.GET)
    public String chancategory(ModelMap modelMap, @PathVariable Integer IdProduct){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        Iterable<category> categories = categoryReporsitory.findAll();
        product products= productReporsitory.findById(IdProduct).get();
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("category",categories);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/editProduct";
    }
    @PostMapping("/editProduct/{IdProduct}")
    public String saveOrUpdate(@RequestParam("imageFile") MultipartFile imageFile, ModelMap model,@PathVariable Integer IdProduct,
                               @Valid @ModelAttribute("products") product product, BindingResult result)  {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        product entity = product;
        Iterable<category> category =categoryReporsitory.findAll();
        product productItem = productReporsitory.findById(IdProduct).get();
        String ImagePr = productItem.getImageProduct();

        if(result.hasErrors()) {
            model.addAttribute("Total",Total());
            model.addAttribute("account",account());
            model.addAttribute("category",category);
            return "admin/product/editProduct";
        }
        String uploadsDir = "uploads/images/"; // Thay đổi đường dẫn thư mục lưu trữ theo yêu cầu
        try {
            if(!imageFile.isEmpty()) {
                Path paths = Paths.get(uploadsDir+ File.separator + ImagePr);
                try {
                    Files.deleteIfExists(paths);

                } catch (Exception e) {
                    System.out.println(e);
                    return "redirect:/admin/viewProduct/";
                }
                String originalFileName = imageFile.getOriginalFilename();
                String randomFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
                String filePath = uploadsDir + randomFileName;
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);
                entity.setImageProduct(randomFileName);
            }else {
                if(!ImagePr.isEmpty()){
                    entity.setImageProduct(ImagePr);
                }else {
                    model.addAttribute("account",account());
                    model.addAttribute("Total",Total());
                    model.addAttribute("category",category);
                    model.addAttribute("imageError","Product photos cannot be blank!");
                    return "admin/product/editProduct";
                }

            }

            int idcategory = entity.getCategory().getIdCategory();
            entity.setIdCategory(idcategory);
            entity.setIdProduct(IdProduct);
            productReporsitory.save(entity);

        }catch (Exception e){
            model.addAttribute("Total",Total());
            model.addAttribute("account",account());
            model.addAttribute("category",category);
            model.addAttribute("error",e.toString());

            return "admin/product/editProduct";
        }

        return "redirect:/admin/addImage/"+IdProduct;
    }
    @RequestMapping(value = "deleteProduct/{IdProduct}",method = RequestMethod.GET)
    public String deleteProduct(ModelMap modelMap,@PathVariable Integer IdProduct){
        product products = productReporsitory.findById(IdProduct).get();
        products.setProperti(1);
        products.setIdProduct(IdProduct);
        productReporsitory.save(products);
        return "redirect:/admin/viewProduct";

    }
    @RequestMapping(value = "restoreProduct/{IdProduct}",method = RequestMethod.GET)
    public String restoreProduct(ModelMap modelMap,@PathVariable Integer IdProduct){
        product products = productReporsitory.findById(IdProduct).get();
        products.setProperti(0);
        products.setIdProduct(IdProduct);
        productReporsitory.save(products);
        return "redirect:/admin/viewProductHistory";
    }
    @RequestMapping(value = "deleteProducts/{IdProduct}",method = RequestMethod.GET)
    public String deleteProducts(ModelMap modelMap,@PathVariable Integer IdProduct){
        product products = productReporsitory.findById(IdProduct).get();
        String image = products.getImageProduct();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return  "redirect:/admin/viewProductHistory";
            }
           productReporsitory.deleteById(IdProduct);
        }catch (Exception e){
            return  "redirect:/admin/viewProductHistory";
        }

        return  "redirect:/admin/viewProductHistory";
    }
    @RequestMapping(value = "deleteProductView/{IdProduct}",method = RequestMethod.GET)
    public String deleteProductView(ModelMap modelMap,@PathVariable Integer IdProduct){
        product products = productReporsitory.findById(IdProduct).get();
        String image = products.getImageProduct();
        String uploadsDir = "uploads/images/";
        try{
            Path path = Paths.get(uploadsDir+ File.separator + image);
            try {
                Files.deleteIfExists(path);

            } catch (Exception e) {
                System.out.println(e);
                return  "redirect:/admin/viewProduct";
            }
            productReporsitory.deleteById(IdProduct);
        }catch (Exception e){
            return  "redirect:/admin/viewProduct";
        }

        return  "redirect:/admin/viewProduct";
    }
    @RequestMapping(value = "ImageViewProduct/{IdProduct}",method = RequestMethod.GET)
    public String ViewImageProduct(ModelMap modelMap, @PathVariable Integer IdProduct){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        account account = accountReporsitory.findById(username).get();
        product product = productReporsitory.findById(IdProduct).get();
//        String imageproduct = product.getImageProduct();
        Iterable<comment> comments = commentRepository.findAll(IdProduct);
        Iterable<imageproduct> imageproducts = imageProductReporsitory.findAll(IdProduct);

        modelMap.addAttribute("product",product);
        modelMap.addAttribute("imageProducts",imageproducts);
        modelMap.addAttribute("comments",comments);
//        modelMap.addAttribute("colors",colorList);
        modelMap.addAttribute("Id",IdProduct);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/product/ViewP";
    }
}
