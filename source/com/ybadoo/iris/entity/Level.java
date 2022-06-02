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
 * Levels of the garbage code
 */
@XmlRootElement(name = "level")
@XmlAccessorType (XmlAccessType.FIELD)
public class Level implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Identifier of the level
   */
  private int uid;

  /**
   * Name of the level
   */
  private String name;

  /**
   * Note about the level
   */
  private String note;

  /**
   * Default constructor
   */
  public Level ()
  {
    // Required for the JAXBContext
  }

  /**
   * Constructor to fill the attributes
   *
   * @param uid the identifier of the level
   * @param name the name of the level
   * @param note the note about the level
   */
  public Level (final int uid, final String name, final String note)
  {
    this.uid = uid;

    this.name = name;

    this.note = note;
  }

  /**
   * Get the name of the level
   *
   * @return the name of the level
   */
  public String getName()
  {
    return name;
  }

  /**
   * Get the note about the level
   *
   * @return the note about the level
   */
  public String getNote()
  {
    return note;
  }

  /**
   * Get the identifier of the level
   *
   * @return the identifier of the level
   */
  public int getUid()
  {
    return uid;
  }

  /**
   * Set the name of the level
   *
   * @param name the name of the level
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * Set the note about the level
   *
   * @param note the note about the level
   */
  public void setNote(final String note)
  {
    this.note = note;
  }

  /**
   * Set the identifier of the level
   *
   * @param uid the identifier of the level
   */
  public void setUid(final int uid)
  {
    this.uid = uid;
  }
}
