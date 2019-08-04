package zzu.sys.entity;

public class Food {
	private int id;
	private String foodName;
	private int foodType_id;
	private double price;
	private double mprice;
	private String remark;
	private String img;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public int getFoodType_id() {
		return foodType_id;
	}
	public void setFoodType_id(int foodType_id) {
		this.foodType_id = foodType_id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getMprice() {
		return mprice;
	}
	public void setMprice(double mprice) {
		this.mprice = mprice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "food [id=" + id + ", foodName=" + foodName + ", foodType_id="
				+ foodType_id + ", price=" + price + ", mprice=" + mprice
				+ ", remark=" + remark + ", img=" + img + "]";
	}
	
	public int hashCode(){
		return this.id;
		
	}
	
	public boolean equals(Object obj){
		Food f = (Food)obj;
		return f.getId()==this.id;
	}
	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		food other = (food) obj;
		if (id != other.id)
			return false;
		return true;
	}*/
	
	
}
