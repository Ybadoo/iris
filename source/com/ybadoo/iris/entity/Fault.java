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
 * Inconsistencies observed in the Ident and MedCod records
 */
@XmlRootElement(name = "fault")
@XmlAccessorType (XmlAccessType.FIELD)
public class Fault implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Field name in the Ident or MedCod records
   */
  private String field;

  /**
   * Description of inconsistency observed in the field
   */
  private String message;

  /**
   * Default constructor
   */
  public Fault()
  {
    // Required for the JAXBContext
  }

  /**
   * Constructor to fill the attributes
   *
   * @param field the field name in the Ident or MedCod records
   * @param message the description of inconsistency observed in the field
   */
  public Fault(final String field, final String message)
  {
    this.field = field;

    this.message = message;
  }

  /**
   * Get the field name in the Ident or MedCod records
   *
   * @return the field name in the Ident or MedCod records
   */
  public String getField()
  {
    return field;
  }

  /**
   * Get the description of inconsistency observed in the field
   *
   * @return the description of inconsistency observed in the field
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Set the field name in the Ident or MedCod records
   *
   * @param field the field name in the Ident or MedCod records
   */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Set the description of inconsistency observed in the field
   *
   * @param message the description of inconsistency observed in the field
   */
  public void setMessage(final String message)
  {
    this.message = message;
  }
}
