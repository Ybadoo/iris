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

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.ybadoo.iris.datasource.HostDatasource;
import com.ybadoo.iris.entity.Manager;
import com.ybadoo.iris.exception.BackendException;

/**
 * Execution of the Iris.exe
 */
public abstract class Engine implements Serializable
{
  /**
   * The log of the execution
   */
  protected static final Logger logger = Logger.getLogger(Engine.class.getName());

  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * The host datasource
   */
  protected final HostDatasource datasource;

  /**
   * The exception on the execution
   */
  protected Exception exception;

  /**
   * The Iris.exe executable address
   */
  private String executable;

  /**
   * The thread responsible of the execution
   */
  private transient Thread motor;

  /**
   * The timeout waiting for Iris.exe response, in seconds
   */
  private long timeout;

  /**
   * The Iris.exe executable runs on wine
   */
  private boolean wine;

  /**
   * Constructor
   *
   * @param datasource the host datasource
   */
  protected Engine(final HostDatasource datasource)
  {
    this.datasource = datasource;
  }

  /**
   * Get the thread responsible of the execution
   *
   * @return the thread responsible of the execution
   */
  private Thread createMotor()
  {
    return new Thread()
    {
      /* (non-Javadoc)
       * @see java.lang.Thread#run()
       */
      @Override
      public synchronized void run()
      {
        Manager manager = null;

        try
        {
          while (exception == null && (manager = datasource.nextCertificate()) != null)
          {
            final var lotName = preSynchronized(manager);

            final var proc = Runtime.getRuntime().exec(getCommandLine(lotName));

            final var errorGobbler = new StreamGobblerEngine(proc.getErrorStream(), "ERROR");

            final var outputGobbler = new StreamGobblerEngine(proc.getInputStream(), "OUTPUT");

            errorGobbler.start();

            outputGobbler.start();

            if (proc.waitFor(timeout, TimeUnit.SECONDS))
            {
              if (proc.exitValue() != 0)
              {
                exception = irisMessageError(proc.exitValue());
              }
            }
            else
            {
              exception = new TimeoutException("Iris not respond");
            }

            proc.destroy();

            if (exception == null)
            {
              posSynchronized(manager);
            }
          }
        }
        catch (final InterruptedException interruptedException)
        {
          logger.log(Level.SEVERE, interruptedException.getMessage(), interruptedException);

          exception = interruptedException;

          Thread.currentThread().interrupt();
        }
        catch (final Exception exceptions)
        {
          logger.log(Level.SEVERE, exceptions.getMessage(), exceptions);

          exception = exceptions;
        }
      }
    };
  }

  /**
   * Get the Iris.exe executable command line
   *
   * @param lotName the name of the lot
   * @return the Iris.exe executable command line
   */
  private String getCommandLine(final String lotName)
  {
    final var commandLine = new StringBuilder();

    if (wine)
    {
      commandLine.append("wine ");
    }

    commandLine.append(executable);

    if (lotName != null)
    {
      commandLine.append(" lname=").append(lotName);
    }

    return commandLine.toString();
  }

  /**
   * Get the exception on the execution
   *
   * @return the exception on the execution
   */
  public Exception getException()
  {
    return exception;
  }

  /**
   * Get the message error
   *
   * @param exitValue the identifier of the message error
   * @return the message error
   */
  private BackendException irisMessageError(final int exitValue)
  {
    switch (exitValue)
    {
      case  0: return null;
      case  2: return new BackendException("(2) No records coded");
      case  4: return new BackendException("(4) MUSE initialization error");
      case  5: return new BackendException("(5) Table database problem");
      case  6: return new BackendException("(6) Coding failure");
      case  7: return new BackendException("(7) Lot-specific error");
      case  8: return new BackendException("(8) Invalid paramater(s)");
      case  9: return new BackendException("(9) Technical failure or coding file is missing");
      default: return new BackendException("exitValue: " + exitValue);
    }
  }

  /**
   * Run post-processing on the lot
   *
   * @param manager the manager of the lot
   */
  protected abstract void posSynchronized(final Manager manager) throws BackendException;

  /**
   * Run pre-processing on the lot
   *
   * @param manager the manager of the lot
   * @return the lot name
   * @throws BackendException
   */
  protected abstract String preSynchronized(final Manager manager) throws BackendException;

  /**
   * Invocar a execucao da ferramenta IRIS
   */
  public synchronized void process()
  {
    if (motor == null || !motor.isAlive())
    {
      motor = createMotor();

      motor.start();
    }
  }

  /**
   * Configure the IRIS's executable address
   *
   * @param executable the IRIS's executable address
   * @throws BackendException encapsulation of the exceptions {@link java.lang.NullPointerException} and {@link java.lang.SecurityException}
   */
  public void setExecutable(final String executable) throws BackendException
  {
    if (StringUtils.isBlank(executable))
    {
      throw new BackendException("host.iris.executable is null or empty");
    }

    final var iris = new File(executable);

    try
    {
      if (!iris.exists())
      {
        throw new BackendException("File in host.iris.executable not found");
      }

      if (!iris.canExecute())
      {
        throw new BackendException("File in host.iris.executable without permission of execution");
      }
    }
    catch (final SecurityException securityException)
    {
      throw new BackendException("host.iris.executable is invalid", securityException);
    }

    this.executable = executable;
  }

  /**
   * Set the timeout waiting for Iris response, in seconds
   *
   * @param timeout timeout waiting for Iris response, in seconds
   * @throws BackendException
   */
  public void setTimeout(final String timeout) throws BackendException
  {
    if (StringUtils.isBlank(timeout))
    {
      throw new BackendException("host.iris.timeout is null or empty");
    }

    try
    {
      this.timeout = Long.parseLong(timeout);

      if (this.timeout < 1l)
      {
        throw new BackendException("host.iris.timeout is invalid");
      }
    }
    catch (final NumberFormatException numberFormatException)
    {
      throw new BackendException("host.iris.timeout is invalid", numberFormatException);
    }
  }

  /**
   * Configure if the IRIS's executable runs on wine
   *
   * @param wine the IRIS's executable runs on wine
   */
  public void setWine(final String wine)
  {
    this.wine = "true".equals(wine);
  }
}
