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

package com.ybadoo.iris.engine;

import com.ybadoo.iris.constant.LotType;
import com.ybadoo.iris.constant.ProcessingStatus;
import com.ybadoo.iris.datasource.HostDatasource;
import com.ybadoo.iris.entity.Manager;
import com.ybadoo.iris.exception.BackendException;

/**
 * Classe responsavel pelo processamento do lote na ferramenta IRIS, armazenado no banco de dados MySQL
 */
public class DatabaseEngine extends Engine
{
  /**
   * @param datasource
   */
  public DatabaseEngine(final HostDatasource datasource)
  {
    super(datasource);
  }

  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;
  /* (non-Javadoc)
   * @see com.ybadoo.iris.engine.Engine#preSynchronized(com.ybadoo.iris.entity.Manager)
   */
  @Override
  protected String preSynchronized(final Manager manager) throws BackendException
  {
    if (manager.getLotType() == LotType.UNIQUE)
    {
      datasource.convertDatabaseToIris(manager);
    }
    else
    {
      datasource.managerStatusUpdate(manager.getUid(), manager.getOwner(), ProcessingStatus.RUNNING);
    }

    return manager.getUid();
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.engine.Engine#posSynchronized(com.ybadoo.iris.entity.Manager)
   */
  @Override
  protected void posSynchronized(final Manager manager) throws BackendException
  {
    if (manager.getLotType() == LotType.UNIQUE)
    {
      datasource.convertIrisToDatabase(manager);
    }
    else
    {
      datasource.managerStatusUpdate(manager.getUid(), manager.getOwner(), ProcessingStatus.FINISHED);
    }
  }
}
