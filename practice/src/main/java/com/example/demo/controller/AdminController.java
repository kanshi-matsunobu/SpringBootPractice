package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Contact;
import com.example.demo.form.AdminForm;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.ContactService;

@Controller
public class AdminController {
	
	@Autowired
	private ContactService contactService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminRepository adminRepository;
	
	@GetMapping("/admin/contacts")
	public String showContactList(Model model) {
		List<Contact> contacts = contactService.getAllContacts();
		model.addAttribute("contacts", contacts);
		return "contactList";
	}
	
	@GetMapping("/admin/contacts/{id}")
	public String showContactDetail(@PathVariable Long id, Model model) {
		Contact contact = contactService.getContactById(id);
		if (contact == null) {
			return "redirect:/admin/contacts/";
		}
		model.addAttribute("contact", contact);
		return "contact_detail";
	}
	
	@GetMapping("/admin/contacts/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model) {
		Contact contact = contactService.getContactById(id);
		if (contact == null) {
			return "redirect:/admin/contacts";
		}
		model.addAttribute("contact", contact);
		return "contact_edit";
	}
	
	@PostMapping("/admin/contacts/{id}/edit")
	public String updateContact(
	        @PathVariable Long id,
	        @ModelAttribute Contact formContact ) {
	    Contact contact = contactService.getContactById(id);
	    if (contact == null) {
	        return "redirect:/admin/contacts";
	    }

	    contact.setLastName(formContact.getLastName());
	    contact.setFirstName(formContact.getFirstName());
	    contact.setEmail(formContact.getEmail());
	    contact.setPhone(formContact.getPhone());
	    contact.setZipCode(formContact.getZipCode());
	    contact.setAddress(formContact.getAddress());
	    contact.setBuildingName(formContact.getBuildingName());
	    contact.setContactType(formContact.getContactType());
	    contact.setBody(formContact.getBody());
	    contact.setUpdatedAt(java.time.LocalDateTime.now());
	    contactService.saveContact(contact);

	    return "redirect:/admin/contacts/" + id;
	}
	
	@PostMapping("/admin/contacts/{id}/delete")
	public String deleteContact(@PathVariable Long id) {
		contactService.deleteContactById(id);
		return "redirect:/admin/contacts";
	}
	
	@GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("adminForm", new AdminForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerAdmin(@ModelAttribute AdminForm adminForm) {
        adminService.register(adminForm);
        return "redirect:/signup?success";
    }
    @GetMapping("/signin")
    public String showSigninForm(Model model) {
        return "signin"; // templates/signin.html を表示
    }
    @PostMapping("/signin")
    public String signin(@RequestParam String email,
                         @RequestParam String password,
                         Model model,
                         HttpSession session) {

        List<Admin> adminList = adminRepository.findByEmail(email);

        if (!adminList.isEmpty()) {
            Admin admin = adminList.get(0); // 最初の1人を使う

            if (password.equals(admin.getPassword())) {
                session.setAttribute("admin", admin);
                return "redirect:/admin/contacts";
            } else {
                model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
                return "signin";
            }
        } else {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
            return "signin";
        }
    }
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを破棄（ログアウト状態にする）
        return "redirect:/signin"; // ログイン画面に戻る
    }
}
