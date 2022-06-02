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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;

/**
 * Fields the identification table (Ident), which contains all the individual data except the causes of death
 */
@XmlRootElement(name = "ident")
@XmlAccessorType (XmlAccessType.FIELD)
public class Ident implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Identifier of the death certificate
   */
  private String certificateKey;

  /**
   * When the record was last recoded
   */
  private String lastChange;

  /**
   * Date of birth
   */
  private String dateBirth;

  /**
   * Date of death
   */
  private String dateDeath;

  /**
   * Age
   */
  private String age;

  /**
   * Sex
   *
   * 1 - male
   * 2 - female
   * 3 - until
   * 8 - unknown
   * 9 - missing data
   */
  private String sex;

  /**
   * Manner of death
   *
   * 0 - disease
   * 1 - homicide (assault)
   * 2 - accident
   * 3 - pending investigation
   * 4 - suicide (intentional self-harm)
   * 5 - could not be determined
   * 6 - unknown (not filled in)
   * 8 - legal intervention
   * 9 - war
   */
  private String mannerOfDeath;

  /**
   * Underlying cause of death (UCOD or UC)
   */
  @XmlElement(name = "ucCode")
  private UCCode ucCode;

  /**
   * Main injury code
   */
  private String mainInjury;

  /**
   * Record processing status
   *
   * Initial  - record has not been processed by Iris
   * Rejected - record was rejected for manual intervention during processing
   * Final    - record completely coded
   * Closed   - record that cannot be modified anymore
   */
  private String status;

  /**
   * Record rejected
   *
   * No - no reject
   * syntax - the string in the ICD-10-WHO codes field is not an ICD-10-WHO code
   * Code - there is no code in the dictionary for this cause of death
   * MultipleCause - multiple cause table rejected the record
   * MayBe - decision tables are unsure of the underlying cause
   * Interval - reject for interval reasons
   * MainInjury - main injury is required but doesn't exists
   * Coder - rejected by coder for later review
   */
  private String reject;

  /**
   * Coding of underlying cause
   *
   * Automatic - multiple and underlying cause assigned by Iris
   * Manual    - underlying cause assigned manually by coder
   * UCDirect  - multiple cause assigned manually by coder
   */
  private String coding;

  /**
   * Version of Iris and tables used for coding
   */
  private String codingVersion;

  /**
   * Coding flags used to help selecting the right code in the dictionary
   */
  private String codingFlags;

  /**
   * ICD-10-WHO codes corresponding to the expressions on the death certificate, before substitution and modification
   */
  private String selectedCodes;

  /**
   * ICD-10-WHO codes come directly from dictionary or are inserted manually by coder or changed by IcdSubstitution table
   */
  private String substitutedCodes;

  /**
   * Not in use since Iris version 5
   */
  private String ernCodes;

  /**
   * ICD-10-WHO codes after MUSE processing or changes at Multiple codes browser
   */
  private String acmeCodes;

  /**
   * List of ICD10 codes reported on the death certificate with the addition of the underlying cause code if different from the other codes used
   */
  private String multipleCodes;

  /**
   * To-do list text box in Iris main window
   */
  private String toDoList;

  /**
   * if autopsy was requested
   *
   * 0 - no
   * 1 - yes
   */
  private String autopsyRequested;

  /**
   * if autopsy findings used in certification
   *
   * 0 - no
   * 1 - yes
   */
  private String autopsyUsed;

  /**
   * If surgery was performed within four weeks before death
   *
   * 0 - no
   * 1 - yes
   */
  private String recentSurgery;

  /**
   * Date of surgery
   */
  private String dateOfSurgery;

  /**
   * Date of injury
   */
  private String dateOfInjury;

  /**
   * Place of occurrence
   *
   * 0 - home
   * 1 - residential institution
   * 2 - school, other institution, public administrative area
   * 3 - sports and athletics area
   * 4 - street and highway
   * 5 - trade and service area
   * 6 - industrial and construction area
   * 7 - farm
   * 8 - other specified place
   * 9 - unspecified place
   */
  private String placeOfOccurrence;

  /**
   * Activity code
   *
   * 0 - sports
   * 1 - leisure
   * 2 - working for income
   * 3 - other work
   * 4 - resting, sleeping, eating or other vital activities
   * 8 - other activities
   * 9 - unspecified activity
   */
  private String activityCode;

  /**
   * If woman was pregnant
   *
   * 0 - pregnant at time of death
   * 1 - had been pregnant within 42 days before death
   * 2 - had been pregnant within 43 days and 1 year before death
   * 3 - not pregnant
   * 9 - unknown
   */
  private String pregnancy;

  /**
   * If the pregnancy did contribute to the death
   *
   * 0 - no
   * 1 - yes
   * 2 - unknown
   */
  private String pregnancyContributeDeath;

  /**
   * Stillborn child
   *
   * 1 - yes
   * U - unknown (optional)
   * N - no (optional)
   */
  private String stillbirth;

  /**
   * Fetus/newborn child in multiple pregnancy
   *
   * 1 - yes
   * U - unknown (optional)
   * N - no (optional)
   */
  private String multiplePregnancy;

  /**
   * Number of completed weeks of pregnancy
   */
  private String completedWeeks;

  /**
   * Birthweight in grams
   */
  private String birthWeight;

  /**
   * Age of mother in years
   */
  private String ageOfMother;

  /**
   * List of the medical causes of death
   */
  @XmlElement(name = "medCod")
  private List<MedCod> medCods;

  /**
   * List of inconsistencies observed in the Ident records
   */
  @XmlElement(name = "fault")
  private List<Fault> faults;

  /**
   * Formatter for printing and parsing date-time objects
   */
  @XmlTransient
  private DateTimeFormatter dateTimeFormatter;

  /**
   * Default constructor
   */
  public Ident()
  {
    // Required for the JAXBContext
  }

  /**
   * Constructs with the formatter for printing and parsing date-time objects
   *
   * @param dateTimeFormatter the formatter for printing and parsing date-time objects
   */
  public Ident(final DateTimeFormatter dateTimeFormatter)
  {
    this.dateTimeFormatter = dateTimeFormatter;
  }

  /**
   * Get the identifier of the death certificate
   *
   * @return the identifier of the death certificate
   */
  public String getCertificateKey()
  {
    return certificateKey;
  }

  /**
   * Set the identifier of the death certificate
   *
   * @param certificateKey the identifier of the death certificate
   */
  public void setCertificateKey(final String certificateKey)
  {
    this.certificateKey = certificateKey;
  }

  /**
   * Validate the identifier of the death certificate
   */
  private void validateCertificateKey()
  {
    if (StringUtils.isNotBlank(certificateKey))
    {
      if (certificateKey.length() > 30)
      {
        addFault(new Fault("certificateKey", "Length is between 1 and 30 characters"));
      }
    }
    else
    {
      addFault(new Fault("certificateKey", "Field is empty"));
    }
  }

  /**
   * Set the identifier of the death certificate
   *
   * @param certificateKey the identifier of the death certificate
   */
  public void configCertificateKey(final String certificateKey)
  {
    this.certificateKey = certificateKey;

    for (MedCod medCod : medCods)
    {
      medCod.setCertificateKey(certificateKey);
    }
  }

  /**
   * Indicate the identifier of the death certificate is repeated
   */
  public void repeatedCertificateKey()
  {
    addFault(new Fault("certificateKey", "Repeated value"));
  }

  /**
   * Get when the record was last recoded
   *
   * @return when the record was last recoded
   */
  public String getLastChange()
  {
    return lastChange;
  }

  /**
   * Get when the record was last recoded
   *
   * @return when the record was last recoded
   */
  public Timestamp getLastChangeAsTimestamp()
  {
    if (StringUtils.isNotBlank(lastChange))
    {
      return Timestamp.valueOf(LocalDateTime.parse(lastChange, dateTimeFormatter));
    }

    return null;
  }

  /**
   * Set when the record was last recoded
   *
   * @param lastChange when the record was last recoded
   */
  public void setLastChange(final Object lastChange)
  {
    if (lastChange != null)
    {
      if (lastChange instanceof String)
      {
        this.lastChange = (String) lastChange;
      }
      else if (lastChange instanceof LocalDateTime)
      {
        this.lastChange = ((LocalDateTime) lastChange).format(dateTimeFormatter);
      }
      else if (lastChange instanceof Timestamp)
      {
        this.lastChange = ((Timestamp) lastChange).toLocalDateTime().format(dateTimeFormatter);
      }
    }
  }

  /**
   * Get the date of birth
   *
   * @return the date of birth
   */
  public String getDateBirth()
  {
    return dateBirth;
  }

  /**
   * Get the date of birth as LocalDateTime type
   *
   * @return the date of birth as LocalDateTime type
   */
  public LocalDateTime getDateBirthAsLocalDateTime()
  {
    if (StringUtils.isNotBlank(dateBirth))
    {
      return LocalDateTime.parse(dateBirth, dateTimeFormatter);
    }

    return null;
  }

  /**
   * Get the date of birth as Timestamp type
   *
   * @return the date of birth as Timestamp type
   */
  public Timestamp getDateBirthAsTimestamp()
  {
    if (StringUtils.isNotBlank(dateBirth))
    {
      return Timestamp.valueOf(LocalDateTime.parse(dateBirth, dateTimeFormatter));
    }

    return null;
  }

  /**
   * Set the date of birth
   *
   * @param dateBirth the date of birth
   */
  public void setDateBirth(final Object dateBirth)
  {
    if (dateBirth != null)
    {
      if (dateBirth instanceof String)
      {
        this.dateBirth = (String) dateBirth;
      }
      else if (dateBirth instanceof LocalDateTime)
      {
        this.dateBirth = ((LocalDateTime) dateBirth).format(dateTimeFormatter);
      }
      else if (dateBirth instanceof Timestamp)
      {
        this.dateBirth = ((Timestamp) dateBirth).toLocalDateTime().format(dateTimeFormatter);
      }
    }
  }

  /**
   * Validate the date of birth
   */
  private void validateDateBirth()
  {
    if (StringUtils.isNotBlank(dateBirth))
    {
      try
      {
        LocalDateTime.parse(dateBirth, dateTimeFormatter);
      }
      catch (final DateTimeParseException exception)
      {
        addFault(new Fault("dateBirth", exception.getMessage()));
      }
    }
  }

  /**
   * Get the date of death
   *
   * @return the date of death
   */
  public String getDateDeath()
  {
    return dateDeath;
  }

  /**
   * Get the date of death as LocalDateTime type
   *
   * @return the date of death as LocalDateTime type
   */
  public LocalDateTime getDateDeathAsLocalDateTime()
  {
    if (StringUtils.isNotBlank(dateDeath))
    {
      return LocalDateTime.parse(dateDeath, dateTimeFormatter);
    }

    return null;
  }

  /**
   * Get the date of death as Timestamp type
   *
   * @return the date of death as Timestamp type
   */
  public Timestamp getDateDeathAsTimestamp()
  {
    if (StringUtils.isNotBlank(dateDeath))
    {
      return Timestamp.valueOf(LocalDateTime.parse(dateDeath, dateTimeFormatter));
    }

    return null;
  }

  /**
   * Set the date of death
   *
   * @param dateDeath the date of death
   */
  public void setDateDeath(final Object dateDeath)
  {
    if (dateDeath != null)
    {
      if (dateDeath instanceof String)
      {
        this.dateDeath = (String) dateDeath;
      }
      else if (dateDeath instanceof LocalDateTime)
      {
        this.dateDeath = ((LocalDateTime) dateDeath).format(dateTimeFormatter);
      }
      else if (dateDeath instanceof Timestamp)
      {
        this.dateDeath = ((Timestamp) dateDeath).toLocalDateTime().format(dateTimeFormatter);
      }
    }
  }

  /**
   * Validate the date of death
   */
  private void validateDateDeath()
  {
    if (StringUtils.isNotBlank(dateDeath))
    {
      LocalDateTime dateDeathType = null;

      try
      {
        dateDeathType = LocalDateTime.parse(dateDeath, dateTimeFormatter);
      }
      catch (final DateTimeParseException exception)
      {
        addFault(new Fault("dateDeath", exception.getMessage()));
      }

      if (StringUtils.isNotBlank(dateBirth))
      {
        try
        {
          final var dateBirthType = LocalDateTime.parse(dateBirth, dateTimeFormatter);

          if (dateBirthType.isAfter(dateDeathType))
          {
            addFault(new Fault("dateDeath", "Date of death before of date of birth"));
          }
        }
        catch (final DateTimeParseException exception)
        {
          addFault(new Fault("dateDeath", exception.getMessage()));
        }
      }
    }
  }

  /**
   * Get the age
   *
   * @return the age
   */
  public String getAge()
  {
    return age;
  }

  /**
   * Set the age
   *
   * @param age the age
   */
  public void setAge(final String age)
  {
    this.age = age;
  }

  /**
   * Validate the age
   */
  private void validateAge()
  {
    if (StringUtils.isNotBlank(age) && age.length() > 20)
    {
      addFault(new Fault("age", "Length is between 1 and 20 characters"));
    }
  }

  /**
   * Get the sex
   *
   * @return the sex
   */
  public String getSex()
  {
    return sex;
  }

  /**
   * Set the sex
   *
   * @param sex the sex
   */
  public void setSex(final String sex)
  {
    this.sex = sex;
  }

  /**
   * Validate the sex
   */
  private void validateSex()
  {
    if (StringUtils.isBlank(sex))
    {
      addFault(new Fault("sex", "Sex value is required"));
    }
    else if (!("1".equals(sex) || // male
               "2".equals(sex) || // female
               "3".equals(sex) || // until
               "8".equals(sex) || // unknown
               "9".equals(sex)))  // missing data
    {
      addFault(new Fault("sex", "Valid values: 1, 2, 3, 8 or 9"));
    }
  }

  /**
   * Get the manner of death
   *
   * @return the manner of death
   */
  public String getMannerOfDeath()
  {
    return mannerOfDeath;
  }

  /**
   * Set the manner of death
   *
   * @param mannerOfDeath the manner of death
   */
  public void setMannerOfDeath(final String mannerOfDeath)
  {
    this.mannerOfDeath = mannerOfDeath;
  }

  /**
   * Validate the manner of death
   */
  private void validateMannerOfDeath()
  {
    if (StringUtils.isBlank(mannerOfDeath))
    {
      addFault(new Fault("mannerOfDeath", "MannerOfDeath value is required"));
    }
    if (!("0".equals(mannerOfDeath) || // disease
          "1".equals(mannerOfDeath) || // homicide (assault)
          "2".equals(mannerOfDeath) || // accident
          "3".equals(mannerOfDeath) || // pending investigation
          "4".equals(mannerOfDeath) || // suicide (intentional self-harm)
          "5".equals(mannerOfDeath) || // could not be determined
          "6".equals(mannerOfDeath) || // unknown (not filled in)
          "8".equals(mannerOfDeath) || // legal intervention
          "9".equals(mannerOfDeath)))  // war
    {
      addFault(new Fault("mannerOfDeath", "Valid values: 0, 1, 2, 3, 4, 5, 6, 8 or 9"));
    }
  }

  /**
   * Get the underlying cause of death (UCOD or UC)
   *
   * @return the underlying cause of death (UCOD or UC)
   */
  public UCCode getUcCode()
  {
    return ucCode;
  }

  /**
   * Set the underlying cause of death (UCOD or UC)
   *
   * @param ucCode the underlying cause of death (UCOD or UC)
   */
  public void setUcCode(final UCCode ucCode)
  {
    this.ucCode = ucCode;
  }

  /**
   * Get the main injury code
   *
   * @return the main injury code
   */
  public String getMainInjury()
  {
    return mainInjury;
  }

  /**
   * Set the main injury code
   *
   * @param mainInjury the main injury code
   */
  public void setMainInjury(final String mainInjury)
  {
    this.mainInjury = mainInjury;
  }

  /**
   * Get the record processing status
   *
   * @return the record processing status
   */
  public String getStatus()
  {
    return status;
  }

  /**
   * Set the record processing status
   *
   * @param status the record processing status
   */
  public void setStatus(final String status)
  {
    this.status = status;
  }

  /**
   * Get the record rejected
   *
   * @return the record rejected
   */
  public String getReject()
  {
    return reject;
  }

  /**
   * Set the record rejected
   *
   * @param reject the record rejected
   */
  public void setReject(final String reject)
  {
    this.reject = reject;
  }

  /**
   * Get the coding of underlying cause
   *
   * @return the coding of underlying cause
   */
  public String getCoding()
  {
    return coding;
  }

  /**
   * Set the coding of underlying cause
   *
   * @param coding the coding of underlying cause
   */
  public void setCoding(final String coding)
  {
    this.coding = coding;
  }

  /**
   * Get the version of Iris and tables used for coding
   *
   * @return the version of Iris and tables used for coding
   */
  public String getCodingVersion()
  {
    return codingVersion;
  }

  /**
   * Set the version of Iris and tables used for coding
   *
   * @param codingVersion the version of Iris and tables used for coding
   */
  public void setCodingVersion(final String codingVersion)
  {
    this.codingVersion = codingVersion;
  }

  /**
   * Get the coding flags used to help selecting the right code in the dictionary
   *
   * @return the coding flags used to help selecting the right code in the dictionary
   */
  public String getCodingFlags()
  {
    return codingFlags;
  }

  /**
   * Set the coding flags used to help selecting the right code in the dictionary
   *
   * @param codingFlags the coding flags used to help selecting the right code in the dictionary
   */
  public void setCodingFlags(final String codingFlags)
  {
    this.codingFlags = codingFlags;
  }

  /**
   * Validate the coding flags used to help selecting the right code in the dictionary
   */
  private void validateCodingFlags()
  {
    if (StringUtils.isNotBlank(codingFlags) && codingFlags.length() > 50)
    {
      addFault(new Fault("codingFlags", "Length is between 1 and 50 characters"));
    }
  }

  /**
   * Get the ICD-10-WHO codes corresponding to the expressions on the death certificate, before substitution and modification
   *
   * @return the ICD-10-WHO codes corresponding to the expressions on the death certificate, before substitution and modification
   */
  public String getSelectedCodes()
  {
    return selectedCodes;
  }

  /**
   * Set the ICD-10-WHO codes corresponding to the expressions on the death certificate, before substitution and modification
   *
   * @param selectedCodes the ICD-10-WHO codes corresponding to the expressions on the death certificate, before substitution and modification
   */
  public void setSelectedCodes(final String selectedCodes)
  {
    this.selectedCodes = selectedCodes;
  }

  /**
   * Get the ICD-10-WHO codes come directly from dictionary or are inserted manually by coder or changed by IcdSubstitution table
   *
   * @return the ICD-10-WHO codes come directly from dictionary or are inserted manually by coder or changed by IcdSubstitution table
   */
  public String getSubstitutedCodes()
  {
    return substitutedCodes;
  }

  /**
   * Set the ICD-10-WHO codes come directly from dictionary or are inserted manually by coder or changed by IcdSubstitution table
   *
   * @param substitutedCodes the ICD-10-WHO codes come directly from dictionary or are inserted manually by coder or changed by IcdSubstitution table
   */
  public void setSubstitutedCodes(final String substitutedCodes)
  {
    this.substitutedCodes = substitutedCodes;
  }

  /**
   * Get the not in use since Iris version 5
   *
   * @return the not in use since Iris version 5
   */
  public String getErnCodes()
  {
    return ernCodes;
  }

  /**
   * Set the not in use since Iris version 5
   *
   * @param ernCodes the not in use since Iris version 5
   */
  public void setErnCodes(final String ernCodes)
  {
    this.ernCodes = ernCodes;
  }

  /**
   * Get the ICD-10-WHO codes after MUSE processing or changes at Multiple codes browser
   *
   * @return the ICD-10-WHO codes after MUSE processing or changes at Multiple codes browser
   */
  public String getAcmeCodes()
  {
    return acmeCodes;
  }

  /**
   * Set the ICD-10-WHO codes after MUSE processing or changes at Multiple codes browser
   *
   * @param acmeCodes the ICD-10-WHO codes after MUSE processing or changes at Multiple codes browser
   */
  public void setAcmeCodes(final String acmeCodes)
  {
    this.acmeCodes = acmeCodes;
  }

  /**
   * Get the list of ICD10 codes reported on the death certificate with the addition of the underlying cause code if different from the other codes used
   *
   * @return the list of ICD10 codes reported on the death certificate with the addition of the underlying cause code if different from the other codes used
   */
  public String getMultipleCodes()
  {
    return multipleCodes;
  }

  /**
   * Set the list of ICD10 codes reported on the death certificate with the addition of the underlying cause code if different from the other codes used
   *
   * @param multipleCodes the list of ICD10 codes reported on the death certificate with the addition of the underlying cause code if different from the other codes used
   */
  public void setMultipleCodes(final String multipleCodes)
  {
    this.multipleCodes = multipleCodes;
  }

  /**
   * Get the to-do list text box in Iris main window
   *
   * @return the to-do list text box in Iris main window
   */
  public String getToDoList()
  {
    return toDoList;
  }

  /**
   * Set the to-do list text box in Iris main window
   *
   * @param toDoList the to-do list text box in Iris main window
   */
  public void setToDoList(final String toDoList)
  {
    this.toDoList = toDoList;
  }

  /**
   * Get if autopsy was requested
   *
   * @return if autopsy was requested
   */
  public String getAutopsyRequested()
  {
    return autopsyRequested;
  }

  /**
   * Set if autopsy was requested
   *
   * @param autopsyRequested if autopsy was requested
   */
  public void setAutopsyRequested(final String autopsyRequested)
  {
    this.autopsyRequested = autopsyRequested;
  }

  /**
   * Set if autopsy was requested
   *
   * @param autopsyRequested if autopsy was requested
   */
  private void validateAutopsyRequested()
  {
    if (StringUtils.isNotBlank(autopsyRequested) && !("0".equals(autopsyRequested) || // no
                                                      "1".equals(autopsyRequested)))  // yes
    {
      addFault(new Fault("autopsyRequested", "Valid values: 0 or 1"));
    }
  }

  /**
   * Get if autopsy findings used in certification
   *
   * @return if autopsy findings used in certification
   */
  public String getAutopsyUsed()
  {
    return autopsyUsed;
  }

  /**
   * Set if autopsy findings used in certification
   *
   * @param autopsyUsed if autopsy findings used in certification
   */
  public void setAutopsyUsed(final String autopsyUsed)
  {
    this.autopsyUsed = autopsyUsed;
  }

  /**
   * Validate if autopsy findings used in certification
   */
  private void validateAutopsyUsed()
  {
    if (StringUtils.isNotBlank(autopsyUsed) && !("0".equals(autopsyUsed) || // no
                                                 "1".equals(autopsyUsed)))  // yes
    {
      addFault(new Fault("autopsyUsed", "Valid values: 0 or 1"));
    }
  }

  /**
   * Get if surgery was performed within four weeks before death
   *
   * @return if surgery was performed within four weeks before death
   */
  public String getRecentSurgery()
  {
    return recentSurgery;
  }

  /**
   * Set if surgery was performed within four weeks before death
   *
   * @param recentSurgery if surgery was performed within four weeks before death
   */
  public void setRecentSurgery(final String recentSurgery)
  {
    this.recentSurgery = recentSurgery;
  }

  /**
   * Validate if surgery was performed within four weeks before death
   */
  private void validateRecentSurgery()
  {
    if (StringUtils.isNotBlank(recentSurgery) && !("0".equals(recentSurgery) || // no
                                                   "1".equals(recentSurgery)))  // yes
    {
      addFault(new Fault("recentSurgery", "Valid values: 0 or 1"));
    }
  }

  /**
   * Get the date of surgery
   *
   * @return the date of surgery
   */
  public String getDateOfSurgery()
  {
    return dateOfSurgery;
  }

  /**
   * Get the date of surgery as LocalDateTime type
   *
   * @return the date of surgery as LocalDateTime type
   */
  public LocalDateTime getDateOfSurgeryAsLocalDateTime()
  {
    if (StringUtils.isNotBlank(dateOfSurgery))
    {
      return LocalDateTime.parse(dateOfSurgery, dateTimeFormatter);
    }

    return null;
  }

  /**
   * Get the date of surgery as Timestamp type
   *
   * @return the date of surgery as Timestamp type
   */
  public Timestamp getDateOfSurgeryAsTimestamp()
  {
    if (StringUtils.isNotBlank(dateOfSurgery))
    {
      return Timestamp.valueOf(LocalDateTime.parse(dateOfSurgery, dateTimeFormatter));
    }

    return null;
  }

  /**
   * Set of date of surgery
   *
   * @param dateOfSurgery the date of surgery
   */
  public void setDateOfSurgery(final Object dateOfSurgery)
  {
    if (dateOfSurgery != null)
    {
      if (dateOfSurgery instanceof String)
      {
        this.dateOfSurgery = (String) dateOfSurgery;
      }
      else if (dateOfSurgery instanceof LocalDateTime)
      {
        this.dateOfSurgery = ((LocalDateTime) dateOfSurgery).format(dateTimeFormatter);
      }
      else if (dateOfSurgery instanceof Timestamp)
      {
        this.dateOfSurgery = ((Timestamp) dateOfSurgery).toLocalDateTime().format(dateTimeFormatter);
      }
    }
  }

  /**
   * Validate of date of surgery
   */
  private void validateDateOfSurgery()
  {
    if (StringUtils.isNotBlank(dateOfSurgery))
    {
      try
      {
        LocalDateTime.parse(dateOfSurgery, dateTimeFormatter);
      }
      catch (final DateTimeParseException exception)
      {
        addFault(new Fault("dateOfSurgery", exception.getMessage()));
      }
    }
  }

  /**
   * Get the date of injury
   *
   * @return the date of injury
   */
  public String getDateOfInjury()
  {
    return dateOfInjury;
  }

  /**
   * Get the date of injury as LocalDateTime type
   *
   * @return the date of injury as LocalDateTime type
   */
  public LocalDateTime getDateOfInjuryAsLocalDateTime()
  {
    if (StringUtils.isNotBlank(dateOfInjury))
    {
      return LocalDateTime.parse(dateOfInjury, dateTimeFormatter);
    }

    return null;
  }

  /**
   * Get the date of injury as Timestamp type
   *
   * @return the date of injury as Timestamp type
   */
  public Timestamp getDateOfInjuryAsTimestamp()
  {
    if (StringUtils.isNotBlank(dateOfInjury))
    {
      return Timestamp.valueOf(LocalDateTime.parse(dateOfInjury, dateTimeFormatter));
    }

    return null;
  }

  /**
   * Set the date of injury
   *
   * @param dateOfInjury the date of injury
   */
  public void setDateOfInjury(final Object dateOfInjury)
  {
    if (dateOfInjury != null)
    {
      if (dateOfInjury instanceof String)
      {
        this.dateOfInjury = (String) dateOfInjury;
      }
      else if (dateOfInjury instanceof LocalDateTime)
      {
        this.dateOfInjury = ((LocalDateTime) dateOfInjury).format(dateTimeFormatter);
      }
      else if (dateOfInjury instanceof Timestamp)
      {
        this.dateOfInjury = ((Timestamp) dateOfInjury).toLocalDateTime().format(dateTimeFormatter);
      }
    }
  }

  /**
   * Set the date of injury
   */
  private void validateDateOfInjury()
  {
    if (StringUtils.isNotBlank(dateOfInjury))
    {
      try
      {
        LocalDateTime.parse(dateOfInjury, dateTimeFormatter);
      }
      catch (final DateTimeParseException exception)
      {
        addFault(new Fault("dateOfInjury", exception.getMessage()));
      }
    }
  }

  /**
   * Get the place of occurrence
   *
   * @return the place of occurrence
   */
  public String getPlaceOfOccurrence()
  {
    return placeOfOccurrence;
  }

  /**
   * Set the place of occurrence
   *
   * @param placeOfOccurrence the place of occurrence
   */
  public void setPlaceOfOccurrence(final String placeOfOccurrence)
  {
    this.placeOfOccurrence = placeOfOccurrence;
  }

  /**
   * Validate the place of occurrence
   */
  private void validatePlaceOfOccurrence()
  {
    if (StringUtils.isNotBlank(placeOfOccurrence) && !("0".equals(placeOfOccurrence) || // home
                                                       "1".equals(placeOfOccurrence) || // residential institution
                                                       "2".equals(placeOfOccurrence) || // school, other institution, public administrative area
                                                       "3".equals(placeOfOccurrence) || // sports and athletics area
                                                       "4".equals(placeOfOccurrence) || // street and highway
                                                       "5".equals(placeOfOccurrence) || // trade and service area
                                                       "6".equals(placeOfOccurrence) || // industrial and construction area
                                                       "7".equals(placeOfOccurrence) || // farm
                                                       "8".equals(placeOfOccurrence) || // other specified place
                                                       "9".equals(placeOfOccurrence)))  // unspecified place
    {
      addFault(new Fault("placeOfOccurrence", "Valid values: 0, 1, 2, 3, 4, 5, 6, 7, 8 or 9"));
    }
  }

  /**
   * Get the activity code
   *
   * @return the activity code
   */
  public String getActivityCode()
  {
    return activityCode;
  }

  /**
   * Set the activity code
   *
   * @param activityCode the activity code
   */
  public void setActivityCode(final String activityCode)
  {
    this.activityCode = activityCode;
  }

  /**
   * Validate the activity code
   */
  private void validateActivityCode()
  {
    if (StringUtils.isNotBlank(activityCode) && !("0".equals(activityCode) || // sports
                                                  "1".equals(activityCode) || // leisure
                                                  "2".equals(activityCode) || // working for income
                                                  "3".equals(activityCode) || // other work
                                                  "4".equals(activityCode) || // resting, sleeping, eating or other vital activities
                                                  "8".equals(activityCode) || // other activities
                                                  "9".equals(activityCode)))  // unspecified activity
    {
      addFault(new Fault("activityCode", "Valid values: 0, 1, 2, 3, 4, 8 or 9"));
    }
  }

  /**
   * Get if woman was pregnant
   *
   * @return if woman was pregnant
   */
  public String getPregnancy()
  {
    return pregnancy;
  }

  /**
   * Set if woman was pregnant
   *
   * @param pregnancy if woman was pregnant
   */
  public void setPregnancy(final String pregnancy)
  {
    this.pregnancy = pregnancy;
  }

  /**
   * Validate if woman was pregnant
   */
  private void validatePregnancy()
  {
    if (StringUtils.isNotBlank(pregnancy) && !("0".equals(pregnancy) || // pregnant at time of death
                                               "1".equals(pregnancy) || // had been pregnant within 42 days before death
                                               "2".equals(pregnancy) || // had been pregnant within 43 days and 1 year before death
                                               "3".equals(pregnancy) || // not pregnant
                                               "9".equals(pregnancy)))  // unknown
    {
      addFault(new Fault("pregnancy", "Valid values: 0, 1, 2, 3 or 9"));
    }
  }

  /**
   * Get if the pregnancy did contribute to the death
   *
   * @return if the pregnancy did contribute to the death
   */
  public String getPregnancyContributeDeath()
  {
    return pregnancyContributeDeath;
  }

  /**
   * Set if the pregnancy did contribute to the death
   *
   * @param pregnancyContributeDeath if the pregnancy did contribute to the death
   */
  public void setPregnancyContributeDeath(final String pregnancyContributeDeath)
  {
    this.pregnancyContributeDeath = pregnancyContributeDeath;
  }

  /**
   * Validate if the pregnancy did contribute to the death
   */
  private void validatePregnancyContributeDeath()
  {
    if (StringUtils.isNotBlank(pregnancyContributeDeath) && !("0".equals(pregnancyContributeDeath) || // no
                                                              "1".equals(pregnancyContributeDeath) || // yes
                                                              "2".equals(pregnancyContributeDeath)))  // unknown
    {
      addFault(new Fault("pregnancyContributeDeath", "Valid values: 0, 1 or 2"));
    }
  }

  /**
   * Get the stillborn children
   *
   * @return the stillborn children
   */
  public String getStillbirth()
  {
    return stillbirth;
  }

  /**
   * Set the stillborn children
   *
   * @param stillbirth the stillborn children
   */
  public void setStillbirth(final String stillbirth)
  {
    this.stillbirth = stillbirth;
  }

  /**
   * Set the stillborn children
   *
   * @param stillbirth the stillborn children
   */
  private void validateStillbirth()
  {
    if (StringUtils.isNotBlank(stillbirth) && !("1".equals(stillbirth) || // yes
                                                "U".equals(stillbirth) || // unknown
                                                "N".equals(stillbirth)))  // no
    {
      addFault(new Fault("stillbirth", "Valid values: 1, U or N"));
    }
  }

  /**
   * Get the fetus/newborn child in multiple pregnancy
   *
   * @return the fetus/newborn child in multiple pregnancy
   */
  public String getMultiplePregnancy()
  {
    return multiplePregnancy;
  }

  /**
   * Set the fetus/newborn child in multiple pregnancy
   *
   * @param multiplePregnancy the fetus/newborn child in multiple pregnancy
   */
  public void setMultiplePregnancy(final String multiplePregnancy)
  {
    this.multiplePregnancy = multiplePregnancy;
  }

  /**
   * Validate the fetus/newborn child in multiple pregnancy
   */
  private void validateMultiplePregnancy()
  {
    if (StringUtils.isNotBlank(multiplePregnancy) && !("1".equals(multiplePregnancy) || // yes
                                                       "U".equals(multiplePregnancy) || // unknown
                                                       "N".equals(multiplePregnancy)))  // no
    {
      addFault(new Fault("multiplePregnancy", "Valid values: 1, U or N"));
    }
  }

  /**
   * Get the number of completed weeks of pregnancy
   *
   * @return the number of completed weeks of pregnancy
   */
  public String getCompletedWeeks()
  {
    return completedWeeks;
  }

  /**
   * Set the number of completed weeks of pregnancy
   *
   * @param completedWeeks the number of completed weeks of pregnancy
   */
  public void setCompletedWeeks(final String completedWeeks)
  {
    this.completedWeeks = completedWeeks;
  }

  /**
   * Validate the number of completed weeks of pregnancy
   */
  private void validateCompletedWeeks()
  {
    if (StringUtils.isNotBlank(completedWeeks))
    {
      try
      {
        var weeks = Integer.parseInt(completedWeeks);

        if (weeks < 0 || weeks > 99)
        {
          addFault(new Fault("completedWeeks", "Range from 0 to 99 weeks"));
        }
      }
      catch (final NumberFormatException exception)
      {
        addFault(new Fault("completedWeeks", exception.getMessage()));
      }
    }
  }

  /**
   * Get the birthweight in grams
   *
   * @return the birthweight in grams
   */
  public String getBirthWeight()
  {
    return birthWeight;
  }

  /**
   * Set the birthweight in grams
   *
   * @param birthWeight the birthweight in grams
   */
  public void setBirthWeight(final String birthWeight)
  {
    this.birthWeight = birthWeight;
  }

  /**
   * Validate the birthweight in grams
   */
  private void validateBirthWeight()
  {
    if (StringUtils.isNotBlank(birthWeight))
    {
      try
      {
        var weight = Integer.parseInt(birthWeight);

        if (weight < 0 || weight > 9999)
        {
          addFault(new Fault("birthWeight", "Range from 0 to 9999 grams"));
        }
      }
      catch (final NumberFormatException exception)
      {
        addFault(new Fault("birthWeight", exception.getMessage()));
      }
    }
  }

  /**
   * Get the age of mother in years
   *
   * @return the age of mother in years
   */
  public String getAgeOfMother()
  {
    return ageOfMother;
  }

  /**
   * Set the age of mother in years
   *
   * @param ageOfMother the age of mother in years
   */
  public void setAgeOfMother(final String ageOfMother)
  {
    this.ageOfMother = ageOfMother;
  }

  /**
   * Validate the age of mother in years
   */
  private void validateAgeOfMother()
  {
    if (StringUtils.isNotBlank(ageOfMother))
    {
      try
      {
        var number = Integer.parseInt(ageOfMother);

        if (number < 0 || number > 99)
        {
          addFault(new Fault("ageOfMother", "Range from 0 to 99 years"));
        }
      }
      catch (final NumberFormatException exception)
      {
        addFault(new Fault("ageOfMother", exception.getMessage()));
      }
    }
  }

  /**
   * Get the list of the medical causes of death
   *
   * @return the list of the medical causes of death
   */
  public List<MedCod> getMedCods()
  {
    return medCods;
  }

  /**
   * Set the list of the medical causes of death
   *
   * @param medCods the list of the medical causes of death
   */
  public void setMedCods(final List<MedCod> medCods)
  {
    this.medCods = medCods;
  }

  /**
   * Validate the list of the medical causes of death
   */
  private void validateMedCods()
  {
    if (medCods != null)
    {
      final boolean[] lines = { true, true, true, true, true, true };

      var isError = false;

      for (MedCod medCod : medCods)
      {
        medCod.validate(certificateKey);

        if (medCod.getFaults() != null)
        {
          if (!isError)
          {
            addFault(new Fault("medCod", "Inconsistencies observed in the MedCod record"));

            isError = true;
          }
        }
        else
        {
          final var line = Integer.parseInt(medCod.getLineNb());

          if (lines[line])
          {
            lines[line] = false;
          }
          else if (!isError)
          {
            addFault(new Fault("medCod", "Repeated lines in medical causes of death table"));

            isError = true;
          }
        }
      }
    }
    else
    {
      addFault(new Fault("medCod", "Empty medical causes of death table"));
    }
  }

  /**
   * Get the list of inconsistencies observed in the Ident record
   *
   * @return the list of inconsistencies observed in the Ident record
   */
  public List<Fault> getFaults()
  {
    return faults;
  }

  /**
   * Add the inconsistencia observed in the Ident record
   *
   * @param fault the inconsistencia observed in the Ident record
   */
  private void addFault(final Fault fault)
  {
    if (faults == null)
    {
      faults = new LinkedList<>();
    }

    faults.add(fault);
  }

  /**
   * Set the list of inconsistencies observed in the Ident record
   *
   * @param faults the list of inconsistencies observed in the Ident record
   */
  public void setFaults(final List<Fault> faults)
  {
    this.faults = faults;
  }

  /**
   * Validate the fields the identification table (Ident)
   *
   * @param dateTimeFormatter the formatter for printing and parsing date-time objects
   * @return true if the fields are filled in correctly, false otherwise
   */
  public boolean validate(final DateTimeFormatter dateTimeFormatter)
  {
    this.dateTimeFormatter = dateTimeFormatter;

    validateCertificateKey();

    validateDateBirth();

    validateDateDeath();

    validateAge();

    validateSex();

    validateMannerOfDeath();

    validateCodingFlags();

    validateAutopsyRequested();

    validateAutopsyUsed();

    validateRecentSurgery();

    validateDateOfSurgery();

    validateDateOfInjury();

    validatePlaceOfOccurrence();

    validateActivityCode();

    validatePregnancy();

    validatePregnancyContributeDeath();

    validateStillbirth();

    validateMultiplePregnancy();

    validateCompletedWeeks();

    validateBirthWeight();

    validateAgeOfMother();

    validateMedCods();

    if (StringUtils.isBlank(age) && (StringUtils.isBlank(dateBirth) || StringUtils.isBlank(dateDeath)))
    {
      addFault(new Fault("age", "Age value is required or birth and death dates"));
    }

    return faults == null;
  }
}
