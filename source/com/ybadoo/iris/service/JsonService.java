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

package com.ybadoo.iris.service;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import com.ybadoo.iris.entity.Error;

import jakarta.servlet.annotation.WebServlet;

/**
 * Handling requests in JSON format
 */
@WebServlet("/v1/api/json")
public class JsonService extends Service
{
  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see com.ybadoo.iris.service.Service#errorMarshal(java.lang.Exception)
   */
  @Override
  protected String errorMarshal(final Exception exception)
  {
    final var error = new Error(exception.getClass(), exception.getMessage());

    try
    {
      final var jaxbContext = JAXBContext.newInstance(Error.class);

      final var marshaller = jaxbContext.createMarshaller();

      marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, getContentType());

      marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);

      final var stringWriter = new StringWriter();

      marshaller.marshal(error, stringWriter);

      return stringWriter.toString();
    }
    catch (final JAXBException jaxbException)
    {
      return "{\"error\":{\"exception\":\"" + jaxbException.getClass().getCanonicalName() + "\",\"message\":\"" + jaxbException.getMessage() + "\"}}";
    }
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.service.Service#getContentType()
   */
  @Override
  protected String getContentType()
  {
    return "application/json";
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.service.Service#marshal(java.lang.Class, java.lang.Object)
   */
  @Override
  protected String marshal(final Class<?> klass, final Object object)
  {
    try
    {
      final var jaxbContext = JAXBContext.newInstance(klass);

      final var marshaller = jaxbContext.createMarshaller();

      marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, getContentType());

      marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);

      final var stringWriter = new StringWriter();

      marshaller.marshal(object, stringWriter);

      return stringWriter.toString();
    }
    catch (final JAXBException exception)
    {
      return errorMarshal(exception);
    }
  }

  /* (non-Javadoc)
   * @see com.ybadoo.iris.service.Service#unmarshal(java.lang.Class, java.lang.String)
   */
  @Override
  protected Object unmarshal(final Class<?> klass, final String text) throws JAXBException
  {
    final var jaxbContext = JAXBContext.newInstance(klass);

    final var jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    jaxbUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, getContentType());

    jaxbUnmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);

    return jaxbUnmarshaller.unmarshal(new StringReader(text));
  }
}
