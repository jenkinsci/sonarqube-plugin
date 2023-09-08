/*
 * SonarQube Scanner for Jenkins
 * Copyright (C) 2007-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package hudson.plugins.sonar.client;

import hudson.plugins.sonar.utils.Logger;
import okhttp3.OkHttpClient;
import org.sonarqube.ws.client.OkHttpClientBuilder;
import org.springframework.beans.InvalidPropertyException;

public class OkHttpClientSingleton {
  private static final long TIME_OUT;
  static {
    String timeoutSysPropValue = System.getProperty("plugin.sonar.http.timeout", "10000");
    try {
      long value = Long.parseLong(timeoutSysPropValue);
      TIME_OUT = value;
    } catch (NumberFormatException e) {
      String msg = "Unable to create Sonar HTTP client: Invalid value for timeout (system) property 'plugin.sonar.http.timeout': " + timeoutSysPropValue;
      Logger.LOG.severe(msg);
      throw new Error(msg, e);
    }
  }
  private static final OkHttpClient okHttpClient = new OkHttpClientBuilder().
          setConnectTimeoutMs(TIME_OUT).
          setReadTimeoutMs(TIME_OUT).
          setUserAgent("Scanner for Jenkins").build();

  private OkHttpClientSingleton() {
    // Nothing to do
  }

  public static OkHttpClient getInstance() {
    return okHttpClient;
  }
}
