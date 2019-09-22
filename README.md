# SIT213 Project - IMT Atlantique

[![Build Status][TRAVIS_CI_BADGE]][TRAVIS_CI]



Fichier décrivant les objectifs : [Goals](.project_docs/Goals.md) [WIP]

## Project folder overview

```
/                   # Root directory
├── .project_docs       # Contain documentation about the project
├── src/                # Contain java sources
├── lib/                # Contain java libraries
├── bin/                # Contain class files (generated on-demand)
├── docs/               # Contain documentation about java sources (generated on-demand)
├── test/               # Contain java test sources
└── build.xml           # Ant build file
```

## How to use

> The following scripts need to have e**X**ecution permission.

### Compile the project

At the root of the project, use the following command:

    $ ./compile

### Clean all the generated files

At the root of the project, use the following command:

    $ ./cleanAll
    
### Generate the documentation

At the root of the project, use the following command:

    $ ./genDoc
    
### Run the auto-test [Not implemented yet]

At the root of the project, use the following command:

    $ ./runTests
    
### Start using the simulator

At the root of the project, use the following command:

    $ ./simulateur

> If you want the list of usage, simply run `$ ./simulateur -h`
    
> If you want to use arguments, simply add them after `simulateur`.  
> For example : `$ ./simulateur -s -mess 01010011`


## Project continuous test management

Automated build triggered at each commit, managed by [Travis CI][TRAVIS_CI]. It allow us to:
* Check if `Unit test` passed
* Check if `Deployment test` passed

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
`o18gueye` | **Gueye** Oulimata  
`s18hugde` | **Hug de Larauze** Sébastien  


[TRAVIS_CI]: https://travis-ci.com/SebastienHUGDELARAUZE/SIT213
[TRAVIS_CI_BADGE]: https://travis-ci.com/SebastienHUGDELARAUZE/SIT213.svg?branch=master