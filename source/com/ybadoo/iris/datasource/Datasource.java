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

package com.ybadoo.iris.datasource;

import java.io.Serializable;

import com.ybadoo.iris.database.Database;
import com.ybadoo.iris.exception.BackendException;

/**
 * Access to the application datasource
 */
public abstract class Datasource implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Access to the application database
   */
  protected final Database database;

  /**
   * Constructor
   *
   * @param database the access to the application database
   */
  protected Datasource(final Database database)
  {
    this.database = database;
  }

  /**
   * Close the database connection
   */
  public void close() throws BackendException
  {
    database.close();
  }

  /**
   * Validate the tables of the datasource
   */
  public abstract void validate() throws BackendException;
}
