package goods.admin.order.web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import goods.order.domain.Order;
import goods.order.service.OrderService;
import goods.page.PageBean;
import goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.queryStatus(oid);
		if(status != 1){//验证订单状态是否为1
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无法取消订单");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//修改订单状态
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已经取消！");
		return "f:/adminjsps/msg.jsp";
	}
	/**
	 * 发货处理
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.queryStatus(oid);
		if(status != 2){//验证订单状态是否为2
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无法发货");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//修改订单状态
		req.setAttribute("code", "success");
		req.setAttribute("msg", "已发货！");
		return "f:/adminjsps/msg.jsp";
	}
	/**
	 *加载订单详细信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		req.setAttribute("btn", req.getParameter("btn"));
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	/**
	 * 查询所有订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);
		String url = getUrl(req);
		PageBean<Order> pb = orderService.findAll(pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/**
	 * 按状态查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);
		String url = getUrl(req);
		int status = Integer.parseInt(req.getParameter("status"));
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	/*
	 * 获取当前页码
	 */
	private int getPc(HttpServletRequest req){
		int pc = 1;
		String param = req.getParameter("pc");
		if(param!=null && !param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(Exception e){}
		}
		return pc;
	}
	/*
	 * 获得URL
	 */
	private String getUrl(HttpServletRequest req) throws UnsupportedEncodingException {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index!=-1){
			url = url.substring(0, index);
		}
		return url;
	}
}
