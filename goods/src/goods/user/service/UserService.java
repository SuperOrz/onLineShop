package goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import goods.user.dao.UserDao;
import goods.user.domain.User;

/*
 * 用户模块业务层
 */
public class UserService {
	private UserDao userDao = new UserDao();
	/*
	 * 激活
	 */
	public void activation(String activationCode) throws UserException{
		try {
			User user = userDao.findByActivation(activationCode);
			if(user==null) throw new UserException("无效的激活码！");
			if(user.isStatus()) throw new UserException("用户已经激活，请不要重复激活");
			userDao.updateStatus(true, user.getUid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 修改密码
	 */
	public void updateLoginpass(String uid, String oldpass, String newpass) throws UserException{
		try {
			boolean b = userDao.findByUidAndLoginpass(uid, oldpass);
			if(!b){
				throw new UserException("您输入的密码不正确！");
			}else{
				userDao.updateLoginpass(uid, newpass);			
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 登陆
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 验证用户名是否注册
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 验证email是否注册
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 注册
	 */
	public void regist(User user){
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivitionCode(CommonUtils.uuid()+CommonUtils.uuid());
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//发送邮件
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		String host =prop.getProperty("host");
		String name = prop.getProperty("username");
		String pass = prop.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);
		
		String from= prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		String content = MessageFormat.format(prop.getProperty("content"),user.getActivitionCode());
		Mail mail = new Mail(from, to, subject, content);
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
