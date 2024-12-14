package businessLogic;

import DaoExceptions.DaoConnectionException;
import businessLogic.LoginHandler.IllegalCredentialsException;
import daoFactory.DaoFactory;

public final class LoginController {

	private LoginHandler loginHandler;
	private DaoFactory daoFacotry;

	public LoginController(LoginHandler loginHandler, DaoFactory daoFacotry) {
		this.loginHandler = loginHandler;
		this.daoFacotry = daoFacotry;
	}
	
	public UserController login(String username, String password) throws DaoConnectionException, IllegalCredentialsException {
		return loginHandler.validationCredentials(username, password, daoFacotry);
	}
}


/*
	// Questa è la soluzione con i supplier
	public UserController login(String username, String password) throws Exception {
		List<LoginHandler<?>> handlers = List.of(new LoginHandler<>(
				() -> daoFactory.createStudentDao().getStudentByUsernameAndPassword(username, password), student -> {
					return new StudentController(student, daoFactory);
				}),
				new LoginHandler<>(
						() -> daoFactory.creatTeacherDao().getTeacherByUsernameAndPassword(username, password),
						teacher -> new TeacherController(teacher, daoFactory)),
				new LoginHandler<>(
						() -> daoFactory.createParentDao().getParentByUsernameWithPassword(username, password),
						parent -> new ParentController(parent, daoFactory)));

		for (LoginHandler<?> handler : handlers) {
			Optional<UserController> controller = handler.tryHandle();
			if (controller.isPresent()) {
				return controller.get();
			}
		}

		throw new IllegalArgumentException("Invalid username or password");
	}

	private static class LoginHandler<T> {
		private final ThrowingSupplier<T> daoSupplier;
		private final ThrowingFunction<T, UserController> controllerFactory;

		public LoginHandler(ThrowingSupplier<T> daoSupplier, ThrowingFunction<T, UserController> controllerFactory) {
			this.daoSupplier = daoSupplier;
			this.controllerFactory = controllerFactory;
		}

		public Optional<UserController> tryHandle() {
			try {
				T user = daoSupplier.get();
				if (user != null) {
					return Optional.of(controllerFactory.apply(user));
				}
			} catch (Exception e) {}
			return Optional.empty();
		}
	}

	@FunctionalInterface
	interface ThrowingFunction<T, R> {
		R apply(T t) throws Exception;
	}

	@FunctionalInterface
	interface ThrowingSupplier<T> {
		T get() throws Exception;
	}
}*/
