antlr4=java -jar /usr/local/lib/antlr-4.5.3-complete.jar
grun=java -cp "/usr/local/lib/antlr-4.5.3-complete.jar:$$CLASSPATH" org.antlr.v4.gui.TestRig

all: antlr compile test

antlr: 
	$(antlr4) LJ.g4 -o build

compile:
	javac build/*.java

test:
	$(grun) LJ program test.lj

gui:
	$(grun) LJ program test.lj -gui

clean:
	rm build/* *.java *.class *.tokens
