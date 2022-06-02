/** Copyright (C) 2009/2022 - Cristiano Lehrer - ybadoo.com.br                  *
  *                                                                             *
  * This file is part of Vital Iris Web Service (IRIS)                          *
  *                                                                             *
  * IRIS is free software: you can redistribute it and/or modify                *
  * it under the terms of the GNU Lesser General Public License as published by *
  * the Free Software Foundation, either version 3 of the License, or           *
  * (at your option) any later version.                                         *
  *                                                                             *
  * IRIS is distributed in the hope that it will be useful,                     *
  * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                *
  * GNU Lesser General Public License for more details.                         *
  *                                                                             *
  * You should have received a copy of the GNU Lesser General Public License    *
  * along with IRIS. If not, see <http://www.gnu.org/licenses/>.                */

package com.ybadoo.iris.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsavel pela leitura da saida da execuvso da ferramenta IRIS no terminal
 */
public class StreamGobblerEngine extends Thread
{
  /**
   * Log do processamento do lote na ferramenta IRIS
   */
  private static final Logger logger = Logger.getLogger(StreamGobblerEngine.class.getName());

  /**
   * Fluxo de dados
   */
  private final InputStream inputStream;

  /**
   * Tipo do fluxo de dados
   */
  private final String type;

  /**
   * Inicializar a impressao da saida da execucao do IRIS
   *
   * @param inputStream fluxo de dados
   * @param type tipo do fluxo de dados
   */
  StreamGobblerEngine(final InputStream inputStream, final String type)
  {
    this.inputStream = inputStream;

    this.type = type;
  }

  /* (non-Javadoc)
   * @see java.lang.Thread#run()
   */
  @Override
  public void run()
  {
    try (final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
    {
      String line = null;

      while ((line = bufferedReader.readLine()) != null)
      {
        if (!line.startsWith("0"))
        {
          logger.log(Level.INFO, "{0} > {1}", new String[] {type, line});
        }
      }
    }
    catch (final Exception exception)
    {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
  }
}
