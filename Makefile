# Override shell executable from sh to bash.
SHELL:=/bin/bash

main:
	make clean
	make java
	make classes

classes:
	cd ./build && javac *.java

java:
	python3 capriccio.py

clean:
	rm -f ./build/*.class
