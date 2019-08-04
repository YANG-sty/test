package zzu.sys.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zzu.sys.dao.IFoodTypeDao;
import zzu.sys.entity.FoodType;
import zzu.sys.utils.JdbcUtils;


/**
 * 2.菜系模块dao 实现
 * @author Administrator
 *
 */
public class FoodTypeDao implements IFoodTypeDao {

	private QueryRunner qr = JdbcUtils.getQueryRunner();
	
	/*@Override
	public void save(FoodType foodType) {
		String sql = "insert into foodType(typeName) values(?);";
		try {
			JdbcUtils.getQueryRunner().update(sql,foodType.getTypeName());
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		
	}*/

	@Override
	public void update(FoodType foodType) {
		String sql = "update foodType set typeName=? where id=?;";
		try {
			qr.update(sql, foodType.getTypeName(), foodType.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from foodType where id=? ;";
		try {
			qr.update(sql,id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public FoodType findById(int id) {
		try {
			String sql = "select * from foodtype where id=? ;";
			return qr.query(sql,new BeanHandler<FoodType>(FoodType.class) ,id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*@Override
	public List<FoodType> getAll() {
		String sql = "select * from foodType;";
		try {
			return JdbcUtils.getQueryRunner().query(sql, new BeanListHandler<FoodType>(FoodType.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}*/

	/*@Override
	public List<FoodType> getAll(String typeName) {
		String sql = "select * from foodType where typeName like ?;";
		try {
			return JdbcUtils.getQueryRunner()
					.query(sql, new BeanListHandler<FoodType>(FoodType.class), "%" + typeName + "%");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

	@Override
	public void add(FoodType foodtype) {
		try {
			String sql = "insert into foodtype(typeName) values(?);";
			qr.update(sql, foodtype.getTypeName());
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}

	@Override
	public List<FoodType> query() {
		try {
			String sql = "select * from foodtype;";
			return qr.query(sql, new BeanListHandler<FoodType>(FoodType.class));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public List<FoodType> query(String keyword) {
		try {
			String sql = "select * from foodtype where typeName like ?;";
			return qr.query(sql	, new BeanListHandler<FoodType>(FoodType.class), "%"+keyword+"%");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer getFirstType() {
		try {
			String sql = "select * from foodtype;";
			return qr.query(sql, new ScalarHandler<Integer>());
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
}
