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

package com.ybadoo.iris.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Error occurs during processing
 */
@XmlRootElement(name = "error")
@XmlAccessorType (XmlAccessType.FIELD)
public class Error implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Exception class occurred during processing
   */
  private Class<? extends Exception> exception;

  /**
   * Description of exception occurred during processing
   */
  private String message;

  /**
   * Default constructor
   */
  public Error()
  {
    // Required for the JAXBContext
  }

  /**
   * Constructor to fill the attributes
   *
   * @param exception the exception class occurred during processing
   * @param message the description of exception occurred during processing
   */
  public Error(final Class<? extends Exception> exception, final String message)
  {
    this.exception = exception;

    this.message = message;
  }

  /**
   * Get the exception class occurred during processing
   *
   * @return the exception class occurred during processing
   */
  public Class<? extends Exception> getException()
  {
    return exception;
  }

  /**
   * Get the description of exception occurred during processing
   *
   * @return the description of exception occurred during processing
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Set the exception class occurred during processing
   *
   * @param exception the exception class occurred during processing
   */
  public void setException(final Class<? extends Exception> exception)
  {
    this.exception = exception;
  }

  /**
   * Set the description of exception occurred during processing
   *
   * @param message the description of exception occurred during processing
   */
  public void setMessage(final String message)
  {
    this.message = message;
  }
}
