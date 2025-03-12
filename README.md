To get a Git project into your build:
Add the JitPack repository to your build file
Add it in your settings.gradle.kts at the end of repositories:

dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
 dependencies {
	        implementation("com.github.lokeshsunhare:searchable-spinner:1.0.2")
	}
