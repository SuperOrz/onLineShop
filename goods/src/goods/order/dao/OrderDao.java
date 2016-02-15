package goods.order.dao;

import goods.book.domain.Book;
import goods.order.domain.Order;
import goods.order.domain.OrderItem;
import goods.page.Constants;
import goods.page.Expression;
import goods.page.PageBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid,int status) throws SQLException{
		String sql = "update t_order set status = ? where oid = ?";
		qr.update(sql,status,oid);
	}
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public int queryStatus(String oid) throws SQLException{
		String sql  = "select status from t_order where oid = ?";
		Number  number = (Number) qr.query(sql, new ScalarHandler(),oid);
		return number.intValue();
	}
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public Order load(String oid) throws SQLException{
		String sql  = "select * from t_order where oid = ?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		loadOrderItem(order);
		return order;
	}
	/**
	 * 生成订单
	 * @param order
	 * @throws SQLException
	 */
	public void add(Order order) throws SQLException{
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] param = {order.getOid(),order.getOrdertime(),order.getTotal(),
				order.getStatus(),order.getAddress(),order.getOwner().getUid()};
		qr.update(sql, param);
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] params = new Object[len][];
		for(int i=0; i<len ;i++){
			OrderItem item = order.getOrderItemList().get(i);
			params[i] = new Object[]{
					item.getOrderItemId(),
					item.getQuantity(),
					item.getSubtotal(),
					item.getBook().getBid(),
					item.getBook().getBname(),
					item.getBook().getCurrPrice(),
					item.getBook().getImage_b(),
					order.getOid()};
		}
		qr.batch(sql, params);
	}
	/*
	 * 按出用户查询
	 */
	public PageBean<Order> findByUid(String uid, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("uid","=",uid );
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/**
	 * 安定单状态查询
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("status","=",status+"");
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/**
	 * 所有订单
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		return findByCriteria(expList, pc);
	}
	private PageBean<Order> findByCriteria(List<Expression> expList,int pc) throws SQLException{
		int ps = Constants.ORDER_PAGE_SIZE;
		//查询总记录数
		List<Object> params = new ArrayList<Object>();//参数列表
		StringBuilder subSql = new StringBuilder("where 1=1");
		for (Expression expression : expList) {
			subSql.append(" and ")
				.append(expression.getName())
				.append(" ")
				.append(expression.getOperator());
			if(!expression.getOperator().equals("is null")){
				subSql.append("?");
				params.add(expression.getValue());
			}
		}
		//查询当前页记录
		String countSql = "select count(*) from t_order "+subSql.toString();
		Number number = (Number) qr.query(countSql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();//得到满足条件的总记录数
		String sql = "select * from t_order "+subSql.toString()+" order by ordertime desc limit ? ,?";
		params.add((pc-1)*ps);//起始位置,当前页首行记录下标
		params.add(ps);//查询个数
		List<Order> beanList = qr.query(sql, new BeanListHandler<Order>(Order.class),params.toArray());
		//beanList中的订单并不包含订单条目，需要遍历并为其加载条目
		for (Order order : beanList) {
			loadOrderItem(order);
		}
		//创建并返回PageBean
		PageBean<Order> pageBean = new PageBean<Order>();
		pageBean.setPc(pc);
		pageBean.setPs(ps);
		pageBean.setTr(tr);
		pageBean.setBeanList(beanList);
		return pageBean;
	}
	/**
	 * 为Order加载它所有的orderitem
	 * @param order
	 * @throws SQLException 
	 */
	private void loadOrderItem(Order order) throws SQLException {
		String sql = "select * from t_orderitem where oid = ?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());  
		List<OrderItem> list = toOrderItemList(mapList);
		order.setOrderItemList(list);
	}
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList){
		List<OrderItem> list = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			list.add(toOrderItem(map));
		}
		return list;
	}
	private OrderItem toOrderItem(Map<String, Object> map){
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book =CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
}

