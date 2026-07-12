## Android Release with Fastlane

### Prerequisites

1. Install Ruby and Fastlane:
   ```bash
   gem install bundler
   bundle install
   ```

2. Set up signing:
   - Create a keystore: `keytool -genkey -v -keystore release.keystore -alias myalias -keyalg RSA -keysize 2048 -validity 10000`
   - Add to `local.properties`:
     ```
     KEYSTORE_FILE=app/release.keystore
     KEY_ALIAS=myalias
     KEY_PASSWORD=yourpassword
     STORE_PASSWORD=yourpassword
     ```

3. Set up Firebase:
   - Create a Firebase project and register your Android app
   - Download `google-services.json` and place in `app/` directory
   - Get your App ID from Firebase Console
   - Generate a CI token: `firebase login:ci`

### Available Lanes

| Lane | Description |
|------|-------------|
| `build_debug` | Build debug APK |
| `build_release` | Build release APK with signing |
| `test` | Run all tests |
| `unit_tests` | Run unit tests only |
| `deploy_firebase` | Deploy to Firebase App Distribution |
| `build_and_deploy_firebase` | Build release and deploy to Firebase |
| `deploy_internal` | Deploy to Play Store internal track |
| `deploy_beta` | Deploy to Play Store beta track |
| `deploy_production` | Deploy to Play Store production |
| `bump_version_code` | Increment version code by 1 |
| `bump_version_name` | Increment version name |

### GitHub Actions Secrets

Add these secrets in your GitHub repository (Settings → Secrets → Actions):

| Secret | Description |
|--------|-------------|
| `KEYSTORE_BASE64` | Base64 encoded keystore (`base64 -w 0 release.keystore`) |
| `KEY_ALIAS` | Keystore alias |
| `KEY_PASSWORD` | Key password |
| `STORE_PASSWORD` | Keystore password |
| `FIREBASE_APP_ID` | Firebase App ID (e.g., `1:123456789:android:abc123`) |
| `FIREBASE_TOKEN` | Firebase CI token from `firebase login:ci` |

### CI/CD Workflow

- **On push to `develop`**: Runs tests and builds debug APK
- **On push to `main`**: Runs tests, builds release APK, and deploys to Firebase App Distribution

### Version Bumping

- **Major version**: `bundle exec fastlane bump_version_name type:major` (1.0.0 → 2.0.0)
- **Minor version**: `bundle exec fastlane bump_version_name type:minor` (1.0.0 → 1.1.0)
- **Patch version**: `bundle exec fastlane bump_version_name type:patch` (1.0.0 → 1.0.1)
