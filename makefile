antlr4 = java -Xmx500M -cp "/usr/local/lib/antlr-4.5.3-complete.jar:$CLASSPATH" org.antlr.v4.Tool
all:
	$(antlr4) LJ.g4 -o build
	javac build/*.java

clean:
	rm build/* 
