# Dev Documentation

## API Check

- To check if the API compatibility is maintained i.e API surface is not changed:

  ```sh
  ./gradlew apiCheck
  ```

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

  > [API Docs](http://nisrulz.com/zentone/) are automatically published from the Github
  > repo, via GH Pages building from `docs` dir from `master` branch.
