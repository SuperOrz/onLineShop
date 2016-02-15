package goods.book.service;

import java.sql.SQLException;

import goods.book.dao.BookDao;
import goods.book.domain.Book;
import goods.page.PageBean;

public class BookService {
	private BookDao bookDao = new BookDao();
	/**
	 * 删除图书
	 * @param bid
	 */
	public void delete(String bid){
		try {
			bookDao.delete(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 修改图书信息
	 * @param book
	 */
	public void edit(Book book){
		try {
			bookDao.edit(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 返回当前分类下图书个数
	 * @param cid
	 * @return
	 */
	public int findBookCountByCategory(String cid) {
		try {
			return bookDao.findBookCountByCategory(cid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 按bid查询
	 */
	public Book load(String bid){
		try {
			Book book = bookDao.findByBid(bid);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 按分类查询
	 */
	public PageBean<Book> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 按书名模糊查询
	 */
	public PageBean<Book> findByName(String bname, int pc){
		try {
			return bookDao.findByName(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 按作者模糊查询
	 */
	public PageBean<Book> findByAuthor(String author, int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 按出版社模糊查询
	 */
	public PageBean<Book> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 多条件组合模糊查询
	 */
	public PageBean<Book> findByCombination(Book book, int pc){
		try {
			return bookDao.findByCombination(book, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book){
		try {
			bookDao.add(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
