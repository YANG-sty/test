package zzu.sys.service.impl;

import java.util.List;

import zzu.sys.dao.IFoodTypeDao;
import zzu.sys.dao.impl.FoodTypeDao;
import zzu.sys.entity.FoodType;
import zzu.sys.factory.BeanFactory;
import zzu.sys.service.IFoodTypeService;


/**
 * 3.业务逻辑层接口实现
 * @author Administrator
 *
 */
public class FoodTypeService implements IFoodTypeService{

	//调用 dao
//	private IFoodTypeDao foodTypeDao = new FoodTypeDao();
	
	//工厂创建对象
	private IFoodTypeDao foodTypeDao = 
			BeanFactory.getInstance("foodTypeDao",IFoodTypeDao.class);
	
	/*@Override
	public void save(FoodType foodType) {
		
		try {
			foodTypeDao.save(foodType);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}*/

	@Override
	public void update(FoodType foodType) {
		try {
			foodTypeDao.update(foodType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(int id) {
		try {
			foodTypeDao.delete(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public FoodType findById(int id) {
		try {
			return foodTypeDao.findById(id);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public void add(FoodType foodtype) {
		try {
			foodTypeDao.add(foodtype);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}

	@Override
	public List<FoodType> query() {
		try {
			return foodTypeDao.query();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public List<FoodType> query(String keyword) {

		return foodTypeDao.query(keyword);

	}

	@Override
	public Integer getFirstType() {
		try {
			return foodTypeDao.getFirstType();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/*@Override
	public List<FoodType> getAll() {
		try {
			return foodTypeDao.getAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

	/*@Override
	public List<FoodType> getAll(String typeName) {
		try {
			return foodTypeDao.getAll(typeName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

}
