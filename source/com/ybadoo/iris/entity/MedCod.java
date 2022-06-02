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
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * Fields the medical causes of death table (MedCod)
 */
@XmlRootElement(name = "medCod")
@XmlAccessorType (XmlAccessType.FIELD)
public class MedCod implements Serializable
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
   * Line of medical causes of death table
   *
   * 0-4 lines A-E in Part 1
   * 5   line in Part 2
   */
  private String lineNb;

  /**
   * Diagnosis, if available
   */
  private String textLine;

  /**
   * Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   */
  private String codeLine;

  /**
   * Duration, onset or status for the diagnosis in the "TextLine" field
   */
  private String intervalLine;

  /**
   * If the "MedCod" record is in Code only mode
   */
  private String codeOnly;

  /**
   * If the "MedCod" record is in Data entry mode
   */
  private String lineCoded;

  /**
   * List of inconsistencies observed in the MedCod records
   */
  @XmlElement(name = "fault")
  private List<Fault> faults;

  /**
   * Get the identifier of the death certificate
   *
   * @return identifier of the death certificate
   */
  public String getCertificateKey()
  {
    return certificateKey;
  }

  /**
   * Set the identifier of the death certificate
   *
   * @param certificateKey identifier of the death certificate
   */
  public void setCertificateKey(final String certificateKey)
  {
    this.certificateKey = certificateKey;
  }

  /**
   * Validate the identifier of the death certificate
   *
   * @param identCertificateKey identifier of the death certificate in the Ident table
   */
  private void validateCertificateKey(final String identCertificateKey)
  {
    if (StringUtils.isNotBlank(certificateKey))
    {
      if (certificateKey.length() > 30)
      {
        addFault(new Fault("certificateKey", "Length is between 1 and 30 characters"));
      }
      else if (!certificateKey.equals(identCertificateKey))
      {
        addFault(new Fault("certificateKey", "Different certificateKey in the Ident table"));
      }
    }
    else
    {
      addFault(new Fault("certificateKey", "Field is empty"));
    }
  }

  /**
   * Get the line of medical causes of death table
   *
   * @return line of medical causes of death table
   */
  public String getLineNb()
  {
    return lineNb;
  }

  /**
   * Set the line of medical causes of death table
   *
   * @param lineNb line of medical causes of death table
   */
  public void setLineNb(final String lineNb)
  {
    this.lineNb = lineNb;
  }

  /**
   * Validate the line of medical causes of death table
   */
  private void validateLineNb()
  {
    if (StringUtils.isNotBlank(lineNb))
    {
      if (!("0".equals(lineNb) || // line A in Part 1
            "1".equals(lineNb) || // line B in Part 1
            "2".equals(lineNb) || // line C in Part 1
            "3".equals(lineNb) || // line D in Part 1
            "4".equals(lineNb) || // line E in Part 1
            "5".equals(lineNb)))  // line in Part 2
      {
        addFault(new Fault("lineNb", "Valid values: 0, 1, 2, 3, 4 or 5"));
      }
    }
    else
    {
      addFault(new Fault("lineNb", "Field is empty"));
    }
  }

  /**
   * Get the diagnosis, if available
   *
   * @return diagnosis, if available
   */
  public String getTextLine()
  {
    return textLine;
  }

  /**
   * Set the diagnosis, if available
   *
   * @param textLine diagnosis, if available
   */
  public void setTextLine(final String textLine)
  {
    this.textLine = textLine;
  }

  /**
   * Validate the diagnosis, if available
   */
  private void validateTextLine()
  {
    if (StringUtils.isNotBlank(textLine))
    {
      if (textLine.length() > 255)
      {
        addFault(new Fault("textLine", "Length is between 1 and 255 characters"));
      }
    }
    else if (StringUtils.isBlank(codeLine))
    {
      addFault(new Fault("textLine", "Field is empty"));
    }
  }

  /**
   * Get the Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   *
   * @return Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   */
  public String getCodeLine()
  {
    return codeLine;
  }

  /**
   * Set the Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   *
   * @param codeLine Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   */
  public void setCodeLine(final String codeLine)
  {
    this.codeLine = codeLine;
  }

  /**
   * Set the Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   *
   * @param codeLine Iris/MUSE adds ICD-10-WHO codes corresponding to the causes of death in the "TextLine" field, or user can add the ICD-10-WHO code from certificate if "TextLine" is empty
   */
  private void validateCodeLine()
  {
    if (StringUtils.isNotBlank(codeLine) && codeLine.length() > 255)
    {
      addFault(new Fault("codeLine", "Length is between 1 and 255 characters"));
    }
  }

  /**
   * Get the duration, onset or status for the diagnosis in the "TextLine" field
   *
   * @return duration, onset or status for the diagnosis in the "TextLine" field
   */
  public String getIntervalLine()
  {
    return intervalLine;
  }

  /**
   * Set the duration, onset or status for the diagnosis in the "TextLine" field
   *
   * @param intervalLine duration, onset or status for the diagnosis in the "TextLine" field
   */
  public void setIntervalLine(final String intervalLine)
  {
    this.intervalLine = intervalLine;
  }

  /**
   * Validate the duration, onset or status for the diagnosis in the "TextLine" field
   */
  private void validateIntervalLine()
  {
    if (StringUtils.isNotBlank(intervalLine) && intervalLine.length() > 255)
    {
      addFault(new Fault("intervalLine", "Length is between 1 and 255 characters"));
    }
  }

  /**
   * Get if the "MedCod" record is in Code only mode
   *
   * @return if the "MedCod" record is in Code only mode
   */
  public String getCodeOnly()
  {
    return codeOnly;
  }

  /**
   * Set if the "MedCod" record is in Code only mode
   *
   * @param codeOnly if the "MedCod" record is in Code only mode
   */
  public void setCodeOnly(final String codeOnly)
  {
    this.codeOnly = codeOnly;
  }

  /**
   * Set if the "MedCod" record is in Code only mode
   */
  private void validateCodeOnly()
  {
    if (StringUtils.isNotBlank(codeOnly) && !"1".equals(codeOnly))
    {
      addFault(new Fault("codeOnly", "Valid value: 1"));
    }
  }

  /**
   * Get if the "MedCod" record is in Data entry mode
   *
   * @return if the "MedCod" record is in Data entry mode
   */
  public String getLineCoded()
  {
    return lineCoded;
  }

  /**
   * Set if the "MedCod" record is in Data entry mode
   *
   * @param lineCoded if the "MedCod" record is in Data entry mode
   */
  public void setLineCoded(final String lineCoded)
  {
    this.lineCoded = lineCoded;
  }

  /**
   * Validate if the "MedCod" record is in Data entry mode
   */
  private void validateLineCoded()
  {
    if (StringUtils.isNotBlank(lineCoded) && !"1".equals(lineCoded))
    {
      addFault(new Fault("lineCoded", "Valid value: 1"));
    }
  }

  /**
   * Get the list of inconsistencies observed in the MedCod record
   *
   * @return list of inconsistencies observed in the MedCod record
   */
  public List<Fault> getFaults()
  {
    return faults;
  }

  /**
   * Add the inconsistencia observed in the MedCod record
   *
   * @param fault inconsistencia observed in the MedCod record
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
   * Set the list of inconsistencies observed in the MedCod record
   *
   * @param faults list of inconsistencies observed in the MedCod record
   */
  public void setTroubles(final List<Fault> faults)
  {
    this.faults = faults;
  }

  /**
   * Validate the fields the medical causes of death table (MedCod)
   *
   * @param identCertificateKey identifier of the death certificate in the Ident table
   * @return true if the fields are filled in correctly, false otherwise
   */
  public boolean validate(final String identCertificateKey)
  {
    validateCertificateKey(identCertificateKey);

    validateLineNb();

    validateTextLine();

    validateCodeLine();

    validateIntervalLine();

    validateCodeOnly();

    validateLineCoded();

    return faults == null;
  }
}
