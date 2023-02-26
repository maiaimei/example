package cn.maiaimei.example.state;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateApplication {

	public static void main(String[] args) {
		Context context = new Context();
		for (int i = 0; i < 10; i++) {
			context.request();
		}
	}
	
	interface State {
		void handle(Context context);
	}
	
	static class ConcreteStateA implements State {
		
		@Override
		public void handle(Context context) {
			log.info("{} 处理请求", this.getClass().getSimpleName());
			context.setState(context.getConcreteStateB());
		}
		
	}
	
	static class ConcreteStateB implements State {

		@Override
		public void handle(Context context) {
			log.info("{} 处理请求", this.getClass().getSimpleName());
			context.setState(context.getConcreteStateA());
		}
		
	}
	
	static class Context {
		
		private State state;
		private State concreteStateA = new ConcreteStateA();
		private State concreteStateB = new ConcreteStateB();
		
		public Context() {
			this.state = concreteStateA;
		}

		public State getConcreteStateA() {
			return concreteStateA;
		}

		public State getConcreteStateB() {
			return concreteStateB;
		}

		public void setState(State state) {
			this.state = state;
		}

		public void request() {
			this.state.handle(this);
		}
		
	}
	

}
