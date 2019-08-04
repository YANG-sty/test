package zzu.sys.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import zzu.sys.entity.Food;
import zzu.sys.entity.FoodType;
import zzu.sys.factory.BeanFactory;
import zzu.sys.service.IFoodService;
import zzu.sys.service.IFoodTypeService;
import zzu.sys.utils.PageBean;
import zzu.sys.utils.WebUtils;

public class FoodServlet extends HttpServlet {

	private IFoodService service = 
			BeanFactory.getInstance("foodService", IFoodService.class);
	private IFoodTypeService ifs = 
			BeanFactory.getInstance("foodTypeService", IFoodTypeService.class);
	
	private Object uri;
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		PageBean<Food> pageBean = new PageBean<Food>();
		pageBean.setPageCount(6);
		service.getAll(pageBean);
		List<Food> list = service.query();
		config.getServletContext().setAttribute("food", list);
		config.getServletContext().setAttribute("pb", pageBean);
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 解决乱码问题的两条语句
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");

		if ("list".equals(method)) {
			list(request, response);
		} else if ("add".equals(method)) {
			
				try {
					add(request, response);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			
		} else if ("update".equals(method)) {
			try {
				update(request,response);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if ("delete".equals(method)){
			delete(request,response);
		} else if ("search".equals(method)){
			search(request,response);
		} else if ("findFoodType".equals(method)){
			findFoodType(request,response);
			uri = request.getRequestDispatcher("/sys/food/saveFood.jsp");
			WebUtils.goTo(request, response, uri);
		}else if ("show".equals(method)){
			show(request,response);
		}
	}

	
	private void show(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		findFoodType(request,response);
		String id = request.getParameter("id");
		Food food = service.findById(Integer.parseInt(id));
		
		request.setAttribute("food", food);
		
		//得到食物里面的食物类型iD
		int foodType_id = food.getFoodType_id();
		
		//
		FoodType type = ifs.findById(foodType_id);
		request.setAttribute("type", type);
		
		uri = request.getRequestDispatcher("/sys/food/updateFood.jsp");
		WebUtils.goTo(request, response, uri);
		
	}

	private void findFoodType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		List<FoodType> foodtypes = ifs.query();
		request.setAttribute("foodtypes", foodtypes);
		
	}

	

	private void search(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String keyword = request.getParameter("keyword");
			if (keyword != null) {
				List<Food> list = service.query(keyword);
				List<FoodType> types = new ArrayList<FoodType>();

				if (list != null) {
					for (Food food : list) {
						FoodType foodtype = ifs.findById(food.getFoodType_id());
						types.add(foodtype);
					}
				}
				request.setAttribute("types", types);
				request.setAttribute("list", list);
				uri = request.getRequestDispatcher("/sys/food/foodList.jsp");
			}
		} catch (Exception e) {
			uri = "/error/error.jsp";
			e.printStackTrace();
		}
		WebUtils.goTo(request, response, uri);

	}

	private void delete(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException,IOException {
		
		try {
			String id = request.getParameter("id");
			service.delete(Integer.parseInt(id));
			list(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			WebUtils.goTo(request, response, uri);
		}
		
	}

	private void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(10*1024*1024);//单个文件
			upload.setSizeMax(50*1024*1024);//总文件
			upload.setHeaderEncoding("UTF-8");//对中文文件编码处理
			
			if(upload.isMultipartContent(request)){
				Food food = new Food();
				List<FileItem> list = upload.parseRequest(request);
				for (FileItem item : list) {
					if(item.isFormField()){
						//普通文本内容
						String name = item.getFieldName();
						//获取值
						String value = item.getString();
						value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
						BeanUtils.setProperty(food, name, value);
						
					}else {
						//上传内容
						String fieldName = item.getFieldName();
						String path = getServletContext().getRealPath("/upload");
						File f = new File(path);
						if(!f.exists()){
							f.mkdir();
						}
						String name = item.getName();
						if(name!=null && !"".equals(name.trim())){
							BeanUtils.setProperty(food, fieldName, ("upload/"+name));
							//在add 方法中没有添加括号
							//拼接文件名
							File file = new File(path,name);
							//上传
							if(!file.isDirectory()){
								item.write(file);
							}
							item.delete();//删除组件运行时产生的临时文件
						}else{
							int id = food.getId();
							String img = service.findById(id).getImg();
							BeanUtils.setProperty(food, "img", img);
						}
					}
				}
				service.updata(food);
			}else {
				
			}
			list(request,response);
		} catch (FileUploadException e) {
			
			e.printStackTrace();
			uri = "/error/error.jsp";
			WebUtils.goTo(request, response, uri);
		}
	}

	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 1.获取当前页参数；（第一次访问当前页为null）
			String currPage = request.getParameter("currentPage");
			// 判断
			if (currPage == null || "".equals(currPage.trim())) {
				currPage = "1"; // 第一次访问，设置当前页为 1 ；
			}
			// 转换
			int currentPage = Integer.parseInt(currPage);
			// 2.创建 PageBean 对象，设置当前页参数：传入service方法参数
			PageBean<Food> pageBean = new PageBean<Food>();
			pageBean.setCurrentPage(currentPage);

			// 3.调用 service
			service.getAll(pageBean);// pageBean已经被 dao 填充了数据
			// 4.保存 pageBean对象 ，到request域中
			List<Food> list = pageBean.getPageData();

			// 获取事物类别的方法
			List<FoodType> types = new ArrayList<FoodType>();

			if (list != null) {
				for (Food food : list) {
					FoodType foodtype = ifs.findById(food.getFoodType_id());
					types.add(foodtype);
				}
			}
			request.setAttribute("types", types);
			request.setAttribute("pageBean", pageBean);
			request.setAttribute("list", list);

			uri = request.getRequestDispatcher("/sys/food/foodList.jsp");
		} catch (Exception e) {
			e.printStackTrace();//测试使用
			//出现错误的时候，跳转到错误页面；给用户友好提示
			uri = "/error/error.jsp";
		}
//		WebUtils.goTo(request,response,uri);
		goTo(request,response,uri);

	}
	private void add(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(10*1024*1024);	//单个文件大小
			upload.setSizeMax(50*1024*1024);	//总文件大小
			upload.setHeaderEncoding("UTF-8");	//对中文编码进行处理
			
			if(upload.isMultipartContent(request)){
				Food food = new Food();
				List<FileItem> list = upload.parseRequest(request);
				for (FileItem item : list) {
					if(item.isFormField()){
						//普通文本内容
						String name = item.getFieldName();
						//获取值
						String value = item.getString();
						
						value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
						BeanUtils.setProperty(food, name, value);	//在方法中又抛出了两个异常
						
					}else {
						//上传内容
						String fieldName = item.getFieldName();
						String path = getServletContext().getRealPath("/upload");
						File f = new File(path);
						if(!f.exists()){
							f.mkdir();
						}
						//全部绝对路径
						String name = item.getName();
						
						BeanUtils.setProperty(food, fieldName, "upload/"+name);
						
						//拼接文件名
						File file = new File(path,name);
						//上传
						if(!file.isDirectory()){
							item.write(file);
						}
						item.delete();//删除组件运行时产生的临时文件
					}
				}
				service.add(food);
			}else{
				
			}
			list(request,response);
		} catch (FileUploadException e) {
			e.printStackTrace();
			uri = "/error/error.jsp";
			WebUtils.goTo(request, response, uri);
		}
		
		
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
	
	
	private void goTo(HttpServletRequest request, HttpServletResponse response,
			Object uri) throws ServletException, IOException {
		if (uri instanceof RequestDispatcher) {
			((RequestDispatcher) uri).forward(request, response);

		} else {
			response.sendRedirect(request.getContextPath() + (String) uri);
		}
	}
	
}
