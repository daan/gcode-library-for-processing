#
#   building on osx
#   download the jdk from Oracle (Processing uses 1.8.0_144)
#

#
#  core file:
# /Applications/Processing.app/Contents/Java/core/library/core.jar 
# /Applications/Processing.app/Contents/Java/modes/java/libraries/serial/library/serial.jar 
#

javac -cp /Applications/Processing.app/Contents/Java/core/library/core.jar:/Applications/Processing.app/Contents/Java/modes/java/libraries/serial/library/serial.jar:/Applications/Processing.app/Contents/Java/modes/java/libraries/serial/library/jssc.jar -d . src/*.java

jar -cvf ./library/gcode-library-for-processing.jar ./gcode/*

rm -r gcode/*.class
# rm *.class
rmdir gcode
