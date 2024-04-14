package ImplementationBase;

import java.io.FileInputStream;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.io.File;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Design.ExcelSpecificActionsForWeb;
import Design.WebSpecificActions;


public class WebImplementationBase implements ExcelSpecificActionsForWeb, WebSpecificActions {

	public RemoteWebDriver driver;
	public WebDriverWait wait;

	public static HashMap<String, String> locator;


	@Override
	public void readLocator(String locatorfilepath, String sheetname) throws IOException {
		locator = new HashMap<>();
		XSSFWorkbook workbook = new XSSFWorkbook(locatorfilepath);
		XSSFSheet sheet = workbook.getSheet(sheetname);
		int rowcount = sheet.getLastRowNum();

		for (int i = 1; i <= rowcount; i++) {
			String stringCellValue = sheet.getRow(i).getCell(2).getStringCellValue();
			String stringCellValue2 = sheet.getRow(i).getCell(3).getStringCellValue();
			locator.put(stringCellValue, stringCellValue2);
			workbook.close();
		}
	}

	@Override
	public String testdataload(String sheetname, String testscriptID, String testdataname) throws IOException {

			HashMap<String, Integer> testdataID = new HashMap<>();
			String key;
			int value;
			String filePath= new File(getConfigurations("testdatapath")).getAbsolutePath();
			XSSFWorkbook workbook = new XSSFWorkbook(filePath);
			System.out.println(getConfigurations("testdatapath"));
			XSSFSheet sheet = workbook.getSheet(sheetname);
			int lastCellNum = sheet.getLastRowNum();
			for (int i = 1; i <= lastCellNum; i++) {
				XSSFCell cell = sheet.getRow(i).getCell(0);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}

				value = i;
				testdataID.put(key, value);

			}

			HashMap<String, Integer> testdatatitle = new HashMap<>();
			int column = sheet.getRow(0).getLastCellNum();
			for (int i = 1; i < column; i++) {
				XSSFCell cell = sheet.getRow(0).getCell(i);
				try {
					switch (cell.getCellType()) {
					case NUMERIC:
						double temp = cell.getNumericCellValue();
						long val = (long) temp;
						key = String.valueOf(val);
						if (DateUtil.isCellDateFormatted(cell)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							Date date = cell.getDateCellValue();
							key = df.format(date);
						}
						break;
					case STRING:
						key = cell.getStringCellValue();
						break;
					case BOOLEAN:
						key = String.valueOf(cell.getBooleanCellValue());
						break;
					case FORMULA:
						key = cell.getCellFormula();
					default:
						key = "DEFAULT";
					}
				} catch (NullPointerException npe) {
					key = " ";

				}
				value = i;
				testdatatitle.put(key, value);

			}

			String expected_value = sheet.getRow(testdataID.get(testscriptID)).getCell(testdatatitle.get(testdataname))
					.getStringCellValue();
			return expected_value;
		}
	


	@Override
	public WebElement locateElement(String locator) {
		String[] split = locator.split("=", 2);
		String locatortype = split[0];
		String lvalue = split[1];
		switch (locatortype.toLowerCase()) {
		case "id":
			return driver.findElement(By.id(lvalue));
		case "xpath":
			return driver.findElement(By.xpath(lvalue));

		case "name":
			return driver.findElement(By.name(lvalue));
		}
		return null;

	}

	@Override
	public boolean elementIsDisplayed(String locator) {
		return locateElement(locator).isDisplayed();

	}

	@Override
	public boolean verifyElementtext(String locator, String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.equals(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public boolean verifyElementContainsText(String locator,String expected) {
		boolean text = false;
		String name = locateElement(locator).getText();
		if (name.contains(expected)) {
			text = true;

		} else {
			text = false;
		}
		return text;
	}

	@Override
	public void clickElement(String locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();

	}

	@Override
	public void EnterInput(String locator, String text) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		WebElement ele = locateElement(locator);
		ele.clear();
		ele.sendKeys(text);

	}

	@Override
	public String getElementText(String locator) {

		String text = locateElement(locator).getText();
		return text;
	}

	@Override
	public Boolean isEnabled(String locator) {
		return locateElement(locator).isDisplayed();
	}

	@Override
	public Boolean isSelected(String locator) {
		return locateElement(locator).isDisplayed();
	}

	@Override
	public void clearInput(String locator) {
		locateElement(locator).clear();

	}

	@Override
	public boolean isDisplayed(String locator) {
		boolean displayed = locateElement(locator).isDisplayed();
		return displayed;

	}

	@Override
	public void selectDropDownByVisibleText(String locator, String visibletext) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByVisibleText(visibletext);

	}

	@Override
	public void selectDropDownByValue(String locator, String value) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByValue(value);

	}

	@Override
	public void selectDropdownByindex(String locator, int index) {
		WebElement ele = locateElement(locator);
		Select drp = new Select(ele);
		drp.selectByIndex(index);

	}

	@Override
	public String getElementColour(String locator) {
		WebElement ele = locateElement(locator);
		String cssValue = ele.getCssValue("color");
		return cssValue;

	}

	@Override
	public void switchToFrameUsingIndex(int index) {
		driver.switchTo().frame(index);

	}

	@Override
	public void switchToFrameUsingLocator(String locator) {
		WebElement ele = locateElement(locator);
		driver.switchTo().frame(ele);

	}

	@Override
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();

	}

	@Override
	public void switchToWindowUsingIndex(int index) {
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		driver.switchTo().window(Handles.get(index));
	}

	@Override
	public void switchToWindowUsingTitle(String title) {
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> Handles = new ArrayList<>(windowHandles);
		for (String string : Handles) {
			if (string.equals(title.toLowerCase())) {
				driver.switchTo().window(string);
			}
		}
	}

	@Override
	public String getCurrentWindowTitle(int index) {
		return driver.getTitle();

	}

	@Override
	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
	}

	@Override
	public void dismissAlert() {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	@Override
	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		String text = alert.getText();
		return text;

	}

	@Override
	public void doubleClick(String locator) {
		WebElement ele = locateElement(locator);
		Actions act = new Actions(driver);
		act.doubleClick(ele).perform();

	}

	@Override
	public void dragAndDrop(String locator, String locator2) {
		WebElement source = locateElement(locator);
		WebElement target = locateElement(locator2);
		Actions act = new Actions(driver);
		act.dragAndDrop(source, target).perform();

	}

	@Override
	public void sendAlertText(String alerttext) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(alerttext);
	}

	@Override
	public void waitUntilInvisibilityOfElement(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsClickable(int waitime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeClickable(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementIsDisplayed(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilElementToBeSelected(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.elementToBeSelected(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilPresenceofElementlocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilVisisblityOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(lvalue)));
		}

	}

	@Override
	public void waitUntilInvisibliltyOfAllElementsLocated(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(lvalue)));
		}
	}

	@Override
	public void waituntilFrameIsVisibleAndSwitchToIt(int waittime, String locator) {
		String[] split = locator.split("=",2);
		String locatortype = split[0];
		String lvalue = split[1];
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		if (locatortype.toLowerCase().contains("xpath")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(lvalue)));
		}
		if (locatortype.toLowerCase().contains("name")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name(lvalue)));
		}
		if (locatortype.toLowerCase().contains("id")) {
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(lvalue)));
		}

	}

	@Override
	public void LaunchBrowser(String browser) {
		switch (browser) {
		case "chrome":
			

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");

			driver = new ChromeDriver(options);
			break;

		case "edge":
			
			EdgeOptions options1 = new EdgeOptions();
			options1.addArguments("--remote-allow-origins=*");

			driver = new EdgeDriver(options1);
			break;

		case "firefox":
			
			driver = new FirefoxDriver();
			break;

		case "safari":
			
			driver = new SafariDriver();
			break;

		default:
			throw new RuntimeException();

		}
	}

	@Override
	public String getConfigurations(String key) {

		String value = null;
		try {
			FileInputStream file = new FileInputStream("./src/main/resources/local repositories/config.properties");
			Properties properties = new Properties();
			try {
				properties.load(file);
				value = properties.getProperty(key);

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return value;
	}
	
	
	public String getLocators(String module,String locator) {
		String value = null;
		try {
			FileInputStream file = new FileInputStream("./src/main/resources/locators repositories/"+module+".properties");
			Properties properties = new Properties();
			try {
				properties.load(file);
				value = properties.getProperty(locator);

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return value;
		
	}

	@Override
	public void clickElement(String locator, int waittime) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).click();

	}

	@Override
	public void EnterInput(String locator, String text, int waittime) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
		wait.until(ExpectedConditions.elementToBeClickable(locateElement(locator)));
		locateElement(locator).sendKeys(text);
	}
	@Override
	public String readAndDecryptexcel(String encryptedValue) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
		return new String(decodedBytes);
	}
	@Override
	public void refreshBrowser() {
		try {
			driver.navigate().refresh();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// Method to perform Copy (Ctrl+C) keyboard shortcut
    public void copyText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "c")).perform();
    }
    // Method to perform Paste (Ctrl+V) keyboard shortcut
    public void pasteText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "v")).perform();
    }

    // Method to perform Cut (Ctrl+X) keyboard shortcut
    public void cutText(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "x")).perform();
    }

    // Method to perform Undo (Ctrl+Z) keyboard shortcut
    public void undoAction() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.chord(Keys.CONTROL, "z")).perform();
    }

    // Method to perform Redo (Ctrl+Y) keyboard shortcut
    public void redoAction() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.chord(Keys.CONTROL, "y")).perform();
    }

    // Method to perform Bold (Ctrl+B) keyboard shortcut
    public void makeTextBold(String element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(locateElement(element)).sendKeys(Keys.chord(Keys.CONTROL, "b")).perform();
    }

}
