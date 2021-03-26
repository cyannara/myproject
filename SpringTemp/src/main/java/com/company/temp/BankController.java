package com.company.temp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.BankAPI;

@Controller
public class BankController {

	@Autowired
	BankAPI bankAPI;
	
	@RequestMapping("auth")
	public String auth() throws Exception {
		//String reqURL = " https://testapi.openbanking.or.kr/oauth/2.0/authorize";      
		String reqURL = "https://testapi.openbanking.or.kr/oauth/2.0/authorize_account";
		String response_type = "code";
		String client_id = "6Y2QQh8lyuIXTDJKP3NMp3avlHUbmXN41pMuaIa7";
		String redirect_uri = "http://localhost/callback";
		String scope = "login inquiry transfer";
		String state = "12345678901234567890123456789012";
		String auth_type = "0";
		
		StringBuilder qstr = new StringBuilder();
		qstr.append("response_type=" + response_type)
			.append("&client_id=" + client_id)
			.append("&redirect_uri=" + redirect_uri)
			.append("&scope=" + scope)
			.append("&state=" + state)
			.append("&auth_type=" + auth_type);
		return "redirect:" + reqURL + "?" + qstr.toString();
	}
	
	@RequestMapping("/callback")
	public String callback(@RequestParam Map<String, Object> map
			              , HttpSession session) {
		System.out.println(map.get("code"));
		String code = (String)map.get("code");
		
		//access_token 받기
		String access_token = bankAPI.getAccessToken(code);
		System.out.println("access_token=" + access_token);
		
		//session
		session.setAttribute("access_token", access_token);
		return "home";
	}
	
	@RequestMapping("/userinfo")
	public String userinfo(HttpSession session
			              , HttpServletRequest request) {
		//String access_token = (String)session.getAttribute("access_token");
		//String access_token = request.getParameter("access_token");
		String access_token = "____";
		String use_num = "____";
		Map<String, Object> userinfo = bankAPI.getUserinfo(access_token, use_num);
		System.out.println("userinfo=" + userinfo);
		return "home";
	}
}
