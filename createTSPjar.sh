#!/usr/bin/sh

cd out/
javac -cp "../src/:../lib/json-simple-1.1.1.jar" -d . ../src/*.java &&
	jar cmf ../MANIFEST.MF ../TSP.jar */*.class Main.class && echo 'compiled successfully!';
exit 0;
