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

package com.ybadoo.iris.constant;

/**
 * Processing status of the Iris web service
 */
public enum ProcessingStatus
{
  /**
   * The lot is in READY state when it’s ready to run and waiting for the Iris software
   */
  READY (0),

  /**
   * When the lot is in RUNNING state, it is being executed by Iris software
   */
  RUNNING (1),

  /**
   * The lot is done and the output will be returned to the user
   */
  FINISHED (2);

  /**
   * Value of processing status
   */
  private final int value;

  /**
   * Constructs a ProcessingStatus with the specified value of processing status
   *
   * @param value the value of processing status
   */
  private ProcessingStatus(final int value)
  {
    this.value = value;
  }

  /**
   * Get the value of processing status
   *
   * @return the value of processing status
   */
  public int getValue()
  {
    return value;
  }
}
