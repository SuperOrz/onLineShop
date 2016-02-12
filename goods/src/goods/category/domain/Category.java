package goods.category.domain;

import java.util.List;

/*
 * 分类模块实体类
 */
public class Category {
	private String cid;//主键
	private String cname;//分类名称
	private Category parent;//父分类
	private List<Category> children;//子分类
	private String desc;//分类描述
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", parent="
				+ parent + ", children=" + children + ", desc=" + desc + "]";
	}

}