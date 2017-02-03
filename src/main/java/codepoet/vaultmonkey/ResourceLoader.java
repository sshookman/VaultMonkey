package codepoet.vaultmonkey;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceLoader {

	private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class.getName());

	public File load(final String resource) {
		try {
			return new File(getClass().getClassLoader().getResource(resource).getFile());
		} catch (Exception exception) {
			LOGGER.log(Level.WARNING, "Failed to load resource: {0} : {1}", new String[]{resource, exception.getMessage()});
			return null;
		}
	}
}
