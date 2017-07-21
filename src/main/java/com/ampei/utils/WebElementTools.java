package com.ampei.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.HashMap;
import java.util.Map;

public class WebElementTools {

	/**
	 * Get element locator
	 * @param element
	 * @return {@link Map}
	 */
	static public HashMap<String, String> getWebElementLocator(WebElement element) {
		HashMap<String, String> locator = new HashMap<String, String>();
		Boolean webElementLocated = true;
		String[] componentParts = null;
		try {
			componentParts = element.toString().split("->");
		} catch (NoSuchElementException e) {
			webElementLocated = false;
			componentParts = e.getLocalizedMessage().split("\\{|\\}")[1].split("\"");
		}
		if (webElementLocated) {
			String part = componentParts[componentParts.length - 1];
			String[] parts = part.substring(0, part.length() - 1).split(":");
			locator.put("By", parts[0]);
			locator.put("Locator", parts[1]);
		} else {
			locator.put("By", componentParts[3]);
			locator.put("Locator", componentParts[7]);
		}

		return locator;
	}
	
	/**
	 * Set webelement attribute
	 * 
	 * @param element
	 * @param attributeName
	 * @param value
	 */
	public static void setAttribute(WebElement element, String attributeName, String value) {
		WrapsDriver wrappedElement = (WrapsDriver) element;

		JavascriptExecutor driver = (JavascriptExecutor) wrappedElement.getWrappedDriver();
		driver.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, attributeName, value);
	}
	
	/**
	 * Highlight element
	 * 
	 * @param element
	 */
	public static void highlightElement(WebElement element) {
		for (int i = 0; i < 5; i++) {
			WrapsDriver wrappedElement = (WrapsDriver) element;
			JavascriptExecutor driver = (JavascriptExecutor) wrappedElement.getWrappedDriver();
			driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: green; border: 2px solid green;");
			driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	}

}