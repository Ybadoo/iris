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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.ybadoo.iris.datasource.HostDatasource;
import com.ybadoo.iris.entity.Ident;
import com.ybadoo.iris.entity.Manager;
import com.ybadoo.iris.entity.MedCod;
import com.ybadoo.iris.entity.UCCode;
import com.ybadoo.iris.exception.BackendException;

/**
 * Classe responsavel pelo processamento do lote na ferramenta IRIS, armazenado no banco de dados Microsoft Access
 */
public class AccessEngine extends Engine
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Arquivo MDB
   */
  private File fileMDB;

  /**
   * Nome do lote
   */
  private String lotName;

  /**
   * @param datasource
   */
  public AccessEngine(final HostDatasource datasource)
  {
    super(datasource);
  }

  /**
   * @param certificates
   * @param certificateKey
   * @return
   */
  private Ident getIdent(final List<Ident> certificates, final String certificateKey)
  {
    for (Ident ident : certificates)
    {
      if (ident.getCertificateKey().equals(certificateKey))
      {
        return ident;
      }
    }

    return null;
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.engine.Engine#posSynchronized(com.ybadoo.iris.entity.Manager)
   */
  @Override
  protected void posSynchronized(final Manager manager) throws BackendException
  {
    final List<Ident> certificates = new ArrayList<>();

    try (final var database = DatabaseBuilder.open(fileMDB))
    {
      final var tableIdent = database.getTable(lotName + "Ident");

      final var tableMedCod = database.getTable(lotName + "MedCod");

      for (var count = 0; count < tableIdent.getRowCount(); count++)
      {
        final var ident = new Ident(datasource.getDateTimeFormatter());

        final var row = tableIdent.getNextRow();

        ident.setCertificateKey(row.getString("CertificateKey"));

        ident.setLastChange(row.getLocalDateTime("LastChange"));

        ident.setDateBirth(row.getLocalDateTime("DateBirth"));

        ident.setDateDeath(row.getLocalDateTime("DateDeath"));

        ident.setAge(row.getString("Age"));

        ident.setSex(row.getString("Sex"));

        ident.setMannerOfDeath(row.getByte("MannerOfDeath").toString());

        ident.setUcCode(new UCCode(row.getString("UCCode")));

        ident.setMainInjury(row.getString("mainInjury"));

        ident.setStatus(row.getString("Status"));

        ident.setReject(row.getString("Reject"));

        ident.setCoding(row.getString("Coding"));

        ident.setCodingVersion(row.getString("CodingVersion"));

        ident.setCodingFlags(row.getString("CodingFlags"));

        ident.setSelectedCodes(row.getString("SelectedCodes"));

        ident.setSubstitutedCodes(row.getString("SubstitutedCodes"));

        ident.setErnCodes(row.getString("ErnCodes"));

        ident.setAcmeCodes(row.getString("AcmeCodes"));

        ident.setMultipleCodes(row.getString("MultipleCodes"));

        ident.setToDoList(row.getString("ToDoList"));

        ident.setAutopsyRequested(row.getString("AutopsyRequested"));

        ident.setAutopsyUsed(row.getString("AutopsyUsed"));

        ident.setRecentSurgery(row.getString("RecentSurgery"));

        ident.setDateOfSurgery(row.getLocalDateTime("DateOfSurgery"));

        ident.setDateOfInjury(row.getLocalDateTime("DateOfInjury"));

        ident.setPlaceOfOccurrence(row.getString("PlaceOfOccurrence"));

        ident.setActivityCode(row.getString("ActivityCode"));

        ident.setPregnancy(row.getString("Pregnancy"));

        ident.setPregnancyContributeDeath(row.getString("PregnancyContributeDeath"));

        ident.setStillbirth(row.getString("Stillbirth"));

        ident.setMultiplePregnancy(row.getString("MultiplePregnancy"));

        ident.setCompletedWeeks(row.getString("CompletedWeeks"));

        ident.setBirthWeight(row.getString("BirthWeight"));

        ident.setAgeOfMother(row.getString("AgeOfMother"));

        ident.setMedCods(new LinkedList<>());

        certificates.add(ident);
      }

      var index = 0;

      var ident = certificates.get(index);

      for (var count = 0; count < tableMedCod.getRowCount(); count++)
      {
        final var medCod = new MedCod();

        final var row = tableMedCod.getNextRow();

        medCod.setCertificateKey(row.getString("CertificateKey"));

        medCod.setLineNb(row.getShort("LineNb").toString());

        medCod.setTextLine(row.getString("TextLine"));

        medCod.setCodeLine(row.getString("CodeLine"));

        medCod.setIntervalLine(row.getString("IntervalLine"));

        medCod.setCodeOnly(row.getString("CodeOnly"));

        medCod.setLineCoded(row.getString("LineCoded"));

        if (ident.getCertificateKey().equals(medCod.getCertificateKey()))
        {
          ident.getMedCods().add(medCod);
        }
        else
        {
          index = index + 1;

          ident = certificates.get(index);

          if (ident.getCertificateKey().equals(medCod.getCertificateKey()))
          {
            ident.getMedCods().add(medCod);
          }
          else
          {
            ident = getIdent(certificates, medCod.getCertificateKey());

            ident.getMedCods().add(medCod);
          }
        }
      }
    }
    catch (final Exception exception)
    {
      throw new BackendException(exception);
    }

    datasource.accessToCertificates(manager, certificates);
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.engine.Engine#preSynchronized(com.ybadoo.iris.entity.Manager)
   */
  @Override
  protected String preSynchronized(final Manager manager) throws BackendException
  {
    try (final var database = DatabaseBuilder.open(fileMDB))
    {
      final var tableIdent = database.getTable(lotName + "Ident");

      for (Row row : tableIdent)
      {
        tableIdent.deleteRow(row);
      }

      final var tableMedCod = database.getTable(lotName + "MedCod");

      for (Row row : tableMedCod)
      {
        tableMedCod.deleteRow(row);
      }

      for (Ident ident : datasource.certificatesToAccess(manager))
      {
        tableIdent.addRow(ident.getCertificateKey(),
                          null, // ident.getLastChange()
                          ident.getDateBirthAsLocalDateTime(),
                          ident.getDateDeathAsLocalDateTime(),
                          ident.getAge(),
                          ident.getSex(),
                          ident.getMannerOfDeath() != null ? Byte.parseByte(ident.getMannerOfDeath()) : null,
                          null, // ident.getUCCode().getCode()
                          null, // ident.getMainInjury()
                          ident.getStatus(),
                          ident.getReject(),
                          ident.getCoding(),
                          null, // ident.getCodingVersion()
                          ident.getCodingFlags(),
                          null, // ident.getSelectedCodes()
                          null, // ident.getSubstitutedCodes()
                          null, // ident.getErnCodes()
                          null, // ident.getAcmeCodes()
                          null, // ident.getMultipleCodes()
                          null, // ident.getComments()
                          null, // ident.getFreeText()
                          null, // ident.getToDoList()
                          null, // ident.getCoderReject()
                          null, // ident.getDiagnosisModified()
                          null, // ident.getResidence()
                          null, // ident.getName()
                          null, // ident.getAddress()
                          ident.getAutopsyRequested(),
                          ident.getAutopsyUsed(),
                          ident.getRecentSurgery(),
                          ident.getDateOfSurgeryAsLocalDateTime(),
                          null, // ident.getReasonSurgery()
                          ident.getDateOfInjuryAsLocalDateTime(),
                          ident.getPlaceOfOccurrence(),
                          ident.getActivityCode(),
                          null, // ident.getExternalFreeText()
                          ident.getPregnancy(),
                          ident.getStillbirth(),
                          ident.getMultiplePregnancy(),
                          ident.getCompletedWeeks(),
                          ident.getBirthWeight(),
                          ident.getAgeOfMother(),
                          null, // ident.getConditionMother()
                          null, // ident.getCertImage()
                          ident.getPregnancyContributeDeath());

          for (MedCod medCod : ident.getMedCods())
          {
            tableMedCod.addRow(medCod.getCertificateKey(),
                               medCod.getLineNb() != null ? Short.parseShort(medCod.getLineNb()) : null,
                               medCod.getTextLine(),
                               medCod.getCodeLine(),
                               medCod.getIntervalLine(),
                               medCod.getCodeOnly(),
                               medCod.getLineCoded());
        }
      }
    }
    catch (final Exception exception)
    {
      throw new BackendException(exception);
    }

    return lotName;
  }

  /**
   * Configurar o arquivo MDB
   *
   * @param fileMDB arquivo MDB
   * @throws BackendException encapsulamento das excecoes {@link java.lang.NullPointerException} e {@link java.lang.SecurityException}
   */
  public void setFileMDB(final String fileMDB) throws BackendException
  {
    if (StringUtils.isBlank(fileMDB))
    {
      throw new BackendException("Key 'host.OLEDB.datasource' is null or empty.");
    }

    final var mdb = new File(fileMDB);

    try
    {
      if (!mdb.exists())
      {
        throw new BackendException("Key 'host.OLEDB.datasource' is not a MDB file valid.");
      }
    }
    catch (final SecurityException exception)
    {
      throw new BackendException("Key 'host.OLEDB.datasource' is invalid.", exception);
    }

    this.fileMDB = mdb;
  }


  /**
   * Configurar o nome do lote
   *
   * @param lotName nome do lote
   * @throws ConfigException encapsulamento da excecao {@link java.io.IOException}
   */
  public void setLotName(final String lotName) throws BackendException
  {
    try (final var database = DatabaseBuilder.open(fileMDB))
    {
      if (database.getTable(lotName + "Ident") == null)
      {
        throw new BackendException("Key 'host.OLEDB.lotName' is invalid. Table '" + lotName + "Ident' not found in MDB file.");
      }

      if (database.getTable(lotName + "MedCod") == null)
      {
        throw new BackendException("Name 'host.OLEDB.lotName' is invalid. Table '" + lotName + "MedCod' not found in MDB file.");
      }
    }
    catch (final IOException exception)
    {
      throw new BackendException("Key 'host.OLEDB.lotName' is invalid.", exception);
    }

    this.lotName = lotName;
  }
}
