/*
 * Copyright 2022 Chris Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tolstoy.proxytest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dizitart.jbus.JBus;
import org.scijava.util.ClassUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

public class ProxyTest {
	private static final Logger logger = LogManager.getLogger( ProxyTest.class );

	private static final boolean USE_PROXY = false;

	protected void runTest( String firefoxProfileName, String url ) throws Exception {
		WebDriverManager.firefoxdriver().setup();

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setLogLevel( FirefoxDriverLogLevel.INFO );
		firefoxOptions.addPreference( "toolkit.asyncshutdown.log", true );

		ProfilesIni profilesIni = new ProfilesIni();
		FirefoxProfile firefoxProfile = profilesIni.getProfile( firefoxProfileName );

		firefoxProfile.setPreference( "app.update.auto", false );
		firefoxProfile.setPreference( "app.update.enabled", false );
		firefoxProfile.setPreference( "browser.shell.checkDefaultBrowser", false );
		firefoxProfile.setPreference( "devtools.console.stdout.content", true );

		if ( USE_PROXY ) {
			BrowserMobProxy proxy = new BrowserMobProxyServer();
			proxy.start( 0 );

			firefoxOptions.setProxy( ClientUtil.createSeleniumProxy( proxy ) );
		}

		firefoxOptions.setProfile( firefoxProfile );

		FirefoxDriver driver = new FirefoxDriver( firefoxOptions );

		driver.get( url );

		logger.info( "URL = {}, title = {}", url, driver.getTitle() );

		Thread.sleep( 10000 );

		driver.quit();
	}

	public static void main(final String[] args) {
		try {
			ProxyTest proxyTest = new ProxyTest();

			//proxyTest.runTest( "twtr_censorship", "https://mail.yahoo.com" );
			proxyTest.runTest( "twtr_censorship", "https://twitter.com/ElonMusk" );

			logger.info( "main ending" );
		}
		catch ( final Exception e ) {
			logger.error( "Error", e );
			e.printStackTrace();
		}

		System.exit( -1 );
	}
}
