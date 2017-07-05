JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Energy_Balance.java \
	CombinationGenerator.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
