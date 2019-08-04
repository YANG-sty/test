package zzu.sys.dao.impl;


import java.util.ArrayList;
import java.util.List;



import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zzu.sys.dao.IFoodDao;
import zzu.sys.entity.Food;
import zzu.sys.utils.Condition;
import zzu.sys.utils.JdbcUtils;
import zzu.sys.utils.PageBean;

public class FoodDao implements IFoodDao {

	private QueryRunner qr = JdbcUtils.getQueryRunner();
	
	@Override
	public void add(Food food) {
		String sql = "insert food(foodName,foodType_id,price,mprice,remark,img) values(?,?,?,?,?,?);";
		try {
			qr.update(sql, food.getFoodName(),food.getFoodType_id(),
					food.getPrice(),food.getMprice(),food.getRemark(),food.getImg());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from food where id=?;";
		try {
			qr.update(sql,id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updata(Food food) {
		String sql = "update food set foodName=?,foodType_id=?,price=?,mprice=?,"
				+ "remark=?,img=? where id=?;";
		try {
			qr.update(sql, food.getFoodName(),food.getFoodType_id(),food.getPrice(),
					food.getMprice(),food.getRemark(),food.getImg(),food.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Food> query() {
		String sql = "select * from food;";
		
		try {
			return qr.query(sql, new BeanListHandler<Food>(Food.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Food findById(int id) {
		String sql = "select * from food where id=?;";
		try {
			return qr.query(sql, new BeanHandler<Food>(Food.class),id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public List<Food> query(String keyword) {
		try {
			String sql = "select * from food where foodName like ?;";
			return qr.query(sql, new BeanListHandler<Food>(Food.class), "%"+keyword+"%");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Food> findByType(int type) {
		String sql = "select * from food where foodType_id=?;";
		try {
			return qr.query(sql, new BeanListHandler<Food>(Food.class),type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void getAll(PageBean<Food> pb) {
		//查询总记录数；设置到pb对象中
		int totalCount = this.getTotalCount(pb);
		pb.setTotalCount(totalCount);
		
		List<Object> list = new ArrayList<Object>();
		
		/*
		 * 问题：jsp页面，如果当前页为首页在点击上一页的时候报错，
		 * 	如果当前页为末页在点击下一页的时候报错
		 * 
		 * 解决：
		 * 1.如果当前页 <=0 ;当前页设置为1
		 * 2.如果当前页 > 最大页数；	当前页设置为最大页数
		 * */
		
		//判断
		if(pb.getCurrentPage() <= 0){
			pb.setCurrentPage(1);	//把当前页设置为 1 
		}else if(pb.getCurrentPage() > pb.getTotalPage()){
			pb.setCurrentPage(pb.getTotalPage());	//把当前页设置为最大页数
		}
		
		//1.获取当前页：计算查询的起始行、返回的行数
		int currentPage = pb.getCurrentPage();
		int index = (currentPage - 1) * pb.getPageCount();	//查询的起始行
		int count = pb.getPageCount();	//查询返回的行数
		
		Condition condition = (Condition) pb.getCondition();
		
		//3.分页查询数据；把查询到的数据设置到pb对象中
		StringBuilder sb = new StringBuilder();
		sb.append("	select");
		sb.append("	f.id,");
		sb.append("	f.foodName,");
		sb.append("	f.foodType_id,");
		sb.append("	f.price,");
		sb.append(" f.mprice,");
		sb.append("	f.remark,");
		sb.append("	f.img,");
		sb.append("	ft.typeName");
		sb.append("	from");
		sb.append("	food f,");
		sb.append("	foodtype ft");
		sb.append("	where 1=1");
		sb.append("	and f.foodType_id=ft.id");
		
		//判断
		if(condition!=null){
			String foodName = condition.getFoodName();
			if(foodName!=null && !foodName.isEmpty()){
				sb.append("	and f.foodName like ?");
				list.add("%"+foodName+"%");
			}
			
			int type_id = condition.getFoodType_id();
			if(type_id>0){
				sb.append("	and f.foodType_id=?");
				list.add(type_id);
			}
		}
		sb.append("	limit ?,?");
		list.add(index);
		list.add(count);

		// 根据当前页，查询当前页数据（一页数据）
		try {
			if (index >= 0) {
				List<Food> pageData = qr.query(sb.toString(),
						new BeanListHandler<Food>(Food.class), list.toArray());
				// 设置到 pb 对象中
				pb.setPageData(pageData);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getTotalCount(PageBean<Food> pb) {
		StringBuilder sb = new StringBuilder();
		List<Object> list = new ArrayList<Object>();
		sb.append("select");
		sb.append("	count(*)");
		sb.append("	from");
		sb.append("	food f,");
		sb.append("	foodtype ft");
		sb.append("	where 1=1");
		sb.append("	and f.foodType_id=ft.id");
		
		Condition condition = (Condition) pb.getCondition();
		//判断
		if(condition!=null){
			String foodName = condition.getFoodName();
			//String foodName = condition.getFoodName();
				//这个语句报错出现报错，需要将condition进行强转
			if(foodName!=null && !foodName.isEmpty()){
				sb.append("	and f.foodName like ?");
				list.add("%"+foodName+"%");
			}
			
			int type_id = condition.getFoodType_id();
			//int type_id = condition.getFoodType_id();
				//这个语句报错出现报错，需要将condition进行强转
			if(type_id>0){
				sb.append("	and f.foodType_id=?");
				list.add(type_id);
			}
		}
		//执行查询，返回结果的第一行的第一列
		try {
			Long count = qr.query(sb.toString(), new ScalarHandler<Long>(),list.toArray());
			return count.intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
