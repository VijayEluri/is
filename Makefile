JFLAGS := -g -Xlint

all:
	javac $(JFLAGS) *.java visitor/*.java

clean:
	$(RM) *.class visitor/*.class

test: all
	java -cp .. ecv.Main
