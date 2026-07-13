{
  description = "MyWeatherApp development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config = {
            android_sdk.accept_license = true;
            allowUnfree = true;
          };
        };


        androidComposition = pkgs.androidenv.composeAndroidPackages {
          platformVersions = [ "34" "35" "37" ];
          buildToolsVersions = [ "34.0.0" "35.0.0" "37.0.0" ];
          cmdLineToolsVersion = "latest";
          platformToolsVersion = "35.0.2";
          includeNDK = false;
          includeEmulator = false;
          includeSystemImages = false;
        };
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            pkgs.ruby
            pkgs.bundler
            pkgs.jdk17
            pkgs.gradle
            pkgs.git
            pkgs.curl
            pkgs.jq
            pkgs.yq-go

            androidComposition.androidsdk
          ];

          ANDROID_HOME = "${androidComposition.androidsdk}/libexec/android-sdk";
          ANDROID_SDK_ROOT = "${androidComposition.androidsdk}/libexec/android-sdk";
          JAVA_HOME = "${pkgs.jdk17}";

          shellHook = ''
            echo "MyWeatherApp development shell"
            echo "  Ruby: $(ruby --version)"
            echo "  Java: $(java -version 2>&1 | head -1)"
            echo "  Android SDK: $ANDROID_HOME"
            echo ""
            bundle install --jobs 4
            echo ""
            echo "Run 'fastlane android build_debug' to build."
          '';
        };
      }
    );
}
