package cz.pcisland.login_and_registration;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.user.User;
import cz.pcisland.user.UserDAO;
import cz.pcisland.user.UserDAOImpl;

/**
 *	Třída přihlášení a registrace:
 *
 *		nastavení titulku,
 *		přihlášení,
 *		registrace
 */

public class LoginAndRegistrationPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Parametry prod uložení do databáze
	private String emailLogin;
	private String passwordLogin;
	private String emailReg;
	private String passwordReg;
	private String passwordConfirmReg;
	private String nameReg;
	private String surnameReg;
	private String genderReg;
	private Integer phoneNumberReg;
	private String streetAddressReg;
	private String cityReg;
	private Integer zipCodeReg;
	private String countryReg = "Česká Republika";
	private Boolean agreeReg;
	
	// Přihlášení
	private final Label emailLoginLabel;
	private final TextField<String> emailLoginTextField;
	private final Label passwordLoginLabel;
	private final PasswordTextField passwordLoginPasswordField;
	private final Button submitLoginButton;
	private final Label successLoginLabel;
	private final Form loginForm;
	
	// Registrace
	private final Label emailRegLabel;
	private final TextField<String> emailRegTextField;
	private final Label passwordRegLabel;
	private final PasswordTextField passwordRegPasswordField;
	private final Label passwordConfirmRegLabel;
	private final PasswordTextField passwordConfirmRegPasswordField;
	private final Label nameRegLabel;
	private final TextField<String> nameRegTextField;
	private final Label surnameRegLabel;
	private final TextField<String> surnameRegTextField;
	private final RadioGroup<String> genderRegRadioGroup;
	private final Label manTagLabel;
	private final Label womanTagLabel;
	private final Label phoneNumberRegLabel;
	private final TextField<Integer> phoneNumberRegTextField;
	private final Label streetAddressRegLabel;
	private final TextField<String> streetAddressRegTextField;
	private final Label cityRegLabel;
	private final TextField<String> cityRegTextField;
	private final Label zipCodeRegLabel;
	private final TextField<Integer> zipCodeRegTextField;
	private final DropDownChoice<String> countryRegDropDownChoice;
	private final CheckBox agreeRegCheckBox;
	private final Button submitRegButton;
	private final Label submitRegLabel;
	private final Label successRegLabel;
	private final Form registrationForm;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public LoginAndRegistrationPage() {
		
		// Nastavení titulku
		setTitle(new Model("Přihlášení | Registrace"));
		
// Přihlášení Form ////////////////////////////////////////////////////////////////////////////////////
		
		loginForm = new Form("loginForm") {
			
			@Override
			protected void onError() {
				super.onError();
					
				// Validace emailu
				if (!emailLoginTextField.isValid()) {
					emailLoginLabel.setDefaultModelObject("Nezadali jste email");
					emailLoginLabel.add(new AttributeModifier("style", "color: red;"));
					emailLoginTextField.add(new AttributeModifier("style", "border: 1px solid red"));
					
				} else {
					emailLoginLabel.setDefaultModelObject("");
					emailLoginTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
				
				// Validace hesla
				if (!passwordLoginPasswordField.isValid()) {
					passwordLoginLabel.setDefaultModelObject("Nezadali jste heslo");
					passwordLoginLabel.add(new AttributeModifier("style", "color: red;"));
					passwordLoginPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					passwordLoginLabel.setDefaultModelObject("");
					passwordLoginPasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Ověření přihlášení
				if (AuthenticatedWebSession.get().signIn(emailLogin, passwordLogin)) {
					AuthenticatedWebSession session = (AuthenticatedWebSession) getSession();
					
					// Nastavení přihlášeného uživatele
					UserDAO userDAO = new UserDAOImpl();
					for (User user : userDAO.getAllUsers()) {
						if (emailLogin.equals(user.getEmail())) {
							session.setAttribute("currentUser", user);
							
						}
					}

					// Změna stylu při úspěšném přihlášení
					successLoginLabel.setDefaultModelObject("Přihlášení proběhlo úspěšně");
					successLoginLabel.add(new AttributeModifier("class", "success"));

					loginForm.add(new AttributeModifier("style", "height: 325px"));
					
					emailLoginLabel.setDefaultModelObject("");
					emailLoginLabel.add(new AttributeModifier("style", "color: green;"));
					emailLoginTextField.add(new AttributeModifier("style", "border: 1px solid green"));
					
					passwordLoginLabel.setDefaultModelObject("");
					passwordLoginLabel.add(new AttributeModifier("style", "color: green;"));
					passwordLoginPasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					
					UserDAO userDAO = new UserDAOImpl();
					boolean emailFound = false;
					boolean passwordFound = false;
					
					for (User user : userDAO.getAllUsers()) {

						// Ověření emailu
						if (emailLogin.equals(user.getEmail())) {
							emailFound = true;
						}
						
						// Ověření hesla
						if (passwordLogin.equals(user.getPassword())) {
							passwordFound = true;
						}
					}
					
					// Změna stylu u neodpovídajících údajů
					if (!emailFound) {
						emailLoginLabel.setDefaultModelObject("Zadaný email je nesprávný");
						emailLoginLabel.add(new AttributeModifier("style", "color: red;"));
						emailLoginTextField.add(new AttributeModifier("style", "border: 1px solid red"));
					
					} else emailLoginTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					if (!passwordFound) {
						passwordLoginLabel.setDefaultModelObject("Zadané heslo je nesprávné");
						passwordLoginLabel.add(new AttributeModifier("style", "color: red;"));
						passwordLoginPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
					
					} else passwordLoginPasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2;"));
				}
			}
		};
		add(loginForm);
		
// Konfigurace komponent //////////////////////////////////////////////////////////////////////////////
		
		successLoginLabel = new Label("successLoginLabel", "");
		loginForm.add(successLoginLabel);
		
		emailLoginLabel = new Label("emailLoginLabel", "");
		loginForm.add(emailLoginLabel);
		
		emailLoginTextField = new TextField("emailLogin", new PropertyModel<String>(this, "emailLogin"));
		emailLoginTextField.setRequired(true);
		emailLoginTextField.add(EmailAddressValidator.getInstance());
		loginForm.add(emailLoginTextField);
		
		passwordLoginLabel = new Label("passwordLoginLabel", "");
		loginForm.add(passwordLoginLabel);
		
		passwordLoginPasswordField = new PasswordTextField("passwordLogin", new PropertyModel<String>(this, "passwordLogin"));
		loginForm.add(passwordLoginPasswordField);
		
		submitLoginButton = new Button("submitLogin") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Deaktivace tlačítka pro přihlášení, pokud je uživatel přihlášený
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					setEnabled(false);
				} else {
					setEnabled(true);
				}
			}
		};
		submitLoginButton.setOutputMarkupId(true);
		loginForm.add(submitLoginButton);
		
// Registrace Form ////////////////////////////////////////////////////////////////////////////////////
		
		registrationForm = new Form("registrationForm") {
			
			@Override
			protected void onError() {
				super.onError();
				
				// Validace emailu
				if (!emailRegTextField.isValid()) {
					emailRegLabel.setDefaultModelObject("Nezadali jste email");
					emailRegLabel.add(new AttributeModifier("style", "color: red;"));
					emailRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					emailRegLabel.setDefaultModelObject("");
					emailRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
				
				// Validace hesla
				if (!passwordRegPasswordField.isValid()) {
					passwordRegLabel.setDefaultModelObject("Nezadali jste heslo");
					passwordRegLabel.add(new AttributeModifier("style", "color: red;"));
					passwordRegPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					passwordRegLabel.setDefaultModelObject("");
					passwordRegPasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				// Validace potvrzení hesla
				if (!passwordConfirmRegPasswordField.isValid()) {
					passwordConfirmRegLabel.setDefaultModelObject("Nezadali jste potvrzení hesla");
					passwordConfirmRegLabel.add(new AttributeModifier("style", "color: red;"));
					passwordConfirmRegPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					passwordConfirmRegLabel.setDefaultModelObject("");
					passwordConfirmRegPasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			
				// Validace jména
				if (!nameRegTextField.isValid()) {
					nameRegLabel.setDefaultModelObject("Nezadali jste jméno");
					nameRegLabel.add(new AttributeModifier("style", "color: red;"));
					nameRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					nameRegLabel.setDefaultModelObject("");
					nameRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace příjmení
				if (!surnameRegTextField.isValid()) {
					surnameRegLabel.setDefaultModelObject("Nezadali jste příjmení");
					surnameRegLabel.add(new AttributeModifier("style", "color: red;"));
					surnameRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					surnameRegLabel.setDefaultModelObject("");
					surnameRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				} 
			
				// Validace pohlaví
				if (!genderRegRadioGroup.isValid()) {
					manTagLabel.add(new AttributeModifier("style", "color: red"));
					womanTagLabel.add(new AttributeModifier("style", "color: red"));
				
				} else {
					manTagLabel.add(new AttributeModifier("style", "color: green"));
					womanTagLabel.add(new AttributeModifier("style", "color: green"));
				}
				
				// Validace telefonního čísla
				if (!phoneNumberRegTextField.isValid()) {
					phoneNumberRegLabel.setDefaultModelObject("Nezadali jste telefonní číslo");
					phoneNumberRegLabel.add(new AttributeModifier("style", "color: red;"));
					phoneNumberRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					phoneNumberRegLabel.setDefaultModelObject("");
					phoneNumberRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace adresy
				if (!streetAddressRegTextField.isValid()) {
					streetAddressRegLabel.setDefaultModelObject("Nezadali jste adresu");
					streetAddressRegLabel.add(new AttributeModifier("style", "color: red;"));
					streetAddressRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					streetAddressRegLabel.setDefaultModelObject("");
					streetAddressRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace města
				if (!cityRegTextField.isValid()) {
					cityRegLabel.setDefaultModelObject("Nezadali jste město");
					cityRegLabel.add(new AttributeModifier("style", "color: red;"));
					cityRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					cityRegLabel.setDefaultModelObject("");
					cityRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace směrovacího čísla
				if(!zipCodeRegTextField.isValid()) {
					zipCodeRegLabel.setDefaultModelObject("Nezadali jste směrovací číslo");
					zipCodeRegLabel.add(new AttributeModifier("style", "color: red;"));
					zipCodeRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					zipCodeRegLabel.setDefaultModelObject("");
					zipCodeRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				} 
				
				successRegLabel.setDefaultModelObject("");
				successRegLabel.add(new AttributeModifier("class", "none"));
				registrationForm.add(new AttributeModifier("style", "height: 965px"));
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Ověření emailu
				UserDAO userDAO = new UserDAOImpl();
				boolean emailFound = false;
				
				for (User userToCheck : userDAO.getAllUsers()) {
					if (emailReg.equals(userToCheck.getEmail())) {
						emailFound = true;
					} 
				}
				
				// Ověření potvzení hesla
				boolean passwordConfirmation = false;
				
				if (passwordConfirmReg.equals(passwordReg)) {
					passwordConfirmation = true;
				
				} else {
					passwordConfirmRegLabel.setDefaultModelObject("Potvzení hesla nesouhlasí");
					passwordConfirmRegLabel.add(new AttributeModifier("style", "color: red"));
				}
				
				// Uložení uživatele, pokud email není v databázi a heslo souhlasí s potvrzovacím heslem
				if (!emailFound && passwordConfirmation) {
					
					User user = new User();
					user.setEmail(emailReg);
					user.setPassword(passwordReg);
					user.setName(nameReg);
					user.setSurname(surnameReg);
					user.setGender(genderReg);
					user.setPhoneNumber(phoneNumberReg);
					user.setStreetAddress(streetAddressReg);
					user.setCity(cityReg);
					user.setZipCode(zipCodeReg);
					user.setCountry(countryReg);
					userDAO.saveUser(user);
					
					// Změna stylu při úspěšné registraci
					successRegLabel.setDefaultModelObject("Registrace proběhla úspěšně");
					successRegLabel.add(new AttributeModifier("class", "success"));
					
					registrationForm.add(new AttributeModifier("style", "height: 1025px"));
					
					emailRegLabel.setDefaultModelObject("");
					emailRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
					passwordRegLabel.setDefaultModelObject("");
					passwordRegPasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
					passwordConfirmRegLabel.setDefaultModelObject("");
					passwordConfirmRegPasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					emailRegLabel.setDefaultModelObject("Zadaný email je již použitý.");
					emailRegLabel.add(new AttributeModifier("style", "color: red"));
					emailRegTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				}
				
				// Změna stylu u parametrů, které jsou správně
				nameRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				surnameRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				if (genderReg.equals("muž")) {
					manTagLabel.add(new AttributeModifier("style", "color: green"));
					womanTagLabel.add(new AttributeModifier("style", "color: none"));
				
				} else if (genderReg.equals("žena")) {
					womanTagLabel.add(new AttributeModifier("style", "color: green"));
					manTagLabel.add(new AttributeModifier("style", "color: none"));
				}
				
				phoneNumberRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				streetAddressRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				cityRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				zipCodeRegTextField.add(new AttributeModifier("style", "border: 1px solid green"));
			}
		};
		add(registrationForm);
		
// Konfigurace komponent //////////////////////////////////////////////////////////////////////////////
		
		successRegLabel = new Label("successRegLabel", "");
		registrationForm.add(successRegLabel);
		
		// Email
		emailRegLabel = new Label("emailRegLabel", "");
		registrationForm.add(emailRegLabel);
		
		emailRegTextField = new TextField("emailReg", new PropertyModel<String>(this, "emailReg"));
		emailRegTextField.setRequired(true);
		emailRegTextField.add(EmailAddressValidator.getInstance());
		registrationForm.add(emailRegTextField);
		
		// Heslo
		passwordRegLabel = new Label("passwordRegLabel", "");
		registrationForm.add(passwordRegLabel);
		
		passwordRegPasswordField = new PasswordTextField("passwordReg", new PropertyModel<String>(this, "passwordReg"));
		registrationForm.add(passwordRegPasswordField);
		
		// Potvrzení hesla
		passwordConfirmRegLabel = new Label("passwordConfirmRegLabel", "");
		registrationForm.add(passwordConfirmRegLabel);
		
		passwordConfirmRegPasswordField = new PasswordTextField("passwordConfirmReg", new PropertyModel<String>(this, "passwordConfirmReg"));
		registrationForm.add(passwordConfirmRegPasswordField);
		
		// Jméno
		nameRegLabel = new Label("nameRegLabel", "");
		registrationForm.add(nameRegLabel);
		
		nameRegTextField = new TextField("nameReg", new PropertyModel<String>(this, "nameReg"));
		nameRegTextField.setRequired(true);
		registrationForm.add(nameRegTextField);
		
		// Příjmení
		surnameRegLabel = new Label("surnameRegLabel", "");
		registrationForm.add(surnameRegLabel);
		
		surnameRegTextField = new TextField("surnameReg", new PropertyModel<String>(this, "surnameReg"));
		surnameRegTextField.setRequired(true);
		registrationForm.add(surnameRegTextField);
		
		// Pohlaví
		genderRegRadioGroup = new RadioGroup<String>("genderReg", new PropertyModel<String>(this, "genderReg"));
			
			genderRegRadioGroup.add(new Radio<String>("man", new Model("muž")));
			genderRegRadioGroup.add(manTagLabel = new Label("manLabel", "Muž"));
		
			genderRegRadioGroup.add(new Radio<String>("woman", new Model("žena")));
			genderRegRadioGroup.add(womanTagLabel = new Label("womanLabel", "Žena"));
		
		genderRegRadioGroup.setRequired(true);
		registrationForm.add(genderRegRadioGroup);
		
		// Telefonní číslo
		phoneNumberRegLabel = new Label("phoneNumberRegLabel", "");
		registrationForm.add(phoneNumberRegLabel);
		
		phoneNumberRegTextField = new TextField<Integer>("phoneNumberReg", new PropertyModel<Integer>(this, "phoneNumberReg"));
		phoneNumberRegTextField.setRequired(true);
		phoneNumberRegTextField.add(new RangeValidator<Integer>(100000000, 999999999));
		registrationForm.add(phoneNumberRegTextField);
		
		// Adresa
		streetAddressRegLabel = new Label("streetAddressRegLabel", "");
		registrationForm.add(streetAddressRegLabel);
		
		streetAddressRegTextField = new TextField("streetAddressReg", new PropertyModel<String>(this, "streetAddressReg"));
		streetAddressRegTextField.setRequired(true);
		registrationForm.add(streetAddressRegTextField);
		
		// Město
		cityRegLabel = new Label("cityRegLabel", "");
		registrationForm.add(cityRegLabel);
		
		cityRegTextField = new TextField("cityReg", new PropertyModel<String>(this, "cityReg"));
		cityRegTextField.setRequired(true);
		registrationForm.add(cityRegTextField);
		
		// Směrovací číslo
		zipCodeRegLabel = new Label("zipCodeRegLabel", "");
		registrationForm.add(zipCodeRegLabel);
		
		zipCodeRegTextField = new TextField("zipCodeReg", new PropertyModel<Integer>(this, "zipCodeReg"));
		zipCodeRegTextField.setRequired(true);
		zipCodeRegTextField.add(new RangeValidator<Integer>(10000, 99999));
		registrationForm.add(zipCodeRegTextField);
		
		// Stát
		countryRegDropDownChoice = new DropDownChoice<String>("countyReg", new PropertyModel<String>(this, "countryReg"), new UserDAOImpl().getCountries());
		registrationForm.add(countryRegDropDownChoice);
	
		agreeRegCheckBox = new CheckBox("agreeReg", new PropertyModel<Boolean>(this, "agreeReg"));
		agreeRegCheckBox.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(submitRegButton);
			}
		});
		registrationForm.add(agreeRegCheckBox);
		
		submitRegButton = new Button("submitReg") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Deaktivace tlačítka pro registraci, pokud nejsou přijaty podmínky
				if (agreeReg != null && agreeReg) {
					submitRegLabel.add(new AttributeModifier("style", "color: green"));
					setEnabled(true);
				} else {
					submitRegLabel.add(new AttributeModifier("style", "color: red"));
					setEnabled(false);
				}
			}
		};
		submitRegButton.setOutputMarkupId(true);
		registrationForm.add(submitRegButton);
		
		submitRegLabel = new Label("submitLabel", "Souhlasím s podmínkami");
		registrationForm.add(submitRegLabel);
	}
	
}
