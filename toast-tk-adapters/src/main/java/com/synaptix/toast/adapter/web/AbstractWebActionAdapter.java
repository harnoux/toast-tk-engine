package com.synaptix.toast.adapter.web;

import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.VALUE_REGEX;
import static com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.WEB_COMPONENT;

import org.openqa.selenium.WebElement;

import com.google.inject.Inject;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.adapter.web.component.WebAutoElement;
import com.synaptix.toast.adapter.web.component.WebSelectElement;
import com.synaptix.toast.automation.driver.web.DriverFactory;
import com.synaptix.toast.automation.driver.web.SeleniumSynchronizedDriver;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.runtime.IActionItemRepository;

@ActionAdapter(name="default-web-driver", value= ActionAdapterKind.web)
public abstract class AbstractWebActionAdapter {

	private final SynchronizedDriver driver;
	private final IActionItemRepository repo;

	@Inject
	public AbstractWebActionAdapter(IActionItemRepository repository) {
		this.repo = repository;
		driver = new SeleniumSynchronizedDriver(DriverFactory.getFactory().getFirefoxDriver());
		for (IFeedableWebPage page : repository.getWebPages()) {
			((DefaultWebPage)page).setDriver(driver);
		}
	}

	@Action(id="navigate", action = "Open browser at "+ VALUE_REGEX, description = "")
	public TestResult openBrowserIn(String url) {
		if(!url.startsWith("http")){
			url = "http://" + url;
		}
		driver.getWebDriver().get(url);
		return new TestResult();
	}

	@Action(id="type_in_web_component", action = "Type " + VALUE_REGEX + " in " + WEB_COMPONENT, description = "")
	public TestResult typeIn(String text, String pageName, String widgetName) throws Exception {
		WebElement pageField = getPageField(pageName, widgetName);
		pageField.sendKeys(text);
		return new TestResult();
	}

	@Action(id="click_on_web_component", action = "Click on " + WEB_COMPONENT, description = "")
	public TestResult ClickOn(String pageName, String widgetName) throws Exception {
		WebElement pageField = getPageField(pageName, widgetName);
		pageField.click();
		return new TestResult();
	}

	@Action(id="select_in_web_component", action = "Select " + VALUE_REGEX + " in " + WEB_COMPONENT, description = "")
	public TestResult SelectAtPos(String pos, String pageName, String widgetName) throws Exception {
		WebAutoElement pageFieldAuto = getPageFieldAuto(pageName, widgetName);
		WebSelectElement pageField = (WebSelectElement) pageFieldAuto;
		pageField.selectByIndex(Integer.valueOf(pos));
		return new TestResult();
	}

	@Action(id="web_component_exists", action = WEB_COMPONENT + " exists", description = "")
	public TestResult checkExist(String pageName, String widgetName) {
		WebElement element = getPageField(pageName, widgetName);
		if (element != null) {
			if (element.isDisplayed()) {
				return new TestResult("Element is available !", ResultKind.SUCCESS);
			} else {
				return new TestResult("Element is not available !", ResultKind.FAILURE);
			}
		}
		return null;
	}
	
	@Action(id="close_browser", action = "Close browser", description = "")
	public TestResult closeBrowser() {
		driver.getWebDriver().quit();
		return new TestResult();
	}

	private WebAutoElement getPageFieldAuto(String pageName, String widgetName) {
		DefaultWebPage page = (DefaultWebPage) repo.getPage(pageName);
		WebAutoElement autoElement = page.getAutoElement(widgetName);
		return autoElement;
	}

	private WebElement getPageField(String pageName, String fieldName) {
		DefaultWebPage page = (DefaultWebPage) repo.getPage(pageName);
		WebAutoElement autoElement = page.getAutoElement(fieldName);
		return autoElement.getWebElement();
	}
}
