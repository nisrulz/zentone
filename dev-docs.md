# Dev Documentation

## Publishing

- To release library to MavenLocal(~/.m2/):

  ```sh
  ./publish_local.sh
  ```

- To release library
  to [MavenCentral](https://search.maven.org/artifact/com.github.nisrulz/zentone):

  ```sh
  ./publish_release.sh
  ```

- To generate documentation:

  ```sh
  ./assemble_docs.sh
  ```

  > API docs are generated into `docs/api` and published to
  > [https://nisrulz.com/zentone/](https://nisrulz.com/zentone/) from the repository's `master`
  > branch via GitHub Pages.
