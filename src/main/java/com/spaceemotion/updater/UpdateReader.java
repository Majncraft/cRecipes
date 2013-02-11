package com.spaceemotion.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateReader {
	private String url;

	public UpdateReader( String url ) {
		this.url = url;
	}

	public JSONObject read() throws IOException, JSONException {
		InputStream is = new URL( url ).openStream();

		try {
			InputStreamReader ir = new InputStreamReader( is, Charset.forName( "UTF-8" ) );
			BufferedReader rd = new BufferedReader( ir );

			return new JSONObject( readAll( rd ) );
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
}
