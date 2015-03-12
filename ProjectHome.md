# Movie site lookup library #

Java library for fetching movie information from the web.

The API is still under development. A (alpha quality) release is available.

```
git clone https://code.google.com/p/flicklib/ 
mvn clean install source:jar javadoc:jar
```

If you are using maven you can use flicklib adding the release repository :
```
  <repository> 
     <id>flicklib-release</id>
     <name>Public flicklib release repository</name>
     <url>https://repository-renszarv.forge.cloudbees.com/release/</url>
  </repository>
```
or the snapshot :

```
  <repository> 
     <id>flicklib-release</id>
     <name>Public flicklib release repository</name>
     <url>https://repository-renszarv.forge.cloudbees.com/snapshot/</url>
  </repository>
```

And this is the maven dependency :
```
  <dependency>
     <groupId>com.flicklib</groupId>
     <artifactId>flicklib</artifactId>
     <version>0.4</version>
  </dependency>
```





## Supported services ##

Movies
  * http://www.imdb.com
  * http://www.movieweb.com
  * http://www.flixster.com
  * http://www.rottentomatoes.com
  * http://www.google.com/movies
  * http://www.netflix.com
  * http://www.port.hu
  * http://www.xpress.hu
  * http://www.omdb.org (still waiting for their webservice)
  * http://www.cinebel.be
  * http://www.ofdb.be
  * http://www.blippr.com

Trailers
  * http://www.apple.com/trailers/

Subtitles
  * http://www.opensubtitles.org

## Join the project ##

Other projects are free to join/merge their code, contact us on http://groups.google.com/group/flicklib

## News ##
  * 20111022 After long pause, the development is resumed, and a new release is created.
  * 20111019 Source repository migrated to Git, continuous integration on CloudBees
  * 20091025 Source repository migrated to mercurial

  * 20090217 Third release of flicklib (0.3)
    * various fixes
    * new movie service: www.ofdb.de

  * 20090210 Second release of flicklib (0.2)
    * caching http requests
    * fixed services for mostly for site updates

  * 20081008 First release of flicklib (0.1)


## Continuous integration ##

[![](http://web-static-cloudfront.s3.amazonaws.com/images/badges/BuiltOnDEV.png)](https://renszarv.ci.cloudbees.com)

Facts:
&lt;wiki:gadget url="http://www.ohloh.net/p/25657/widgets/project\_basic\_stats.xml" height="220" border="1"/&gt;
&lt;wiki:gadget url="http://www.ohloh.net/p/25657/widgets/project\_factoids.xml" border="0" height="220" width="300"/&gt;
