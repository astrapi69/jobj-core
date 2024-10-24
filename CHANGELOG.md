## Change log
----------------------

Version 9.2-SNAPSHOT
-------------

ADDED:

- new model class MethodTypes that holds all generic types of a given method
- new extension class MethodTypeArgumentsExtensions
- new method in class MethodTypeArgumentsExtensions that gets all generic parameter types from the given method
- new method in class MethodTypeArgumentsExtensions that get the first generic parameter type from the given method
- new method in class MethodTypeArgumentsExtensions that get the generic return type from the given method

CHANGED:

- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 7.0.0.BETA3
- dependencies are managed now over bundles

Version 9.1
-------------

ADDED:

- new extension class OptionalExtensions for Optional objects
- new abstract class InterruptableThread that provides a template for threads that can be interrupted gracefully during
  execution

CHANGED:

- update of gradle to new version 8.10.2
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.3.0
- update of test-dependency silly-collection to new major version 28.1
- update of test-dependency equalsverifier version to 3.17.1

Version 9
-------------

ADDED:

- new method in class ThreadExtensions for shut down the given executor service with a given time out with a task size

CHANGED:

- rename of module to new name 'io.github.astrapisixtynine.jobj.core'
- major version to 9.*

Version 8.4
-------------

ADDED:

- new test dependencies junit-jupiter-* in version 5.11.0
- new decorator method in class ThreadExtensions for get the available processors (cores) on the current machine
- new decorator method in class ThreadExtensions for get the half of the available processors (cores) on the current
  machine
- new method in class ThreadExtensions for shut down the given executor service with a given time out

CHANGED:

- remove of test dependency testng

Version 8.3
-------------

ADDED:

- new libs.versions.toml file for new automatic catalog versions update
- new method in class ClassExtensions that checks if the given class is loaded from a jar file
- new method in class ThreadExtensions that can run a Runnable task with a timeout

CHANGED:

- update of gradle to new version 8.10
- update of lombok version to 1.18.34
- update of gradle-plugin dependency io.freefair.gradle:lombok-plugin to new patch version 8.10
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.51.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.2
- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 7.0.0.BETA2
- update of test dependency guava to new version 33.3.0-jre
- update of test-dependency equalsverifier version to 3.16.2
- update of test-dependency testng version to 7.10.2
- update of test dependency throwable to new version 3

Version 8.2
-------------

ADDED:

- new method in class ClassExtensions that resolves the protocol

CHANGED:

- update of gradle to new version 8.5
- update of gradle-plugin dependency io.freefair.gradle:lombok-plugin to new patch version 8.4
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.50.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.1
- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.23.3
- update of test dependency guava to new version 33.0.0-jre
- update of the test-dependency equalsverifier version to 3.15.4
- update of test-dependency test-object to new version 8.2

Version 8.1
-------------

ADDED:

- new extension class EnumExtensions that provides methods for get enum values from string
- new method in class EnumExtensions that can get enum values from a given enum field as string or as a generic value
- new test-dependency crypt-api for unit testing in version 8.7

CHANGED:

- update of gradle to new version 8.4-rc-3
- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.22.0
- update of the test-dependency equalsverifier version to 3.15.2

Version 8
-------------

CHANGED:

- update of jdk to version 17
- update of gradle to new version 8.4-rc-1
- update of lombok version to 1.18.30
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.48.0
- update of gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.21.0
- remove of package reflect cause of existing in its own module
- update of test dependency guava to new version 32.1.2-jre
- update of test-dependency silly-collection to new version 27
- update of test-dependency equalsverifier version to 3.15.1
- update of test-dependency testng version to 7.8.0

Version 7.1
-------------

ADDED:

- new package reflect
- new class ReflectionExtensions
- new package-info java classes and removed obsolete package.html files

CHANGED:

- update of gradle to new version 8.1.1
- update of lombok version to 1.18.26
- update of com.github.ben-manes.versions.gradle.plugin to new minor version 0.46.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.18.0
- update of test-dependency test-object to new version 7.2
- update of test-dependency silly-collection to new version 21
- update of test-dependency equalsverifier version to 3.14.1
- update of test-dependency testng version to 7.7.1

Version 7
-------------

ADDED:

- new test dependency throwable in new version 2.3 for decorate checked exception in unit tests
- new test dependency guava in new version 31.1-jre

CHANGED:

- remove of reflection package
- remove of objenesis dependency

Version 6.1
-------------

ADDED:

- new method in ClassExtensions that checks if the given class is instantiable

CHANGED:

- update of gradle to new version 7.5.1
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.9.1
- update of dependency objenesis to new version to 3.3
- update of test-dependency test-object to new version 7
- update of test-dependency silly-collection to new version 20
- update of test-dependency silly-bean to new version 2
- update of test-dependency equalsverifier version to 3.10.1
- update of test-dependency testng version to 7.6.1

Version 6
-------------

ADDED:

- new method in ReflectionExtensions for copy a field from a source object to a target object
- new gradle plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' for formatting source code with gradle
  build task
- new module-info.java file with definition of required modules and packages to export

CHANGED:

- update of jdk to version 11
- update of gradle to new version 7.5-rc-2
- update of lombok version to 1.18.24
- update of com.github.ben-manes.versions.gradle.plugin to new minor version 0.42.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.0.0
- update of test-dependency silly-collections version to 18.2
- update of test-dependency test-objects to new minor version 6.1
- update of test-dependency equalsverifier version to 3.10
- update of test-dependency testng version to 7.6.0

Version 5.3
-------------

ADDED:

- new method for get from a given class the running jar file

CHANGED:

- update of com.github.ben-manes.versions.gradle.plugin to new version 0.41.0
- update of test-dependency test-objects version to 5.7
- update of test-dependency equalsverifier version to 3.9
- update of test-dependency testng version to 7.5

Version 5.2
-------------

ADDED:

- new method for get the default field names that can be always ignored that can be used in unit tests

CHANGED:

- bugfix for method ReflectionExtensions#getAllDeclaredFields(Class, String...) for find all declared fields

Version 5.1
-------------

ADDED:

- new class type check for array in the method newInstanceWithClass in ReflectionExtensions class

CHANGED:

- update gradle to new version 7.3.3
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.40.0
- new gradle-plugin dependency of 'org.ajoberstar.grgit:grgit-gradle' in version 4.4.1 for create git release tags
- update of test-dependency test-objects version to 5.6
- update of test-dependency equalsverifier version to 3.8.1

Version 5
-------------

ADDED:

- new extension class for resolve the free memory and the memory status and freeing memory that is available for the
  application by running the garbage collector
- new method created that set the current thread priority
- improve gradle build performance by adding new gradle parameters for caching, parallel, configure on demand and file
  watch

CHANGED:

- update gradle to new version 7.3
- update of lombok version to 1.18.22
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin to new version
  0.16.1
- update of test-dependency silly-collections version to 18
- update of test-dependency test-objects version to 5.5
- update of test-dependency equalsverifier version to 3.7.2

Version 3.9
-------------

ADDED:

- new model classes for hold java class fields like method, field, class and annotation
- new model classes for hold thread related fields

  Version 3.8

-------------

ADDED:

- new methods in the class Argument for cover the checks for the primitive array types
- new methods in the class Check for cover the checks for the primitive array types

Version 3.7
-------------

ADDED:

- new method in the class ClassExtensions that gets all resources with a optional array of excluding url protocols from
  it

CHANGED:

- update of gradle to new version 6.9
- update of objenesis dependency version to 3.2
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.39.0
- update of dependency lombok version to 1.18.20
- update of test-dependency silly-collections version to 8.6
- update of test-dependency test-objects version to 5.4
- update of test-dependency runtime-compiler version to 1.3
- update of test-dependency testng version to 7.4.0
- changed to new package io.github.astrapi69

Version 3.6
-------------

ADDED:

- new method in the class TypeArgumentsExtensions that gets the generic return type
- new method in the class TypeArgumentsExtensions that gets the class of the generic return type
- new method in the class TypeArgumentsExtensions that gets the ParameterizedType from a given class
- new method in the class TypeArgumentsExtensions that gets that returns the generic type array from a given class

CHANGED:

- update of gradle to new version 6.6.1
- removed kotlin nature
- extracted project properties to gradle.properties
- extracted project gradle plugin versions to buildscript.ext area in gradle.properties
- update of test-dependency silly-collections version to 8.2
- update of test-dependency test-objects version to 5.3
- update of test-dependency testng version to 7.3.0

Version 3.5
-------------

ADDED:

- kotlin nature and ported most classes to kotlin

CHANGED:

- removed lombok dependency
- removed unused junit dependency
- removed unused mockito dependency

Version 3.4
-------------

ADDED:

- new method that resolves if the given class is an primitive array type

CHANGED:

- migrate to gradle build system
- update of test-dependency silly-collections version to 5.8
- removed deprecated methods in ClassExtensions class
- removed deprecated methods in ReflectionExtensions class
- method newInstance from the ReflectionExtensions class considers now array types on creation

Version 3.3
-------------

ADDED:

- new method that resolves the generic component class type of a given generic array

CHANGED:

- update of parent version to 5.3
- update of objenesis dependency version to 3.1
- update of cglib dependency version to 3.3.0

Version 3.2.1
-------------

CHANGED:

- update of method newInstance in ReflectionExtensions
- tagged method setFieldValue with field name as deprecated

Version 3.2
-------------

CHANGED:

- update of parent version to 5
- update of silly-collections version to 5.2.1
- update of test-objects dependency version to 5.2
- update of cglib dependency version to 3.2.12

Version 3.1
-------------

ADDED:

- new method created for create new instance of an array with reflection with capacity
- new method created for copy an existing array
- new methods created for set a field value from a given source object over a field and field name

Version 3
-------------

ADDED:

- this changelog file
- created PULL_REQUEST_TEMPLATE.md file
- created CODE_OF_CONDUCT.md file
- created CONTRIBUTING.md file
- provide package.html for the javadoc of packages
- moved classes from obsolet jobject-core project
