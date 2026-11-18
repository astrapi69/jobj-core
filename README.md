# jobj-core

**Java helpers for the core classes: argument checks with a named message, the type, the
resources and the proxies of a class, packages, the type arguments of a generic subclass, default
values, and threads.**

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/jobj-core/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/jobj-core/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/jobj-core/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/jobj-core)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Fjobj-core%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/jobj-core.svg?style=flat)](https://github.com/astrapi69/jobj-core/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/jobj-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/jobj-core)
[![Javadoc](https://www.javadoc.io/badge/io.github.astrapi69/jobj-core.svg)](https://www.javadoc.io/doc/io.github.astrapi69/jobj-core)
[![MIT license](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/projects/jdk/25/)

</div>

jobj-core holds the small helpers around `Class`, `Package`, generics and threads that otherwise
get written again in every project, and keeps them tested: the build fails when the line or branch
coverage drops, and PIT checks that the tests notice when the code changes.

## Why jobj-core

It answers questions the JDK answers only in pieces: is this argument empty or out of its range,
with a message that names it; what kind of class is this (array, collection, map, enum, member,
local, anonymous), can a value of it be created, is it a proxy and what does it proxy; which class
does a generic subclass bind a type parameter of its base to. It has no runtime dependency. If you
need more, [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) (`Validate`,
`ClassUtils`, `reflect.TypeUtils`) and [Guava](https://github.com/google/guava) (`Preconditions`,
`reflect.TypeToken`) are far larger and used almost everywhere.

## Table of contents

- [Why jobj-core](#why-jobj-core)
- [Requirements](#requirements)
- [Installation](#installation)
- [Upgrading from 9.x](#upgrading-from-9x)
- [Quick start](#quick-start)
- [Features](#features)
- [Quality](#quality)
- [Verifying the release signatures](#verifying-the-release-signatures)
- [Releasing](#releasing)
- [Contributing](#contributing)
- [How this project is made](#how-this-project-is-made)
- [License](#license)
- [Support this project](#support-this-project)
- [Similar projects](#similar-projects)
- [Credits](#credits)

## Requirements

**JDK 25 or newer**, at build and at runtime: from 10.0 on its jar holds Java 25 bytecode and does
not load on an older JVM. **9.1 is the last release for Java 17.** jobj-core has no runtime
dependency; Lombok is used at compile time only.

## Installation

Latest release on Maven Central:

[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/jobj-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/jobj-core)

**Gradle** (version catalog, `gradle/libs.versions.toml`):

```toml
[versions]
jobj-core-version = "10.0"

[libraries]
jobj-core = { module = "io.github.astrapi69:jobj-core", version.ref = "jobj-core-version" }
```

```groovy
dependencies {
    implementation libs.jobj.core
}
```

**Gradle** (plain):

```groovy
dependencies {
    implementation "io.github.astrapi69:jobj-core:10.0"
}
```

**Maven**:

```xml
<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>jobj-core</artifactId>
    <version>10.0</version>
</dependency>
```

The module name is `io.github.astrapisixtynine.jobj.core`; it exports `io.github.astrapi69.check`,
`io.github.astrapi69.lang` and `io.github.astrapi69.lang.thread`.

A snapshot, built from `develop` by a manual run of `publish.yml`, is on the Central Portal's
snapshot repository, `https://central.sonatype.com/repository/maven-snapshots/`.

## Upgrading from 9.x

10.0 needs Java 25 and removes or changes API; the CHANGELOG entry of 10.0 lists each change. In
short:

- 12 types of 9.1 that no caller in the family used are gone: `DefaultValue`, `EnumExtensions`,
  `MemoryExtensions`, `OptionalExtensions`, the packages `lang.manifest` and `lang.model`, and
  `ThreadFieldDataBean`. Most are data holders; for the others the JDK or Commons Lang has a
  counterpart, for instance `Optional.orElse`, `Runtime.freeMemory()` or Commons Lang
  `EnumUtils.getEnumIgnoreCase` (#16)
- `ClassExtensions.isInstantiable` answers false for an abstract class, for a class whose
  constructors are all private and for a no-argument constructor that `Constructor.newInstance`
  cannot call even when made accessible, where it answered true (#29)
- the `isInRange` methods of `Check` take both bounds, as `Argument.isInRange` does, and
  `Argument.isInRange` refuses a null value with an `IllegalArgumentException` (#34)
- `ClassExtensions.getComponentClassType` answers the component type of the array, not the class
  of its first element, and `TypeArgumentsExtensions.getTypeArguments` resolves a generic
  interface as the base
- several methods answer where they threw: `PackageExtensions` for primitive and array types,
  `ThreadDataBean.of` for a terminated thread, `TypeArgumentsExtensions.getTypeArgument` for an
  index below 0

## Quick start

The examples are checked: each block runs, with the values its comments name as assertions, in
[`ReadmeExamplesTest`](src/test/java/io/github/astrapi69/ReadmeExamplesTest.java).

**Checking arguments.** Each check returns its argument, or throws an `IllegalArgumentException`
that names it.

```java
String name = Argument.notEmpty("jobj", "name");          // "jobj"
int percent = Argument.isInRange(0, 100, 100, "percent"); // 100: both bounds are in the range
Argument.notEmpty("  ", "name");                          // throws: a text of blanks is empty
Check.get().notNull(name, "name").isInRange(1, 10, 5, "count"); // chained
```

**Classes.**

```java
ClassExtensions.getClassType(String[].class);      // ClassType.ARRAY
ClassExtensions.isInstantiable(ArrayList.class);   // true
ClassExtensions.isInstantiable(InputStream.class); // false: abstract
ClassExtensions.getSimpleName(Map.Entry.class);    // "Entry"
ClassExtensions.getResource("io/github/astrapi69/lang/resources.properties"); // the URL
```

**Packages.**

```java
PackageExtensions.getPackageName(String.class);  // "java.lang"
PackageExtensions.getPackagePath(String.class);  // "java/lang/"
PackageExtensions.getPackageName(int.class);     // "java.lang"
PackageExtensions.getPackageName("Unqualified"); // "", the unnamed package
```

**Type arguments.** The class a generic subclass binds a type parameter to.

```java
class StringList extends ArrayList<String> { }

TypeArgumentsExtensions.getFirstTypeArgument(StringList.class); // String.class
TypeArgumentsExtensions.getTypeArgument(StringList.class, 1);   // null: there is no second one
```

**Default values.**

```java
ObjectExtensions.isDefaultValue(int.class, 0);       // true
ObjectExtensions.isDefaultValue(int.class, 1);       // false
ObjectExtensions.isDefaultValue(String.class, null); // true
```

**Threads.**

```java
ThreadExtensions.runWithTimeout(task, 100, TimeUnit.MILLISECONDS); // TimeoutException, and the task is stopped, if it takes longer
ThreadExtensions.runCallableWithCpuCores(() -> 6 * 7, 2);         // 42, on a pool of 2 threads
ThreadExtensions.newThreadData();                                 // a snapshot of the running threads
```

## Features

| Package `io.github.astrapi69...` | What it holds |
|---|---|
| `check` | `Argument` (static checks: not null, not empty for texts, collections, maps and arrays, in a range), `Check` (the same checks, chained) |
| `lang` | `ClassExtensions` (the type, the name, the loader, the resources and the jar of a class, proxies, whether it is instantiable, collection or map), `ClassType`, `PackageExtensions` (package names and paths), `TypeArgumentsExtensions` (the type arguments a generic subclass binds), `ObjectExtensions` (default values, the class type of an object) |
| `lang.thread` | `ThreadExtensions` (a task with a timeout, on a pool of a given size, the running threads, priorities, shutting down an executor), `ThreadDataBean`, `InterruptableThread` |

The [Javadoc](https://www.javadoc.io/doc/io.github.astrapi69/jobj-core) has the details.

## Quality

Measured by the release gate of 10.0 (Temurin 25.0.5+8, the JDK `.sdkmanrc` names; the tree of
`RELEASE-10.0`); each release takes the numbers of its own release gate:

| | |
|---|---|
| tests | 377 in 18 test classes, none skipped, most of them rows of parameterized tables |
| line and branch coverage (JaCoCo) | 594 of 597 lines (99.50 %), 377 of 378 branches (99.74 %); the build fails below 97 % of the lines or 97 % of the branches (`jacocoTestCoverageVerification` in `gradle/testing.gradle`) |
| mutation score (PIT) | 451 of 454 mutants detected, 99 %, with PIT's STRONGER mutators next to its defaults; the run fails below the floor of 97 % (`mutationThreshold` in `build.gradle`, which also says why the three survivors are equivalent mutants) |
| javadoc | a warning fails the build (`-Werror`), in every locale the same jar |
| reproducible | two builds of the same commit give the same bytes for all five artifacts (`scripts/check-reproducible.sh`) |

The CI build runs the tests and the coverage check on every pull request, and the workflow
"Mutation testing" runs PIT on GitHub, on every pull request and every push to `develop` and
`master`, and publishes the mutation score of `develop` as the badge above. A pull request into
`develop` is merged only when both are green.

## Verifying the release signatures

The releases on Maven Central from 10.0 on are signed with the signing subkey

```text
6D67 F844 2A6F CC96 BD7C  1B5E 9FCF 7C97 10E2 BD8D
```

of the OpenPGP key

```text
5B2F A6B1 1E29 8BC0 9287  F423 D8C4 0351 8C49 CA75
```

The key, with the subkey, is on keys.openpgp.org. Fetch it once, then verify the signature next to
each file:

```sh
gpg --keyserver hkps://keys.openpgp.org --recv-keys 5B2FA6B11E298BC09287F423D8C403518C49CA75
gpg --verify jobj-core-10.0.jar.asc jobj-core-10.0.jar
```

gpg answers `Good signature` and prints the `Primary key fingerprint` and the `Subkey fingerprint`;
both have to be the ones above. The warning that the key "is not certified with a trusted
signature" only says that you have not marked it as trusted in your own keyring.

## Releasing

What a release consists of, in the order it is run:

1. **No open issue** but those of a later milestone: every issue of the release's milestone is
   closed, and a finding on the way is fixed in the release or, if it changes what callers rely
   on, assigned to the next major release.
2. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
3. **The release gate** on that pull request's tree: the tests and `jacocoTestCoverageVerification`
   with its floor, `--rerun-tasks --no-build-cache`, on the JDK `.sdkmanrc` names, with an empty
   local Maven repository (`-Dmaven.repo.local=<an empty directory>`; `gradle/repositories.gradle`
   reads `mavenLocal()` only with `-PuseMavenLocal`); javadoc prints no warning (the build runs it
   with `-Werror`), the class files are of Java 25 (major version 69), and the resolved
   dependencies hold no `-SNAPSHOT`; `scripts/check-reproducible.sh` builds the five artifacts twice
   and finds them identical. PIT does not run on a local machine: its numbers and its floor come
   from the run of "Mutation testing" on the merge commit of the pull request, whose tree is the
   tag's tree. The numbers of the gate go into the CHANGELOG entry and into the Quality section
   above, in the same pull request.
4. **Tag the merge commit** with `./gradlew tagRelease` (`make tag-release CONFIRM=yes`), which names it
   `RELEASE-X.Y`, and push the tag. `publish.yml` uploads to the Central Portal, where the
   maintainer releases it. A pushed tag is never moved or deleted; a failed run is fixed on the
   branch and run again.
5. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`; a local build of the tag gives the same bytes as Central for all
   five.
6. **GitHub release** from the CHANGELOG entry.
7. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; never a force push.
8. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`, by a pull request.
9. **Bump drafts in the consumers** (RL-127): in every repository of the family that declares
   jobj-core, a draft pull request that raises it to the release and shows the build before and
   after it, so that a break shows the day the release appears; each is merged by RL-065, a runtime
   major in a session of its own.

## Contributing

Pull requests are welcome: fork [astrapi69/jobj-core](https://github.com/astrapi69/jobj-core/fork)
and [open a pull request](https://github.com/astrapi69/jobj-core/pull/new/develop) against
`develop`. [CONTRIBUTING.md](.github/CONTRIBUTING.md) has the details.

Please add tests for your change, as rows of the parameterized tables of the test class where they
fit; the coverage and mutation floors above hold for it too. The `Makefile` runs the usual tasks:

```shell
make build          # build: tests, coverage check, javadoc, formatting check
make spotless-apply # format, and write the license headers
make pitest         # mutation testing on GitHub for the pushed branch
make pitest-status  # the summary of its newest run
```

PIT does not run on your machine: `make pitest` starts `mutation-testing.yml` on GitHub for the
current branch, which has to be pushed first. PIT's report of a run is its artifact
`pitest-report`: `gh run download <run> -n pitest-report -D <directory>`.

The `pre-commit` hook in `.githooks` refuses a commit whose files Spotless would format. Activate it
once per clone or worktree:

```bash
git config core.hooksPath .githooks
```

The versions follow the
[Simplified Semantic Versioning](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
guidelines, `<major>.<minor>.<patch>`. Questions, bug reports and feature requests belong on the
[issues page](https://github.com/astrapi69/jobj-core/issues).

## How this project is made

jobj-core was written by its maintainer over many years, since 2015, before there were AI tools;
release 10.0 was implemented mostly by AI agents, under the maintainer's review and with the
maintainer's decision on every change of behaviour. How AI is used in the projects of this profile:
[AI_USAGE.md](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md).

## License

MIT - see [LICENSE](LICENSE).

## Support this project

If jobj-core is useful to you, a star on [GitHub](https://github.com/astrapi69/jobj-core) helps
others find it. Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button,
which reads [.github/FUNDING.yml](.github/FUNDING.yml).

## Similar projects

- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/): `Validate`, `ClassUtils`,
  `reflect.TypeUtils`
- [Guava](https://github.com/google/guava): `Preconditions`, `reflect.TypeToken`
- [ClassGraph](https://github.com/classgraph/classgraph): scanning the class path and modules

## Credits

| | |
|---|---|
| [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/jobj-core) | for hosting the artifacts of this open source project |
| [codecov.io](https://codecov.io) | for free code coverage reporting |
| [javadoc.io](https://www.javadoc.io) | for free javadoc hosting |
| [PIT](https://pitest.org/) | for mutation testing |

No animals were harmed in the making of this library.
