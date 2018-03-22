package com.github.pshirshov.izumi.idealingua.model

import com.github.pshirshov.izumi.idealingua.model
import com.github.pshirshov.izumi.idealingua.model.common.{AbstractTypeId, Package, PackageTools}
import com.github.pshirshov.izumi.idealingua.model.il.DomainId

import scala.reflect._

case class JavaType(pkg: Package, name: String) {
  def parent: JavaType = {
    JavaType(pkg.init, pkg.last)
  }

  def withRoot: JavaType = {
    JavaType("_root_" +: pkg, name)
  }


  def minimize(domainId: DomainId): JavaType = {
    val minimalPackageRef = PackageTools.minimize(pkg, domainId.toPackage)
    JavaType(minimalPackageRef, name)
  }
}

object JavaType {
  def get[T:ClassTag]: JavaType = {
    val clazz = classTag[T].runtimeClass.getCanonicalName

    val parts = clazz.split('.').toSeq
    val nameParts = parts.last.split('$').toSeq
    val pkg = parts.init ++ nameParts.init
    model.JavaType(pkg, nameParts.last)
  }

  def apply(typeId: AbstractTypeId): JavaType = new JavaType(typeId.pkg, typeId.name)
  def apply(typeId: DomainId): JavaType = new JavaType(typeId.pkg, typeId.id)
}