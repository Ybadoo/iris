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

import com.ybadoo.iris.constant.LotType;
import com.ybadoo.iris.constant.ProcessingStatus;

/**
 *
 */
public class Manager
{
  /**
   * Lot's type
   */
  private LotType lotType;

  /**
   *
   */
  private String owner;

  /**
   * Processing status of the Iris webservice
   */
  private ProcessingStatus status;

  /**
   *
   */
  private String uid;

  /**
   * Get the lot's type
   *
   * @return lot's type
   */
  public LotType getLotType()
  {
    return lotType;
  }

  /**
   * @return the owner
   */
  public String getOwner()
  {
    return owner;
  }

  /**
   * Get the processing status of the Iris webservice
   *
   * @return processing status of the Iris webservice
   */
  public ProcessingStatus getStatus()
  {
    return status;
  }

  /**
   * @return the uid
   */
  public String getUid()
  {
    return uid;
  }

  /**
   * Set the lot's type by your value
   *
   * @param value value of lot's type
   */
  public void setLotType(final int value)
  {
    if (value == LotType.UNIQUE.getValue())
    {
      setLotType(LotType.UNIQUE);
    }
    else
    {
      setLotType(LotType.MULTIPLE);
    }
  }

  /**
   * Set the lot's type
   *
   * @param lotType lot's type
   */
  public void setLotType(final LotType lotType)
  {
    this.lotType = lotType;
  }

  /**
   * @param owner the owner to set
   */
  public void setOwner(final String owner)
  {
    this.owner = owner;
  }

  /**
   * Set the processing status of the Iris webservice
   *
   * @param value value of processing status
   */
  public void setStatus(final int value)
  {
    if (value == ProcessingStatus.READY.getValue())
    {
      setStatus(ProcessingStatus.READY);
    }
    else if (value == ProcessingStatus.RUNNING.getValue())
    {
      setStatus(ProcessingStatus.RUNNING);
    }
    else
    {
      setStatus(ProcessingStatus.FINISHED);
    }
  }

  /**
   * Set the processing status of the Iris webservice
   *
   * @param status processing status of the Iris webservice
   */
  public void setStatus(final ProcessingStatus status)
  {
    this.status = status;
  }

  /**
   * @param uid the uid to set
   */
  public void setUid(final String uid)
  {
    this.uid = uid;
  }
}
