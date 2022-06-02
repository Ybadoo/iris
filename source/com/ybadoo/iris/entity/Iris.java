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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Identifies the JSON / XML document as a IRIS message
 */
@XmlRootElement(name = "iris")
@XmlAccessorType (XmlAccessType.FIELD)
public class Iris implements Serializable
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * List of certificates
   */
  @XmlElement(name = "ident")
  private List<Ident> certificates;

  /**
   * Monitoring the processing of the request to Iris
   */
  private Recover recover;

  /**
   * Return the list of certificates
   *
   * @return the list of certificates
   */
  public List<Ident> getCertificates()
  {
    return certificates;
  }

  /**
   * Get the monitoring the processing of the request to Iris
   *
   * @return the monitoring the processing of the request to Iris
   */
  public Recover getRecover()
  {
    return recover;
  }

  /**
   * Set the list of certificates
   *
   * @param idents the list of certificates
   */
  public void setCertificates(final List<Ident> certificates)
  {
    this.certificates = certificates;
  }

  /**
   * Set the monitoring the processing of the request to Iris
   *
   * @param recover the monitoring the processing of the request to Iris
   */
  public void setRecover(final Recover recover)
  {
    this.recover = recover;
  }
}
