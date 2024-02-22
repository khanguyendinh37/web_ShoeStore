package com.shoeStore.Kha.shoeStore.Contronller.login;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Reporsitory.accountReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("register")
public class RegisterController {
    @Autowired
    accountReporsitory accountRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        account account = new account();
        modelMap.addAttribute("account", account);
        return "/passlogin/register";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String RegisterAccount(@Valid @ModelAttribute account account, BindingResult bindingResult, @RequestParam("userName") String username,
                                  @RequestParam("re_password") String re_password, @RequestParam("password") String password,
                                  @RequestParam("email") String email, @RequestParam("phoneNumber") String phone,
                                  ModelMap modelMap) {
        if (!Objects.equals(re_password, password)) {
            modelMap.addAttribute("error_password", "Confirmation password does not match");
            return "passlogin/register";
        }else if (accountRepository.GetAccountByUsername(username).isPresent()) {
            modelMap.addAttribute("error_username", "Username already exists");
            return "passlogin/register";
        } else if (accountRepository.GetAccountByEmail(email).isPresent()) {
            modelMap.addAttribute("error_email", "Email already exists");
            return "passlogin/register";
        } else if (accountRepository.GetAccountByPhone(phone).isPresent()) {
            modelMap.addAttribute("error_phone", "Phone number already exists");
            return "passlogin/register";
        } else {
            if (bindingResult.hasErrors()) {
                return "passlogin/register";
            } else {

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(password);
                account.setPassword(hashedPassword);
                accountRepository.save(account);
                return "passlogin/login";
            }
        }
    }
}
