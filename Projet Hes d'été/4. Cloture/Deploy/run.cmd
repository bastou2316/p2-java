@echo off
rem -cp=classPath
rem .=met dans le cp tous .class du repertoire courant 
rem ./*= met dans le cp tous les jar du repertoire courant
rem ./ext/* met dans le cp tous les jars du folder ext
rem le séparateur de cp est ; dans windows, et : dans linux/mac
rem -Xms:mémoire minimale alouée ppar l'os que la jvm l'emploie ou pas
rem -Xmx:mémoire max alloué que jvm peut utiliser
rem -verbose:gc= utile pour optimisation (à ne pas livrer chez le client)
rem -D:propriété système uitlisée par la classe UseQuadratic


rem java -version
set JRE_PATH=./jre/bin;./ext
set PATH=%PATH%;%JRE_PATH%

java -cp .;./*;./ext/* ch.hearc.p2.java.UseNSolver

pause