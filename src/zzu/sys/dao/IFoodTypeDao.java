package zzu.sys.dao;

import java.util.List;

import zzu.sys.entity.FoodType;

/*
 * 2.菜系模块，dao接口设计
 * */
public interface IFoodTypeDao {
	/*
	 * 添加
	 * */
	void add(FoodType foodtype);
	
	/*
	 * 更新
	 * */
	void update(FoodType foodtype);
	
	/*
	 * 删除
	 * */
	void delete(int id);
	
	/*
	 * 查询全部
	 * */
	List<FoodType> query();
	
	/*
	 * 根据主键查询
	 * */
	FoodType findById(int id);
	
	
	/*
	 * 根据菜系名称查询
	 * */
	List<FoodType> query(String keyword);
	
	/*
	 * 得到第一个菜系名称
	 * */
	Integer getFirstType();
}
