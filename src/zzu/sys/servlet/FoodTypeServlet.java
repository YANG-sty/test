package zzu.sys.servlet;

import java.io.IOException;
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
import zzu.sys.service.IFoodTypeService;

public class FoodTypeServlet extends HttpServlet {

	/*
	 * 4.菜系管理 Servlet 开发
	 * 
	 * a.添加菜系
	 * b.菜系列表展示
	 * c.进入更新页面
	 * d.删除
	 * e.更新
	 * 
	 * */
	
	//调用的菜系service
	private IFoodTypeService foodTypeService = 
			BeanFactory.getInstance("foodTypeService", IFoodTypeService.class);
	//跳转资源
	private Object uri;
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		List<FoodType> list = foodTypeService.query();
		config.getServletContext().setAttribute("foodtype", list);
	}		
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//获取操作类型
		String method = request.getParameter("method");
		//判断
		if("add".equals(method)){
			//添加 菜品
			add(request,response);
		}else if("list".equals(method)){
			//列表展示
			list(request,response);
		}else if("update".equals(method)){
			//进入页面更新
			update(request,response);
		}else if("delete".equals(method)){
			//删除
			delete(request,response);
		}else if("search".equals(method)){
			//更新
			search(request,response);
		}else if("show".equals(method)){
			show(request,response);
		}
	}
	public void search(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		if(keyword!=null){
			List<FoodType> list = foodTypeService.query(keyword);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/sys/type/foodtypelist.jsp").forward(request, response);
		}
	}
	/*private void search(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		try {
			String keyword = request.getParameter("keyword");
			if(keyword!=null){
				List<FoodType> list = foodTypeService.query(keyword);
				request.setAttribute("list", list);
				uri = request.getRequestDispatcher("/sys/type/foodtypelist.jsp");
//				list(request,response);
			}
		} catch (Exception e) {
			uri = "/error/error.jsp";
			e.printStackTrace();
//			goTo(request, response, uri);
		} finally {

			// 跳转
			goTo(request, response, uri);
		}
	}*/

	//a.添加菜系
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//1.获取请求数据封装
			String name = request.getParameter("name");
			FoodType ft = new FoodType();
			ft.setTypeName(name);
			
			//2.调用 service 处理业务逻辑
			foodTypeService.add(ft);
			
			//将内容参数，传递到list 方法
			list(request,response);
			
			//3.跳转
			//uri = request.getRequestDispatcher("/foodType?method=list");
			
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request,response,uri);
		}
		
		//goTo(request,response,uri);
	}
	

	//b.菜系列表展示
	public void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//调用 service 查询所有的 类别
			List<FoodType> list = foodTypeService.query();
			//保存
			request.setAttribute("list", list);
			
//			request.getServletContext().setAttribute("foodtype", list);
			//跳转到菜系 页面
			uri = request.getRequestDispatcher("/sys/type/foodtypelist.jsp");
			
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
		}finally{
			
			//跳转
			goTo(request,response,uri);
		}
	}
	//c.进入更新页面
	public void show(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//1.获取请求id
			String id = request.getParameter("id");
			//2.根据 id 查询对象
			FoodType ft = foodTypeService.findById(Integer.parseInt(id));
			//3.保存
			request.setAttribute("type", ft);
			//4.跳转
			uri = request.getRequestDispatcher("/sys/type/foodtypeupdate.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
		}
		goTo(request, response, uri);
	}
	//d.删除
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//1.获取请求 id
			String id = request.getParameter("id");
			//2.调用service
			foodTypeService.delete(Integer.parseInt(id));
			//3.跳转
//			uri = "/foodType?method=list";
			list(request,response);
			
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request, response, uri);
		}
	}
	//e.更新
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			/*//1.获取请求数据封装
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("foodTypeName");
			
			FoodType foodType = new FoodType();
			foodType.setId(id);
			foodType.setTypeName(name);
			
			//2.调用service 更新
			foodTypeService.update(foodType);
			//3.跳转
			uri = "/foodType?method=list";*/
			
			FoodType type = new FoodType();
			Map<String,String[]> map = request.getParameterMap();
			BeanUtils.populate(type, map);
			
			foodTypeService.update(type);
			list(request,response);
			
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			goTo(request, response, uri);
		}
	}
	
	//跳转的通用方法
	private void goTo(HttpServletRequest request, HttpServletResponse response,Object uri) 
			throws ServletException,IOException{
		if(uri instanceof RequestDispatcher){
			((RequestDispatcher) uri).forward(request, response);
		}else if(uri instanceof String){
			response.sendRedirect(request.getContextPath() + uri);
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
