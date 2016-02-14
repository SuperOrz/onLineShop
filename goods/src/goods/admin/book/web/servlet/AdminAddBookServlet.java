package goods.admin.book.web.servlet;

import goods.book.domain.Book;
import goods.book.service.BookService;
import goods.category.domain.Category;
import goods.category.service.CategoryService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {
	private BookService bookService = new BookService();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//编码问题处理
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//第一步创建工厂
		FileItemFactory factory = new DiskFileItemFactory();
		//第二部创建解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(100*1024);//设置单个上传文件的最大为100kb
		//第三部解析request得到List<FileItem>
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			error("上传图片不能超过100KB", request, response);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//处理普通表单项
		for (FileItem fileItem : fileItemList) {
			if(fileItem.isFormField()){//如果是普通表单项
				map.put(fileItem.getName(), fileItem.getString("UTF-8"));
			}
		}
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		book.setBid(CommonUtils.uuid());
		//处理大图
		FileItem fileItem = fileItemList.get(1);
		String fileName = fileItem.getName();
		int index = fileName.lastIndexOf("\\");
		if(index!=-1){
			fileName = fileName.substring(index+1);//截取文件名
		}
		fileName = CommonUtils.uuid()+"_"+fileName;//添加前缀避免文件重名
		if(!fileName.toLowerCase().endsWith(".jpg")){//校验图片扩展名
			error("上传的图片必须为jpg格式", request, response);
			return;
		}
		//保存文件
		String path = this.getServletContext().getRealPath("/book_img");//获取绝对路径
		File file  =  new File(path, fileName);
		try {
			fileItem.write(file);//将临时文件写入磁盘，然后删除临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		book.setImage_w("book_img/"+fileName);
		//处理小图
		fileItem = fileItemList.get(2);
		fileName = fileItem.getName();
		index = fileName.lastIndexOf("\\");
		if(index!=-1){
			fileName = fileName.substring(index+1);//截取文件名
		}
		fileName = CommonUtils.uuid()+"_"+fileName;//添加前缀避免文件重名
		if(!fileName.toLowerCase().endsWith(".jpg")){//校验图片扩展名
			error("上传的图片必须为jpg格式", request, response);
			return;
		}
		//保存文件
		path = this.getServletContext().getRealPath("/book_img");//获取绝对路径
		file  =  new File(path, fileName);
		try {
			fileItem.write(file);//将临时文件写入磁盘，然后删除临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		book.setImage_b("book_img/"+fileName);
		//保存到数据库
		bookService.add(book);
		request.setAttribute("msg", "图书添加成功！");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}
	/**
	 * 保存错误信息，转发到add.jsp
	 * @param msg
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void error(String msg,HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
	}
}
