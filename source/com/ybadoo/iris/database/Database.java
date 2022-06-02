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

package com.ybadoo.iris.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.ybadoo.iris.exception.BackendException;

/**
 * Access to the application database (host and cluster database)
 */
public abstract class Database implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Database address URL
   */
  protected String address;

  /**
   * Connection (session) with a specific database
   */
  protected transient Connection connection;

  /**
   * Database password
   */
  protected String password;

  /**
   * Database schema
   */
  protected String schema;

  /**
   * Database username
   */
  protected String username;

  /**
   * Build the error message
   *
   * @param key the key in web.xml
   * @return the error message
   */
  private String buildErrorMessage(final String key)
  {
    return "Key '" + key + "' is null or empty.";
  }

  /**
   * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released
   *
   * @throws BackendException if a database access error occurs
   */
  public synchronized void close() throws BackendException
  {
    try
    {
      if (connection != null && !connection.isClosed())
      {
        connection.close();
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Tests whether the table exists in the database
   *
   * @param tableName the table name
   * @throws BackendException if a database access error occurs
   */
  public boolean existsTable(final String tableName) throws BackendException
  {
    try (final var preparedStatement = getConnection().prepareStatement("SELECT count(table_name) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?"))
    {
      preparedStatement.setString(1, schema);

      preparedStatement.setString(2, tableName);

      try (final var resultSet = preparedStatement.executeQuery())
      {
        return (resultSet.next() && resultSet.getInt(1) == 1);
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Get the connection (session) with a specific database
   *
   * @return the connection (session) with a specific database
   * @throws BackendException if a database access error occurs
   */
  public abstract Connection getConnection() throws BackendException;

  /**
   * Get the database schema
   *
   * @return the database schema
   */
  public String getSchema()
  {
    return schema;
  }

  /**
   * Get the SQL script to create the logs ident table (cluster database)
   *
   * @return the SQL script to create the logs ident table (cluster database)
   */
  public abstract String logsIdentCreateScript();

  /**
   * Get the SQL script to create the logs medCod table (cluster database)
   *
   * @return the SQL script to create the logs medCod table (cluster database)
   */
  public abstract String logsMedCodCreateScript();

  /**
   * Get the SQL script to create the lot ident table (certificate database)
   *
   * @param lot the lot name
   * @return the SQL script to create the lot ident table (certificate database)
   */
  public abstract String lotIdentCreateScript(final String lot);

  /**
   * Get the SQL script to create the lot medCod table (certificate database)
   *
   * @param lot the lot name
   * @return the SQL script to create the lot medCod table (certificate database)
   */
  public abstract String lotMedCodCreateScript(final String lot);

  /**
   * Get the SQL script to create the manager table (certificate database)
   *
   * @return the SQL script to create the manager table (certificate database)
   */
  public abstract String managerCreateScript();

  /**
   * Set the database address URL
   *
   * @param address the database address URL
   * @param key the key in web.xml
   * @throws BackendException if a database access error occurs
   */
  public void setAddress(final String address, final String key) throws BackendException
  {
    if (StringUtils.isBlank(address))
    {
      throw new BackendException(buildErrorMessage(key));
    }

    this.address = address;
  }

  /**
   * Set the database password
   *
   * @param password the database password
   * @param key the key in web.xml
   * @throws BackendException if a database access error occurs
   */
  public void setPassword(final String password, final String key) throws BackendException
  {
    if (StringUtils.isBlank(password))
    {
      throw new BackendException(buildErrorMessage(key));
    }

    this.password = password;
  }

  /**
   * Set the database schema
   *
   * @param schema the database schema
   * @param key the key in web.xml
   * @throws BackendException if a database access error occurs
   */
  public void setSchema(final String schema, final String key) throws BackendException
  {
    if (StringUtils.isBlank(schema))
    {
      throw new BackendException(buildErrorMessage(key));
    }

    this.schema = schema;
  }

  /**
   * Set the database username
   *
   * @param username the database username
   * @param key the key in web.xml
   * @throws BackendException if a database access error occurs
   */
  public void setUsername(final String username, final String key) throws BackendException
  {
    if (StringUtils.isBlank(username))
    {
      throw new BackendException(buildErrorMessage(key));
    }

    this.username = username;
  }
}
