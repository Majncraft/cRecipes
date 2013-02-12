package com.spaceemotion.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class UpdateReader {
	private String url;

	public UpdateReader( String url ) {
		this.url = url;
	}

	public UpdateMessage read() throws IOException, Exception {
		InputStream is = new URL( url ).openStream();

		try {
			InputStreamReader ir = new InputStreamReader( is, Charset.forName( "UTF-8" ) );
			BufferedReader rd = new BufferedReader( ir );

			String[] str = readAll( rd ).split( ";" );

			UpdateMessage msg = new UpdateMessage();

			for (String s : str) {
				String[] split = s.split( "=" );
				if (split.length < 2) throw new Exception( "Invalid Argument: " + s );

				String key = split[0], value = split[1];

				if (key.equalsIgnoreCase( "status" )) {
					msg.status = UpdateMessage.Status.valueOf( value.toUpperCase() );
				} else if (key.equalsIgnoreCase( "update" )) {
					msg.update = Boolean.parseBoolean( value );
				} else if (key.equalsIgnoreCase( "message" )) {
					msg.message = value;
				}
			}

			return msg;
		} finally {
			is.close();
		}
	}

	private String readAll( Reader rd ) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;

		while ((cp = rd.read()) != -1) {
			sb.append( (char) cp );
		}

		return sb.toString();
	}

	public static class UpdateMessage {
		public enum Status {
			SUCCESS, ERROR;
		}

		public Status status = Status.ERROR;
		public boolean update = false;
		public String message;
	}
}
