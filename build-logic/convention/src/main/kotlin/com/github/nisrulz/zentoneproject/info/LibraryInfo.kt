package com.github.nisrulz.zentoneproject.info

object LibraryInfo {
    const val POM_VERSION = "2.3.0"
    const val POM_ARTIFACT_ID = "zentone"
    const val POM_NAME = "ZenTone"
    const val POM_DESCRIPTION =
        "Android library to easily generate audio tone of a specific frequency and volume."
    const val POM_INCEPTION_YEAR = "2016"

    private const val GITHUB_REPO_NAME = "zentone"
    private const val GITHUB_USER = "github.com/nisrulz"
    const val POM_URL = "https://$GITHUB_USER/$GITHUB_REPO_NAME/"
    const val POM_SCM_URL = "https://$GITHUB_USER/$GITHUB_REPO_NAME/"
    const val POM_SCM_CONNECTION = "scm:git:git://$GITHUB_USER/$GITHUB_REPO_NAME.git"
    const val POM_SCM_DEV_CONNECTION = "scm:git:ssh://git@$GITHUB_USER/$GITHUB_REPO_NAME.git"
}
