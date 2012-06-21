JFLAGS := -g -Xlint

all:
	javac $(JFLAGS) *.java

clean:
	$(RM) *.class

test: all
	java -cp .. ecv.Main
