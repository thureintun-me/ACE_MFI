package com.mfi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mfi.model.Permission;
import com.mfi.model.Role;
import com.mfi.model.User;
import com.mfi.service.MyUserDetails;
import com.mfi.service.PermissionService;
import com.mfi.service.RoleService;
import com.mfi.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private PermissionService perService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	 private  PasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String home() {
	Permission p=	perService.findByName("MASTER");
	if(p == null) {
		createPermission();
	}
		return "redirect:/login";
	}
	@RequestMapping("/login")
	public String login() {
		
		User user=userService.findByEmail("master@gmail.com");
		
		System.out.println("perService" + perService.findByName("MASTER"));
		if(user==null) {
			Role role = new Role();
			
			role.setRolePosition("Checker");
			roleService.save(role);
			List<Permission> permission = new ArrayList<Permission>();
			permission.add(perService.findByName("MASTER"));
			
			User master=new User();
			master.setEmail("master@gmail.com");
			master.setPassword(passwordEncoder.encode("master"));
			master.setRole(role);
			for(int i=0;i<permission.size();i++) {
				master.getPermission().add(permission.get(i));
				userService.save(master);
			}
			
			
		}
		return "MFI_LGN_01";
	}

	@RequestMapping("/loginError")
	public String loginError(Model model) {
		model.addAttribute("err", "Error");
		return "MFI_LGN_01";
	}

	@RequestMapping("/index")
	public String index() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails currentPrincipalName = (MyUserDetails) authentication.getPrincipal();
		int id = currentPrincipalName.getUserId();
		System.out.println("id:" + currentPrincipalName.getUserId());

		User user = userService.selectOne(id);
		for (Permission p : user.getPermission()) {
			System.out.println("p"+p.getPerName());
		}

		if (user.getRole().getRolePosition().equals("Checker") || user.getRole().getRolePosition().equals("MASTER") ) {
			
			return "redirect:/checker";
			//return "mfi/dashboard/checker_dashboard" ;
		} else {
			
			return "redirect:/maker";

		}

	}

	
	  	 public void createPermission() {
	  
	  Permission permission11 = new Permission(); permission11.setPerName("MAKER");
	  
	  Permission permission10 = new Permission();
	  permission10.setPerName("CHECKER");
	  
	  
	  Permission permission8 = new Permission(); permission8.setPerName("MASTER");
	  
	  
	  
	  
	  Permission permission1 = new Permission(); permission1.setPerName("CRM");
	  Permission permission2 = new Permission(); permission2.setPerName("Account");
	  Permission permission3 = new Permission(); permission3.setPerName("Reports");
	  Permission permission4 = new Permission(); permission4.setPerName("Loan");
	  Permission permission5 = new Permission();
	  permission5.setPerName("Transaction"); Permission permission6 = new
	  Permission(); permission6.setPerName("COA"); Permission permission7 = new
	  Permission(); permission7.setPerName("Blacklist"); Permission permission12 =
	  new Permission(); permission12.setPerName("Admin");
	  
	  perService.createPermission(permission8);
	  perService.createPermission(permission11);
	  perService.createPermission(permission10);
	  perService.createPermission(permission12);
	  
	  perService.createPermission(permission1);
	  
	  perService.createPermission(permission2);
	  perService.createPermission(permission3);
	  perService.createPermission(permission4);
	  perService.createPermission(permission5);
	  perService.createPermission(permission6);
	  perService.createPermission(permission7);
	  
	  
	  
	  }
	 
}
