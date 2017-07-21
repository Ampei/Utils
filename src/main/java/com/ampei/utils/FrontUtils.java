package com.ampei.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FrontUtils {

	private static WebDriver driver;

	/**
	 * Waits for the completion of Ajax jQuery processing by checking "return
	 * jQuery.active == 0" condition.
	 * 
	 * @param timeOutInSeconds
	 *            - The time in seconds to wait until returning a failure
	 * @return {@link Boolean} true or false(condition fail, or if the timeout
	 *         is reached)
	 */
	public boolean waitForJQueryProcessing(int timeOutInSeconds) {
		boolean jQcondition = false;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			new WebDriverWait(driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driverObject) {
					return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
				}
			});
			jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return jQcondition;
		} catch (Exception e) {
		}
		return jQcondition;
	}

	/**
	 * Waits for the Condition of JavaScript.
	 * 
	 * @param timeOutInSeconds
	 * @param condition
	 * @return {@link Boolean} true or false(condition fail, or if the timeout
	 *         is reached)
	 */
	public boolean waitForJavaScriptCondition(int timeOutInSeconds, final String condition) {
		boolean jscondition = false;
		try {
			// nullify implicitlyWait()
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			new WebDriverWait(driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driverObject) {
					return (Boolean) ((JavascriptExecutor) driverObject).executeScript(condition);
				}
			});
			jscondition = (Boolean) ((JavascriptExecutor) driver).executeScript(condition);
			// reset implicitlyWait
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return jscondition;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Wait for the Text to not be present in the given element, regardless of
	 * being displayed or not.
	 * 
	 * @param element
	 *            element, which should contain the text
	 * @param String
	 *            The text we don't want to see
	 * @param int
	 *            The time in seconds to wait until returning a failure
	 * @return {@link Boolean}
	 */
	public boolean waitForTextNotPresent(final WebElement element, final String text, int timeOutInSeconds) {
		boolean isPresent = false;
		try {
			// nullify implicitlyWait()
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			new WebDriverWait(driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driverObject) {
					// is the Text in the DOM
					return !isTextPresent(driverObject, element, text);
				}
			});
			isPresent = isTextPresent(driver, element, text);
			// reset implicitlyWait
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return isPresent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if the text is present in the element.
	 * 
	 * @param driver
	 *            - The driver object to use to perform this element search
	 * @param element
	 *            - WebElement contain text
	 * @param text
	 *            - The Text element you are looking for
	 * @return {@link Boolean}
	 */
	public boolean isTextPresent(WebDriver driver, WebElement element, String text) {
		try {
			return element.getText().contains(text);
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * Do javascript click
	 * 
	 * @param element
	 */
	public void javaScriptClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	/**
	 * Scroll bottom of the Page
	 */
	public void scrollBottomOfThePage() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
	}

	/**
	 * Force onchange trigger
	 * 
	 * @param element
	 */
	public void forceOnChange(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].onchange();", element);
	}

	/**
	 * Scroll Top of Page
	 */
	public void scrollTopOfPage() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
	}

	/**
	 * Scroll to view
	 * 
	 * @param e
	 */
	public void scrollToView(WebElement e) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", e);
	}

	/**
	 * Is vertical scroll present
	 * 
	 * @return {@link Boolean}
	 */
	public Boolean isVerticalScrollPresent() {
		return (Boolean) ((JavascriptExecutor) driver)
				.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
	}

	/**
	 * Is element present by
	 * 
	 * @param by
	 * @return {@link Boolean}
	 */
	public Boolean isElementPresentById(By by) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElement(by).isDisplayed();
	}

	/**
	 * Get window dimension
	 * 
	 * @return {@link Dimension}
	 */
	public Dimension getWindowDimension() {
		return new Dimension(
				((Long) ((JavascriptExecutor) driver).executeScript("return $(window).width();")).intValue(),
				((Long) ((JavascriptExecutor) driver).executeScript("return $(window).height();")).intValue());
	}

	/**
	 * Get body dimension
	 * 
	 * @return {@link Dimension}
	 */
	public Dimension getBodyDimension() {
		return new Dimension(
				((Long) ((JavascriptExecutor) driver).executeScript("return $(window).width();")).intValue(),
				((Long) ((JavascriptExecutor) driver).executeScript("return $(window).height();")).intValue());
	}

	/**
	 * Is all webelement visible
	 * 
	 * @param w
	 * @return {@link Boolean}
	 */
	public Boolean isWebElementVisible(WebElement w) {
		Dimension weD = w.getSize();
		Point weP = w.getLocation();
		Dimension d = driver.manage().window().getSize();

		int x = d.getWidth();
		int y = d.getHeight();
		int x2 = weD.getWidth() + weP.getX();
		int y2 = weD.getHeight() + weP.getY();

		return x2 <= x && y2 <= y;
	}

	/**
	 * Is all element visible in android device
	 * 
	 * @param w
	 * @return
	 */
	public Boolean isWebElementVisibleAndroid(WebElement w) {
		Dimension weD = w.getSize();
		Point weP = w.getLocation();

		int x = ((Long) ((JavascriptExecutor) driver).executeScript("return $(window).width();")).intValue();
		int y = ((Long) ((JavascriptExecutor) driver).executeScript("return $(window).height();")).intValue();
		int x2 = weD.getWidth() + weP.getX();
		int y2 = weD.getHeight() + weP.getY();

		return x2 <= x && y2 <= y;
	}

	/**
	 * Get section visible
	 * 
	 * @return {@link WebElement}
	 */
	public static WebElement getSectionVisible() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(20, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NullPointerException.class).ignoring(NoSuchElementException.class);
		WebElement e = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return (WebElement) ((JavascriptExecutor) driver)
						.executeScript("return $('section:visible').get(0);");
			}
		});
		return e;
	}

	/**
	 * Get element dimensions
	 * 
	 * @param e
	 * @return {@link Dimension}
	 */
	public static Dimension getWebElementDimension(WebElement e) {
		return e.getSize();
	}

	/**
	 * Capture element bitmap
	 * 
	 * @param element
	 * @return {@link File}
	 * @throws Exception
	 */
	public static File captureElementBitmap(WebElement element) throws Exception {

		// Get the WrapsDriver of the WebElement
		WrapsDriver wrapsDriver = (WrapsDriver) element;

		// Get the entire Screenshot from the driver of passed WebElement
		File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);

		// Create an instance of Buffered Image from captured screenshot
		BufferedImage img = ImageIO.read(screen);

		// Get the Width and Height of the WebElement using getSize()
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();

		// Create a rectangle using Width and Height
		Rectangle rect = new Rectangle(width, height);

		// Get the Location of WebElement in a Point.
		// This will provide X & Y co-ordinates of the WebElement
		Point p = element.getLocation();

		// Create image by for element using its location and size.
		// This will give image data specific to the WebElement
		BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);

		// Write back the image data for element in File object
		ImageIO.write(dest, "png", screen);

		// Return the File object containing image data
		return screen;
	}

}
