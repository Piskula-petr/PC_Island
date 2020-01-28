package cz.pcisland.account_setting;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.user.User;
import cz.pcisland.user.UserDAO;
import cz.pcisland.user.UserDAOImpl;

/*
 *	Třída nastavení účtu:
 *		
 *		nastavení titulku,
 *		změnu emailu,
 *		změnu hesla,
 *		změnu dodacích údajů
 */

public class AccountSettingsPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Paramerty pro uložení do databáze
	private String emailChange;
	private String currentPassword;
	private String passwordChange;
	private String passwordConfirmChange;
	private String nameChange;
	private String surnameChange;
	private Integer phoneNumberChange;
	private String streetAddressChange;
	private String cityChange;
	private Integer zipCodeChange;
	private String countryChange;
	
	// Změna Emailu
	private final Label emaiLChangeLabel;
	private final TextField<String> emailChangeTextField;
	private final Label successEmailChangeLabel;
	private final Form emailChangeForm;
	
	// Změna hesla
	private final Label currentPasswordLabel;
	private final PasswordTextField currentPasswordField;
	private final Label passwordChangeLabel;
	private final PasswordTextField passwordChangePasswordField;
	private final Label passwordConfirmChangeLabel;
	private final PasswordTextField passwordConfirmChangePasswordField;
	private final Label successPasswordChangeLabel;
	private final Form passwordChangeForm;
	
	// Změna dodacích údajů
	private final Label nameChangeLabel;
	private final TextField<String> nameChangeTextField;
	private final Label surnameChangeLabel;
	private final TextField<String> surnameChangeTextField;
	private final Label phoneNumberChangeLabel;
	private final TextField<Integer> phoneNumberChangeTextField;
	private final Label streetAddressChangeLabel;
	private final TextField<String> streetAddressChangeTextField;
	private final Label cityChangeLabel;
	private final TextField<String> cityChangeTextField;
	private final Label zipCodeChangeLabel;
	private final TextField<Integer> zipCodeTextField;
	private final DropDownChoice<String> countryChangeDropDownChoice;
	private final Label successDeliveryChangeLabel;
	private final Form deliveryDataChangeForm;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public AccountSettingsPage(User user) {
		
		// Nastavení titulku
		setTitle(new Model("Nastavení účtu"));
		
// Změna emailu Form //////////////////////////////////////////////////////////////////////////////////
		
		emailChangeForm = new Form("emailChangeForm") {
			
			@Override
			protected void onError() {
				super.onError();

				// Validace emailu
				if (!emailChangeTextField.isValid()) {
					emaiLChangeLabel.setDefaultModelObject("Nezadali jste email");
					emaiLChangeLabel.add(new AttributeModifier("style", "color: red;"));
					emailChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					emaiLChangeLabel.setDefaultModelObject("");
					emailChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Ověření jestli není email stjený jako původní
				if (!emailChange.equals(user.getEmail())) {
					UserDAO userDAO = new UserDAOImpl();
					userDAO.changeEmail(user.getEmail(), emailChange);
					
					// Nastavení nového emailu
					user.setEmail(emailChange);
					Session.get().setAttribute("currentUser", user);
					
					// Změna stylu po úspěšném přihlášení
					successEmailChangeLabel.setDefaultModelObject("Email byl úspěšně změněn");
					successEmailChangeLabel.add(new AttributeModifier("class", "success"));
					
					emailChangeForm.add(new AttributeModifier("style", "height: 250px"));
					
					emaiLChangeLabel.setDefaultModelObject("");
					emailChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					
					// Změna stylu při duplicitním emailu
					successEmailChangeLabel.setDefaultModelObject("");
					successEmailChangeLabel.add(new AttributeModifier("class", "none"));
					emailChangeForm.add(new AttributeModifier("style", "height: 190px"));
					
					emaiLChangeLabel.setDefaultModelObject("Zadaný email nesmí být stejný");
					emaiLChangeLabel.add(new AttributeModifier("style", "color: red;"));
					emailChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				}
			}
		};
		add(emailChangeForm);
		
// Konfigurace komponent //////////////////////////////////////////////////////////////////////////////
		
		successEmailChangeLabel = new Label("successEmailChangeLabel", "");
		emailChangeForm.add(successEmailChangeLabel);
		
		emaiLChangeLabel = new Label("emaiLChangeLabel", "");
		emailChangeForm.add(emaiLChangeLabel);
		
		emailChangeTextField = new TextField("emailChange", new PropertyModel<String>(this, "emailChange"));
		emailChangeTextField.setRequired(true);
		emailChangeTextField.add(EmailAddressValidator.getInstance());
		emailChangeTextField.setDefaultModelObject(user.getEmail());
		emailChangeForm.add(emailChangeTextField);
		
// Změna hesla Form ///////////////////////////////////////////////////////////////////////////////////
		
		passwordChangeForm = new Form("passwordChangeForm") {
			
			@Override
			protected void onError() {
				super.onError();
				
				// Validace aktuálního hesla
				if(!currentPasswordField.isValid()) {
					currentPasswordLabel.setDefaultModelObject("Nezadali jste aktuální heslo");
					currentPasswordLabel.add(new AttributeModifier("style", "color: red;"));
					currentPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					currentPasswordLabel.setDefaultModelObject("");
					currentPasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			
				// Validace nového hesla
				if (!passwordChangePasswordField.isValid()) {
					passwordChangeLabel.setDefaultModelObject("Nezadali jste nové heslo");
					passwordChangeLabel.add(new AttributeModifier("style", "color: red;"));
					passwordChangePasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					passwordChangeLabel.setDefaultModelObject("");
					passwordChangePasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			
				// Validace potvrzení hesla
				if (!passwordConfirmChangePasswordField.isValid()) {
					passwordConfirmChangeLabel.setDefaultModelObject("Nezadali jste potvrzení hesla");
					passwordConfirmChangeLabel.add(new AttributeModifier("style", "color: red;"));
					passwordConfirmChangePasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					passwordConfirmChangeLabel.setDefaultModelObject("");
					passwordConfirmChangePasswordField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				successPasswordChangeLabel.setDefaultModelObject("");
				successPasswordChangeLabel.add(new AttributeModifier("class", "none"));
				passwordChangeForm.add(new AttributeModifier("style", "height: 340px"));
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Ověření správnosti hesla
				boolean passwordOk = false;
				
				if (currentPassword.equals(user.getPassword())) {
					passwordOk = true;
					currentPasswordLabel.setDefaultModelObject("");
					currentPasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					currentPasswordLabel.setDefaultModelObject("Zadané heslo je nesprávné");
					currentPasswordLabel.add(new AttributeModifier("style", "color: red"));
					currentPasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				}
				
				// Ověření jestli není heslo stjené jako původní
				boolean passwordIsNotSame = false;

				if (!passwordConfirmChange.equals(user.getPassword())) {
					passwordIsNotSame = true;
					
					passwordChangeLabel.setDefaultModelObject("");
					passwordChangePasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					passwordChangeLabel.setDefaultModelObject("Heslo nesmí být stejné");
					passwordChangeLabel.add(new AttributeModifier("style", "color: red"));
					passwordChangePasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				}
				
				// Ověření potvrzení hesla
				boolean passwordConfirmation = false;
				
				if (passwordConfirmChange.equals(passwordChange)) {
					passwordConfirmation = true;
					
					passwordConfirmChangeLabel.setDefaultModelObject("");
					passwordConfirmChangePasswordField.add(new AttributeModifier("style", "border: 1px solid green"));
				
				} else {
					passwordConfirmChangeLabel.setDefaultModelObject("Potvzení hesla nesouhlasí");
					passwordConfirmChangeLabel.add(new AttributeModifier("style", "color: red"));
					passwordConfirmChangePasswordField.add(new AttributeModifier("style", "border: 1px solid red"));
				}
				
				// Uložení hesla, pokud je správné, není stejně a nové heslo souhlací s potvrzovacím
				if (passwordOk && passwordIsNotSame && passwordConfirmation) {
					UserDAO userDAO = new UserDAOImpl();
					userDAO.changePassword(user.getEmail(), passwordChange);
					
					// Nastaví nové heslo
					user.setPassword(passwordChange);
					Session.get().setAttribute("currentUser", user);
					 
					// Změna stylu při úspěchu
					successPasswordChangeLabel.setDefaultModelObject("Heslo bylo úspěšně změněno");
					successPasswordChangeLabel.add(new AttributeModifier("class", "success"));	
					
					passwordChangeForm.add(new AttributeModifier("style", "height: 400px"));
				}
			}
		};
		add(passwordChangeForm);
		
// Konfigurace komponent /////////////////////////////////////////////////////////////////////////////
		
		successPasswordChangeLabel = new Label("successPasswordChangeLabel", "");
		passwordChangeForm.add(successPasswordChangeLabel);
		
		currentPasswordLabel = new Label("currentPasswordLabel", "");
		passwordChangeForm.add(currentPasswordLabel);
		
		currentPasswordField = new PasswordTextField("currentPassword", new PropertyModel<String>(this, "currentPassword"));
		passwordChangeForm.add(currentPasswordField);
		
		passwordChangeLabel = new Label("passwordChangeLabel", "");
		passwordChangeForm.add(passwordChangeLabel);
		
		passwordChangePasswordField = new PasswordTextField("passwordChange", new PropertyModel<String>(this, "passwordChange"));
		passwordChangeForm.add(passwordChangePasswordField);
		
		passwordConfirmChangeLabel = new Label("passwordConfirmChangeLabel", "");
		passwordChangeForm.add(passwordConfirmChangeLabel);
		
		passwordConfirmChangePasswordField = new PasswordTextField("passwordConfirmChange", new PropertyModel<String>(this, "passwordConfirmChange"));
		passwordChangeForm.add(passwordConfirmChangePasswordField);
		
// Změna dodacích údajů Form //////////////////////////////////////////////////////////////////////////
		
		deliveryDataChangeForm = new Form("deliveryDataChangeForm") {
			
			@Override
			protected void onError() {
				super.onError();
				
				// Validace jména
				if (!nameChangeTextField.isValid()) {
					nameChangeLabel.setDefaultModelObject("Nezadali jste jméno");
					nameChangeLabel.add(new AttributeModifier("style", "color: red;"));
					nameChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					nameChangeLabel.setDefaultModelObject("");
					nameChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			
				// Validace příjmení
				if (!surnameChangeTextField.isValid()) {
					surnameChangeLabel.setDefaultModelObject("Nezadali jste příjmení");
					surnameChangeLabel.add(new AttributeModifier("style", "color: red;"));
					surnameChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					surnameChangeLabel.setDefaultModelObject("");
					surnameChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				// Validace telefonního čísla
				if (!phoneNumberChangeTextField.isValid()) {
					phoneNumberChangeLabel.setDefaultModelObject("Nezadali jste telefonní číslo");
					phoneNumberChangeLabel.add(new AttributeModifier("style", "color: red;"));
					phoneNumberChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					phoneNumberChangeLabel.setDefaultModelObject("");
					phoneNumberChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			
				// Validace adresy
				if (!streetAddressChangeTextField.isValid()) {
					streetAddressChangeLabel.setDefaultModelObject("Nezadali jste adresu");
					streetAddressChangeLabel.add(new AttributeModifier("style", "color: red;"));
					streetAddressChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					streetAddressChangeLabel.setDefaultModelObject("");
					streetAddressChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				// Validace města
				if (!cityChangeTextField.isValid()) {
					cityChangeLabel.setDefaultModelObject("Nezadali jste město");
					cityChangeLabel.add(new AttributeModifier("style", "color: red;"));
					cityChangeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					cityChangeLabel.setDefaultModelObject("");
					cityChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				// Validace směrovacího čísla
				if (!zipCodeTextField.isValid()) {
					zipCodeChangeLabel.setDefaultModelObject("Nezadali jste směrovací číslo");
					zipCodeChangeLabel.add(new AttributeModifier("style", "color: red;"));
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					zipCodeChangeLabel.setDefaultModelObject("");
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				successEmailChangeLabel.setDefaultModelObject("");
				successEmailChangeLabel.add(new AttributeModifier("class", "none"));
				deliveryDataChangeForm.add(new AttributeModifier("style", "height: 635px"));
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				boolean valueChange = false;
				UserDAO userDAO = new UserDAOImpl();
				
				// Nastavení nového jména
				if (!nameChange.equals(user.getName())) {
					userDAO.changeDeliveryData(user.getEmail(), "name", nameChange);
					valueChange = true;
					
					nameChangeLabel.setDefaultModelObject("");
					nameChangeLabel.add(new AttributeModifier("style", "color: green"));
					nameChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setName(nameChange);
					Session.get().setAttribute("currentUser", user);
				
				} else nameChangeLabel.add(new AttributeModifier("style", "color: red"));

				
				// Nastavení nového příjmení
				if (!surnameChange.equals(user.getSurname())) {
					userDAO.changeDeliveryData(user.getEmail(), "surname", surnameChange);
					valueChange = true;
					
					surnameChangeLabel.setDefaultModelObject("");
					surnameChangeLabel.add(new AttributeModifier("style", "color: green"));
					surnameChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setSurname(surnameChange);
					Session.get().setAttribute("currentUser", user);
				
				} else surnameChangeLabel.add(new AttributeModifier("style", "color: red"));
				
				// Nastavení nového telefonního čísla
				if (!phoneNumberChange.equals(user.getPhoneNumber())) {
					userDAO.changeDeliveryData(user.getEmail(), "phone_number", String.valueOf(phoneNumberChange));
					valueChange = true;
					
					phoneNumberChangeLabel.setDefaultModelObject("");
					phoneNumberChangeLabel.add(new AttributeModifier("style", "color: green"));
					phoneNumberChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setPhoneNumber(phoneNumberChange);
					Session.get().setAttribute("currentUser", user);
				
				} else phoneNumberChangeLabel.add(new AttributeModifier("style", "color: red"));
				
				// Nastavení nové adresy
				if (!streetAddressChange.equals(user.getStreetAddress())) {
					userDAO.changeDeliveryData(user.getEmail(), "street_address", streetAddressChange);
					valueChange = true;
					
					streetAddressChangeLabel.setDefaultModelObject("");
					streetAddressChangeLabel.add(new AttributeModifier("style", "color: green"));
					streetAddressChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setStreetAddress(streetAddressChange);
					Session.get().setAttribute("currentUser", user);
				
				} else streetAddressChangeLabel.add(new AttributeModifier("style", "color: red"));
				
				// Nastavení nového město
				if (!cityChange.equals(user.getCity())) {
					userDAO.changeDeliveryData(user.getEmail(), "city", cityChange);
					valueChange = true;
					
					cityChangeLabel.setDefaultModelObject("");
					cityChangeLabel.add(new AttributeModifier("style", "color: green"));
					cityChangeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setCity(cityChange);
					Session.get().setAttribute("currentUser", user);
				
				} else cityChangeLabel.add(new AttributeModifier("style", "color: red"));
				
				// Nastavení nového směrovacího čísla
				if (!zipCodeChange.equals(user.getZipCode())) {
					userDAO.changeDeliveryData(user.getEmail(), "zip_code", String.valueOf(zipCodeChange));
					valueChange = true;
					
					zipCodeChangeLabel.setDefaultModelObject("");
					zipCodeChangeLabel.add(new AttributeModifier("style", "color: green"));
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setZipCode(zipCodeChange);
					Session.get().setAttribute("currentUser", user);
				
				} else zipCodeChangeLabel.add(new AttributeModifier("style", "color: red"));
				
				// Nastavení nového státu
				if (!countryChange.equals(user.getCountry())) {
					userDAO.changeDeliveryData(user.getEmail(), "country", countryChange);
					valueChange = true;
					countryChangeDropDownChoice.add(new AttributeModifier("style", "border: 1px solid green"));
				
					user.setCountry(countryChange);
					Session.get().setAttribute("currentUser", user);
				}
				
				// Oznámení o změně hodnot
				if (valueChange) {
					successDeliveryChangeLabel.setDefaultModelObject("Změny údajů proběhly úspěšně");
					successDeliveryChangeLabel.add(new AttributeModifier("class", "success"));
					
					deliveryDataChangeForm.add(new AttributeModifier("style", "height: 695px"));
				
				} else {
					successDeliveryChangeLabel.setDefaultModelObject("Údaje zůstaly nezměněny");
					successDeliveryChangeLabel.add(new AttributeModifier("class", "success"));
					successDeliveryChangeLabel.add(new AttributeModifier("style", "border: 1px solid red; color: red"));
				
					deliveryDataChangeForm.add(new AttributeModifier("style", "height: 695px"));
				
					// Změna stylu na neutrální vzhled
					nameChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					surnameChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					phoneNumberChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					streetAddressChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					cityChangeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
					countryChangeDropDownChoice.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			}
		};
		add(deliveryDataChangeForm);
		
// Konfigurace komponent /////////////////////////////////////////////////////////////////////////////
		
		successDeliveryChangeLabel = new Label("successDeliveryChangeLabel", "");
		deliveryDataChangeForm.add(successDeliveryChangeLabel);
		
		// Jméno
		nameChangeLabel = new Label("nameChangeLabel", "");
		deliveryDataChangeForm.add(nameChangeLabel);
		
		nameChangeTextField = new TextField("nameChange", new PropertyModel<String>(this, "nameChange"));
		nameChangeTextField.setRequired(true);
		nameChangeTextField.setDefaultModelObject(user.getName());
		deliveryDataChangeForm.add(nameChangeTextField);
		
		// Příjmení
		surnameChangeLabel = new Label("surnameChangeLabel", "");
		deliveryDataChangeForm.add(surnameChangeLabel);
		
		surnameChangeTextField = new TextField("surnameChange", new PropertyModel<String>(this, "surnameChange"));
		surnameChangeTextField.setRequired(true);
		surnameChangeTextField.setDefaultModelObject(user.getSurname());
		deliveryDataChangeForm.add(surnameChangeTextField);
			
		// Telefonní číslo
		phoneNumberChangeLabel = new Label("phoneNumberChangeLabel", "");
		deliveryDataChangeForm.add(phoneNumberChangeLabel);
		
		phoneNumberChangeTextField = new TextField<Integer>("phoneNumberChange", new PropertyModel<Integer>(this, "phoneNumberChange"));
		phoneNumberChangeTextField.setRequired(true);
		phoneNumberChangeTextField.add(new RangeValidator<Integer>(100000000, 999999999));
		phoneNumberChangeTextField.setDefaultModelObject(user.getPhoneNumber());
		deliveryDataChangeForm.add(phoneNumberChangeTextField);
		
		// Adresa
		streetAddressChangeLabel = new Label("streetAddressChangeLabel", "");
		deliveryDataChangeForm.add(streetAddressChangeLabel);
		
		streetAddressChangeTextField = new TextField("streetAddressChange", new PropertyModel<String>(this, "streetAddressChange"));
		streetAddressChangeTextField.setRequired(true);
		streetAddressChangeTextField.setDefaultModelObject(user.getStreetAddress());
		deliveryDataChangeForm.add(streetAddressChangeTextField);
		
		// Město
		cityChangeLabel = new Label("cityChangeLabel", "");
		deliveryDataChangeForm.add(cityChangeLabel);
		
		cityChangeTextField = new TextField("cityChange", new PropertyModel<String>(this, "cityChange"));
		cityChangeTextField.setRequired(true);
		cityChangeTextField.setDefaultModelObject(user.getCity());
		deliveryDataChangeForm.add(cityChangeTextField);
		
		// Směrovací číslo
		zipCodeChangeLabel = new Label("zipCodeChangeLabel", "");
		deliveryDataChangeForm.add(zipCodeChangeLabel);
		
		zipCodeTextField = new TextField("zipCodeChange", new PropertyModel<Integer>(this, "zipCodeChange"));
		zipCodeTextField.setRequired(true);
		zipCodeTextField.add(new RangeValidator<Integer>(10000, 99999));
		zipCodeTextField.setDefaultModelObject(user.getZipCode());
		deliveryDataChangeForm.add(zipCodeTextField);
		
		// Stát
		countryChangeDropDownChoice = new DropDownChoice("countryChange", new PropertyModel<String>(this, "countryChange"), new UserDAOImpl().getCountries());
		countryChangeDropDownChoice.setDefaultModelObject(user.getCountry());
		deliveryDataChangeForm.add(countryChangeDropDownChoice);
	}
	
}
