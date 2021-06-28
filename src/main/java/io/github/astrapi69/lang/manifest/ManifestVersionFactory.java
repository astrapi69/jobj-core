/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.lang.manifest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import io.github.astrapi69.lang.ClassExtensions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Simple bean to hold information about the version of a Manifest. Can be used for JAR, EAR and WAR
 * manifest files.
 */
@UtilityClass
public class ManifestVersionFactory
{

	/**
	 * Returns a ManifestVersion object by reading the manifest file from the JAR, WAR or EAR file
	 * that contains the given class.
	 *
	 * @param clazz
	 *            the clazz
	 * @return the manifest version
	 */
	public static ManifestVersion get(final Class<?> clazz)
	{
		final String manifestUrl = ManifestVersionFactory.getManifestUrl(clazz);
		try
		{
			return of(manifestUrl != null ? new URL(manifestUrl) : null);
		}
		catch (final MalformedURLException ignore)
		{
			return of(null);
		}
	}

	/**
	 * Creates a new {@code ManifestVersion} object of the given url.
	 *
	 * @param url
	 *            the url
	 * @return the manifest version
	 */
	public static ManifestVersion of(final URL url)
	{
		final ManifestVersion version = new ManifestVersion();
		if (url != null)
		{
			URLConnection urlConnection = null;
			try
			{
				urlConnection = url.openConnection();
				version.setLastModified(LocalDateTime.ofInstant(
					Instant.ofEpochMilli(urlConnection.getLastModified()), ZoneId.systemDefault()));
				version.setManifest(new Manifest(urlConnection.getInputStream()));
				version
					.setTitle(version.getManifestAttribute(Attributes.Name.IMPLEMENTATION_TITLE));
				version.setVersion(
					version.getManifestAttribute(Attributes.Name.IMPLEMENTATION_VERSION));
			}
			catch (final IOException e)
			{
				throw new RuntimeException("Error while try to load manifest file from " + url, e);
			}
		}
		return version;
	}

	/**
	 * If the given class is in a JAR, WAR or EAR file than the manifest url as String is returned.
	 *
	 * @param clazz
	 *            The class.
	 * @return the manifest url as String if the given class is in a JAR, WAR or EAR file.
	 */
	public static String getManifestUrl(@NonNull Class<?> clazz) {
		if (clazz == null) {
			throw new NullPointerException("clazz is marked non-null but is null");
		} else {
			String manifestUrl = null;
			String path = ClassExtensions.getPath(clazz);
			URL classUrl = ClassExtensions.getResource(path);
			if (classUrl != null) {
				String classUrlString = classUrl.toString();
				if (classUrlString.startsWith("jar:") && classUrlString.indexOf(path) > 0 || classUrlString.startsWith("war:") && classUrlString.indexOf(path) > 0 || classUrlString.startsWith("ear:") && classUrlString.indexOf(path) > 0 || classUrlString.startsWith("file:") && classUrlString.indexOf(path) > 0) {
					manifestUrl = classUrlString.replace(path, "/META-INF/MANIFEST.MF");
				}
			}
			return manifestUrl;
		}
	}

}
