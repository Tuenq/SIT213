# SIT213 Project - IMT Atlantique

[![Build Status][TRAVIS_CI_BADGE]][TRAVIS_CI]

TODO: Description

[Goals file](.project_docs/Goals.md)

## Project folder overview

```
/                   # Root directory
├── src/                # Contain java sources
├── lib/                # Contain java libraries
├── bin/                # Contain class files (generated on-demand)
├── docs/               # Contain documentation about java sources (generated on-demand)
├── test/               # Contain java test sources
├── deliverable/        # Contain deliverable archive (generated on-demand)
└── build.xml           # Ant build file
```

## Project continuous test management

Automated build triggered at each commit, managed by [Travis CI][TRAVIS_CI]. It allow us to:
* Check if `Unit test` passed
* Chech if `Deployment test` passed

And deploy with confidence to the client.

## Project management board

[Glo board](https://app.gitkraken.com/glo/board/XXQe6QtDJAAPHMYs) used to manage our project.

## Application tag versionning

This project follow this specification for its version tag: `v.X.Y.Z`

| v.  | X.        | Y.             | Z      |
| --- | ---       | ---            | ---    |
| Tag | TP number | Version number | Status |

**Status** part can be:
 * `pre-release`
 * `release`

## Authors

`m18barto` | **Bartoli** Mathieu  
`l18dumes` | **Dumestre** Lucas  
`l18franc` | **Francis** Ludovic  
`s18hugde` | **Hug de Larauze** Sébastien  


[TRAVIS_CI]: https://travis-ci.com/SebastienHUGDELARAUZE/SIT213
[TRAVIS_CI_BADGE]: https://travis-ci.com/SebastienHUGDELARAUZE/SIT213.svg?branch=master