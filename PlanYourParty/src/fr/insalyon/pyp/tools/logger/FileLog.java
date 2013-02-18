package fr.insalyon.pyp.tools.logger;

import java.io.IOException;
import java.util.logging.Level;

import fr.insalyon.pyp.R;
import fr.insalyon.pyp.tools.PYPContext;
import fr.insalyon.pyp.tools.Constants;

import android.content.Context;



/**
 * @author A510944
 * Implemenation of AbstractLogger
 */
public class FileLog implements AbstractLogger {

	/* (non-Javadoc)
	 * @see bnpp.esafe.tools.logger.AbstractLogger#write(java.lang.String, java.util.logging.Level)
	 */
	public void write(String message, Level level) {
		try {
			FileLogger file = new FileLogger();
			file.open();
			file.setAppend(true);
			file.setFormatter(new SimpleFormatter());
			file.doLog(Constants.TAG, level, message);
			file.close();

		} catch (IOException e) {

			logStackTrace(e);
		}
	}

	public void logStackTrace(Exception e) {

		Context context = PYPContext.getContext();
		if (context.getString(R.string.prop_logging).equals("console")
				|| context.getString(R.string.prop_logging).equals("file")) {
			e.printStackTrace();
		}
	}

}
