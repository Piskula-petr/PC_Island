package cz.pcisland;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;

import cz.pcisland.home_page.HomePage;
import cz.pcisland.login_and_registration.LoginAndRegistrationPage;
import cz.pcisland.user.UserAuthentication;

public class WicketApplication extends AuthenticatedWebApplication {

	@Override
	public Class<? extends WebPage> getHomePage() {
		
		// Domovská stránka
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();
		
		getDebugSettings().setAjaxDebugModeEnabled(false);
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return UserAuthentication.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginAndRegistrationPage.class;
	}
	
}
