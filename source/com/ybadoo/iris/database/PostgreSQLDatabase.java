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
 * Access to the PostgreSQL database (host and cluster database)
 */
public class PostgreSQLDatabase extends Database implements Serializable
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
        connection = DriverManager.getConnection("jdbc:postgresql://" + address, username, password);
      }
    }
    catch (final SQLException exception)
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
         .append("lastChange TIMESTAMP NOT NULL,")
         .append("host CHARACTER VARYING(2) NOT NULL,")
         .append("certificateKey CHARACTER VARYING(30) NOT NULL,")
         .append("dateBirth TIMESTAMP,")
         .append("dateDeath TIMESTAMP,")
         .append("age CHARACTER VARYING(20),")
         .append("sex CHARACTER VARYING(1),")
         .append("mannerOfDeath SMALLINT,")
         .append("ucCode CHARACTER VARYING(5),")
         .append("mainInjury CHARACTER VARYING(5),")
         .append("status CHARACTER VARYING(10) DEFAULT 'Initial',")
         .append("reject CHARACTER VARYING(10) DEFAULT 'No',")
         .append("coding CHARACTER VARYING(10) DEFAULT 'Automatic',")
         .append("codingVersion CHARACTER VARYING(255),")
         .append("codingFlags CHARACTER VARYING(50),")
         .append("selectedCodes CHARACTER VARYING(255),")
         .append("substitutedCodes CHARACTER VARYING(255),")
         .append("ernCodes CHARACTER VARYING(255),")
         .append("acmeCodes CHARACTER VARYING(255),")
         .append("multipleCodes CHARACTER VARYING(255),")
         .append("toDoList TEXT,")
         .append("autopsyRequested CHARACTER VARYING(1),")
         .append("autopsyUsed CHARACTER VARYING(1),")
         .append("recentSurgery CHARACTER VARYING(1),")
         .append("dateOfSurgery TIMESTAMP,")
         .append("dateOfInjury TIMESTAMP,")
         .append("placeOfOccurrence CHARACTER VARYING(1),")
         .append("activityCode CHARACTER VARYING(1),")
         .append("pregnancy CHARACTER VARYING(1),")
         .append("pregnancyContributeDeath CHARACTER VARYING(1),")
         .append("stillbirth CHARACTER VARYING(1),")
         .append("multiplePregnancy CHARACTER VARYING(1),")
         .append("completedWeeks CHARACTER VARYING(2),")
         .append("birthWeight CHARACTER VARYING(4),")
         .append("ageOfMother CHARACTER VARYING(2),")
         .append("CONSTRAINT logsident_pkey PRIMARY KEY (lastchange, host, certificatekey)")
       .append(");");

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
         .append("lastChange TIMESTAMP NOT NULL,")
         .append("host CHARACTER VARYING(2) NOT NULL,")
         .append("certificateKey CHARACTER VARYING(30) NOT NULL,")
         .append("lineNb SMALLINT NOT NULL,")
         .append("textLine TEXT,")
         .append("codeLine CHARACTER VARYING(255),")
         .append("intervalLine CHARACTER VARYING(255),")
         .append("codeOnly CHARACTER VARYING(1),")
         .append("lineCoded CHARACTER VARYING(1),")
         .append("CONSTRAINT logsmedcod_pkey PRIMARY KEY (lastchange, host, certificatekey, linenb)")
       .append(");");

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
         .append("certificateKey CHARACTER VARYING(30) NOT NULL,")
         .append("lastChange TIMESTAMP,")
         .append("dateBirth TIMESTAMP,")
         .append("dateDeath TIMESTAMP,")
         .append("age CHARACTER VARYING(20),")
         .append("sex CHARACTER VARYING(1),")
         .append("mannerOfDeath SMALLINT,")
         .append("ucCode CHARACTER VARYING(5),")
         .append("mainInjury CHARACTER VARYING(5),")
         .append("status CHARACTER VARYING(10),")
         .append("reject CHARACTER VARYING(10),")
         .append("coding CHARACTER VARYING(10),")
         .append("codingVersion CHARACTER VARYING(255),")
         .append("codingFlags CHARACTER VARYING(50),")
         .append("selectedCodes CHARACTER VARYING(255),")
         .append("substitutedCodes CHARACTER VARYING(255),")
         .append("ernCodes CHARACTER VARYING(255),")
         .append("acmeCodes CHARACTER VARYING(255),")
         .append("multipleCodes CHARACTER VARYING(255),")
         .append("comments TEXT,")
         .append("freeText TEXT,")
         .append("toDoList TEXT,")
         .append("coderReject CHARACTER VARYING(1),")
         .append("diagnosisModified CHARACTER VARYING(1),")
         .append("residence CHARACTER VARYING(50),")
         .append("name CHARACTER VARYING(50),")
         .append("address CHARACTER VARYING(50),")
         .append("autopsyRequested CHARACTER VARYING(1),")
         .append("autopsyUsed CHARACTER VARYING(1),")
         .append("recentSurgery CHARACTER VARYING(1),")
         .append("dateOfSurgery TIMESTAMP,")
         .append("reasonSurgery TEXT,")
         .append("dateOfInjury TIMESTAMP,")
         .append("placeOfOccurrence CHARACTER VARYING(1),")
         .append("activityCode CHARACTER VARYING(1),")
         .append("externalFreeText TEXT,")
         .append("pregnancy CHARACTER VARYING(1),")
         .append("pregnancyContributeDeath CHARACTER VARYING(1),")
         .append("stillbirth CHARACTER VARYING(1),")
         .append("multiplePregnancy CHARACTER VARYING(1),")
         .append("completedWeeks CHARACTER VARYING(2),")
         .append("birthWeight CHARACTER VARYING(4),")
         .append("ageOfMother CHARACTER VARYING(2),")
         .append("conditionsMother TEXT,")
         .append("certImage CHARACTER VARYING(255),")
         .append("CONSTRAINT ").append(lot).append("Ident_pkey PRIMARY KEY (certificatekey)")
       .append(");");

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
         .append("certificateKey CHARACTER VARYING(30) NOT NULL,")
         .append("lineNb SMALLINT NOT NULL,")
         .append("textLine TEXT,")
         .append("codeLine CHARACTER VARYING(255),")
         .append("intervalLine CHARACTER VARYING(255),")
         .append("codeOnly CHARACTER VARYING(1),")
         .append("lineCoded CHARACTER VARYING(1),")
         .append("CONSTRAINT ").append(lot).append("MedCod_pkey PRIMARY KEY (certificatekey, lineNb)")
       .append(");");

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
         .append("uid CHARACTER VARYING(30) NOT NULL,")
         .append("owner CHARACTER VARYING(32) NOT NULL,")
         .append("created TIMESTAMP NOT NULL,")
         .append("accessed TIMESTAMP NOT NULL,")
         .append("lotType SMALLINT NOT NULL,")
         .append("status SMALLINT NOT NULL,")
         .append("CONSTRAINT manager_pkey PRIMARY KEY (uid, owner)")
       .append(");");

    return sql.toString();
  }
}
