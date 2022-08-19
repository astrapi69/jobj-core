## Change log
----------------------

Version 6.2-SNAPSHOT
-------------

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
- new gradle plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' for formatting source code with gradle build task
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

- new extension class for resolve the free memory and the memory status and freeing memory that is available for the application by running the garbage collector
- new method created that set the current thread priority
- improve gradle build performance by adding new gradle parameters for caching, parallel, configure on demand and file watch

CHANGED:

- update gradle to new version 7.3
- update of lombok version to 1.18.22
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin to new version 0.16.1
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

- new method in the class ClassExtensions that gets all resources with a optional array of excluding url protocols from it

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
