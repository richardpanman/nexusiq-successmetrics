# getmetrics
![Coverage](.github/badges/jacoco.svg)

## Getting Started

### Prerequisites
  * Java 8+

### Download the app
  * There are two ways to run this application, directly running the application jar file (Option A) or with the application docker image (Option B)
  * In both cases, start by downloading the release zip file. To do so, go to the Releases pane on the right side of this page and click on the latest release
  * Click on the *getmetrics-[releasenumber].zip* file on the assets page to download it
  * Unzip the contents into a directory of your choice
  * Change into the *getmetrics-[releasenumber]* directory (this will be your working directory)

```
unzip getmetrics-[releasenumber].zip
cd getmetrics-[releasenumber]
```

### Quickstart

#### Generating the *successmetrics.csv* file

Edit the *config/application.properties* to set your Nexus IQ server URL and credentials

The default settings are:

* iq.user = admin
* iq.password = admin123
* iq.url = http://localhost:8070

##### Option A (Run with jar file)


Run script to generate the file
```
runapp.bat (Windows)
sh runapp.bat (Linux)
```

##### Option B (Run with docker image)
* Additionally, edit the *config/application.properties* and set the docker internal host for the Nexus IQ server URL
* *iq.url=http://host.docker.internal:8070*
```
runapp-docker.bat (Windows)
sh runapp-docker.sh (Linux)
```

### Other Settings

#### Edit the *config/application.properties* file for additional filters for the data

#### Time Period

* *iq.api.sm.period* (default = month)
* *iq.api.sm.payload.timeperiod.first* (default = 2020-01)
* *iq.api.sm.payload.timeperiod.last* (optional, default = current period)

#### Selecting Organisations and Applications
The default is all organisations and all applications.
To select a specific list of organisations or applications to report on
* For Organisations, add a list of comma-separated Organisation names to
* *iq.api.sm.payload.organisation.name*
* For Applications, add a list of comma-separated Applications names to
* *iq.api.sm.payload.application.name*
* If both are set, Organisations will take precedence

#### Addtional metrics
By default, the application produces the *successmetrics.csv* file only. You may also produce metrics for additional reports. This includes:
* Application Evaluations: *metrics.applicationsevaluations*
* Policy Violations: *metrics.policyviolations*
* Waivers: *metrics.waivers*
* Firewall: *metrics.firewall*

Edit the *config/application.properties* file set the relevant *metrics* property to *true* to produce any of these metrics files



## The Fine Print
* We recommend running it for 4 weeks of data at a time and for sets of orgs instead of the full scope if you have a large dataset.
* It is worth noting that this is NOT SUPPORTED by Sonatype, and is a contribution of ours to the open source community (read: you!)

* Don't worry, using this community item does not "void your warranty". In a worst case scenario, you may be asked by the Sonatype Support team to remove the community item in order to determine the root cause of any issues.

* Remember:

* Use this contribution at the risk tolerance that you have
* Do NOT file Sonatype support tickets related to iq-success-metrics
* DO file issues here on GitHub, so that the community can pitch in
* Phew, that was easier than I thought. Last but not least of all:

* Have fun creating and using this application and the Nexus platform, we are glad to have you here!
