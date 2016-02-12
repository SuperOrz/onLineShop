package goods.user.dao;

import goods.user.domain.User;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mchange.v2.c3p0.impl.NewPooledConnection;

import cn.itcast.jdbc.TxQueryRunner;
/*
 * 用户模块持久层
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	/*
	 * 根据激活码查找用户
	 */
	public User findByActivation (String activationCode) throws SQLException{
		String sql = "select * from t_user where activationCode =?";
		return qr.query(sql, new BeanHandler<User>(User.class), activationCode);
	}
	/*
	 * 通过用户名和密码查询用户
	 */
	public User findByLoginnameAndLoginpass (String loginname,String loginpass) throws SQLException{
		String sql = "select * from t_user where loginname =? and loginpass=?";
		return qr.query(sql, new BeanHandler<User>(User.class), loginname, loginpass);
	}
	/*
	 * 通过用户ID和密码查询用户
	 */
	public boolean findByUidAndLoginpass (String uid,String loginpass) throws SQLException{
		String sql = "select count(*) from t_user where uid =? and loginpass=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),uid,loginpass);
		return number.intValue() > 0;
	}
	/*
	 * 修改密码
	 */
	public void updateLoginpass (String uid,String loginpass) throws SQLException{
		String sql = "update t_user set loginpass = ? where uid = ?";
		qr.update(sql, loginpass, uid);
	}
	/*
	 * 激活用户ByUid
	 */
	public void updateStatus(boolean status ,String uid) throws SQLException{
		String sql = "update t_user set status = ? where uid = ?";
		qr.update(sql, status, uid);
	}
	/*
	 * 验证用户名是否注册
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		String sql="select count(1) from t_user where loginname=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),loginname);
		boolean result = (number.intValue() == 0);
		return result;
	}
	/*
	 * 验证email是否注册
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException {
		String sql="select count(1) from t_user where email=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),email);
		return number.intValue() == 0;
	}
	/*
	 * 向数据库插入新用户
	 */
	public void add(User user) throws SQLException{
		String sql = "insert into t_user values (?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),
				user.getEmail(),user.isStatus(),user.getActivitionCode()};
		qr.update(sql, params);
	}
}
