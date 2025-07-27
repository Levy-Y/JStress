.PHONY: build clean mvn

clean:
	@mvn clean

build:
	@mvn clean package