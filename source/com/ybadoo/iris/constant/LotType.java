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
 * Lot's type
 */
public enum LotType
{
  /**
   * Unique certificate in the lot
   */
  UNIQUE (0),

  /**
   * Multiple certificates in the lot
   */
  MULTIPLE (1);

  /**
   * Value of lot's type
   */
  private final int value;

  /**
   * Constructs a LotType with the specified value of lot's type
   *
   * @param value the value of lot's type
   */
  private LotType(final int value)
  {
    this.value = value;
  }

  /**
   * Get the value of lot's type
   *
   * @return the value of lot's type
   */
  public int getValue()
  {
    return value;
  }
}
