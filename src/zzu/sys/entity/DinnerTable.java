package zzu.sys.entity;

import java.util.Date;

public class DinnerTable {
	private int id;	//餐桌主键
	private String tableName;	//餐桌名称
	private int tableStatus;	//餐桌状态，0，空闲；1，预定
	private Date orderDate;		//下单时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(int tableStatus) {
		this.tableStatus = tableStatus;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	@Override
	public String toString() {
		return "DinnerTable [id=" + id + ", tableName=" + tableName
				+ ", tableStatus=" + tableStatus + ", orderDate=" + orderDate
				+ "]";
	}
	
	

}
