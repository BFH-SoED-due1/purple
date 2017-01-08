/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Sendet EMails via den Server von mir
 * @author Lukas
 *
 */
public class Email {

	private String to, subject, message;
	private HttpsURLConnection con;
	private TrustManager[] trustAllCerts;
	private SSLContext sc;
	private URL site;

	/**
	 * Konstruktor der Email Klasse. Es werden alle Parameter direkt hier übergeben
	 * @param to Empfänger
	 * @param subject Betreff
	 * @param message Nachricht
	 */
	public Email(String to, String subject, String message) {
		this.to = to;
		this.subject = subject;
		this.message = message;

		//Trust all Certs, so we can use our own certified ssl certificate -> more security
		trustAllCerts = new TrustManager[] { new X509TrustManager() {
		      @Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		        return null;
		      }

		      @Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
		      }

		      @Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
		      }
		    }
		};
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		HostnameVerifier allHostsValid = new HostnameVerifier() {
	        @Override
			public boolean verify(String hostname, SSLSession session) {
	          return true;
	        }
	    };
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    // Install the all-trusting host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		try {
			//site = new URL(Resources.getSystem().getString(R.string.php_url));
			this.site = new URL("https://sds-ranking.ch/bfh/reservation.php");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Liefert den Empfänger zurück
	 * @return Den Empfänger als String
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Setzt den Empfänger des Mails
	 * @param to Der Empfänger
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Liefert den Betreff der Mail
	 * @return Der Betreff
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Setzt den Betreff der Mail
	 * @param subject Der Betreff
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gibt die zu versendende Email zurück
	 * @return Die Email
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setzt den Text des Mails (kann auch HTML sein!)
	 * @param message Der Inhalt des Mails (auch in HTML)
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sendet das E-Mail
	 * @return true wenn gesendet, false sonst
	 */
	public boolean send()
	{
		String data, response = "false";
		data = "to=" + to + "&subject=" + subject + "&message=" + message;
		try
		{
			if(con == null)
			{
				con = (HttpsURLConnection) site.openConnection();
				con.setRequestMethod("POST");
				con.setDoOutput(true);
			}
			OutputStream out = con.getOutputStream();
			data += "&key=TeamPurple";
			out.write(data.getBytes());
			out.flush();
			out.close();
			response = read();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if(response.equals("true"))
			return true;
		return false;
	}

	private String read()
	{
		try
		{
			if (con == null)
				con = (HttpsURLConnection) site.openConnection();
			con.setReadTimeout(2000);
			@SuppressWarnings("resource")
			Scanner s = new Scanner(con.getInputStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
		catch (IOException e) {
			System.out.println("IOE Server");
			return "IOE";
		}
	}
}
