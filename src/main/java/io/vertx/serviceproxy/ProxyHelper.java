/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.serviceproxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Constructor;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ProxyHelper {

  public static <T> T createProxy(Class<T> clazz, Vertx vertx, String address, ObjectMapper objectMapper) {
    String proxyClassName = clazz.getName() + "VertxEBProxy";
    Class<?> proxyClass = loadClass(proxyClassName, clazz);
    Constructor constructor = getConstructor(proxyClass, Vertx.class, String.class, ObjectMapper.class);
    Object instance = createInstance(constructor, vertx, address, objectMapper);
    return (T)instance;
  }

  public static final long DEFAULT_CONNECTION_TIMEOUT = 5 * 60; // 5 minutes

  public static <T> MessageConsumer<JsonObject> registerService(Class<T> clazz, Vertx vertx, T service, String address,
                                                                ObjectMapper objectMapper) {
    // No timeout - used for top level services
    return registerService(clazz, vertx, service, address, DEFAULT_CONNECTION_TIMEOUT, objectMapper);
  }

  public static <T> MessageConsumer<JsonObject> registerService(Class<T> clazz, Vertx vertx, T service, String address,
                                                                long timeoutSeconds, ObjectMapper objectMapper) {
    // No timeout - used for top level services
    return registerService(clazz, vertx, service, address, true, timeoutSeconds, objectMapper);
  }

  public static <T> MessageConsumer<JsonObject> registerService(Class<T> clazz, Vertx vertx, T service, String address,
                                                                boolean topLevel,
                                                                long timeoutSeconds, ObjectMapper objectMapper) {
    String handlerClassName = clazz.getName() + "VertxProxyHandler";
    Class<?> handlerClass = loadClass(handlerClassName, clazz);
    Constructor constructor = getConstructor(handlerClass, Vertx.class, clazz, boolean.class, long.class, ObjectMapper.class);
    Object instance = createInstance(constructor, vertx, service, topLevel, timeoutSeconds, objectMapper);
    ProxyHandler handler = (ProxyHandler)instance;
    return handler.registerHandler(address);
  }

  private static Class<?> loadClass(String name, Class origin) {
    try {
      return origin.getClassLoader().loadClass(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Cannot find proxyClass: " + name, e);
    }
  }

  private static Constructor getConstructor(Class<?> clazz, Class<?>... types) {
    try {
      return clazz.getDeclaredConstructor(types);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Cannot find constructor on: " + clazz.getName(), e);
    }
  }

  private static Object createInstance(Constructor constructor, Object... args) {
    try {
      return constructor.newInstance(args);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to call constructor on", e);
    }
  }
}
