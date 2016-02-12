package goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import goods.user.domain.User;
import goods.user.service.UserException;
import goods.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
/*
 * 用户模块控制层
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	/*
	 * 验证用户名是否注册
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String loginname = req.getParameter("loginname");
		boolean b = userService.ajaxValidateLoginname(loginname);
		resp.getWriter().print(b);
		return null;
	}
	/*
	 * 验证email是否注册
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("email");
		boolean b = userService.ajaxValidateEmail(email);
		resp.getWriter().print(b);
		return null;
	}
	/*
	 * 修改密码
	 */
	public String updateLoginpass(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User form = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		if(user==null){
			req.setAttribute("msg", "您还没有登陆！");
			return "f:/jsps/user/login.jsp";
		}
		Map<String, String> errors = new HashMap<String, String>();
		errors = validateUpdateLoginpass(form, req.getSession());
		if(errors.size()>0){
			req.setAttribute("errors", errors);
			req.setAttribute("user", form);
			return "f:/jsps/user/pwd.jsp";
		}else{
			try {
				userService.updateLoginpass(user.getUid(), form.getLoginpass(), form.getNewpass());
				req.setAttribute("code", "success");
				req.setAttribute("msg", "修改密码成功");
				return "f:/jsps/msg.jsp";
			} catch (UserException e) {
				req.setAttribute("msg", e.getMessage());
				req.setAttribute("user", form);
				return "f:/jsps/user/pwd.jsp";
			}
		}
		
	}
	/*
	 * 注册
	 */	
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//参数封装到user
		User user = CommonUtils.toBean(req.getParameterMap(), User.class);
		//校验表单
		Map<String, String> errors  = validateRegist(user, req.getSession());
		if(errors.size() > 0){
			req.setAttribute("errors", errors);
			req.setAttribute("form", user);
			return "f:/jsps/user/regist.jsp";
		}else{	
			//注册
			userService.regist(user);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "注册成功");
			return "f:/jsps/msg.jsp";
		}
	}
	/*
	 * 注册校验
	 */
	private Map<String , String> validateUpdateLoginpass(User user, HttpSession session){
		Map<String, String> errors =new HashMap<String, String>();
		return errors;
	}
	/*
	 * 注册校验
	 */
	private Map<String , String> validateRegist(User user, HttpSession session){
		Map<String, String> errors =new HashMap<String, String>();
		//校验用户名
		String loginname = user.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()>20 || loginname.length()<3){
			errors.put("loginname", "用户名长度必须在3-20位");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名已存在");
		}
		//校验密码
		String loginpass = user.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()>20 || loginpass.length()<3){
			errors.put("loginpass", "密码长度必须在3-20位");
		}
		//校验确认密码
		String reloginpass = user.getReloginpass();
		if(reloginpass==null||reloginpass.trim().isEmpty()){
			errors.put("reloginpass", "确认密码不能为空");
		}else if(!reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次输入密码必须一致");
		}
		//校验email
		String email = user.getEmail();
		if(email==null||email.trim().isEmpty()){
			errors.put("email", "Email不能为空");
		}else if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
			errors.put("email", "Email格式不正确");
		}else if(!userService.ajaxValidateLoginname(email)){
			errors.put("email", "Email已存在");
		}
		//校验验证码
		String verifyCode = user.getVerifyCode();
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空");
		}else if(!verifyCode.equalsIgnoreCase((String) session.getAttribute("vCode"))){
			errors.put("verifyCode", "验证码错误");
		}
		return errors;
	}
	/*
	 * 验证码是否正确
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String verifyCode1 = (String) req.getSession().getAttribute("vCode");
		String verifyCode2 = req.getParameter("verifyCode");
		boolean b =(verifyCode1.equalsIgnoreCase(verifyCode2));
		resp.getWriter().print(b);
		return null;
	}
	/*
	 * 激活
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String activationCode = req.getParameter("activationCode");
		try{
			userService.activation(activationCode);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "激活成功");
		}catch(UserException e){
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}
	/*
	 * 登陆
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User formUser  = CommonUtils.toBean(req.getParameterMap(), User.class);
		Map<String, String> errors = validateLogin(formUser,req.getSession());
		if(errors.size()!=0){
			req.setAttribute("errors", errors);
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}
		User user = userService.login(formUser);
		if(user==null){
			req.setAttribute("msg", "用户名或密码不正确");
			req.setAttribute("form", formUser);
			return "f:/jsps/user/login.jsp";
		}else{
			if(!user.isStatus()){
				req.setAttribute("msg", "您还没有激活");
				req.setAttribute("form", formUser);
				return "f:/jsps/user/login.jsp";
			}else{
				//用户保存到session
				req.getSession().setAttribute("sessionUser", user);
				//用户保存到cookie
				String loginname= user.getLoginname();
				loginname = URLEncoder.encode(loginname,"utf-8");
				Cookie cookie = new Cookie("cookieUser", loginname);
				cookie.setMaxAge(60*60*24*7);//保存7天
				resp.addCookie(cookie);
				return "r:/index.jsp";
			}
		}
		
	}
	/*
	 * 退出
	 */
	public String logout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	/*
	 * 校验登陆信息
	 */
	private Map<String , String> validateLogin(User user, HttpSession session){
		Map<String, String> errors =new HashMap<String, String>();
		//校验用户名
		String loginname = user.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()>20 || loginname.length()<3){
			errors.put("loginname", "用户名长度必须在3-20位");
		}
		//校验密码
		String loginpass = user.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()>20 || loginpass.length()<3){
			errors.put("loginpass", "密码长度必须在3-20位");
		}
		//校验验证码
		String verifyCode = user.getVerifyCode();
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空");
		}else if(!verifyCode.equalsIgnoreCase((String) session.getAttribute("vCode"))){
			errors.put("verifyCode", "验证码错误");
		}
		return errors;
	}
}
