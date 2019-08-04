package zzu.sys.service;

import java.util.List;

import zzu.sys.entity.DinnerTable;

public interface IDinnerTableService {
	/*
	 * 添加餐桌
	 * */
	void add(DinnerTable dt);
	
	/*
	 * 删除餐桌
	 * */
	void delete(int id);
	
	/*
	 * 更新餐桌状态
	 * */
	void update(DinnerTable dt);
	
	/*
	 * 显示所有的餐桌
	 * */
	List<DinnerTable> query();
	
	/*
	 * 查询指定的餐桌——序号
	 * */
	DinnerTable findById(int id);
	
	/*
	 * 查询指定的餐桌——名称
	 * */
	List<DinnerTable> query(String keyword);
	
	/*
	 * 退桌
	 * */
	void quitTable(int id);
	
	/*
	 * 改变餐桌的状态
	 */
	DinnerTable changeState(int id);
	
}
