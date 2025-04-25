package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Contact;
import com.example.demo.form.AdminForm;
import com.example.demo.service.AdminService;
import com.example.demo.service.ContactService;

@Controller
public class AdminController {
	
	@Autowired
	private ContactService contactService;
	@Autowired
	private AdminService adminService;
	
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
}
