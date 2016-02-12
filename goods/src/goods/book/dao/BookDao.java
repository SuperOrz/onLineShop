package goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import goods.book.domain.Book;
import goods.category.domain.Category;
import goods.page.Constants;
import goods.page.Expression;
import goods.page.PageBean;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	/*
	 * 按bid查询
	 */
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book where bid = ?";
		Map<String, Object> map = qr.query(sql, new MapHandler(),bid);
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		return book;
	}
	/*
	 * 按分类查询
	 */
	public PageBean<Book> findByCategory(String cid, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("cid","=",cid );
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/*
	 * 按书名模糊查询
	 */
	public PageBean<Book> findByName(String bname, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("bname","like","%"+bname+"%" );
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/*
	 * 按作者模糊查询
	 */
	public PageBean<Book> findByAuthor(String author, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("author","like","%"+author+"%" );
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/*
	 * 按出版社模糊查询
	 */
	public PageBean<Book> findByPress(String press, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		Expression e = new Expression("press","like","%"+press+"%" );
		expList.add(e);
		return findByCriteria(expList, pc);
	}
	/*
	 * 多条件组合模糊查询
	 */
	public PageBean<Book> findByCombination(Book book, int pc) throws SQLException{
		List<Expression> expList = new ArrayList<Expression>();
		expList.add(new Expression("bname","like","%"+book.getBname()+"%" ));
		expList.add(new Expression("author","like","%"+book.getAuthor()+"%" ));
		expList.add(new Expression("press","like","%"+book.getPress()+"%" )); 
		return findByCriteria(expList, pc);
	}
	
	/*
	 * 根据多种条件查询
	 */
	private PageBean<Book> findByCriteria(List<Expression> expList,int pc) throws SQLException{
		int ps = Constants.BOOK_PAGE_SIZE;
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
		String countSql = "select count(*) from t_book "+subSql.toString();
		Number number = (Number) qr.query(countSql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();//得到满足条件的总记录数
		String sql = "select * from t_book "+subSql.toString()+" order by orderBy limit ? ,?";
		params.add((pc-1)*ps);//起始位置,当前页首行记录下标
		params.add(ps);//查询个数
		List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class),params.toArray());
		//创建并返回PageBean
		PageBean<Book> pageBean = new PageBean<Book>();
		pageBean.setPc(pc);
		pageBean.setPs(ps);
		pageBean.setTr(tr);
		pageBean.setBeanList(beanList);
		return pageBean;
	}
}