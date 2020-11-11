import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;

public class TestMultiLogin {
	public AndroidDriver driver;
	
  @Test
  public void multiLoginFromExcelFile() throws InterruptedException, IOException {
	  	driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
	  	Thread.sleep(3000);
	  	driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
	    Thread.sleep(5000);

		File file= new File(".\\src\\main\\resources\\TestData.xlsx");
		FileInputStream fis=new FileInputStream(file);
		XSSFWorkbook wb= new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		int rc= sheet.getLastRowNum();
		System.out.println("total number of rows having data= "+rc);
		for(int i=1;i<=rc;i++) {
			String username=sheet.getRow(i).getCell(0).getStringCellValue();
			String password=sheet.getRow(i).getCell(1).getStringCellValue();
		
		    driver.findElementByAccessibilityId("Enter an e-mail address or username").sendKeys(username);
		    driver.findElementByAccessibilityId("Password").sendKeys(password);
		    driver.findElementByAccessibilityId("Sign in").click();
		    Thread.sleep(10000);
		    //Taking Screenshots
		    File ScreenShot  = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		    FileUtils.copyFile(ScreenShot, new File(".\\test-output\\Screenshots\\Screenshot"+i+".jpg"));
	        
		    //assertion
		    try {
	            String error =driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"Invalid password\")")).getText();
	            System.out.println(i+" - Invalid Login- error message on the screen: "+error);
	            }
	        catch(NoSuchElementException e) {
	            String succes=driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Need to add a class?\")")).getText();
	            System.out.println(i+" - successful login - "+succes);
	            driver.findElement(MobileBy.className("android.widget.ImageView")).click();
	            Thread.sleep(2000);
	    	    driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign out\")")).click();
	    	    driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
	    	    Thread.sleep(3000);
	    	    driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Sign in\")")).click();
	    	    Thread.sleep(5000);
	            
	            }

		}
  }
  
  
  @BeforeClass
  public void beforeClass() throws MalformedURLException, InterruptedException {
	  DesiredCapabilities capability= new DesiredCapabilities();
	    capability.setCapability("deviceName", "Dksingh");
	    capability.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
	    //capability.setCapability(MobileCapabilityType.NO_RESET, true);
	    capability.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "org.khanacademy.android");
	    capability.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "org.khanacademy.android.ui.library.MainActivity");
	    driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"),capability);
	    //driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	    Thread.sleep(15000);
	    driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Dismiss\")")).click();

  }

  
  @AfterClass
  public void afterClass() throws InterruptedException {
      Thread.sleep(10000);
      driver.pressKey(new KeyEvent(AndroidKey.HOME));
  }

}
