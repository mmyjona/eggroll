/*
 * Copyright (c) 2019 - now, Eggroll Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.eggroll.core.command

import java.lang.reflect.Method

import com.google.protobuf.{ByteString, GeneratedMessageV3}
import com.webank.eggroll.core.constant.StringConstants
import com.webank.eggroll.core.serdes.BaseSerializable
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.reflect.{ConstructorUtils, MethodUtils}

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

trait CommandRoutable {

}

object Scope {

}

object CommandRouter {
  private val serviceRouteTable = TrieMap[String, (Any, Method)]()
  private val messageParserMethodCache = mutable.Map[Class[_], Method]()

  // todo: consider different scope of instance like 'singleton', 'proto', 'session' etc,
  //  which can be implemented as an annotation reader
  def register(serviceName: String,
               serviceParamTypes: List[Class[_]],
               routeToClass: Class[_] = null,
               routeToMethodName: String = null,
               routeToCallBasedClassInstance: Any = null,
               callBasedClassInstanceInitArgs: List[AnyRef] = null): Unit = this.synchronized {
    if (serviceRouteTable.contains(serviceName)) {
      throw new IllegalStateException("Service " + serviceName
        + " has been registered at: " + serviceRouteTable(serviceName))
    }

    val finalRouteToMethodName =
      if (routeToMethodName != null) routeToMethodName
      else StringUtils.substringAfterLast(serviceName, StringConstants.DOT)

    val finalRouteToClass =
      if (routeToClass != null) routeToClass
      else Class.forName(StringUtils.substringBeforeLast(serviceName, StringConstants.DOT))

    val routeToMethod = MethodUtils.getAccessibleMethod(
      finalRouteToClass, finalRouteToMethodName, serviceParamTypes: _*)

    if (routeToMethod == null) {
      throw new NoSuchMethodException("accessible method not found for " + serviceName)
    }

    val finalCallBasedInstance =
      if (routeToCallBasedClassInstance != null) {
        routeToCallBasedClassInstance
      } else {
        val finalCallBasedClassInstanceInitArgsArray =
          if (callBasedClassInstanceInitArgs != null) callBasedClassInstanceInitArgs.toArray else null
        ConstructorUtils.invokeConstructor(finalRouteToClass, finalCallBasedClassInstanceInitArgsArray: _*)
      }

    serviceRouteTable.put(serviceName, (finalCallBasedInstance, routeToMethod))
  }

  def dispatch(serviceName: String, args: Array[_ <: AnyRef], kwargs: mutable.Map[String, _ <: AnyRef]): Array[Byte] = {
    val target = query(serviceName)
    if (target == null) {
      throw new IllegalStateException("service " + serviceName + " has not been registered")
    }

    val method = target._2
    val paramTypes = method.getParameterTypes

    // todo: separate to SerDes
    // deserialization
    val realArgs = args.zip(paramTypes).map {
      case (arg, paramType) => {
        if (classOf[GeneratedMessageV3].isAssignableFrom(paramType)) { // pb message
          var method: Method = null
          messageParserMethodCache.get(paramType) match {
            case m: Some[Method] => method = m.get
            case None =>
              method = paramType.getMethod(StringConstants.GRPC_PARSE_FROM, classOf[ByteString])
              messageParserMethodCache.put(paramType, method)
          }
          method.invoke(null, arg)
        } else { // todo: change here when non-grpc message call is supported
          arg
        }
      }
    }

    // actual call
    val callResult = target._2.invoke(target._1, realArgs: _*) // method.invoke(instance, args)

    // serialization to response
    callResult match {
      case serializer: BaseSerializable =>
        serializer.toBytes()
      case _ => // todo: change here when non-grpc message call is supported
        callResult.toString.getBytes()
    }
  }

  def query(serviceName: String): (Any, Method) = {
    serviceRouteTable(serviceName)
  }
}