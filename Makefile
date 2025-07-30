.PHONY: build clean mvn package dpkg-deb

clean:
	@./mvnw clean

build: clean
	@./mvnw package

package: build
	@echo NOTE: This build target can only be run on Debian based systems
	cp target/jst package/usr/local/bin/
	dpkg-deb --root-owner-group --build package ./target/jstress.deb
	rm -f package/usr/local/bin/jst
