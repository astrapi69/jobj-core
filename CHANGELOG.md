## Change log
----------------------

Version 3.6-SNAPSHOT
-------------

ADDED:

- added new method that gets the generic return type in the class TypeArgumentsExtensions
- added new method that gets the class of the generic return type in the class TypeArgumentsExtensions

CHANGED:

- update of gradle to new version 6.5.1
- update kotlin to new version 1.3.72
- extracted project properties to gradle.properties
- extracted project gradle plugin versions to buildscript.ext area in gradle.properties
- update of test-dependency silly-collections version to 8.2
- update of test-dependency test-objects version to 8.2
- update of test-dependency testng version to 7.2.0

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


