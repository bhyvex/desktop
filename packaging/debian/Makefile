PACKAGE_NAME=stacksync
DIST_DIR=../../target
RESOURCE_DIR=../../resources
VERSION=$(shell cat ../../version.properties | grep 'VERSION' | cut -d '=' -f 2)
VERSION_GIT=$(shell cat ../../version.properties | grep 'VERSION_GIT' | cut -d '=' -f 2)
INSTALLED_SIZE=$(shell du -sx $(DIST_DIR) | cut -f1)

all: compile package

.PHONY: clean

compile: 
	cd ../../; mvn clean; mvn assembly:assembly

package: clean
	# Create package folder and folder structure
	mkdir -p $(PACKAGE_NAME)/usr/lib/stacksync/
	mkdir -p $(PACKAGE_NAME)/usr/bin
	mkdir -p $(PACKAGE_NAME)/usr/lib/nautilus/extensions-2.0
	mkdir -p $(PACKAGE_NAME)/usr/lib/nautilus/extensions-3.0
	mkdir -p $(PACKAGE_NAME)/usr/share/applications
	# Copy DEBIAN folder
	cp -r debian $(PACKAGE_NAME)/DEBIAN
	# Copy bin
	cp utils/stacksync $(PACKAGE_NAME)/usr/bin/
	# Copy the Dist folder
	cp -r $(RESOURCE_DIR)/bin/ $(PACKAGE_NAME)/usr/lib/stacksync/
	cp -r $(RESOURCE_DIR)/conf/ $(PACKAGE_NAME)/usr/lib/stacksync/
	cp -r $(RESOURCE_DIR)/res/ $(PACKAGE_NAME)/usr/lib/stacksync/
	cp $(DIST_DIR)/desktop-client-$(VERSION_GIT)-jar-with-dependencies.jar $(PACKAGE_NAME)/usr/lib/stacksync/Stacksync.jar
	#cp $(DIST_DIR)/UpdaterClient.jar $(PACKAGE_NAME)/usr/lib/stacksync/
	# Copy nautilus extension library
	cp utils/libnautilus-syncany.so $(PACKAGE_NAME)/usr/lib/nautilus/extensions-3.0/
	# Create symlink for nautilus extensions 2.0
	ln -s ../extensions-3.0/libnautilus-syncany.so $(PACKAGE_NAME)/usr/lib/nautilus/extensions-2.0/libnautilus-syncany.so
	# Copy menu entry
	cp utils/stacksync.desktop $(PACKAGE_NAME)/usr/share/applications/
	# Update version
	sed -i 's/VERSION/$(VERSION_GIT)/g' $(PACKAGE_NAME)/DEBIAN/control
	# Update package size
	sed -i 's/INSTALLED_SIZE/$(INSTALLED_SIZE)/g' $(PACKAGE_NAME)/DEBIAN/control
	# Generate md5sum file
	cd $(PACKAGE_NAME); find . -type f ! -regex '.*.hg.*' ! -regex '.*?debian-binary.*' ! -regex '.*?DEBIAN.*' -printf '%P ' | xargs md5sum > DEBIAN/md5sums
	# Create debian package
	dpkg --build $(PACKAGE_NAME) stacksync_$(VERSION_GIT)_all.deb
	

clean:
	rm -rf $(PACKAGE_NAME)
	


    
