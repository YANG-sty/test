package zzu.sys.dao;

import java.util.List;

import zzu.sys.entity.Food;
import zzu.sys.utils.PageBean;

public interface IFoodDao {

	/*
	 * 1、添加
	 * 2、删除
	 * 3、更新
	 * 4、列表查询全部
	 * 5、按照Id编号查询
	 * 6、按照名字查询
	 * 7、按照食物的类别查询
	 * 
	 * 8、分页查询数据
	 * 9、查询总记录数
	 */
	// * 1、添加
	void add(Food food);
	// * 2、删除
	void delete(int id);
	// * 3、更新
	void updata(Food food);
	// * 4、列表查询全部
	List<Food> query();
	// * 5、按照Id编号查询
	Food findById(int id);
	// * 6、按照名字查询
	List<Food> query(String keyword);
	// * 7、按照食物的类别查询
	List<Food> findByType(int type);
	
	// * 8、分页查询数据
	void getAll(PageBean<Food> pb);
	// * 9、查询总记录数
	int getTotalCount(PageBean<Food> pb);
	
}
