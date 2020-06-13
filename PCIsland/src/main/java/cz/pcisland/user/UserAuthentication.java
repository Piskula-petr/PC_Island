package cz.pcisland.user;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

/**
 * 	Třída ověřující přihlášení uživatele;
 */

public class UserAuthentication extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

	public UserAuthentication(Request request) {
		super(request);
	}

	@Override
	protected boolean authenticate(String email, String password) {
		
		// Ověření přihlašovacích údajů
		UserDAO userDAO = new UserDAOImpl();
		
		for (User user : userDAO.getAllUsers()) {
			if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Roles getRoles() {
		return null;
	}

}
