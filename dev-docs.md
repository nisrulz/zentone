# Dev Documentation

## Publishing

- To release library to MavenLocal(~/.m2/):

  ```sh
  ./gradlew releaseToMavenLocal
  ```

- To release library
  to [MavenCentral](https://search.maven.org/artifact/com.github.nisrulz/zentone):

  ```sh
  ./gradlew releaseToMavenCentral
  ```

- To generate documentation:

  ```sh
  ./gradlew assembleDocs
  ```

  > API docs are generated into `docs/api` and published to
  > [https://nisrulz.com/zentone/](https://nisrulz.com/zentone/) from the repository's `main`
  > branch via GitHub Pages.
