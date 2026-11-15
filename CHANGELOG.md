## Change log
----------------------

Version 10.0
-------------

10.0 is a major release: jobj-core needs Java 25, it removes twelve public types of 9.1 that no project of the family
used (#16), and five methods answer differently, as their Javadoc of 10.0 states (`ClassExtensions.isInstantiable`,
#29; `Check.isInRange`, #34; `ClassExtensions.getComponentClassType`; `TypeArgumentsExtensions.getTypeArguments`;
`Argument.isInRange` for a null value). It is published through the Central Portal, and a build of the same commit
gives the same bytes. The tests take parameterized tables of edge cases, PIT runs on GitHub on every pull request and
every push to `develop` and `master`, and the build fails below its coverage floors, on a javadoc warning and when a
test class leaves no result. Measured by the release gate (Temurin 25.0.5+8, the JDK `.sdkmanrc` names, on the tree of
`RELEASE-10.0`): 377 tests in 18 test classes, none skipped; 594 of 597 lines and 377 of 378 branches covered
(JaCoCo, floors 97% and 97%). Measured by the workflow "Mutation testing" on GitHub, on the same sources: 451 of 454
mutants detected, 99% (PIT 1.30.1, DEFAULTS and STRONGER, floor 97%).

BREAKING CHANGES:

- jobj-core needs Java 25: its class files are of version 69, its module descriptor requires `java.base` 25 and its
  Gradle module metadata a JVM of 25, where 9.1 ran on Java 17. **Note:** a project on an older Java stays on 9.1
- removed, as no caller in the family used them (#16); each was a plain data holder or has a counterpart in the JDK or
  Commons Lang:
  - `io.github.astrapi69.lang`: `DefaultValue` (its lookup is private to `ObjectExtensions` now), `EnumExtensions`,
    `MemoryExtensions`, `OptionalExtensions`
  - `io.github.astrapi69.lang.manifest`: `ManifestVersion`, `ManifestVersionFactory`; the package is no longer
    exported
  - `io.github.astrapi69.lang.model`: `AnnotationModel`, `ClassModel`, `FieldModel`, `MethodModel`,
    `PropertiesKeyAndParameters`; the package is no longer exported
  - `io.github.astrapi69.lang.thread`: `ThreadFieldDataBean`
  - with the data holders the builder classes Lombok generated for them, and the module no longer requires
    `java.compiler`

  `MethodTypes` and `MethodTypeArgumentsExtensions`, which the unreleased 9.2 had added, are gone as well and were never
  published. **Note:** for `OptionalExtensions.getOptionalValue` use `Optional.orElse`, for `MemoryExtensions`
  `Runtime.totalMemory()` and `freeMemory()`, for `EnumExtensions.enumValueFromString` Commons Lang
  `EnumUtils.getEnumIgnoreCase`, for `ManifestVersionFactory` `Package.getImplementationVersion()` on the class path;
  in a named module read `META-INF/MANIFEST.MF` with `java.util.jar.Manifest`
- `ClassExtensions.isInstantiable` answers false for an abstract class and for a class whose constructors are all
  private, such as a utility class, where it answered true for any declared no-argument constructor; the no-argument
  constructor counts only when `Constructor.newInstance` can call it, made accessible where it is not, so a protected
  one in a package its module does not open, as `java.io.ObjectInputStream` has, is false as well. A class without a
  no-argument constructor and an interface stay false, and array, `Collection` and `Map` types, their interfaces
  included, stay true, as the Javadoc explains (#29)
- `Check.isInRange` (Integer, Long, Float, Double) accepts a value equal to `min` or `max`, as `Argument.isInRange` and
  Commons Lang `Validate.inclusiveBetween` do, where it refused it; NaN stays outside every range.
  `Argument.isInRange` throws `IllegalArgumentException` for a null value instead of `NullPointerException` (#34)
- `ClassExtensions.getComponentClassType` answers the component type the array was created with, as its Javadoc says,
  where it answered the class of the first element: `Number` for `new Number[] { 1, 2.5 }` instead of `Integer`, and
  `String` for `new String[] { null }` instead of a `NullPointerException`
- `TypeArgumentsExtensions.getTypeArguments(Class, Class)`, and the `getTypeArgument` and `getFirstTypeArgument`
  methods that call it, resolve a generic interface as the base, directly, through a superclass or through an
  interface that extends it, where they answered with the type arguments of the child's topmost class (`[]` for a
  class implementing `Repository<Number>`); a child that is no subtype of the base, which only raw types allow, gets an
  `IllegalArgumentException`

CHANGED:

- jobj-core is published through the Central Portal (Maven Central), signed with the family's signing subkey; a release
  leaves only from a `RELEASE-*` tag through `publish.yml` and waits in the portal for the maintainer
- a build of the same commit gives the same bytes: the manifest no longer carries the build time, the JDK patch level
  or the operating system and names the feature release as `Build-Jdk-Spec: 25` instead, javac compiles with
  `--release 25`, and the javadoc jar is the same in every locale. **Note:** the manifest attributes
  `Build-Timestamp`, `Build-Jdk` and `Build-OS` are gone, `Build-Jdk-Spec` is new
- `ThreadDataBean` is written out instead of generated by Lombok, with Javadoc on every member; its API, `toString`
  text included, is unchanged, `hashCode` values may differ

FIXED:

- `ThreadDataBean.of(Thread)` takes a thread that has terminated, whose thread group is null, and leaves `threadGroup`
  null instead of throwing a `NullPointerException`; with it `ThreadExtensions.newThreadData()` no longer throws when a
  thread ends while it collects
- `PackageExtensions.getPackageName(Class)`, `getPackageNameWithDot`, `getPackagePath(Class)` and
  `getPackagePathWithSlash` answer for primitive types, void and array types as `Class.getPackageName()` does
  (`"java.lang"`, the element type's package) instead of throwing a `NullPointerException`; `getPackageName(String)`
  gives `""` for a name without a dot instead of throwing `StringIndexOutOfBoundsException`
- `InterruptableThread` stops after `interrupt()` also when `process()` does not block: its flag is volatile, so the
  loop sees the write
- `ThreadExtensions.runWithTimeout` keeps the interrupt status of a caller that is interrupted while it waits, and
  stops the task then as on a timeout; `runCallableWithCpuCores` and `runAsyncSupplierWithCpuCores` shut down the
  `ForkJoinPool` they create, whose worker lived on until its keep-alive ended
- `TypeArgumentsExtensions.getTypeArgument` answers null for an index below 0, as its Javadoc says, instead of throwing
  `IndexOutOfBoundsException`
- `ClassExtensions.getPath` gives the path of the class file of a nested, an anonymous or a local class
  (`/java/util/Map$Entry.class`), where it built a path from the simple name that does not exist; with it
  `getResource(Class)` and `getURL(Class)` find their class files, where they answered null, and `getProtocol` and
  `isFromJar` no longer throw a `NullPointerException` for them
- `ClassExtensions.getResource(Class, String)` and `getResource(String, Object)` find an absolute name on the class path
  when the class cannot, as for a class of the JDK, where the fallback asked the class loader with the leading slash and
  found nothing

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
