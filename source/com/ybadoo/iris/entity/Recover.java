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

import org.apache.commons.lang3.StringUtils;

/**
 * Monitoring the processing of the request to Iris
 */
@XmlRootElement(name = "recover")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Recover implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Status of processing
   */
  private String status;

  /**
   * Identifier of the processing
   */
  private String uid;

  /**
   * Init the class
   */
  public Recover()
  {
    // Required for the JAXBContext
  }

  /**
   * Init the class
   *
   * @param field field name
   * @param message error message
   */
  public Recover(final String uid, final String status)
  {
    this.uid = uid;

    this.status = status;
  }

  /**
   * Get the processing queue position
   *
   * @return processing queue position
   */
  public String getStatus()
  {
    return status;
  }

  /**
   * Get the identifier of the processing
   *
   * @return identifier of the processing
   */
  public String getUid()
  {
    return uid;
  }

  /**
   * Set the processing queue position
   *
   * @param position processing queue position
   */
  public void setStatus(final String status)
  {
    this.status = status;
  }

  /**
   * Set the identifier of the processing
   *
   * @param uid identifier of the processing
   */
  public void setUid(final String uid)
  {
    this.uid = uid;
  }

  /**
   * @return
   */
  public boolean validate()
  {
    return StringUtils.isNotBlank(uid);
  }
}
