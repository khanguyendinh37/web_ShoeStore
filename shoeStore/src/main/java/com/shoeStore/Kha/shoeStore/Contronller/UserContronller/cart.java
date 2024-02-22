package com.shoeStore.Kha.shoeStore.Contronller.UserContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.billDetail;
import com.shoeStore.Kha.shoeStore.Model.product;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billDetailReporitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.billReporsitory;
import com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "")
public class cart {
    @Autowired
    productReporsitory productReporsitory;
    @Autowired
    billReporsitory billReporsitory;
    @Autowired
    billDetailReporitory billDetailReporitory;
    @Autowired
    accountReporsitory accountReporsitory;
    boolean success = false;

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

    @RequestMapping(value = "shoppingCart",method = RequestMethod.GET)
    public String getCart(ModelMap modelMap,HttpSession session){
        Iterable<product> products = productReporsitory.findAll();
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        account account;
        if(account() == null){
             account = new account();
        }else {
            account = account();
        }
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("billDetail",billDetails);
        modelMap.addAttribute("bill",bills);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("accountCart",account);
        return "customer/Home/cart";
    }

    @RequestMapping(value = "add/{IdProduct}",method = RequestMethod.GET)
    public String getadd(ModelMap modelMap, @PathVariable Integer IdProduct,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        product product = productReporsitory.findById(IdProduct).get();
        for(int i = 0;i<billDetails.size();i++){
            if(billDetails.get(i).getIdProduct() == IdProduct){
                int count =  billDetails.get(i).getQuantity();
                if( product.getQuantity() <= count){

                    break;
                }else {
                    billDetails.get(i).setQuantity(count + 1);
                    float price = billDetails.get(i).getQuantity() * Float.parseFloat(billDetails.get(i).getPrice());
                    String LinePrice = String.valueOf(price);
                    billDetails.get(i).setLinePrice(LinePrice);

                }


            }
        }
        int tt = 0;
        float totalPrice = 0;

        for (int i = 0; i < billDetails.size(); i++) {
            float priceBill = Float.parseFloat(billDetails.get(i).getLinePrice());
            totalPrice += priceBill ;
            tt += billDetails.get(i).getQuantity();
        }
        bills.setTotalPrice(String.valueOf(totalPrice));
        bills.setQuantity(tt);
        if(account()!= null){
            bills.setUserName(account().getUserName());
            bills.setFullName(account().getFullName());
        }
        return "redirect:/shoppingCart";
    }
    @RequestMapping(value = "Sub/{IdProduct}",method = RequestMethod.GET)
    public String getSub(ModelMap modelMap, @PathVariable Integer IdProduct,HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill  bills = (bill)session.getAttribute("bills");
        for(int i = 0;i<billDetails.size();i++){
            if(billDetails.get(i).getIdProduct() == IdProduct){
                int count =  billDetails.get(i).getQuantity();
                billDetails.get(i).setQuantity(count-1);
                float price =  billDetails.get(i).getQuantity() * Float.parseFloat(billDetails.get(i).getPrice());
                String LinePrice = String.valueOf(price);
                billDetails.get(i).setLinePrice( LinePrice);
            }
        }
        for(int i = 0;i<billDetails.size();i++){
            int count =  billDetails.get(i).getQuantity();
            if(billDetails.get(i).getIdProduct() == IdProduct){
               if (count == 0){
                   billDetails.remove(i);
               }
            }
        }
        int tt = 0;
        float totalPrice = 0;
        for (int i = 0; i < billDetails.size(); i++) {
            float priceBill = Float.parseFloat(billDetails.get(i).getLinePrice());
            tt += billDetails.get(i).getQuantity();
            totalPrice += priceBill ;
        }
        bills.setTotalPrice(String.valueOf(totalPrice));
        bills.setQuantity(tt);
        if(account()!= null){
            bills.setUserName(account().getUserName());
            bills.setFullName(account().getFullName());
        }
        return "redirect:/shoppingCart";
    }
    @RequestMapping(value = "/oderCart", method = RequestMethod.POST)
    public String postCart(ModelMap modelMap, @Valid @ModelAttribute("bill")bill bill, HttpSession session){
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill bills = (bill)session.getAttribute("bills");
        ArrayList<billDetail> billDetailsActive = (ArrayList<billDetail>) session.getAttribute("billDetailsActive");
        ArrayList<bill> billsActive = (ArrayList<bill>)session.getAttribute("billsActive");

        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        if (billDetailsActive == null) {
            billDetailsActive = new ArrayList<>();
            session.setAttribute("billDetailsActive", billDetailsActive);
        }
        if (billsActive == null) {
            billsActive = new ArrayList<>();
            session.setAttribute("billsActive", billsActive);
        }

        bill.setTotalPrice(bills.getTotalPrice());
        bill.setQuantity(bills.getQuantity());
        if(account()!= null){
            bill.setUserName(account().getUserName());
            bill.setFullName(account().getFullName());
        }

        if(bill.getPay() == 0){
            bill bill1 = billReporsitory.save(bill);
            billsActive.add(bill1);


            for(int i = 0; i < billDetails.size() ; i++){
                billDetail currentBillDetail = billDetails.get(i);
                currentBillDetail.setIdBill(bill1.getIdBill());
                billDetailReporitory.save(currentBillDetail);
                product product = productReporsitory.findById(billDetails.get(i).getIdProduct()).get();
                product.setSold(product.getSold()+billDetails.get(i).getQuantity());
                productReporsitory.save(product);

            }
            bills.setQuantity(0);
            for(int i = 0; i < billDetails.size() ; i++){
                billDetails.remove(i);
            }
            success = true;

            return "redirect:/showSuccessPage";
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping(value = "/showSuccessPage", method = RequestMethod.GET)
    public String showSuccessPage(ModelMap modelMap, HttpSession session){
        Iterable<product> products = productReporsitory.findAll();
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill bills = (bill)session.getAttribute("bills");
        ArrayList<billDetail> billDetailsActive = (ArrayList<billDetail>) session.getAttribute("billDetailsActive");
        ArrayList<bill> billsActive = (ArrayList<bill>)session.getAttribute("billsActive");

        // Kiểm tra và xử lý dữ liệu từ session
        // Khởi tạo các danh sách nếu chúng chưa tồn tại
        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        if (billDetailsActive == null) {
            billDetailsActive = new ArrayList<>();
            session.setAttribute("billDetailsActive", billDetailsActive);
        }
        if (billsActive == null) {
            billsActive = new ArrayList<>();
            session.setAttribute("billsActive", billsActive);
        }

        // Xử lý dữ liệu trong danh sách để hiển thị trên trang success
//        int totalQuantity = 0;
//        float totalPrice = 0;
//        for (billDetail currentBillDetail : billDetails) {
//            float priceBill = Float.parseFloat(currentBillDetail.getLinePrice());
//            totalQuantity += currentBillDetail.getQuantity();
//            totalPrice += priceBill;
//        }
//        bills.setTotalPrice(String.valueOf(totalPrice));
//        bills.setQuantity(totalQuantity);

        if(account()!= null){
//            bills.setUserName(account().getUserName());
//            bills.setFullName(account().getFullName());
            for(int i=0;i<billsActive.size();i++){
                billsActive.get(i).setUserName(account().getUserName());
                billsActive.get(i).setFullName(account().getFullName());
                billReporsitory.save(billsActive.get(i));
            }
            billsActive = billReporsitory.findByNameContaining(account().getUserName());

        }
        if(success == true){
            modelMap.addAttribute("success",success);
            success = false;
        }
        modelMap.addAttribute("products",products);
        // Truyền dữ liệu vào modelMap để hiển thị trên trang success
        modelMap.addAttribute("billDetailActive", billDetailsActive);

        modelMap.addAttribute("billActive", billsActive);
        modelMap.addAttribute("billDetail", billDetails);
        modelMap.addAttribute("bill", bills);
        modelMap.addAttribute("account", account());

        return "customer/Home/showSuccessPage";
    }
    @RequestMapping(value = "billDetail/{IdBill}",method = RequestMethod.GET)
    public String getBillDetail(ModelMap modelMap,@PathVariable Integer IdBill,HttpSession session){
        Iterable<product> products = productReporsitory.findAll();
        ArrayList<billDetail> billDetails = (ArrayList<billDetail>) session.getAttribute("billDetails");
        bill bills = (bill)session.getAttribute("bills");
        ArrayList<billDetail> billDetailsActive = (ArrayList<billDetail>) session.getAttribute("billDetailsActive");
        ArrayList<bill> billsActive = (ArrayList<bill>)session.getAttribute("billsActive");

        // Kiểm tra và xử lý dữ liệu từ session
        // Khởi tạo các danh sách nếu chúng chưa tồn tại
        if (billDetails == null) {
            billDetails = new ArrayList<>();
            session.setAttribute("billDetails", billDetails);
        }
        if(bills == null){
            bills = new bill();
            session.setAttribute("bills", bills);
        }
        if (billDetailsActive == null) {
            billDetailsActive = new ArrayList<>();
            session.setAttribute("billDetailsActive", billDetailsActive);
        }
        if (billsActive == null) {
            billsActive = new ArrayList<>();
            session.setAttribute("billsActive", billsActive);
        }
        billDetailsActive =billDetailReporitory.findByNameContaining(IdBill);
        modelMap.addAttribute("billDetailActive", billDetailsActive);
        modelMap.addAttribute("products",products);
        modelMap.addAttribute("billDetail", billDetails);
        modelMap.addAttribute("bill", bills);
        modelMap.addAttribute("account", account());
        return "customer/Home/billDetail";
    }
    @RequestMapping(value = "CancelOder/{Idbill}",method = RequestMethod.GET)
    public String CancelOder(ModelMap modelMap,@PathVariable Integer Idbill){
        bill bill = billReporsitory.findById(Idbill).get();
        billReporsitory.delete(bill);
        ArrayList<billDetail> billDetails =billDetailReporitory.findByNameContaining(Idbill);
        for(int i = 0;i<billDetails.size();i++){
            billDetail detail = billDetails.get(i);
            billDetailReporitory.delete(detail);
        }
        return "redirect:/showSuccessPage";
    }
}
