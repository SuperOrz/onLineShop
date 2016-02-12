package goods.user.domain;
/*
 * 用户模块实体类
 */
public class User {
	private String uid;//主键
	private String loginname;//登录名
	private String loginpass;//登陆密码
	private String email;//邮箱
	private String activitionCode;//激活码，唯一
	private boolean status;//用户状态，true表示激活
	
	//注册表单
	private String reloginpass;//确认密码
	private String verifyCode;//验证码
	//修改密码
	private String newpass;//新密码
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActivitionCode() {
		return activitionCode;
	}
	public void setActivitionCode(String activitionCode) {
		this.activitionCode = activitionCode;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", email=" + email + ", activitionCode="
				+ activitionCode + ", status=" + status + ", reloginpass="
				+ reloginpass + ", verifyCode=" + verifyCode + ", newpass="
				+ newpass + "]";
	}

	
}
