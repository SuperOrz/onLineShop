package goods.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import goods.cart.service.CartItemService;
import goods.order.dao.OrderDao;
import goods.order.domain.Order;
import goods.page.PageBean;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	/**
	 * 修改订单状态
	 * @param oid
	 * @return
	 */
	public void updateStatus(String oid,int status){
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	public int queryStatus(String oid){
		try {
			return orderDao.queryStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid){
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 生成订单
	 * @param order
	 */
	public void createOrder(Order order){
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 我的订购单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid,int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUid(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status,int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 所有订单
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
}
