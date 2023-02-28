package cn.maiaimei.example.decorator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratorApplication {
	public static void main(String[] args) {
		ConcreteComponent concreteComponent = new ConcreteComponent();
		ConcreteDecoratorA concreteDecoratorA = new ConcreteDecoratorA();
		ConcreteDecoratorB concreteDecoratorB = new ConcreteDecoratorB();
		
		concreteDecoratorA.setComponent(concreteComponent);
		concreteDecoratorB.setComponent(concreteDecoratorA);
		concreteDecoratorB.operation();
	}
	
	interface Component{
		void operation();
	}
	
	static class ConcreteComponent implements Component{
		@Override
		public void operation() {
			log.info("{} 的操作", this.getClass().getSimpleName());
		}
	}
	
	static abstract class Decorator implements Component {
		private Component component;
		
		public void setComponent(Component component) {
			this.component = component;
		}
		
		@Override
		public void operation() {
			if(component != null) {
				component.operation();
			}
		}
	}
	
	static class ConcreteDecoratorA extends Decorator {
		@Override
		public void operation() {
			super.operation();
			log.info("{} 的操作", this.getClass().getSimpleName());
		}
	}
	
	static class ConcreteDecoratorB extends Decorator {
		@Override
		public void operation() {
			log.info("{} 的操作", this.getClass().getSimpleName());
			super.operation();
		}
	}
}
