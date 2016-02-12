package goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import goods.cart.dao.CartItemDao;
import goods.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();
	/**
	 * 批量加载
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 更新quantity
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		try{
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		}catch (SQLException e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加条目
	 * @param cartItem
	 */
	public void add(CartItem cartItem){
		try{
			CartItem _cartItem = cartItemDao.findByUidAndBid(
					cartItem.getUser().getUid(), cartItem.getBook().getBid());
			if(_cartItem == null){
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}
			else{
				int quantity = cartItem.getQuantity() + _cartItem.getQuantity();
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 批量删除
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 *我的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid){
		try {
			return cartItemDao.findByUid(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
