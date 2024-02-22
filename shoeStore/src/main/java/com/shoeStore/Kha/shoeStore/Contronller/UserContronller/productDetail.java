package com.shoeStore.Kha.shoeStore.Contronller.UserContronller;

import com.shoeStore.Kha.shoeStore.Model.*;
import com.shoeStore.Kha.shoeStore.Model.product;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("")
public class productDetail {
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
//    public static ArrayList<billDetail> billDetails = new ArrayList<>() ;

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

    @RequestMapping(value = "cart/{IdProudct}",method = RequestMethod.GET)
    public String getCart(ModelMap modelMap, @PathVariable Integer IdProudct, HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        // Nếu giỏ hàng chưa tồn tại trong session, hãy tạo mới
        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        Iterable<com.shoeStore.Kha.shoeStore.Model.product> products = productReporsitory.findAll();

        product productCart = productReporsitory.findById(IdProudct).get();
        List<String> colorList = Arrays.asList(productCart.getColor().split(","));
        List<String> sizeList = Arrays.asList(productCart.getSize().split(","));
        Iterable<imageproduct> imageProducts = imageProductReporsitory.findAll(IdProudct);
        Iterable<comment> comments = commentRepository.findAll(IdProudct);
        Iterable<billDetail> bill = billDetails;
        int s =  ((Collection<?>) comments).size();

        modelMap.addAttribute("productCart",productCart);
        modelMap.addAttribute("imagesP",imageProducts);
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("color",colorList);
        modelMap.addAttribute("size",sizeList);
        modelMap.addAttribute("comment",comments);
        modelMap.addAttribute("s",s);
        modelMap.addAttribute("billDetail",bill);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("account",account());

        return "customer/Home/productDetail";
    }
    @RequestMapping(value = "cart/{IdProudct}",method = RequestMethod.POST)
    public String postCart(ModelMap modelMap, @PathVariable Integer IdProudct,@Valid @ModelAttribute("billDetail") billDetail billDetail,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        // Nếu giỏ hàng chưa tồn tại trong session, hãy tạo mới
        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        product product = productReporsitory.findById(IdProudct).get();
        billDetail.setIdProduct(IdProudct);

        float price =  billDetail.getQuantity() * Float.parseFloat(product.getPrice());
        billDetail.setLinePrice(String.valueOf(price));
        billDetail.setImageProduct(product.getImageProduct());
        billDetail.setPrice(product.getPrice());
        billDetail.setNameProduct(product.getTitleProduct());
        boolean count = false;

        if(billDetail.getQuantity() <= product.getQuantity()){

            for(int i = 0;i<billDetails.size();i++){
                if(IdProudct == billDetails.get(i).getIdProduct()){
                    if(billDetail.getColorBill().equals(billDetails.get(i).getColorBill()) && billDetail.getSizeBill().equals(billDetails.get(i).getSizeBill())){
                        int sl = billDetails.get(i).getQuantity() + billDetail.getQuantity();
                        if(sl > product.getQuantity()){
                            billDetails.get(i).setQuantity(product.getQuantity());
                        }else {
                            billDetails.get(i).setQuantity(sl);
                        }
                        float lp = billDetails.get(i).getQuantity() * Float.parseFloat(billDetails.get(i).getPrice());
                        billDetails.get(i).setLinePrice(String.valueOf(lp));

                        count = true;
                        break;
                    }

                }

            }
            if (count==false){

                billDetails.add(billDetail);
            }
            float totalPrice = 0;
            int tt = 0;
            for (int i = 0; i < billDetails.size(); i++) {
                float priceBill = Float.parseFloat(billDetails.get(i).getLinePrice());
                totalPrice += priceBill ;
                tt += billDetails.get(i).getQuantity();
            }

            if(account()!= null){
                bills.setUserName(account().getUserName());
                bills.setFullName(account().getFullName());
            }
            bills.setTotalPrice(String.valueOf(totalPrice));
            bills.setQuantity(tt);
            System.out.println(bills.getQuantity());

            try {

                Thread.sleep(1200); // Tạm ngừng luồng trong 2 giây (2000 milliseconds)
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }else {

            try {

                Thread.sleep(1200); // Tạm ngừng luồng trong 2 giây (2000 milliseconds)
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }

        return "redirect:/cart/"+IdProudct;
    }
    @GetMapping(value = "deleteCart/{IdProduct}")
    public String deleteCart (ModelMap modelMap,@PathVariable Integer IdProduct,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        try {
            for(int i = 0; i< billDetails.size();i++){
                if(IdProduct == billDetails.get(i).getIdProduct()){
                    billDetails.remove(i);
                }
            }
            int tt = 0;
            for (int i = 0;i<billDetails.size();i++){
                tt += billDetails.get(i).getQuantity();
            }
            System.out.println(tt);
            bills.setQuantity(tt);

            Thread.sleep(1500); // Tạm ngừng luồng trong 2 giây (2000 milliseconds)
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return "redirect:/cart/"+IdProduct;
    }
}
