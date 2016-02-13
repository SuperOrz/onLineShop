package goods.admin.admin.service;

import java.sql.SQLException;

import goods.admin.admin.dao.AdminDao;
import goods.admin.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao = new AdminDao();
	/**
	 * 登陆功能
	 * @param form
	 * @return
	 */
	public Admin login(Admin form){
		try {
			return adminDao.find(form.getAdminname(), form.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
