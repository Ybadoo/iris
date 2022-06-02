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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation of the icd10code table, which the ICD-10 codes returned to the user
 */
@XmlRootElement(name = "ucCode")
@XmlAccessorType (XmlAccessType.FIELD)
public class UCCode implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * ICD-10 Code
   */
  private String code;

  /**
   * Name of the ICD-10 Code
   */
  private String name;

  /**
   * If the ICD-10 Code is a garbage code
   */
  private boolean garbage;

  /**
   * Category of the unusable ICD-10 Code
   */
  @XmlElement(name = "category")
  private Category category;

  /**
   * Level of the garbage code
   */
  @XmlElement(name = "level")
  private Level level;

  /**
   * Note about the ICD-10 Code
   */
  private String note;

  /**
   * Default constructor
   */
  public UCCode()
  {
    // Required for the JAXBContext
  }

  /**
   * Constructor to fill the code attribute
   *
   * @param code the ICD-10 Code
   */
  public UCCode(final String code)
  {
    this.code = code;
  }

  /**
   * Get the category of the unusable ICD-10 Code
   *
   * @return the category of the unusable ICD-10 Code
   */
  public Category getCategory()
  {
    return category;
  }

  /**
   * Get the ICD-10 Code
   *
   * @return the ICD-10 Code
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Get if the ICD-10 Code is a garbage code
   *
   * @return if the ICD-10 Code is a garbage code
   */
  public boolean getGarbage()
  {
    return garbage;
  }

  /**
   * Get the level of the garbage code
   *
   * @return the level of the garbage code
   */
  public Level getLevel()
  {
    return level;
  }

  /**
   * Get the name of the ICD-10 Code
   *
   * @return the name of the ICD-10 Code
   */
  public String getName()
  {
    return name;
  }

  /**
   * Get the note about the ICD-10 Code
   *
   * @return the note about the ICD-10 Code
   */
  public String getNote()
  {
    return note;
  }

  /**
   * Set the category of the unusable ICD-10 Code
   *
   * @param category the category of the unusable ICD-10 Code
   */
  public void setCategory(final Category category)
  {
    this.category = category;
  }

  /**
   * Set the ICD-10 Code
   *
   * @param code the ICD-10 Code
   */
  public void setCode(final String code)
  {
    this.code = code;
  }

  /**
   * Set if the ICD-10 Code is a garbage code
   *
   * @param garbage if the ICD-10 Code is a garbage code
   */
  public void setGarbage(final boolean garbage)
  {
    this.garbage = garbage;
  }

  /**
   * @param garbage the garbage to set
   */
  public void setGarbage(final int garbage)
  {
    setGarbage(garbage != 0);
  }

  /**
   * Set the level of the garbage code
   *
   * @param level the level of the garbage code
   */
  public void setLevel(final Level level)
  {
    this.level = level;
  }

  /**
   * Set the name of the ICD-10 Code
   *
   * @param name the name of the ICD-10 Code
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * Set the note about the ICD-10 Code
   *
   * @param note the note about the ICD-10 Code
   */
  public void setNote(final String note)
  {
    this.note = note;
  }
}
