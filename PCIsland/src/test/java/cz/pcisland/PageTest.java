package cz.pcisland;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import cz.pcisland.WicketApplication;
import cz.pcisland.cart.CartPage;
import cz.pcisland.graphics_cards.GraphicsCardsPage;
import cz.pcisland.hard_disks.HardDisksPage;
import cz.pcisland.home_page.HomePage;
import cz.pcisland.login_and_registration.LoginAndRegistrationPage;
import cz.pcisland.memory.MemoryPage;
import cz.pcisland.motherboards.MotherboardsPage;
import cz.pcisland.power_supply_units.PowerSupplyUnitsPage;

/**
 * 	Test spuštění a vyrendrování hlavních stránek
 */
public class PageTest
{
	private WicketTester tester;

	@Before
	public void setUp() {
		tester = new WicketTester(new WicketApplication());
	}

	@Test
	public void homePageRendersSuccessfully() {
		tester.startPage(HomePage.class);
		tester.assertRenderedPage(HomePage.class);
	}
	
	@Test
	public void graphicsCardsPagePageRenderSuccessfully() {
		tester.startPage(GraphicsCardsPage.class);
		tester.assertRenderedPage(GraphicsCardsPage.class);
	}
	
	@Test
	public void memoryPageRenderSuccessfully() {
		tester.startPage(MemoryPage.class);
		tester.assertRenderedPage(MemoryPage.class);
	}
	
	@Test
	public void motherboardsPageRenderSuccessfully() {
		tester.startPage(MotherboardsPage.class);
		tester.assertRenderedPage(MotherboardsPage.class);
	}
	
	@Test
	public void hardDisksPageRenderSuccessfully() {
		tester.startPage(HardDisksPage.class);
		tester.assertRenderedPage(HardDisksPage.class);
	}
	
	@Test
	public void powerSupplyUnitsPageRenderSuccessfully() {
		tester.startPage(PowerSupplyUnitsPage.class);
		tester.assertRenderedPage(PowerSupplyUnitsPage.class);
	}
	
	@Test
	public void loginAndRegistrationPageRenderSuccessfully() {
		tester.startPage(LoginAndRegistrationPage.class);
		tester.assertRenderedPage(LoginAndRegistrationPage.class);
	}
	
	@Test
	public void cartPageRenderSuccessfully() {
		tester.startPage(CartPage.class);
		tester.assertRenderedPage(CartPage.class);
	}
}
