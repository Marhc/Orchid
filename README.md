
# Orchid

[![Build Status](https://travis-ci.org/JavaEden/Orchid.svg?branch=master)](https://travis-ci.org/JavaEden/Orchid) (Linux and Mac)
[![Build status](https://ci.appveyor.com/api/projects/status/0358qdkmfhbqedo1/branch/master?svg=true)](https://ci.appveyor.com/project/cjbrooks12/orchid/branch/master) (Windows)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8bca7e84b6094c03ae1316278cf63ae1)](https://www.codacy.com/app/cjbrooks12/Orchid?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=JavaEden/Orchid&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/JavaEden/Orchid/branch/master/graph/badge.svg)](https://codecov.io/gh/JavaEden/Orchid)
[![Gitter chat](https://img.shields.io/gitter/room/nwjs/nw.js.svg)](https://gitter.im/JavaEden/Orchid)


> A beautiful and truly unique documentation engine and static site generator.

![Example Orchid site](https://i.imgur.com/Fs3YFyY.png)

Orchid is a general-purpose static site generator with a focus on extensibility, and aimed at developers looking to 
improve their technical documentation. Orchid was born out of a desire for better-looking Javadocs and frustration with 
how difficult is it to manage large Jekyll sites and keep it up-to-date with your code. 

Orchid supports a variety of plugins, including a wiki, static pages, blogs, and much more. It aims to have high 
compatibility with many of the existing static site generators, such as Jekyll, Gitbook, and Hugo, so that migration to 
Orchid is painless. And if you can't find a plugin to do what you need, Orchid provides an intuitive way to add your own 
private plugins and a rich API so you can make your site as beautiful and unique as an Orchid.

### Features

- Plugin-driven architecture
- Multi-theme support
- Admin interface to manage your content and gain deep insights into your build
- Link to other Orchid sites
- Powerful and flexible indexing, unique to Orchid
- Full-text static search 
- Component-based content management
- Fully replaces Jekyll, Hugo, Gitbook, Javadocs, and more!

[View the full documentation](https://orchid.netlify.com) or see the quick-start below.

## Table of Contents

1. [Quick Start](#quick-start)
1. [Example Orchid Sites](#example-orchid-sites)
1. [Development Progress](#development-progress)
    1. [Core Packages](#core-packages)
    1. [Themes](#themes)
    1. [Plugins](#plugins)
1. [License](#license)
1. [Contributing](#contributing)
1. [Contact](#contact)

## Quick Start

Orchid integrates with any new or existing Gradle project. The simplest way to get started is to deploy the 
[starter repo](https://github.com/JavaEden/OrchidStarter) directly to Netlify. Just click the button below to 
automatically clone this repo and deploy it to Netlify. The starter repo includes the 
[Netlify CMS](https://www.netlifycms.org/), so you will be up and publishing content as soon as possible. You will need 
to set the Github user/repo in `src/orchid/resources/config.yml`, but the rest of the CMS config is automatically 
generated based on your current Orchid plugins and configurations. 

[![Deploy to Netlify](https://www.netlify.com/img/deploy/button.svg)](https://app.netlify.com/start/deploy?repository=https://github.com/JavaEden/OrchidStarter)

To run Orchid locally, the only system dependency necessary is a valid Java 8 JDK. Orchid is run via Gradle, and can 
integrate with any new or existing Gradle project. To get started, pick a Bundle (OrchidAll or OrchidBlog) or manually 
choose your desired Orchid plugins. You may pick a bundle to start with and add any number of plugins afterward, both 
official and unofficial. Then, setup your project's build.gradle file like so:

```groovy
plugins {
    // Add the official Orchid Gradle plugin so you can use Orchid with the custom DSL   
    id "com.eden.orchidPlugin" version "{version}"
}

repositories {
    // Orchid uses dependencies from both Jcenter and Jitpack, so both must be included. jcenter also includes 
    // everything available from MavenCentral, while Jitpack makes accessible any Github project.
    jcenter() 
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Add an Orchid Bundle. OrchidAll comes with all official themes included.
    // You must include a theme separately when using the OrchidBlog bundle.
    // Any additional plugins may be added as dependencies here as well.
    orchidRuntime 'io.github.javaeden.orchid:OrchidAll:{version}'
}

orchid {
    // Theme is required
    theme   = "{theme}"
    
    // The following properties are optional
    version = "${project.version}"
    baseUrl = "{baseUrl}"                         // a baseUrl appended to all generated links. Defaults to '/'
    srcDir  = "path/to/new/source/directory"      // defaults to 'src/orchid/resources'
    destDir = "path/to/new/destination/directory" // defaults to 'build/docs/orchid'
    runTask = "build"                             // specify a task to run with 'gradle orchidRun'
}
```

You can now run Orchid in the following ways:

1) `./gradlew orchidRun` - Runs an Orchid task. The `runTask` should be specified in `build.gradle` or passed as a Gradle project property (`-PorchidRunTask=build`). The task `listTasks` will show a list of all tasks that can be run given the plugins currently installed.
2) `./gradlew orchidBuild` - Runs the Orchid build task a single time then exits. The resulting Orchid site will be in `build/docs/orchid` unless the output directory has been changed. You can then view the site by starting any HTTP file server in the root of the output directory.
3) `./gradlew orchidWatch` - Runs the Orchid build task a single time, then begins watching the source directory for changes. Anytime a file is changes, the build will run again, and the resulting Orchid site will be in `build/docs/orchid` unless the output directory has been changed.
4) `./gradlew orchidServe` - Sets up a development server and watches files for changes. The site can be viewed at `localhost:8080` (or the closest available port).
5) If you are developing a Java application, Orchid replaces the standard Javadoc task with its own `build` task. In addition to running the standard Orchid build, when Orchid is run from Javadoc it will be able to create pages for all your project's classes and packages, just like you'd expect from a normal Javadoc site, but embedded within your chosen Orchid theme. You must have the `OrchidJavadoc` plugin installed for this to work properly.
    
_On windows, all the above commands need to be run with `gradlew` instead of `./gradlew`._

The Orchid Gradle plugin adds a new configuration and content root to your project, in the `src/orchid` directory 
(you may have to create this folder yourself). All your site content sits in `src/orchid/resources`, and any 
additional classes you'd like to include as a private plugin can be placed in `src/orchid/java`. 

## Example Orchid Sites

* [Official Orchid documentation](https://javaeden.github.io/Orchid/latest/OrchidCore/)
* [Clog documentation](https://javaeden.github.io/Clog/)
* [Krow documentation](https://javaeden.github.io/Krow/)
* [caseyjbrooks.com](https://www.caseyjbrooks.com/)

## Development Progress

Orchid is nearing its first public release. While much of the public API is solid and not likely to change, it is still 
in development and I make no guarantees that any particular API will not change or be removed.
 
The following tables list all official Orchid packages:

#### Core Packages

| Name                 | Version |
| -------------------- | ------- |
| Orchid Core          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidCore/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidCore/_latestVersion) |
| Orchid Gradle Plugin | [ ![Download](https://img.shields.io/badge/Gradle%20Plugin-v0.4.1-blue.svg)                    ](https://plugins.gradle.org/plugin/com.eden.orchidPlugin)  |

#### Themes

| Name                  | Version |
| --------------------- | ------- |
| OrchidBsDoc           | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidBsDoc/images/download.svg)           ](https://bintray.com/javaeden/Orchid/OrchidBsDoc/_latestVersion) |
| OrchidEditorial       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidEditorial/images/download.svg)       ](https://bintray.com/javaeden/Orchid/OrchidEditorial/_latestVersion) |
| OrchidFutureImperfect | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidFutureImperfect/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidFutureImperfect/_latestVersion) |

#### Plugins

| Name                | Version |
| ------------------- | ------- |
| OrchidChangelog     | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidChangelog/images/download.svg)     ](https://bintray.com/javaeden/Orchid/OrchidChangelog/_latestVersion) |
| OrchidForms         | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidForms/images/download.svg)         ](https://bintray.com/javaeden/Orchid/OrchidForms/_latestVersion) |
| OrchidJavadoc       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidJavadoc/images/download.svg)       ](https://bintray.com/javaeden/Orchid/OrchidJavadoc/_latestVersion) |
| OrchidKSS           | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidKSS/images/download.svg)           ](https://bintray.com/javaeden/Orchid/OrchidKSS/_latestVersion) |
| OrchidNetlifyCMS    | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidNetlifyCMS/images/download.svg)    ](https://bintray.com/javaeden/Orchid/OrchidNetlifyCMS/_latestVersion) |
| OrchidPages         | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPages/images/download.svg)         ](https://bintray.com/javaeden/Orchid/OrchidPages/_latestVersion) |
| OrchidPluginDocs    | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPluginDocs/images/download.svg)    ](https://bintray.com/javaeden/Orchid/OrchidPluginDocs/_latestVersion) |
| OrchidPosts         | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPosts/images/download.svg)         ](https://bintray.com/javaeden/Orchid/OrchidPosts/_latestVersion) |
| OrchidPresentations | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidPresentations/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidPresentations/_latestVersion) |
| OrchidSearch        | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidSearch/images/download.svg)        ](https://bintray.com/javaeden/Orchid/OrchidSearch/_latestVersion) |
| OrchidSwagger       | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidSwagger/images/download.svg)       ](https://bintray.com/javaeden/Orchid/OrchidSwagger/_latestVersion) |
| OrchidTaxonomies    | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidTaxonomies/images/download.svg)    ](https://bintray.com/javaeden/Orchid/OrchidTaxonomies/_latestVersion) |
| OrchidWiki          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidWiki/images/download.svg)          ](https://bintray.com/javaeden/Orchid/OrchidWiki/_latestVersion) |

#### Language Extensions

| Name                    | Version |
| ----------------------- | ------- |
| OrchidAsciidoc          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidAsciidoc/images/download.svg)          ](https://bintray.com/javaeden/Orchid/OrchidAsciidoc/_latestVersion) |
| OrchidBible             | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidBible/images/download.svg)             ](https://bintray.com/javaeden/Orchid/OrchidBible/_latestVersion) |
| OrchidDiagrams          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidDiagrams/images/download.svg)          ](https://bintray.com/javaeden/Orchid/OrchidDiagrams/_latestVersion) |
| OrchidSyntaxHighlighter | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidSyntaxHighlighter/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidSyntaxHighlighter/_latestVersion) |
| OrchidWritersBlocks     | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidWritersBlocks/images/download.svg)     ](https://bintray.com/javaeden/Orchid/OrchidWritersBlocks/_latestVersion) |

#### Bundles

| Name               | Version |
| ------------------ | ------- |
| OrchidAll          | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidAll/images/download.svg)               ](https://bintray.com/javaeden/Orchid/OrchidAll/_latestVersion) |
| OrchidBlog         | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidBlog/images/download.svg)              ](https://bintray.com/javaeden/Orchid/OrchidBlog/_latestVersion) |
| OrchidLanguagePack | [ ![Download](https://api.bintray.com/packages/javaeden/Orchid/OrchidSyntaxHighlighter/images/download.svg) ](https://bintray.com/javaeden/Orchid/OrchidSyntaxHighlighter/_latestVersion) |

## License

Orchid is open-source software licensed under the GNU Lesser General Public License (LGPL). See License.md for more 
information.

## Contributing

This repository is comprised of many individual projects, which are all listed above. You can build and run any project
with Gradle from the project root, such as `gradle :OrchidCore:assemble` or `gradle :plugins:OrchidWiki:assemble`, or 
you may navigate to a particular project's subdirectory to run the Gradle commands directly. When contributing code, 
please indent using 4 spaces and keep braces on the same lines.

Orchid releases are deployed continuously. With any push to the `master` branch, the patch version is incremented, then
the compiled sources are uploaded to Bintray, Github Pages, and a git tag created and uploaded to Github Releases. All
code should be deployed via pull request. 

Documentation updates and submissions for new themes or plugins should be made as PRs to the `docs` branch. 
Documentation is hosted on Netlify, and a deploy preview will be made for your PR before it is merged into `docs`.

Orchid uses the [Gradle Semantic Versioning](https://github.com/vivin/gradle-semantic-build-versioning) plugin to set
the package version and create the git tag. The major and minor versions can be incremented by including `[major]` or
`[minor]` anywhere in any commit message since the previous released version. 

The Orchid Gradle Plugin tracks a different version than the git tag, and it's deploy process is handled manually:
 
- Start with clean Git index
- `cd buildSrc` (buildSrc is not recognized as a normal part of the Gradle project, but its is a project in its own local directory)
- Increment `version` in `buildSrc/build.gradle`, according to [Semantic Versioning](http://semver.org/) and prefixed with `v`
- `gradle publishPlugins`

## Contact

Orchid is being actively developed by Casey Brooks. Please open an issue here with questions or bug/feature requests, or
you can reach me directly at cjbrooks12@gmail.com.
