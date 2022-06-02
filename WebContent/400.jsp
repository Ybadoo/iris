<%@ page language="java" contentType="application/json;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %><%--
Copyright (C) 2009/2022 - Cristiano Lehrer - ybadoo.com.br

This file is part of Vital Iris Web Service (IRIS)

IRIS is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

IRIS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with IRIS. If not, see <http://www.gnu.org/licenses/>.
--%><% out.print("{\"error\":{\"exception\":\"" + exception.getClass().getCanonicalName() + "\",\"message\":\"" + exception.getMessage().replace("\"", "") + "\"}}"); %>
