package net.catharos.recipes.util;

import java.util.regex.Pattern;

public class TextUtil {
	private static final Pattern COLOR_PATTERN = Pattern.compile( "&([0-9a-fk-or])" );

	public static String parseColors( String msg ) {
		if (msg == null) return null;

		return COLOR_PATTERN.matcher( msg ).replaceAll( "\u00a7$1" );
	}

	public static String parseArguments( String msg, Object... args ) {
		for (int i = 0; i < args.length; i++)
			msg = msg.replaceAll( "%" + i, args[i].toString() );

		return parseColors( msg );
	}
}
