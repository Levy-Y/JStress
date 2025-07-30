.PHONY: clean package deb cd

clean:
	@./mvnw clean
	rm -rf debian/.debhelper
	rm -rf debian/jstress
	rm -f debian/debhelper-build-stamp
	rm -f debian/files
	rm -f debian/jstress.substvars
	rm -f debian/jstress.debhelper.log

package: clean
	@./mvnw package

deb: clean
	dpkg-buildpackage -us -uc
