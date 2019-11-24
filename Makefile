# Override shell executable from sh to bash.
SHELL:=/bin/bash

main:
	make clean
	make java
	make classes
	make standalone

standalone:
	cd ./build && jar cfe capriccio.jar Main *.class

classes:
	cd ./build && javac *.java

java:
	python3 capriccio.py

clean:
	rm -f ./build/*.class
