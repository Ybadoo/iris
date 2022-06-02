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

package com.ybadoo.iris.exception;

/**
 * Problems occurred in the execution of the webservice, causing it to be stopped for analysis
 */
public class BackendException extends Exception
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a BackendException with the specified detail message
   *
   * @param message the detail message
   */
  public BackendException(final String message)
  {
    super(message);
  }

  /**
   * Constructs a BackendException with the specified detail message and cause
   *
   * @param message the detail message
   * @param cause the cause
   */
  public BackendException(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Constructs a BackendException with the specified cause
   *
   * @param cause the cause
   */
  public BackendException(final Throwable cause)
  {
    super(cause);
  }
}
