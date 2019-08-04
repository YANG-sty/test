package zzu.sys.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import zzu.sys.dao.IDinnerTableDao;
import zzu.sys.entity.DinnerTable;
import zzu.sys.utils.JdbcUtils;

public class DinnerTableDao implements IDinnerTableDao{

	private QueryRunner qr = JdbcUtils.getQueryRunner();
	
	@Override
	public void add(DinnerTable dt) {
		try {
			String sql = "insert dinnertable(tableName) values(?);";
			qr.update(sql,dt.getTableName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int id) {
		String sql = "delete from dinnertable where id=?;";
		try {
			qr.update(sql,id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(DinnerTable dt) {
		String sql = "update dinnertable set tableStatus=?,orderDate=? where id=?; ";
		Date date = dt.getOrderDate();
		try {
			JdbcUtils.getQueryRunner().update(sql, dt.getTableStatus(),date,dt.getId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DinnerTable> query() {
		String sql = "select * from dinnertable;";
		try {
			return qr.query(sql, new BeanListHandler<DinnerTable>(DinnerTable.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DinnerTable findById(int id) {
		String sql = "select * from dinnertable where id=?;";
		try {
			return qr.query(sql, new BeanHandler<DinnerTable>(DinnerTable.class), id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<DinnerTable> query(String keyword) {
		String sql = "select * from dinnertable where tableName like ?;";
		try {
			return qr.query(sql, new BeanListHandler<DinnerTable>(DinnerTable.class), "%"+keyword+"%");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void quitTable(int id) {
		String sql = "update dinnertable set tableStatus=?,orderDate=? where id=?;";
		
		try {
			/*
			 * 进行 退桌 处理，将时间设置为 null，餐桌状态设置为 0 
			 * */
			JdbcUtils.getQueryRunner().update(sql, 0,null,id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
