package cn.maiaimei.example.decorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DrinkApplication {

	public static void main(String[] args) {
		MilkTea milkTea = new MilkTea();
		log.info("奶茶：{}元", milkTea.getPrice());
		Pearl pearl = new Pearl();
		pearl.setDrink(milkTea);
		log.info("奶茶+珍珠：{}元", pearl.getPrice());
		Pearl pearl2 = new Pearl();
		pearl2.setDrink(pearl);
		log.info("奶茶+珍珠+珍珠：{}元", pearl2.getPrice());
	}
	
	/**
	 * 抽象组件角色
	 */
	interface Drink {
		int getPrice();
	}
	
	/**
	 * 具体组件角色，实现抽象组件角色
	 */
	static class MilkTea implements Drink{

		@Override
		public int getPrice() {
			return 8;
		}
	}
	
	/**
	 * 抽象装饰角色，实现抽象组件角色
	 */
	static abstract class Ingredients implements Drink {
		private Drink drink;
		
		public void setDrink(Drink drink) {
			this.drink = drink;
		}
		
		@Override
		public int getPrice() {
			if(drink != null) {
				return drink.getPrice();
			}
			return 0;
		}
	}
	
	/**
	 * 具体装饰角色，继承抽象装饰角色
	 */
	static class Pearl extends Ingredients{
		
		@Override
		public int getPrice() {
			return super.getPrice() + 2;
		}
	}

}
