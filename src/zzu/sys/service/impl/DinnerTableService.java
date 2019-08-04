package zzu.sys.service.impl;

import java.util.Date;
import java.util.List;

import zzu.sys.dao.IDinnerTableDao;
import zzu.sys.entity.DinnerTable;
import zzu.sys.factory.BeanFactory;
import zzu.sys.service.IDinnerTableService;

public class DinnerTableService implements IDinnerTableService{

	IDinnerTableDao dao = 
			BeanFactory.getInstance("dinnerTableDao", IDinnerTableDao.class);
	@Override
	public void add(DinnerTable dt) {
		dao.add(dt);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
	}

	@Override
	public void update(DinnerTable dt) {
		dao.update(dt);
	}

	@Override
	public List<DinnerTable> query() {
		return dao.query();
	}

	@Override
	public DinnerTable findById(int id) {
		return dao.findById(id);
	}

	@Override
	public List<DinnerTable> query(String keyword) {
		return dao.query(keyword);
	}

	@Override
	public void quitTable(int id) {
		dao.quitTable(id);
	}

	@Override
	// 改变餐桌的状态
	public DinnerTable changeState(int id) {
		//找到与id相对应的餐桌
		DinnerTable table = dao.findById(id);
		//初始化餐桌的状态，得到餐桌的  tableStatus 的值
		int status = table.getTableStatus();
		//对 餐桌的状态值进行改变
		if(status==0){//餐桌没有被预定，当需要改变餐桌的状态的时候，说明餐桌是要被预定的
			status=1;//将餐桌的状态值设置为 1 
			Date date = new Date();//获得当时的时间数据
			table.setOrderDate(date);//对 餐桌的下单时间进行赋值操作
		}else if(status==1){
			status=0;
			table.setOrderDate(null);//取消餐桌的预定或者退桌的时候将餐桌的下单时间置为空
		}
		
		table.setTableStatus(status);
		dao.update(table);//将数据封装到table中然后进行更新操作
		return table;
	}
	
}
