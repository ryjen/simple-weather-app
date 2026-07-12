fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android build_debug

```sh
[bundle exec] fastlane android build_debug
```

Build debug APK

### android build_release

```sh
[bundle exec] fastlane android build_release
```

Build release APK with signing

### android test

```sh
[bundle exec] fastlane android test
```

Build and test

### android unit_tests

```sh
[bundle exec] fastlane android unit_tests
```

Run unit tests

### android deploy_firebase

```sh
[bundle exec] fastlane android deploy_firebase
```

Deploy to Firebase App Distribution

### android build_and_deploy_firebase

```sh
[bundle exec] fastlane android build_and_deploy_firebase
```

Build and deploy to Firebase

### android deploy_internal

```sh
[bundle exec] fastlane android deploy_internal
```

Build and deploy to internal track

### android deploy_beta

```sh
[bundle exec] fastlane android deploy_beta
```

Build and deploy to beta track

### android deploy_production

```sh
[bundle exec] fastlane android deploy_production
```

Build and deploy to production

### android bump_version_code

```sh
[bundle exec] fastlane android bump_version_code
```

Increment version code

### android bump_version_name

```sh
[bundle exec] fastlane android bump_version_name
```

Increment version name

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
