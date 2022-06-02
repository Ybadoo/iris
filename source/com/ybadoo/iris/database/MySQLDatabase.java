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
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ybadoo.iris.exception.BackendException;

/**
 * Access to the MySQL database (host and cluster database)
 */
public class MySQLDatabase extends Database implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#getConnection()
   */
  @Override
  public synchronized Connection getConnection() throws BackendException
  {
    try
    {
      if (connection == null || connection.isClosed())
      {
        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mysql://" + address, username, password);
      }
    }
    catch (final ClassNotFoundException | SQLException exception)
    {
      throw new BackendException(exception);
    }

    return connection;
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#logsIdentCreateScript()
   */
  @Override
  public String logsIdentCreateScript()
  {
    final var sql = new StringBuilder();

    sql.append("CREATE TABLE IF NOT EXISTS ").append(schema).append(".logsIdent (")
         .append("lastChange DATETIME NOT NULL,")
         .append("host VARCHAR(2) NOT NULL,")
         .append("certificateKey VARCHAR(30) NOT NULL,")
         .append("dateBirth DATETIME,")
         .append("dateDeath DATETIME,")
         .append("age VARCHAR(20),")
         .append("sex VARCHAR(1),")
         .append("mannerOfDeath TINYINT(1) UNSIGNED,")
         .append("ucCode VARCHAR(5),")
         .append("mainInjury VARCHAR(5),")
         .append("status VARCHAR(10),")
         .append("reject VARCHAR(10),")
         .append("coding VARCHAR(10),")
         .append("codingVersion VARCHAR(255),")
         .append("codingFlags VARCHAR(50),")
         .append("selectedCodes VARCHAR(255),")
         .append("substitutedCodes VARCHAR(255),")
         .append("ernCodes VARCHAR(255),")
         .append("acmeCodes VARCHAR(255),")
         .append("multipleCodes VARCHAR(255),")
         .append("toDoList TEXT,")
         .append("autopsyRequested VARCHAR(1),")
         .append("autopsyUsed VARCHAR(1),")
         .append("recentSurgery VARCHAR(1),")
         .append("dateOfSurgery DATETIME,")
         .append("dateOfInjury DATETIME,")
         .append("placeOfOccurrence VARCHAR(1),")
         .append("activityCode VARCHAR(1),")
         .append("pregnancy VARCHAR(1),")
         .append("pregnancyContributeDeath VARCHAR(1),")
         .append("stillbirth VARCHAR(1),")
         .append("multiplePregnancy VARCHAR(1),")
         .append("completedWeeks VARCHAR(2),")
         .append("birthWeight VARCHAR(4),")
         .append("ageOfMother VARCHAR(2),")
         .append("PRIMARY KEY USING BTREE (lastChange, host, certificateKey)")
       .append(") ENGINE=InnoDB CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';");

    return sql.toString();
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#logsMedCodCreateScript()
   */
  @Override
  public String logsMedCodCreateScript()
  {
    final var sql = new StringBuilder();

    sql.append("CREATE TABLE IF NOT EXISTS ").append(schema).append(".logsMedCod (")
         .append("lastChange DATETIME NOT NULL,")
         .append("host VARCHAR(2) NOT NULL,")
         .append("certificateKey VARCHAR(30) NOT NULL,")
         .append("lineNb TINYINT(1) UNSIGNED NOT NULL,")
         .append("textLine TEXT,")
         .append("codeLine VARCHAR(255),")
         .append("intervalLine VARCHAR(255),")
         .append("codeOnly VARCHAR(1),")
         .append("lineCoded VARCHAR(1),")
         .append("PRIMARY KEY USING BTREE (lastChange, host, certificateKey, lineNb)")
       .append(") ENGINE=InnoDB CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';");

    return sql.toString();
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#lotIdentCreateScript(java.lang.String)
   */
  @Override
  public String lotIdentCreateScript(final String lot)
  {
    final var sql = new StringBuilder();

    sql.append("CREATE TABLE ").append(schema).append('.').append(lot).append("Ident (")
         .append("certificateKey VARCHAR(30) NOT NULL,")
         .append("lastChange DATETIME,")
         .append("dateBirth DATETIME,")
         .append("dateDeath DATETIME,")
         .append("age VARCHAR(20),")
         .append("sex VARCHAR(1),")
         .append("mannerOfDeath TINYINT(1) UNSIGNED,")
         .append("ucCode VARCHAR(5),")
         .append("mainInjury VARCHAR(5),")
         .append("status VARCHAR(10) DEFAULT 'Initial',")
         .append("reject VARCHAR(10) DEFAULT 'No',")
         .append("coding VARCHAR(10) DEFAULT 'Automatic',")
         .append("codingVersion VARCHAR(255),")
         .append("codingFlags VARCHAR(50),")
         .append("selectedCodes VARCHAR(255),")
         .append("substitutedCodes VARCHAR(255),")
         .append("ernCodes VARCHAR(255),")
         .append("acmeCodes VARCHAR(255),")
         .append("multipleCodes VARCHAR(255),")
         .append("comments TEXT,")
         .append("freeText TEXT,")
         .append("toDoList TEXT,")
         .append("coderReject VARCHAR(1),")
         .append("diagnosisModified VARCHAR(1),")
         .append("residence VARCHAR(50),")
         .append("name VARCHAR(50),")
         .append("address VARCHAR(50),")
         .append("autopsyRequested VARCHAR(1),")
         .append("autopsyUsed VARCHAR(1),")
         .append("recentSurgery VARCHAR(1),")
         .append("dateOfSurgery DATETIME,")
         .append("reasonSurgery TEXT,")
         .append("dateOfInjury DATETIME,")
         .append("placeOfOccurrence VARCHAR(1),")
         .append("activityCode VARCHAR(1),")
         .append("externalFreeText TEXT,")
         .append("pregnancy VARCHAR(1),")
         .append("pregnancyContributeDeath VARCHAR(1),")
         .append("stillbirth VARCHAR(1),")
         .append("multiplePregnancy VARCHAR(1),")
         .append("completedWeeks VARCHAR(2),")
         .append("birthWeight VARCHAR(4),")
         .append("ageOfMother VARCHAR(2),")
         .append("conditionsMother TEXT,")
         .append("certImage VARCHAR(255),")
         .append("PRIMARY KEY USING BTREE (certificateKey)")
       .append(") ENGINE=InnoDB CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';");

    return sql.toString();
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#lotMedCodCreateScript(java.lang.String)
   */
  @Override
  public String lotMedCodCreateScript(final String lot)
  {
    final var sql = new StringBuilder();

    sql.append("CREATE TABLE ").append(schema).append('.').append(lot).append("MedCod (")
         .append("certificateKey VARCHAR(30) NOT NULL,")
         .append("lineNb TINYINT(1) UNSIGNED NOT NULL,")
         .append("textLine TEXT,")
         .append("codeLine VARCHAR(255),")
         .append("intervalLine VARCHAR(255),")
         .append("codeOnly VARCHAR(1),")
         .append("lineCoded VARCHAR(1),")
         .append("PRIMARY KEY USING BTREE (certificateKey, lineNb)")
       .append(") ENGINE=InnoDB CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';");

    return sql.toString();
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.database.Database#managerCreateScript()
   */
  @Override
  public String managerCreateScript()
  {
    final var sql = new StringBuilder();

    sql.append("CREATE TABLE ").append(schema).append(".manager (")
         .append("uid VARCHAR(30) NOT NULL,")
         .append("owner VARCHAR(32) NOT NULL,")
         .append("created DATETIME NOT NULL,")
         .append("accessed DATETIME NOT NULL,")
         .append("lotType TINYINT(1) UNSIGNED NOT NULL,")
         .append("status TINYINT(1) UNSIGNED NOT NULL,")
         .append("PRIMARY KEY USING BTREE (uid, owner)")
       .append(") ENGINE=InnoDB CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';");

    return sql.toString();
  }
}
