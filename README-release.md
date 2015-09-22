## Releasing to Artifact Repository

### Manually

Only authorized users can perform the release.  Contact one of the core contributors if you think you should have access.

* Run `gradle uploadArchives` and provide Sonatype credentials when prompted (if they are not already supplied in ~/.gradle/gradle.properties).  This will build both projects and generate pomfiles, javadoc, test, and sources artifacts, and then upload them to the Sonatype Nexus repository.