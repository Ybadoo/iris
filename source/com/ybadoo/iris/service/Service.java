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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.ybadoo.iris.constant.ProcessingStatus;
import com.ybadoo.iris.database.Database;
import com.ybadoo.iris.database.MySQLDatabase;
import com.ybadoo.iris.database.PostgreSQLDatabase;
import com.ybadoo.iris.datasource.HostDatasource;
import com.ybadoo.iris.datasource.ClusterDatasource;
import com.ybadoo.iris.datasource.Datasource;
import com.ybadoo.iris.engine.AccessEngine;
import com.ybadoo.iris.engine.DatabaseEngine;
import com.ybadoo.iris.engine.Engine;
import com.ybadoo.iris.entity.Ident;
import com.ybadoo.iris.entity.Iris;
import com.ybadoo.iris.entity.Recover;
import com.ybadoo.iris.exception.BackendException;
import com.ybadoo.iris.exception.FrontendException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Front Controller das requisicoes ao webservice da ferramenta IRIS
 */
public abstract class Service extends HttpServlet
{
  /**
   * Log of application
   */
  private static final Logger logger = Logger.getLogger(Service.class.getName());

  /**
   * Version number of serializable class
   */
  private static final long serialVersionUID = 1L;

  /**
   * Identifier of the session key to the initialization error of the IRIS tool
   */
  private static final String SERVLET_CONTEXT_HOST_KEY = "iris.host";

  /**
   * Identifier of the session key to the initialization error of the IRIS tool
   */
  private static final String SERVLET_CONTEXT_CLUSTER_KEY = "iris.cluster";

  /**
   * Identifier of the session key to the IRIS tool container
   */
  private static final String SERVLET_CONTEXT_ENGINE_KEY = "iris.engine";

  /**
   * Identifier of the session key to the initialization error of the IRIS tool
   */
  private static final String SERVLET_CONTEXT_ERROR_KEY = "iris.error";

  /**
   * Mask for format for printing and parsing date objects
   */
  private DateTimeFormatter dateTimeFormatter;

  /* (non-Javadoc)
   * @see jakarta.servlet.GenericServlet#destroy()
   */
  @Override
  public void destroy()
  {
    destroyDatasource(SERVLET_CONTEXT_HOST_KEY);

    destroyDatasource(SERVLET_CONTEXT_CLUSTER_KEY);

    getServletContext().removeAttribute(SERVLET_CONTEXT_ENGINE_KEY);

    getServletContext().removeAttribute(SERVLET_CONTEXT_ERROR_KEY);
  }

  /**
   * Close of database connection in the datasource
   *
   * @param datasourceKey datasource session key
   */
  private void destroyDatasource(final String datasourceKey)
  {
    final ServletContext servletContext = getServletContext();

    if (servletContext.getAttribute(datasourceKey) != null)
    {
      final var datasource = (Datasource) getServletContext().getAttribute(datasourceKey);

      try
      {
        datasource.close();
      }
      catch (final BackendException exception)
      {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
      }

      servletContext.removeAttribute(datasourceKey);
    }
  }

  /* (non-Javadoc)
   * @see jakarta.servlet.http.HttpServlet#doGet(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
   */
  @Override
  protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
  {
    response.setCharacterEncoding("UTF-8");

    response.setContentType(getContentType());

    try (final var writer = response.getWriter())
    {
      writer.print(errorMarshal(new UnsupportedOperationException("GET method unsupported")));
    }
    catch (final IllegalStateException | UnsupportedEncodingException exception)
    {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
  }

  /* (non-Javadoc)
   * @see jakarta.servlet.http.HttpServlet#doPost(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
   */
  @Override
  protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
  {
    response.setCharacterEncoding("UTF-8");

    response.setContentType(getContentType());

    try (final var writer = response.getWriter())
    {
      if (getServletContext().getAttribute(SERVLET_CONTEXT_ERROR_KEY) != null)
      {
        writer.print(errorMarshal((Exception) request.getServletContext().getAttribute(SERVLET_CONTEXT_ERROR_KEY)));

        return;
      }

      final var engine = (Engine) getServletContext().getAttribute(SERVLET_CONTEXT_ENGINE_KEY);

      if (engine.getException() != null)
      {
        writer.print(errorMarshal(engine.getException()));

        return;
      }

      final var bodyRequest = new StringBuilder();

      try (final var reader = request.getReader())
      {
        String line = null;

        while ((line = reader.readLine()) != null)
        {
          bodyRequest.append(line);
        }
      }
      catch (final IOException exception)
      {
        writer.print(errorMarshal(exception));
      }

      Iris iris = null;

      try
      {
        iris = irisUnmarshal(bodyRequest.toString());
      }
      catch (final JAXBException exception)
      {
        writer.print(errorMarshal(exception));

        return;
      }

      if ((iris.getCertificates() == null || iris.getCertificates().isEmpty()) && iris.getRecover() == null)
      {
        writer.print(errorMarshal(new IllegalArgumentException("No arguments passed in the request")));
      }
      else if (iris.getCertificates() != null && !iris.getCertificates().isEmpty() && iris.getRecover() != null)
      {
        writer.print(errorMarshal(new IllegalArgumentException("Many arguments passed in the request")));
      }
      else if (iris.getCertificates() != null)
      {
        try
        {
          writer.print(processCertificates(request.getSession().getId(), iris.getCertificates()));
        }
        catch (final BackendException exception)
        {
          logger.log(Level.SEVERE, exception.getMessage(), exception);

          writer.print(errorMarshal(exception));

          getServletContext().setAttribute(SERVLET_CONTEXT_ERROR_KEY, exception);
        }
      }
      else if (iris.getRecover() != null)
      {
        try
        {
          writer.print(processRecover(request.getSession().getId(), iris.getRecover()));
        }
        catch (final BackendException exception)
        {
          logger.log(Level.SEVERE, exception.getMessage(), exception);

          writer.print(errorMarshal(exception));

          getServletContext().setAttribute(SERVLET_CONTEXT_ERROR_KEY, exception);
        }
        catch (final FrontendException exception)
        {
          writer.print(errorMarshal(exception));
        }
      }
      else
      {
        writer.print(errorMarshal(new NullPointerException("Unknown kind of entity was encountered")));
      }
    }
    catch (final IllegalStateException | UnsupportedEncodingException exception)
    {
      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
  }

  /**
   * Return the error occurs during processing
   *
   * @param exception occurs during processing
   * @return error occurs during processing
   */
  protected abstract String errorMarshal(final Exception exception);

  /**
   * Get the content type of the response
   *
   * @return content type of the response
   */
  protected abstract String getContentType();

  /* (non-Javadoc)
   * @see jakarta.servlet.GenericServlet#init(jakarta.servlet.ServletConfig)
   */
  @Override
  public void init(final ServletConfig servletConfig) throws ServletException
  {
    super.init(servletConfig);

    final ServletContext servletContext = servletConfig.getServletContext();

    try
    {
      final var hostServer = servletContext.getInitParameter("host.database.server");

      Database hostDatabase = null;

      if ("MySQL".equals(hostServer))
      {
        hostDatabase = new MySQLDatabase();
      }
      else if ("PostgreSQL".equals(hostServer))
      {
        hostDatabase = new PostgreSQLDatabase();
      }
      else
      {
        throw new BackendException("Key 'host.database.server' is invalid");
      }

      hostDatabase.setAddress(servletContext.getInitParameter("host.database.url"), "host.database.url");

      hostDatabase.setSchema(servletContext.getInitParameter("host.database.schema"), "host.database.schema");

      hostDatabase.setUsername(servletContext.getInitParameter("host.database.user"), "host.database.user");

      hostDatabase.setPassword(servletContext.getInitParameter("host.database.password"), "host.database.password");

      dateTimeFormatter = DateTimeFormatter.ofPattern(servletContext.getInitParameter("host.database.dateTimePattern"));

      final var hostDatasource = new HostDatasource(hostDatabase, dateTimeFormatter);

      hostDatasource.validate();

      servletContext.setAttribute(SERVLET_CONTEXT_HOST_KEY, hostDatasource);

      var clusterDatabase = hostDatabase;

      if ("true".equals(servletContext.getInitParameter("cluster.available")))
      {
        final String clusterServer = servletContext.getInitParameter("cluster.database.server");

        if ("MySQL".equals(clusterServer))
        {
          clusterDatabase = new MySQLDatabase();
        }
        else if ("PostgreSQL".equals(clusterServer))
        {
          clusterDatabase = new PostgreSQLDatabase();
        }
        else
        {
          throw new BackendException("Key 'cluster.database.server' is invalid");
        }

        clusterDatabase.setAddress(servletContext.getInitParameter("cluster.database.url"), "cluster.database.url");

        clusterDatabase.setSchema(servletContext.getInitParameter("cluster.database.schema"), "cluster.database.schema");

        clusterDatabase.setUsername(servletContext.getInitParameter("cluster.database.user"), "cluster.database.user");

        clusterDatabase.setPassword(servletContext.getInitParameter("cluster.database.password"), "cluster.database.password");
      }

      final var datasourceCluster = new ClusterDatasource(clusterDatabase, servletContext.getInitParameter("host.uid"));

      datasourceCluster.validate();

      servletContext.setAttribute(SERVLET_CONTEXT_CLUSTER_KEY, datasourceCluster);

      final var irisServer = servletContext.getInitParameter("host.iris.server");

      Engine engine = null;

      if ("OLEDB".equals(irisServer))
      {
        final var engineAccess = new AccessEngine(hostDatasource);

        engineAccess.setFileMDB(servletContext.getInitParameter("host.OLEDB.datasource"));

        engineAccess.setLotName(servletContext.getInitParameter("host.OLEDB.lotName"));

        engine = engineAccess;
      }
      else if ("PostgreSQL".equals(irisServer) || "MySQL".equals(irisServer))
      {
        engine = new DatabaseEngine(hostDatasource);
      }
      else
      {
        throw new BackendException("Key 'host.iris.server' is invalid");
      }

      engine.setWine(servletContext.getInitParameter("host.iris.wine"));

      engine.setExecutable(servletContext.getInitParameter("host.iris.executable"));

      engine.setTimeout(servletContext.getInitParameter("host.iris.timeout"));

      servletContext.setAttribute(SERVLET_CONTEXT_ENGINE_KEY, engine);
    }
    catch (final Exception exception)
    {
      servletContext.setAttribute(SERVLET_CONTEXT_ERROR_KEY, exception);

      logger.log(Level.SEVERE, exception.getMessage(), exception);
    }
  }

  /**
   * Return the JSON / XML document as a IRIS message
   *
   * @param document JSON / XML document
   * @return identifies the JSON / XML document as a IRIS message
   * @throws JAXBException exception in unmarshal document
   */
  private Iris irisUnmarshal(final String document) throws JAXBException
  {
    return (Iris) unmarshal(Iris.class, document);
  }

  /**
   * Convert the Java object to the text (JSON or XML)
   *
   * @param klass class of Java object
   * @param object java object
   * @return text (JSON or XML) to convert
   */
  protected abstract String marshal(final Class<?> klass, final Object object);

  /**
   * Process certificates submitted by the owner
   *
   * @param certificates list of certificates
   * @return monitoring the processing of the request to Iris or error or list of certificates invalids
   * @throws BackendException
   */
  private String processCertificates(final String owner, final List<Ident> certificates) throws BackendException
  {
    final boolean single = certificates.size() < 2;

    if (single)
    {
      final var certificateKey = RandomStringUtils.randomAlphabetic(30);

      certificates.get(0).configCertificateKey(certificateKey);
    }

    final var iris = new Iris();

    if (validateCertificates(certificates))
    {
      final var hostDatasource = (HostDatasource) getServletContext().getAttribute(SERVLET_CONTEXT_HOST_KEY);

      final var recover = new Recover(hostDatasource.beginProcess(owner, certificates), ProcessingStatus.READY.toString());

      iris.setRecover(recover);

      final var engine = (Engine) getServletContext().getAttribute(SERVLET_CONTEXT_ENGINE_KEY);

      engine.process();
    }
    else
    {
      if (single)
      {
        certificates.get(0).configCertificateKey(null);
      }

      iris.setCertificates(certificates);
    }

    return marshal(Iris.class, iris);
  }

  /**
   * Process the monitoring of the request to Iris
   *
   * @param recover monitoring the processing of the request to Iris
   * @return monitoring the processing of the request to Iris or error or list of certificates processed
   * @throws FrontendException
   * @throws BackendException
   */
  private String processRecover(final String owner, final Recover recover) throws BackendException, FrontendException
  {
    if (recover.validate())
    {
      final var hostDatasource = (HostDatasource) getServletContext().getAttribute(SERVLET_CONTEXT_HOST_KEY);

      final var iris = hostDatasource.recover(owner, recover);

      if (iris.getCertificates() != null)
      {
        final var clusterDatasource = (ClusterDatasource) getServletContext().getAttribute(SERVLET_CONTEXT_CLUSTER_KEY);

        clusterDatasource.processCluster(iris.getCertificates());

        if (iris.getCertificates().size() < 2)
        {
          iris.getCertificates().get(0).configCertificateKey(null);
        }
      }
      else
      {
        final var engine = (Engine) getServletContext().getAttribute(SERVLET_CONTEXT_ENGINE_KEY);

        engine.process();
      }

      return marshal(Iris.class, iris);
    }

    return errorMarshal(new NullPointerException("uid is empty"));
  }

  /**
   * Convert the text (JSON or XML) to the Java object
   *
   * @param klass class of Java object
   * @param text text (JSON or XML) to convert
   * @return Java object
   * @throws JAXBException problems in the conversion
   */
  protected abstract Object unmarshal(final Class<?> klass, final String text) throws JAXBException;

  /**
   * Validate the list of certificates
   *
   * @param certificates list of certificates
   * @return true if list of certificates is correct, false otherwise
   */
  private boolean validateCertificates(final List<Ident> certificates)
  {
    final List<String> certificateKeys = new ArrayList<>();

    var isCorrect = true;

    for (Ident ident: certificates)
    {
      isCorrect = ident.validate(dateTimeFormatter) && isCorrect;

      final var certificateKey = ident.getCertificateKey();

      if (StringUtils.isNotBlank(certificateKey) && certificateKey.length() < 31)
      {
        if (certificateKeys.contains(certificateKey))
        {
          ident.repeatedCertificateKey();

          isCorrect = false;
        }
        else
        {
          certificateKeys.add(certificateKey);
        }
      }
    }

    return isCorrect;
  }
}
