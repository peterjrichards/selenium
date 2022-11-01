package com.liferay.sales.selenium.liferaycity;
import com.liferay.sales.selenium.ClickpathBase;

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class LiferayCityClickpath1 extends ClickpathBase {
	public LiferayCityClickpath1(WebDriver driver, JavascriptExecutor js, String baseUrl) {
		super(driver, js, baseUrl);
	}
	
	public void run(Map<String, String> cityUsers) {
		
		for (int i = 0; i < 100; i++) {
			for (Map.Entry<String, String> user : cityUsers.entrySet()) {
				doResize(1536, 835);
			  	deleteAllCookies();	
			    doGoTo(baseUrl);
			    doGoTo(baseUrl + "welcome?p_p_id=com_liferay_login_web_portlet_LoginPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_com_liferay_login_web_portlet_LoginPortlet_mvcRenderCommandName=%2Flogin%2Flogin&saveLastPath=false");
			    
			    sleep(2000);
			    
			    getLoginField("login").sendKeys(Keys.chord(Keys.CONTROL, "a"));;
			    getLoginField("login").sendKeys(Keys.DELETE);
			    getLoginField("login").sendKeys(user.getKey());
			    getLoginField("password").sendKeys(user.getValue()); 
			    getLoginField("login").sendKeys(Keys.ENTER);
			    
			    sleep(2000);	    
			    
			    doClickText("AKTUELLES");
			    String[] newsLinkText = new String[] {
			    		"Vorbereitung auf Karneval", 
			    		"Entschlossen, Technologie für Bürger*innen zu verbessern", 
			    		"LiferayCity öffnet eine neue Bibliothek", 
			    		"Mehr als 200 ältere Mitbürger*innen haben bereits an Seminaren zur Bedienung ihres Telefons teilgenommen"
			    };
				doClickRandomText(newsLinkText);
			    
			    sleep(4000);
			    
//			    driver.quit(); 
		  	}
		}
	}

	
}