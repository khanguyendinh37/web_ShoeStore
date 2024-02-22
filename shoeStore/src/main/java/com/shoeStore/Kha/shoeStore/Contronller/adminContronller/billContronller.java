package com.shoeStore.Kha.shoeStore.Contronller.adminContronller;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.billDetail;
import com.shoeStore.Kha.shoeStore.Model.category;
import com.shoeStore.Kha.shoeStore.Reporsitory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "admin/")
public class billContronller {

    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.productReporsitory productReporsitory;
    @Autowired
    com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory accountReporsitory;
    @Autowired
    billReporsitory billReporsitory;
    @Autowired
    billDetailReporitory billDetailReporitory;

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
    public int Total(){
        ArrayList<bill> bills = billReporsitory.findByAll();
        int count = bills.size();
        return count;
    }
    @RequestMapping(value = "viewBills",method = RequestMethod.GET)
    public String getBill(ModelMap modelMap){

        Iterable<bill> bills = billReporsitory.findAll();
        modelMap.addAttribute("bills",bills);
        modelMap.addAttribute("account",account());

        return "admin/bill/viewBill";
    }
    @RequestMapping(value = "viewBillsConfirm",method = RequestMethod.GET)
    public String getBillConfirm(ModelMap modelMap){

        Iterable<bill> bills = billReporsitory.findByAll();
        modelMap.addAttribute("bills",bills);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());

        return "admin/bill/confimed";
    }
    @RequestMapping(value = "confirm/{idBill}",method = RequestMethod.GET)
    public String confirm(ModelMap modelMap, @PathVariable Integer idBill) {
        bill bill = billReporsitory.findById(idBill).get();
        bill.setStatus(1);
        billReporsitory.save(bill);
        return "redirect:/admin/viewBillsConfirm";
    }
    @RequestMapping(value = "confirm1/{idBill}",method = RequestMethod.GET)
    public String confirm1(ModelMap modelMap, @PathVariable Integer idBill) {
        bill bill = billReporsitory.findById(idBill).get();
        bill.setStatus(1);
        billReporsitory.save(bill);
        return "redirect:/admin/viewBills";
    }
    @RequestMapping(value = "Detail/{idBill}",method = RequestMethod.GET)
    public String detail(ModelMap modelMap, @PathVariable Integer idBill) {
        ArrayList<billDetail> billDetails = billDetailReporitory.findByNameContaining(idBill);
        modelMap.addAttribute("billdetail",billDetails);
        modelMap.addAttribute("account",account());
        modelMap.addAttribute("Total",Total());
        return "admin/bill/detail" ;
    }

    @RequestMapping(value = "all",method = RequestMethod.GET)
    public String confirmAll (ModelMap modelMap){
        ArrayList <bill> bills=billReporsitory.findByAll();
        for (int i= 0;i<bills.size();i++){
            bills.get(i).setStatus(1);
            billReporsitory.save(bills.get(i));
        }
        return "redirect:/admin/viewBillsConfirm";
    }
}
