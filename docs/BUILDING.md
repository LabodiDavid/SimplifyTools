## SimplifyTools - Building
Some of these dependencies only needed
#### Dependencies:
- (1.18.2) - spigot-api-1.18.2-R0.1-SNAPSHOT-shaded.jar (from Spigot Repo Downloads Automatically OR **BuildTools** compiled `Spigot\Spigot-API\target\`)
- (1.18.2) - brigadier-1.0.18.jar (from **BuildTools** compiled `spigot-x.xx.x.jar\META-INF\libraries\`)
- (1.18.2) - craftbukkit-1.18.2-R0.1-SNAPSHOT.jar (from downloaded `craftbukkit-x.xx.x.jar\META-INF\versions\`)
- (1.16.5) - spigot-1.16.5.jar (**BuildTools** compiled)
- (1.15.2) - spigot-1.15.2.jar (**BuildTools** compiled)
- (1.14.4) - spigot-1.14.4.jar (**BuildTools** compiled)
##### Installing Dependencies:
You can install the dependencies to your maven repository through CLI like this:

- mvn install:install-file -Dfile=spigot-api-1.18.2-R0.1-SNAPSHOT-shaded.jar -DgroupId=org.spigotmc -DartifactId=spigot-api -Dversion=1.18.2-R0.1-SNAPSHOT -Dpackaging=jar


- mvn install:install-file -Dfile=craftbukkit-1.18.2-R0.1-SNAPSHOT.jar -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion=1.18.2-R0.1-SNAPSHOT -Dpackaging=jar


- mvn install:install-file -Dfile=brigadier-1.0.18.jar -DgroupId=com.mojang -DartifactId=brigadier -Dversion=1.0.18 -Dpackaging=jar


- mvn install:install-file -Dfile=spigot-1.16.5.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dversion=1.16.5-R0.1-SNAPSHOT -Dpackaging=jar


- mvn install:install-file -Dfile=spigot-1.15.2.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dversion=1.15.2-R0.1-SNAPSHOT -Dpackaging=jar


- mvn install:install-file -Dfile=spigot-1.14.4.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dversion=1.14.4-R0.1-SNAPSHOT -Dpackaging=jar

- mvn install:install-file -Dfile=spigot-1.17.1.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dversion=1.17.1-R0.1-SNAPSHOT -Dpackaging=jar