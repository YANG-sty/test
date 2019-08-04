package zzu.sys.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import zzu.sys.entity.DinnerTable;
import zzu.sys.entity.FoodType;
import zzu.sys.factory.BeanFactory;
import zzu.sys.service.IDinnerTableService;

public class DinnerTableServlet extends HttpServlet {
	
	private IDinnerTableService dinnerTableService = 
			BeanFactory.getInstance("dinnerTableService", IDinnerTableService.class);
	//跳转资源
	private Object uri;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		List<DinnerTable> list = dinnerTableService.query();
		config.getServletContext().setAttribute("table", list);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//解决乱码问题的两条语句
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String method = request.getParameter("method");
		
		if("add".equals(method)){
			add(request,response);
		}else if("list".equals(method)){
			list(request,response);
		}else if("update".equals(method)){
			update(request,response);
		}else if("delete".equals(method)){
			delete(request,response);
		}else if("search".equals(method)){
			search(request,response);
		}
		
	}

	
	public void add(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			String tableName = request.getParameter("tableName");
			if (tableName != null) {
				DinnerTable dt = new DinnerTable();
				dt.setTableName(tableName);
				// 调用service 处理业务逻辑
				dinnerTableService.add(dt);
				// 将内容参数，传递到list方法
				list(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request, response, uri);
		}

	}

	public void update(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		String id = request.getParameter("id");
		//changeState表示餐桌的状态
		dinnerTableService.changeState(Integer.parseInt(id));
		list(request,response);
		
		/*try {
			DinnerTable table = new DinnerTable();
			Map<String,String[]> map = request.getParameterMap();
			BeanUtils.populate(table, map);
			dinnerTableService.update(table);
			list(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request, response, uri);
		}*/
		
	}

	public void list(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			//调用service查询所有的类别
			List<DinnerTable> list = dinnerTableService.query();
			// 保存
			request.setAttribute("list", list);

			// 将 餐桌 列表 存到 context 里 传到 前台显示
			// request.getServletContext().setAttribute("table", list);

			// request.getRequestDispatcher("sys/board/boardList.jsp").forward(request,
			// response);

			uri = request.getRequestDispatcher("sys/board/boardList.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			uri="/error/error.jsp";
		}finally{
			goTo(request,response,uri);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			//获取请求id
			String id = request.getParameter("id");
			//调用service
			dinnerTableService.delete(Integer.parseInt(id));
			//跳转
			list(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request, response, uri);
		}
	}

	public void search(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		if(keyword!=null){
			List<DinnerTable> list = dinnerTableService.query(keyword);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/sys/board/boardList.jsp").forward(request, response);
		}
	}

	// 跳转的通用方法,可以在 util工具类中，将该方法单独写一个类
	private void goTo(HttpServletRequest request, HttpServletResponse response,
			Object uri) throws ServletException, IOException {
		if (uri instanceof RequestDispatcher) {
			((RequestDispatcher) uri).forward(request, response);
		} else if (uri instanceof String) {
			response.sendRedirect(request.getContextPath() + uri);
		}

	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	
	}

}
