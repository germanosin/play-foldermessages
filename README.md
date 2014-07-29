# Play FolderMessages library

This module allows you to split localization messages files into separate manageable files. Localized messages prefixed by file name. This allows for granular organization of localizations of large websites with lot of pages.

You can organize files in folder-per-language structure, containing multiple files:
```
\en
   - users
   - products
\fr
   - users
   - products
```

localization messages accessed by file. prefix.

### Example:


File `conf/en/users` contains:
```
greeting=Welcome!
```

You can then retrieve this message 

Scala using the play.api.i18n.Messages object:
```scala
val title = Messages("users.greeting")
```

Java using play.i18n.Messages object:
```java
String title = Messages.get("users.greeting")
```

## Installation (using sbt)

The current 1.0 version is compatible only with Play 2.3

Add a dependency on the following artifact:

```scala
libraryDependencies += "com.germanosin" %% "play-foldermessages" % "1.0"
```

## Configuration

Add following line to `application.conf`:
```
defaultmessagesplugin=disabled
foldermessagesplugin=enabled
```


## Author

German Osin