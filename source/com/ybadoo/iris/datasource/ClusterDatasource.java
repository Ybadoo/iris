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

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ybadoo.iris.database.Database;
import com.ybadoo.iris.entity.Category;
import com.ybadoo.iris.entity.Ident;
import com.ybadoo.iris.entity.Level;
import com.ybadoo.iris.entity.MedCod;
import com.ybadoo.iris.exception.BackendException;

/**
 * Access to the cluster datasource, to handle:
 *
 * icd10code table
 * category table
 * level table
 * log table
 */
public class ClusterDatasource extends Datasource
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Identifier of the host inside the cluster
   */
  private final String host;

  /**
   * Constructor
   *
   * @param database the access to the application database
   * @param host the identifier of the host inside the cluster
   * @throws BackendException if a database access error occurs
   */
  public ClusterDatasource(final Database database, final String host) throws BackendException
  {
    super(database);

    if (StringUtils.isBlank(host) || host.length() > 2)
    {
      throw new BackendException("Key 'host.uid' is invalid");
    }

    this.host = host;
  }

  /**
   * Complement the UCCode field, with the help of the icd10code, category and level tables
   *
   * @param certificates the list of certificates
   * @throws BackendException if a database access error occurs
   */
  private void fillUCCode(final List<Ident> certificates) throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("SELECT a.name, a.garbage, a.category, a.level, a.note, b.name AS cName, b.note AS cNote, c.name AS lName, c.note AS lNote FROM " + database.getSchema() + ".icd10code a INNER JOIN " + database.getSchema() + ".category b ON a.category = b.uid INNER JOIN " + database.getSchema() + ".level c ON a.level = c.uid WHERE a.code = ?;"))
    {
      for (Ident ident : certificates)
      {
        if (ident.getUcCode() != null)
        {
          preparedStatement.clearParameters();

          preparedStatement.setString(1, ident.getUcCode().getCode());

          try (final var resultSet = preparedStatement.executeQuery())
          {
            if (resultSet.next())
            {
              final var ucCode = ident.getUcCode();

              ucCode.setName(resultSet.getString("name"));

              ucCode.setGarbage(resultSet.getInt("garbage"));

              ucCode.setNote(resultSet.getString("note"));

              if (resultSet.getInt("category") != 0)
              {
                ucCode.setCategory(new Category(resultSet.getInt("category"), resultSet.getString("cName"), resultSet.getString("cNote")));
              }

              if (resultSet.getInt("level") != 0)
              {
                ucCode.setLevel(new Level(resultSet.getInt("level"), resultSet.getString("lName"), resultSet.getString("lNote")));
              }
            }
          }
        }
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Process the certificates in the cluster, complementing the UCCode field and saving the certificates in the log table
   *
   * @param certificates the list of certificates
   * @throws BackendException if a database access error occurs
   */
  public void processCluster(final List<Ident> certificates) throws BackendException
  {
    fillUCCode(certificates);

    saveLog(certificates);
  }

  /**
   * Save the certificates in the log table
   *
   * @param certificates the list of certificates
   * @throws BackendException if a database access error occurs
   */
  private void saveLog(final List<Ident> certificates) throws BackendException
  {
    try (final var identStatement = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + ".logsIdent (lastChange, host, certificateKey, dateBirth, dateDeath, age, sex, mannerOfDeath, ucCode, mainInjury, status, reject, coding, codingVersion, codingFlags, selectedCodes, substitutedCodes, ernCodes, acmeCodes, multipleCodes, toDoList, autopsyRequested, autopsyUsed, recentSurgery, dateOfSurgery, dateOfInjury, placeOfOccurrence, activityCode, pregnancy, pregnancyContributeDeath, stillbirth, multiplePregnancy, completedWeeks, birthWeight, ageOfMother) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
         final var medCodStatement = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + ".logsMedCod (lastChange, host, certificateKey, lineNb, textLine, codeLine, intervalLine, codeOnly, lineCoded) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"))
    {
      for (Ident ident : certificates)
      {
        identStatement.clearParameters();

        identStatement.setTimestamp(1, ident.getLastChangeAsTimestamp());

        identStatement.setString(2, host);

        identStatement.setString(3, ident.getCertificateKey());

        identStatement.setTimestamp(4, ident.getDateBirthAsTimestamp());

        identStatement.setTimestamp(5, ident.getDateDeathAsTimestamp());

        identStatement.setString(6, ident.getAge());

        identStatement.setString(7, ident.getSex());

        identStatement.setObject(8, ident.getMannerOfDeath(), Types.TINYINT);

        identStatement.setString(9, ident.getUcCode() != null ? ident.getUcCode().getCode() : null);

        identStatement.setString(10, ident.getMainInjury());

        identStatement.setString(11, ident.getStatus());

        identStatement.setString(12, ident.getReject());

        identStatement.setString(13, ident.getCoding());

        identStatement.setString(14, ident.getCodingVersion());

        identStatement.setString(15, ident.getCodingFlags());

        identStatement.setString(16, ident.getSelectedCodes());

        identStatement.setString(17, ident.getSubstitutedCodes());

        identStatement.setString(18, ident.getErnCodes());

        identStatement.setString(19, ident.getAcmeCodes());

        identStatement.setString(20, ident.getMultipleCodes());

        identStatement.setString(21, ident.getToDoList());

        identStatement.setString(22, ident.getAutopsyRequested());

        identStatement.setString(23, ident.getAutopsyUsed());

        identStatement.setString(24, ident.getRecentSurgery());

        identStatement.setTimestamp(25, ident.getDateOfSurgeryAsTimestamp());

        identStatement.setTimestamp(26, ident.getDateOfInjuryAsTimestamp());

        identStatement.setString(27, ident.getPlaceOfOccurrence());

        identStatement.setString(28, ident.getActivityCode());

        identStatement.setString(29, ident.getPregnancy());

        identStatement.setString(30, ident.getPregnancyContributeDeath());

        identStatement.setString(31, ident.getStillbirth());

        identStatement.setString(32, ident.getMultiplePregnancy());

        identStatement.setString(33, ident.getCompletedWeeks());

        identStatement.setString(34, ident.getBirthWeight());

        identStatement.setString(35, ident.getAgeOfMother());

        identStatement.addBatch();

        for (MedCod medCod : ident.getMedCods())
        {
          medCodStatement.clearParameters();

          medCodStatement.setTimestamp(1, ident.getLastChangeAsTimestamp());

          medCodStatement.setString(2, host);

          medCodStatement.setString(3, ident.getCertificateKey());

          medCodStatement.setObject(4, medCod.getLineNb(), Types.TINYINT);

          medCodStatement.setString(5, medCod.getTextLine());

          medCodStatement.setString(6, medCod.getCodeLine());

          medCodStatement.setString(7, medCod.getIntervalLine());

          medCodStatement.setString(8, medCod.getCodeOnly());

          medCodStatement.setString(9, medCod.getLineCoded());

          medCodStatement.addBatch();
        }
      }

      identStatement.executeBatch();

      medCodStatement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.datasource.Datasource#validate()
   */
  @Override
  public void validate() throws BackendException
  {
    if (!database.existsTable("icd10code"))
    {
      throw new BackendException("Icd10code table not found");
    }

    if (!database.existsTable("category"))
    {
      throw new BackendException("Category table not found");
    }

    if (!database.existsTable("level"))
    {
      throw new BackendException("Level table not found");
    }

    try (final var statement = database.getConnection().createStatement())
    {
      statement.addBatch(database.logsIdentCreateScript());

      statement.addBatch(database.logsMedCodCreateScript());

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }
}
