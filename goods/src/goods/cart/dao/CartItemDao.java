package goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import goods.book.domain.Book;
import goods.cart.domain.CartItem;
import goods.user.domain.User;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	private QueryRunner qr = new TxQueryRunner();
	/**
	 * 私有工具类
	 * @return
	 */
	private CartItem toCartItem(Map<String, Object> map){
		if(map==null || map.size()==0) return null;
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList){
		List<CartItem> cartItemLIst = new ArrayList<CartItem>();
		for (Map<String, Object> map : mapList) {
			cartItemLIst.add(toCartItem(map));
		}
		return cartItemLIst;
	}
	/**
	 * 生成where子句
	 * @param len
	 * @return
	 */
	private String toWhereSql(int len){
		StringBuilder sb = new StringBuilder("cartItemId in (");
		for (int j=1;j<len;j++){
			sb.append("?,");
		}
		sb.append("?)");
		return sb.toString();
	}
	/**
	 * 批量加载
	 * @throws SQLException 
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		Object[] params = cartItemIds.split(",");
		String whereSql = toWhereSql(params.length);
		String sql = "select * from t_cartitem c,t_book b where c.bid=b.bid and "+whereSql;
		return toCartItemList(qr.query(sql, new MapListHandler(),params));
	}
	/**
	 * 批量删除
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds) throws SQLException{
		Object[] params = cartItemIds.split(",");
		String whereSql = toWhereSql(params.length);
		String sql = "delete  from t_cartitem where " + whereSql;
		qr.update(sql, params);
	}
	/**
	 * 按cartItemId查找
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from t_book b,t_cartitem c where b.bid=c.bid and c.cartItemId = ?";
		Map<String, Object> map = qr.query(sql, new MapHandler(),cartItemId);
		return toCartItem(map);
	}
	/**
	 * 通过uid查找
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUid(String uid) throws SQLException{
		String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and uid =? order by c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),uid);
		return toCartItemList(mapList);
	}
	/**
	 * 使用bid和uid查询
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid,String bid) throws SQLException{
		String sql = "select * from t_cartitem where uid = ? and bid =?";
		Map<String, Object> map = qr.query(sql, new MapHandler(),uid,bid);
		return toCartItem(map);
	}
	/**
	 * 根据cartItemId修改quantity
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId,int quantity) throws SQLException{
		String sql = "update t_cartitem set quantity = ? where cartItemId =?";
		qr.update(sql, quantity,cartItemId);
	}
	/**
	 * 添加条目
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql = "insert into t_cartitem(cartItemId,quantity,bid,uid) values(?,?,?,?)";
		Object[] param = {cartItem.getCartItemId(),cartItem.getQuantity()
		                  ,cartItem.getBook().getBid(),cartItem.getUser().getUid()};
		qr.update(sql,param);
	}
}
