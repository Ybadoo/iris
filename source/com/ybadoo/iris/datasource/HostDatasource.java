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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.ybadoo.iris.constant.LotType;
import com.ybadoo.iris.constant.ProcessingStatus;
import com.ybadoo.iris.database.Database;
import com.ybadoo.iris.entity.Ident;
import com.ybadoo.iris.entity.Iris;
import com.ybadoo.iris.entity.Manager;
import com.ybadoo.iris.entity.MedCod;
import com.ybadoo.iris.entity.Recover;
import com.ybadoo.iris.entity.UCCode;
import com.ybadoo.iris.exception.BackendException;
import com.ybadoo.iris.exception.FrontendException;

/**
 * Access to the host datasource, to handle:
 *
 * manager table
 * unique tables (uniqueIdent and uniqueMedCod)
 * xxxIdent tables
 * xxxMedCod tables
 */
public class HostDatasource extends Datasource
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Name of the table responsible for storing unique certificates
   */
  private static final String UNIQUE_TABLES = "unique";

  /**
   * Formatter for printing and parsing date-time objects
   */
  private final transient DateTimeFormatter dateTimeFormatter;

  /**
   * Constructor
   *
   * @param database the access to the application database
   * @param dateTimeFormatter the formatter for printing and parsing date-time objects
   */
  public HostDatasource(final Database database, final DateTimeFormatter dateTimeFormatter)
  {
    super(database);

    this.dateTimeFormatter = dateTimeFormatter;
  }

  /**
   * Set the list of certificates processed by Access database
   *
   * @param manager the lot's manager
   * @param certificates the list of certificates
   * @throws BackendException if a database access error occurs
   */
  public void accessToCertificates(final Manager manager, final List<Ident> certificates) throws BackendException
  {
    var table = UNIQUE_TABLES;

    if (manager.getLotType() == LotType.MULTIPLE)
    {
      table = manager.getUid();
    }

    try (final var preparedStatementIdent = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + "." + table + "Ident (certificateKey, lastChange, dateBirth, dateDeath, age, sex, mannerOfDeath, ucCode, mainInjury, status, reject, coding, codingVersion, codingFlags, selectedCodes, substitutedCodes, ernCodes, acmeCodes, multipleCodes, toDoList, autopsyRequested, autopsyUsed, recentSurgery, dateOfSurgery, dateOfInjury, placeOfOccurrence, activityCode, pregnancy, pregnancyContributeDeath, stillbirth, multiplePregnancy, completedWeeks, birthWeight, ageOfMother) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
         final var preparedStatementMedCod = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + "." + table + "MedCod (certificateKey, lineNb, textLine, codeLine, intervalLine, codeOnly, lineCoded) VALUES (?, ?, ?, ?, ?, ?, ?);"))
    {
      for (Ident ident : certificates)
      {
        preparedStatementIdent.clearParameters();

        preparedStatementIdent.setString(1, ident.getCertificateKey());

        preparedStatementIdent.setTimestamp(2, ident.getLastChangeAsTimestamp());

        preparedStatementIdent.setTimestamp(3, ident.getDateBirthAsTimestamp());

        preparedStatementIdent.setTimestamp(4, ident.getDateDeathAsTimestamp());

        preparedStatementIdent.setString(5, ident.getAge());

        preparedStatementIdent.setString(6, ident.getSex());

        preparedStatementIdent.setObject(7, ident.getMannerOfDeath(), Types.TINYINT);

        preparedStatementIdent.setString(8, ident.getUcCode().getCode());

        preparedStatementIdent.setString(9, ident.getMainInjury());

        preparedStatementIdent.setString(10, ident.getStatus());

        preparedStatementIdent.setString(11, ident.getReject());

        preparedStatementIdent.setString(12, ident.getCoding());

        preparedStatementIdent.setString(13, ident.getCodingVersion());

        preparedStatementIdent.setString(14, ident.getCodingFlags());

        preparedStatementIdent.setString(15, ident.getSelectedCodes());

        preparedStatementIdent.setString(16, ident.getSubstitutedCodes());

        preparedStatementIdent.setString(17, ident.getErnCodes());

        preparedStatementIdent.setString(18, ident.getAcmeCodes());

        preparedStatementIdent.setString(19, ident.getMultipleCodes());

        preparedStatementIdent.setString(20, ident.getToDoList());

        preparedStatementIdent.setString(21, ident.getAutopsyRequested());

        preparedStatementIdent.setString(22, ident.getAutopsyUsed());

        preparedStatementIdent.setString(23, ident.getRecentSurgery());

        preparedStatementIdent.setTimestamp(24, ident.getDateOfSurgeryAsTimestamp());

        preparedStatementIdent.setTimestamp(25, ident.getDateOfInjuryAsTimestamp());

        preparedStatementIdent.setString(26, ident.getPlaceOfOccurrence());

        preparedStatementIdent.setString(27, ident.getActivityCode());

        preparedStatementIdent.setString(28, ident.getPregnancy());

        preparedStatementIdent.setString(29, ident.getPregnancyContributeDeath());

        preparedStatementIdent.setString(30, ident.getStillbirth());

        preparedStatementIdent.setString(31, ident.getMultiplePregnancy());

        preparedStatementIdent.setString(32, ident.getCompletedWeeks());

        preparedStatementIdent.setString(33, ident.getBirthWeight());

        preparedStatementIdent.setString(34, ident.getAgeOfMother());

        preparedStatementIdent.addBatch();

        for (MedCod medCod : ident.getMedCods())
        {
          preparedStatementMedCod.clearParameters();

          preparedStatementMedCod.setString(1, medCod.getCertificateKey());

          preparedStatementMedCod.setObject(2, medCod.getLineNb(), Types.TINYINT);

          preparedStatementMedCod.setString(3, medCod.getTextLine());

          preparedStatementMedCod.setString(4, medCod.getCodeLine());

          preparedStatementMedCod.setString(5, medCod.getIntervalLine());

          preparedStatementMedCod.setString(6, medCod.getCodeOnly());

          preparedStatementMedCod.setString(7, medCod.getLineCoded());

          preparedStatementMedCod.addBatch();
        }
      }

      preparedStatementIdent.executeBatch();

      preparedStatementMedCod.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }

    try (final var statement = database.getConnection().createStatement())
    {
      if (manager.getLotType() == LotType.MULTIPLE)
      {
        statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.FINISHED.getValue() + " WHERE uid = '" + manager.getUid() + "'");
      }
      else
      {
        for (Ident ident : certificates)
        {
          statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.FINISHED.getValue() + " WHERE uid = '" + ident.getCertificateKey() + "'");
        }
      }

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Facade to begin the process in the webservice
   *
   * @param owner the identifier of user's session
   * @param certificates the list of certificates
   * @return the user's request identifier
   * @throws BackendException if a database access error occurs
   */
  public String beginProcess(final String owner, final List<Ident> certificates) throws BackendException
  {
    final var manager = new Manager();

    String uid = null;

    if (certificates.size() > 1)
    {
      uid = RandomStringUtils.randomAlphabetic(30);

      createCertificatesTable(uid);

      insertCertificatesLot(uid, certificates);

      manager.setLotType(LotType.MULTIPLE);
    }
    else
    {
      uid = certificates.get(0).getCertificateKey();

      insertCertificatesLot(UNIQUE_TABLES, certificates);

      manager.setLotType(LotType.UNIQUE);
    }

    manager.setUid(uid);

    manager.setOwner(owner);

    manager.setStatus(ProcessingStatus.READY);

    managerInsert(manager);

    return uid;
  }

  /**
   * Get the list of certificates to be process by Access database
   *
   * @param manager the lot's manager
   * @return the list of certificates
   * @throws BackendException if a database access error occurs
   */
  public List<Ident> certificatesToAccess(final Manager manager) throws BackendException
  {
    var sqlIdent = "SELECT a.* FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident a, " + database.getSchema() + ".manager b WHERE b.status = " + ProcessingStatus.READY.getValue() + " AND b.lotType = " + LotType.UNIQUE.getValue() + " AND b.uid = a.certificateKey";

    var sqlMedCod = "SELECT * FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod WHERE certificateKey = ?";

    if (manager.getLotType() == LotType.MULTIPLE)
    {
      sqlIdent = "SELECT * FROM " + database.getSchema() + "." + manager.getUid() + "Ident";

      sqlMedCod = "SELECT * FROM " + database.getSchema() + "." + manager.getUid() + "MedCod WHERE certificateKey = ?";
    }

    final List<Ident> certificates = new LinkedList<>();

    try (final var resultSet = database.getConnection().createStatement().executeQuery(sqlIdent);
         final var preparedStatement = database.getConnection().prepareStatement(sqlMedCod))
    {
      while (resultSet.next())
      {
        final var ident = new Ident(dateTimeFormatter);

        ident.setCertificateKey(resultSet.getString("certificateKey"));

        ident.setDateBirth(resultSet.getTimestamp("dateBirth"));

        ident.setDateDeath(resultSet.getTimestamp("dateDeath"));

        ident.setAge(resultSet.getString("age"));

        ident.setSex(resultSet.getString("sex"));

        ident.setMannerOfDeath(resultSet.getString("mannerOfDeath"));

        ident.setStatus(resultSet.getString("status"));

        ident.setReject(resultSet.getString("reject"));

        ident.setCoding(resultSet.getString("coding"));

        ident.setCodingFlags(resultSet.getString("codingFlags"));

        ident.setAutopsyRequested(resultSet.getString("autopsyRequested"));

        ident.setAutopsyUsed(resultSet.getString("autopsyUsed"));

        ident.setRecentSurgery(resultSet.getString("recentSurgery"));

        ident.setDateOfSurgery(resultSet.getTimestamp("dateOfSurgery"));

        ident.setDateOfInjury(resultSet.getTimestamp("dateOfInjury"));

        ident.setPlaceOfOccurrence(resultSet.getString("placeOfOccurrence"));

        ident.setActivityCode(resultSet.getString("activityCode"));

        ident.setPregnancy(resultSet.getString("pregnancy"));

        ident.setPregnancyContributeDeath(resultSet.getString("pregnancyContributeDeath"));

        ident.setStillbirth(resultSet.getString("stillbirth"));

        ident.setMultiplePregnancy(resultSet.getString("multiplePregnancy"));

        ident.setCompletedWeeks(resultSet.getString("completedWeeks"));

        ident.setBirthWeight(resultSet.getString("birthWeight"));

        ident.setAgeOfMother(resultSet.getString("ageOfMother"));

        preparedStatement.clearParameters();

        preparedStatement.setString(1, ident.getCertificateKey());

        try (final var resultSetMedCod = preparedStatement.executeQuery())
        {
          final List<MedCod> medCods = new LinkedList<>();

          while (resultSetMedCod.next())
          {
            final var medCod = new MedCod();

            medCod.setCertificateKey(resultSetMedCod.getString("certificateKey"));

            medCod.setLineNb(resultSetMedCod.getString("lineNb"));

            medCod.setTextLine(resultSetMedCod.getString("textLine"));

            medCod.setCodeLine(resultSetMedCod.getString("codeLine"));

            medCod.setIntervalLine(resultSetMedCod.getString("intervalLine"));

            medCod.setCodeOnly(resultSetMedCod.getString("codeOnly"));

            medCod.setLineCoded(resultSetMedCod.getString("lineCoded"));

            medCods.add(medCod);
          }

          ident.setMedCods(medCods);
        }

        certificates.add(ident);
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }

    try (final var statement = database.getConnection().createStatement())
    {
      if (manager.getLotType() == LotType.MULTIPLE)
      {
        statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.RUNNING.getValue() + " WHERE uid = '" + manager.getUid() + "'");

        statement.addBatch("DELETE FROM " + database.getSchema() + "." + manager.getUid() + "Ident");

        statement.addBatch("DELETE FROM " + database.getSchema() + "." + manager.getUid() + "MedCod");
      }
      else
      {
        for (Ident ident : certificates)
        {
          statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.RUNNING.getValue() + " WHERE uid = '" + ident.getCertificateKey() + "'");

          statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident WHERE certificateKey = '" + ident.getCertificateKey() + "'");

          statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod WHERE certificateKey = '" + ident.getCertificateKey() + "'");
        }
      }

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }

    return certificates;
  }

  /**
   * Convert unique certificates from database to the Iris
   *
   * @param manager the lot's manager
   * @throws BackendException if a database access error occurs
   */
  public void convertDatabaseToIris(final Manager manager) throws BackendException
  {
    manager.setUid(RandomStringUtils.randomAlphabetic(30));

    manager.setOwner(RandomStringUtils.randomAlphabetic(32));

    manager.setLotType(LotType.UNIQUE);

    manager.setStatus(ProcessingStatus.RUNNING);

    try (final var statement = database.getConnection().createStatement())
    {
      statement.addBatch(database.lotIdentCreateScript(manager.getUid()));

      statement.addBatch(database.lotMedCodCreateScript(manager.getUid()));

      statement.addBatch("INSERT INTO " + database.getSchema() + "." + manager.getUid() + "Ident SELECT a.* FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident a, " + database.getSchema() + ".manager b WHERE b.status = " + ProcessingStatus.READY.getValue() + " AND b.lotType = " + LotType.UNIQUE.getValue() + " AND b.uid = a.certificateKey;");

      statement.addBatch("INSERT INTO " + database.getSchema() + "." + manager.getUid() + "MedCod SELECT a.* FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod a, " + database.getSchema() + "." + manager.getUid() + "Ident b WHERE b.certificateKey = a.certificateKey;");

      statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.RUNNING.getValue() + " WHERE uid IN (SELECT certificateKey FROM " + database.getSchema() + "." + manager.getUid() + "Ident);");

      statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident WHERE certificateKey IN (SELECT certificateKey FROM " + database.getSchema() + "." + manager.getUid() + "Ident);");

      statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod WHERE certificateKey IN (SELECT certificateKey FROM " + database.getSchema() + "." + manager.getUid() + "Ident);");

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }

    managerInsert(manager);
  }

  /**
   * Convert unique certificates from Iris to the database
   *
   * @param manager the lot's manager
   * @throws BackendException if a database access error occurs
   */
  public void convertIrisToDatabase(final Manager manager) throws BackendException
  {
    try (final var statement = database.getConnection().createStatement())
    {
      statement.addBatch("INSERT INTO " + database.getSchema() + "." + UNIQUE_TABLES + "Ident SELECT * FROM " + database.getSchema() + "." + manager.getUid() + "Ident;");

      statement.addBatch("INSERT INTO " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod SELECT * FROM " + database.getSchema() + "." + manager.getUid() + "MedCod;");

      statement.addBatch("UPDATE " + database.getSchema() + ".manager SET status = " + ProcessingStatus.FINISHED.getValue() + " WHERE uid IN (SELECT certificateKey FROM " + database.getSchema() + "." + manager.getUid() + "Ident);");

      statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + manager.getUid() + "Ident CASCADE;");

      statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + manager.getUid() + "MedCod CASCADE;");

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }

    managerDelete(manager.getUid(), manager.getOwner());
  }

  /**
   * Create the certificates tables (Ident and MedCod)
   *
   * @param lot the lot's name
   * @throws BackendException if a database access error occurs
   */
  private void createCertificatesTable(final String lot) throws BackendException
  {
    try (final var statement = database.getConnection().createStatement())
    {
      statement.addBatch(database.lotIdentCreateScript(lot));

      statement.addBatch(database.lotMedCodCreateScript(lot));

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Get the formatter for printing and parsing date-time objects
   *
   * @return the formatter for printing and parsing date-time objects
   */
  public DateTimeFormatter getDateTimeFormatter()
  {
    return dateTimeFormatter;
  }

  /**
   * Insert the certificates in the lot
   *
   * @param lot the lot's name
   * @param certificates the list of certificates
   * @throws BackendException if a database access error occurs
   */
  private void insertCertificatesLot(final String lot, final List<Ident> certificates) throws BackendException
  {
    try (final var preparedStatementIdent = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + "." + lot + "Ident (certificateKey, dateBirth, dateDeath, age, sex, mannerOfDeath, codingFlags, autopsyRequested, autopsyUsed, recentSurgery, dateOfSurgery, dateOfInjury, placeOfOccurrence, activityCode, pregnancy, pregnancyContributeDeath, stillbirth, multiplePregnancy, completedWeeks, birthWeight, ageOfMother) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
         final var preparedStatementMedCod = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + "." + lot + "MedCod (certificateKey, lineNb, textLine, codeLine, intervalLine, codeOnly, lineCoded) VALUES (?, ?, ?, ?, ?, ?, ?);"))
    {
      for (Ident ident : certificates)
      {
        preparedStatementIdent.clearParameters();

        preparedStatementIdent.setString(1, ident.getCertificateKey());

        preparedStatementIdent.setTimestamp(2, ident.getDateBirthAsTimestamp());

        preparedStatementIdent.setTimestamp(3, ident.getDateDeathAsTimestamp());

        preparedStatementIdent.setString(4, ident.getAge());

        preparedStatementIdent.setString(5, ident.getSex());

        preparedStatementIdent.setObject(6, ident.getMannerOfDeath(), Types.TINYINT);

        preparedStatementIdent.setString(7, ident.getCodingFlags());

        preparedStatementIdent.setString(8, ident.getAutopsyRequested());

        preparedStatementIdent.setString(9, ident.getAutopsyUsed());

        preparedStatementIdent.setString(10, ident.getRecentSurgery());

        preparedStatementIdent.setTimestamp(11, ident.getDateOfSurgeryAsTimestamp());

        preparedStatementIdent.setTimestamp(12, ident.getDateOfInjuryAsTimestamp());

        preparedStatementIdent.setString(13, ident.getPlaceOfOccurrence());

        preparedStatementIdent.setString(14, ident.getActivityCode());

        preparedStatementIdent.setString(15, ident.getPregnancy());

        preparedStatementIdent.setString(16, ident.getPregnancyContributeDeath());

        preparedStatementIdent.setString(17, ident.getStillbirth());

        preparedStatementIdent.setString(18, ident.getMultiplePregnancy());

        preparedStatementIdent.setString(19, ident.getCompletedWeeks());

        preparedStatementIdent.setString(20, ident.getBirthWeight());

        preparedStatementIdent.setString(21, ident.getAgeOfMother());

        preparedStatementIdent.addBatch();

        for (MedCod medCod : ident.getMedCods())
        {
          preparedStatementMedCod.clearParameters();

          preparedStatementMedCod.setString(1, medCod.getCertificateKey());

          preparedStatementMedCod.setObject(2, medCod.getLineNb(), Types.TINYINT);

          preparedStatementMedCod.setString(3, medCod.getTextLine());

          preparedStatementMedCod.setString(4, medCod.getCodeLine());

          preparedStatementMedCod.setString(5, medCod.getIntervalLine());

          preparedStatementMedCod.setString(6, medCod.getCodeOnly());

          preparedStatementMedCod.setString(7, medCod.getLineCoded());

          preparedStatementMedCod.addBatch();
        }
      }

      preparedStatementIdent.executeBatch();

      preparedStatementMedCod.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Delete of lot in the manager table
   *
   * @param uid the identifier of the lot
   * @param owner the identifier of the owner
   */
  public void managerDelete(final String uid, final String owner) throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("DELETE FROM " + database.getSchema() + ".manager WHERE uid = ? AND owner = ?"))
    {
      preparedStatement.setString(1, uid);

      preparedStatement.setString(2, owner);

      preparedStatement.execute();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Create a new lot in the manager table
   *
   * @param uid the identifier of the lot
   * @param owner the identifier of the owner
   */
  private void managerInsert(final Manager manager) throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("INSERT INTO " + database.getSchema() + ".manager (uid, owner, created, accessed, lotType, status) VALUES (?, ?, ?, ?, ?, ?);"))
    {
      preparedStatement.setString(1, manager.getUid());

      preparedStatement.setString(2, manager.getOwner());

      preparedStatement.setObject(3, LocalDateTime.now(), Types.TIMESTAMP);

      preparedStatement.setObject(4, LocalDateTime.now(), Types.TIMESTAMP);

      preparedStatement.setInt(5, manager.getLotType().getValue());

      preparedStatement.setInt(6, manager.getStatus().getValue());

      preparedStatement.execute();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Recover the status of the lot in the manager table
   *
   * @param uid the identifier of the lot
   * @param owner the identifier of the owner
   * @return the status of the lot
   */
  private Manager managerRecoverStatus(final String uid, final String owner) throws BackendException, FrontendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("SELECT * FROM " + database.getSchema() + ".manager WHERE uid = ? AND owner = ?"))
    {
      preparedStatement.setString(1, uid);

      preparedStatement.setString(2, owner);

      try (final var resultSet = preparedStatement.executeQuery())
      {
        if (resultSet.next())
        {
          final var manager = new Manager();

          manager.setUid(resultSet.getString("uid"));

          manager.setOwner(resultSet.getString("owner"));

          manager.setLotType(resultSet.getInt("lotType"));

          manager.setStatus(resultSet.getInt("status"));

          return manager;
        }

        throw new FrontendException("Lot not found");
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Update the status of lot in the manager table
   *
   * @param uid the identifier of the lot
   * @param owner the identifier of the owner
   * @param status the new status
   */
  public void managerStatusUpdate(final String uid, final String owner, final ProcessingStatus status) throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("UPDATE " + database.getSchema() + ".manager SET accessed = ?, status = ? WHERE uid = ? AND owner = ?"))
    {
      preparedStatement.setObject(1, LocalDateTime.now(), Types.TIMESTAMP);

      preparedStatement.setInt(2, status.getValue());

      preparedStatement.setString(3, uid);

      preparedStatement.setString(4, owner);

      preparedStatement.executeUpdate();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Update the access of the lot in the manager table
   *
   * @param uid the identifier of the lot
   * @param owner the identifier of the owner
   */
  public void managerUpdateAccessed(final String uid, final String owner) throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("UPDATE " + database.getSchema() + ".manager SET accessed = ? WHERE uid = ? AND owner = ?"))
    {
      preparedStatement.setObject(1, LocalDateTime.now(), Types.TIMESTAMP);

      preparedStatement.setString(2, uid);

      preparedStatement.setString(3, owner);

      preparedStatement.executeUpdate();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Get the next certificate to be processed
   *
   * @return the next certificate to be processed
   */
  public Manager nextCertificate() throws BackendException
  {
    try (final var preparedStatement = database.getConnection().prepareStatement("SELECT * FROM " + database.getSchema() + ".manager WHERE status = ? ORDER BY created"))
    {
      preparedStatement.setInt(1, ProcessingStatus.READY.getValue());

      try (final var resultSet = preparedStatement.executeQuery())
      {
        if (resultSet.next())
        {
          final var manager = new Manager();

          manager.setUid(resultSet.getString("uid"));

          manager.setOwner(resultSet.getString("owner"));

          manager.setLotType(resultSet.getInt("lotType"));

          return manager;
        }

        return null;
      }
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Recover the lot of the owner
   *
   * @param owner the identifier of the owner
   * @param recover the identifier of the lot
   * @return the lot of the owner
   * @throws BackendException problems in the back-end processing
   * @throws FrontendException problems in the front-end processing
   */
  public Iris recover(final String owner, final Recover recover) throws BackendException, FrontendException
  {
    final var manager = managerRecoverStatus(recover.getUid(), owner);

    final var iris = new Iris();

    if (manager.getStatus() == ProcessingStatus.READY || manager.getStatus() == ProcessingStatus.RUNNING)
    {
      recover.setStatus(manager.getStatus().toString());

      managerUpdateAccessed(recover.getUid(), owner);

      iris.setRecover(recover);
    }
    else
    {
      if (manager.getLotType() == LotType.UNIQUE)
      {
        iris.setCertificates(recoverCertificateUnique(manager.getUid()));

        try (final var statement = database.getConnection().createStatement())
        {
          statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident WHERE certificateKey = '" + manager.getUid() + "';");

          statement.addBatch("DELETE FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod WHERE certificateKey = '" + manager.getUid() + "';");

          statement.executeBatch();
        }
        catch (final SQLException exception)
        {
          throw new BackendException(exception);
        }
      }
      else
      {
        iris.setCertificates(recoverCertificatesLot(manager.getUid()));

        try (final var statement = database.getConnection().createStatement())
        {
          statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + manager.getUid() + "Ident CASCADE;");

          statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + manager.getUid() +  "MedCod CASCADE;");

          statement.executeBatch();
        }
        catch (final SQLException exception)
        {
          throw new BackendException(exception);
        }
      }

      managerDelete(manager.getUid(), manager.getOwner());
    }

    return iris;
  }

  /**
   * Recover the certificates keep in the lot
   *
   * @param lot the lot's name
   * @return the list of certificates
   * @throws BackendException if a database access error occurs
   */
  private List<Ident> recoverCertificatesLot(final String lot) throws BackendException
  {
    try (final var resultSet = database.getConnection().createStatement().executeQuery("SELECT * FROM " + database.getSchema() + "." + lot + "Ident");
         final var preparedStatement = database.getConnection().prepareStatement("SELECT * FROM " + database.getSchema() + "." + lot + "MedCod WHERE certificateKey = ?"))
    {
      final List<Ident> list = new LinkedList<>();

      while (resultSet.next())
      {
        final var ident = new Ident(dateTimeFormatter);

        ident.setCertificateKey(resultSet.getString("certificateKey"));

        ident.setLastChange(resultSet.getTimestamp("lastChange"));

        ident.setDateBirth(resultSet.getTimestamp("dateBirth"));

        ident.setDateDeath(resultSet.getTimestamp("dateDeath"));

        ident.setAge(resultSet.getString("age"));

        ident.setSex(resultSet.getString("sex"));

        ident.setMannerOfDeath(resultSet.getString("mannerOfDeath"));

        ident.setUcCode(new UCCode(resultSet.getString("ucCode")));

        ident.setMainInjury(resultSet.getString("mainInjury"));

        ident.setStatus(resultSet.getString("status"));

        ident.setReject(resultSet.getString("reject"));

        ident.setCoding(resultSet.getString("coding"));

        ident.setCodingVersion(resultSet.getString("codingVersion"));

        ident.setCodingFlags(resultSet.getString("codingFlags"));

        ident.setSelectedCodes(resultSet.getString("selectedCodes"));

        ident.setSubstitutedCodes(resultSet.getString("substitutedCodes"));

        ident.setErnCodes(resultSet.getString("ernCodes"));

        ident.setAcmeCodes(resultSet.getString("acmeCodes"));

        ident.setMultipleCodes(resultSet.getString("multipleCodes"));

        ident.setToDoList(resultSet.getString("toDoList"));

        ident.setAutopsyRequested(resultSet.getString("autopsyRequested"));

        ident.setAutopsyUsed(resultSet.getString("autopsyUsed"));

        ident.setRecentSurgery(resultSet.getString("recentSurgery"));

        ident.setDateOfSurgery(resultSet.getTimestamp("dateOfSurgery"));

        ident.setDateOfInjury(resultSet.getTimestamp("dateOfInjury"));

        ident.setPlaceOfOccurrence(resultSet.getString("placeOfOccurrence"));

        ident.setActivityCode(resultSet.getString("activityCode"));

        ident.setPregnancy(resultSet.getString("pregnancy"));

        ident.setPregnancyContributeDeath(resultSet.getString("pregnancyContributeDeath"));

        ident.setStillbirth(resultSet.getString("stillbirth"));

        ident.setMultiplePregnancy(resultSet.getString("multiplePregnancy"));

        ident.setCompletedWeeks(resultSet.getString("completedWeeks"));

        ident.setBirthWeight(resultSet.getString("birthWeight"));

        ident.setAgeOfMother(resultSet.getString("ageOfMother"));

        preparedStatement.clearParameters();

        preparedStatement.setString(1, ident.getCertificateKey());

        try (final var resultSetMedCod = preparedStatement.executeQuery())
        {
          List<MedCod> medCods = new LinkedList<>();

          while (resultSetMedCod.next())
          {
            final var medCod = new MedCod();

            medCod.setCertificateKey(resultSetMedCod.getString("certificateKey"));

            medCod.setLineNb(resultSetMedCod.getString("lineNb"));

            medCod.setTextLine(resultSetMedCod.getString("textLine"));

            medCod.setCodeLine(resultSetMedCod.getString("codeLine"));

            medCod.setIntervalLine(resultSetMedCod.getString("intervalLine"));

            medCod.setCodeOnly(resultSetMedCod.getString("codeOnly"));

            medCod.setLineCoded(resultSetMedCod.getString("lineCoded"));

            medCods.add(medCod);
          }

          ident.setMedCods(medCods);
        }

        list.add(ident);
      }

      return list;
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }

  /**
   * Recover the certificates keep in the lot
   *
   * @param uid the user's request identifier
   * @return the list of certificates
   * @throws BackendException if a database access error occurs
   */
  public List<Ident> recoverCertificateUnique(final String uid) throws BackendException, FrontendException
  {
    try (final var preparedStatementIdent = database.getConnection().prepareStatement("SELECT * FROM " + database.getSchema() + "." + UNIQUE_TABLES + "Ident WHERE certificateKey = ?;");
         final var preparedStatementMedCod = database.getConnection().prepareStatement("SELECT * FROM " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod WHERE certificateKey = ?;"))
    {
      preparedStatementIdent.setString(1, uid);

      preparedStatementMedCod.setString(1, uid);

      try (final var resultSet = preparedStatementIdent.executeQuery())
      {
        if (resultSet.next())
        {
          final var ident = new Ident(dateTimeFormatter);

          ident.setCertificateKey(uid);

          ident.setLastChange(resultSet.getTimestamp("lastChange"));

          ident.setDateBirth(resultSet.getTimestamp("dateBirth"));

          ident.setDateDeath(resultSet.getTimestamp("dateDeath"));

          ident.setAge(resultSet.getString("age"));

          ident.setSex(resultSet.getString("sex"));

          ident.setMannerOfDeath(resultSet.getString("mannerOfDeath"));

          ident.setUcCode(new UCCode(resultSet.getString("ucCode")));

          ident.setMainInjury(resultSet.getString("mainInjury"));

          ident.setStatus(resultSet.getString("status"));

          ident.setReject(resultSet.getString("reject"));

          ident.setCoding(resultSet.getString("coding"));

          ident.setCodingVersion(resultSet.getString("codingVersion"));

          ident.setCodingFlags(resultSet.getString("codingFlags"));

          ident.setSelectedCodes(resultSet.getString("selectedCodes"));

          ident.setSubstitutedCodes(resultSet.getString("substitutedCodes"));

          ident.setErnCodes(resultSet.getString("ernCodes"));

          ident.setAcmeCodes(resultSet.getString("acmeCodes"));

          ident.setMultipleCodes(resultSet.getString("multipleCodes"));

          ident.setToDoList(resultSet.getString("toDoList"));

          ident.setAutopsyRequested(resultSet.getString("autopsyRequested"));

          ident.setAutopsyUsed(resultSet.getString("autopsyUsed"));

          ident.setRecentSurgery(resultSet.getString("recentSurgery"));

          ident.setDateOfSurgery(resultSet.getTimestamp("dateOfSurgery"));

          ident.setDateOfInjury(resultSet.getTimestamp("dateOfInjury"));

          ident.setPlaceOfOccurrence(resultSet.getString("placeOfOccurrence"));

          ident.setActivityCode(resultSet.getString("activityCode"));

          ident.setPregnancy(resultSet.getString("pregnancy"));

          ident.setPregnancyContributeDeath(resultSet.getString("pregnancyContributeDeath"));

          ident.setStillbirth(resultSet.getString("stillbirth"));

          ident.setMultiplePregnancy(resultSet.getString("multiplePregnancy"));

          ident.setCompletedWeeks(resultSet.getString("completedWeeks"));

          ident.setBirthWeight(resultSet.getString("birthWeight"));

          ident.setAgeOfMother(resultSet.getString("ageOfMother"));

          try (final var resultSetMedCod = preparedStatementMedCod.executeQuery())
          {
            List<MedCod> medCods = new LinkedList<>();

            while (resultSetMedCod.next())
            {
              final var medCod = new MedCod();

              medCod.setCertificateKey(resultSetMedCod.getString("certificateKey"));

              medCod.setLineNb(resultSetMedCod.getString("lineNb"));

              medCod.setTextLine(resultSetMedCod.getString("textLine"));

              medCod.setCodeLine(resultSetMedCod.getString("codeLine"));

              medCod.setIntervalLine(resultSetMedCod.getString("intervalLine"));

              medCod.setCodeOnly(resultSetMedCod.getString("codeOnly"));

              medCod.setLineCoded(resultSetMedCod.getString("lineCoded"));

              medCods.add(medCod);
            }

            ident.setMedCods(medCods);
          }

          final List<Ident> list = new LinkedList<>();

          list.add(ident);

          return list;
        }

        throw new FrontendException("Certificate not found");
      }
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
    try (final var statement = database.getConnection().createStatement())
    {
      statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + UNIQUE_TABLES + "Ident CASCADE;");

      statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + UNIQUE_TABLES + "MedCod CASCADE;");

      if (database.existsTable("manager"))
      {
        try (final var resultSet = database.getConnection().createStatement().executeQuery("SELECT uid FROM " + database.getSchema() + ".manager WHERE lotType = " + LotType.MULTIPLE.getValue()))
        {
          while (resultSet.next())
          {
            statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + resultSet.getString("uid") + "Ident CASCADE;");

            statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + "." + resultSet.getString("uid") + "MedCod CASCADE;");
          }
        }
        catch (final SQLException exception)
        {
          throw new BackendException(exception);
        }
      }

      statement.addBatch("DROP TABLE IF EXISTS " + database.getSchema() + ".manager CASCADE;");

      statement.addBatch(database.managerCreateScript());

      statement.addBatch(database.lotIdentCreateScript(UNIQUE_TABLES));

      statement.addBatch(database.lotMedCodCreateScript(UNIQUE_TABLES));

      statement.executeBatch();
    }
    catch (final SQLException exception)
    {
      throw new BackendException(exception);
    }
  }
}
