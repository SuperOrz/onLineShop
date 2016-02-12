package goods.category.dao;

import goods.category.domain.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.mchange.v2.c3p0.impl.NewPooledConnection;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

/*
 * 分类模块持久层
 */
public class CategoryDao {
	 private QueryRunner qr = new TxQueryRunner();
	 /*
	  * map映射为category
	  * map.keyset = {cid,pid,cname,desc}
	  */
	 private Category toCategory(Map<String, Object> map){
		 Category category = CommonUtils.toBean(map, Category.class);
		 String pid = (String) map.get("pid");
		 if(pid!=null){
			 Category parent = new Category();
			 parent.setCid(pid);
			 category.setParent(parent);
		 }
		 return category;
	 }
	 /*
	  * list<map>映射为list<category>
	  */
	 private List<Category> toCategoryList(List<Map<String, Object>> mapList){
		 List<Category> parent = new ArrayList<Category>();
		 for (Map<String, Object> map : mapList) {
				Category category = toCategory(map);
				parent.add(category);
			}
		 return parent;
	 }
	 /*
	  * 返回所有分类
	  */
	 public List<Category> findAll() throws SQLException{
		 String sql = "select * from t_category where pid is null";
		 List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		 List<Category> parents = toCategoryList(mapList);
		 for (Category category : parents) {
			String pid = category.getCid();
			List<Category> children = findByParent(pid);
			category.setChildren(children);
		}
		return parents;
	 }
	 /*
	  * 通过父分类查询子分类
	  */
	 public List<Category> findByParent(String pid) throws SQLException{
		 String sql = "select * from t_category where pid = ?";
		 List< Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),pid);
		 return toCategoryList(mapList);
	 }
}
