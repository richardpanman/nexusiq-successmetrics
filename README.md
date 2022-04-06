# Success Metrics Application for IQ Server

- [Success Metrics Application for IQ Server](#success-metrics-application-for-iq-server)
  - [Installation](#installation)
  - [Fetch metrics from the IQ Server (get-metrics)](#fetch-metrics-from-the-iq-server-get-metrics)
    - [Get-Metrics Command Line Options](#get-metrics-command-line-options)
      - [Nexus IQ server details](#nexus-iq-server-details)
      - [Metrics](#metrics)
      - [Time period for which Success Metrics data should be fetched](#time-period-for-which-success-metrics-data-should-be-fetched)
      - [Limit extracted data to given organisations or applications](#limit-extracted-data-to-given-organisations-or-applications)
    - [Running get-metrics using Java](#running-get-metrics-using-java)
      - [Get-metrics command line examples using Java on Linux/Mac machines](#get-metrics-command-line-examples-using-java-on-linuxmac-machines)
        - [Fetch successmetrics data since 2021-07 on Mac/Linux](#fetch-successmetrics-data-since-2021-07-on-maclinux)
        - [Fetch successmetrics data between 2021-07 and 2022-01 using Java on Mac/Linux](#fetch-successmetrics-data-between-2021-07-and-2022-01-using-java-on-maclinux)
        - [Fetch weekly successmetrics data between 2021-W07 and 2022-W01 using Java on Mac/Linux](#fetch-weekly-successmetrics-data-between-2021-w07-and-2022-w01-using-java-on-maclinux)
        - [Fetch successmetrics data since 2021-07 for organisations org1 and org2 using Java on Mac/Linux](#fetch-successmetrics-data-since-2021-07-for-organisations-org1-and-org2-using-java-on-maclinux)
        - [Fetch successmetrics data since 2021-07 for applications app1 and app2 using Java on Mac/Linux](#fetch-successmetrics-data-since-2021-07-for-applications-app1-and-app2-using-java-on-maclinux)
      - [Running get-metrics using Java](#running-get-metrics-using-java-1)
        - [Fetch successmetrics data since 2021-07 using Java on Windows](#fetch-successmetrics-data-since-2021-07-using-java-on-windows)
        - [Fetch successmetrics data between 2021-07 and 2022-01 using Java on Windows](#fetch-successmetrics-data-between-2021-07-and-2022-01-using-java-on-windows)
        - [Fetch weekly successmetrics data between 2021-W07 and 2022-W01 using Java on Windows](#fetch-weekly-successmetrics-data-between-2021-w07-and-2022-w01-using-java-on-windows)
        - [Fetch successmetrics data since 2021-07 for organisations org1 and org2 using Java on Windows](#fetch-successmetrics-data-since-2021-07-for-organisations-org1-and-org2-using-java-on-windows)
        - [Fetch successmetrics data since 2021-07 for applications app1 and app2 on Mac/Linux](#fetch-successmetrics-data-since-2021-07-for-applications-app1-and-app2-on-maclinux)
    - [Running get-metrics using Docker](#running-get-metrics-using-docker)
  - [view-metrics](#view-metrics)
    - [Web (interactive) mode](#web-interactive-mode)
    - [Data extract (non-interactive) mode](#data-extract-non-interactive-mode)
    - [View-metrics Command Line Options](#view-metrics-command-line-options)
  - [Running the view-metrics application](#running-the-view-metrics-application)
    - [Running view-metrics using Java](#running-view-metrics-using-java)
      - [View-metrics command line examples - Mac/ Linux](#view-metrics-command-line-examples---mac-linux)
        - [Web (interactive mode) using Java on Linux/Mac](#web-interactive-mode-using-java-on-linuxmac)
        - [Data (non-interactive mode) using Java on Linux/Mac](#data-non-interactive-mode-using-java-on-linuxmac)
      - [View-metrics command line examples - Windows](#view-metrics-command-line-examples---windows)
        - [Web (interactive mode) using Java on Windows](#web-interactive-mode-using-java-on-windows)
        - [Data (non-interactive mode) using Java on Windows](#data-non-interactive-mode-using-java-on-windows)
    - [Running view-metrics using Docker](#running-view-metrics-using-docker)
  - [The Fine Print](#the-fine-print)

IQ Server has a number of REST APIs which can be used to extract policy evaluation, violation and remediation data. The Success Metrics get-metrics application extracts common metrics using this API and the view-metrics application aggregates the data into web or text reports.

Using the Success Metrics application is a two step process:

1. Extract metrics from IQ server via REST API (get-metrics)
2. Run the Success Metrics application against the success metrics csv (view-metrics)

## Installation

1. From the Releases pane on the right side of this page select the latest release
1. Click on the *nexusiq-successmetrics-[releasenumber].zip* file on the assets page to download and save to your machine
1. Unzip the contents into a directory of your choice

   ```bash
   unzip nexusiq-successmetrics-[releasenumber].zip
   ```

1. Navigate to the *successmetrics-[releasenumber]* directory (this will be the working directory for the rest of the commands given in this README)

   ```bash
   cd nexusiq-successmetrics-[releasenumber].zip
   ```

## Fetch metrics from the IQ Server (get-metrics)

The get-metrics script extract metrics from an IQ server and stores the result in the `./iqmetrics` directory.

get-metrics can be executed using a [Java 1.8 jar](#running-get-metrics-using-java) or [Docker image](#running-get-metrics-using-docker).

&#9888; For large installations/datasets, the extract should be limited to a shorter period (e.g. previous 6 months or weeks) or subset of organisations and/or applications.

### Get-Metrics Command Line Options

#### Nexus IQ server details

These parameters should be updated with the details of the Nexus IQ instance.

| Parameter | Description | Default |
| --------- | ----------- | ------- |
| iq.url | The URL of the IQ server | http://127.0.0.1:8070 |
| iq.user | The IQ user username | admin |
| iq.passwd | The IQ user password | admin123 |

&#9888; If you are using the get-metrics Docker image on the Nexus IQ machine then you may not be able to use `127.0.0.1` in the iq.url. You can instead try `host.docker.internal`.

#### Metrics

To fetch associated metrics from Nexus IQ the following properties may be set to true.

| Parameter | Description | Default |
| --------- | ----------- | ------- |
| metrics.successmetrics | Fetch SuccessMetrics data from IQ | true |
| metrics.applicationsevaluations | Fetch application evaluation metrics from IQ | false |
| metrics.waivers | Fetch waiver metrics from IQ | false |
| metrics.policyviolations | Fetch policy violation metrics from IQ | false |
| metrics.firewall | Fetch firewal metrics from IQ | false |

#### Time period for which Success Metrics data should be fetched

| Parameter | Description | Default |
| --------- | ----------- | ------- |
| timeperiod.duration | The periods over which the IQ data will be aggregated. The value should be month `month` or `week` |
| first.timeperiod | The first period to collect data from. The format of this parameter is YYYY-MM (for example) `2022-01` if `timeperiod.duration` is set to month or YYYY-Www (for example `2022-W01`) if `timeperiod.duration` is set to `week | 2022-01 |
| last.timeperiod | The last period to collect data from. The format of this parameter is `2022-01` if `timeperiod.duration` is set to month or `2022-W01` if `timeperiod.duration` is set to `week`. Specifying no value will collect all available data | "" |

#### Limit extracted data to given organisations or applications

| Parameter | Description | Default |
| --------- | ----------- | ------- |
| organisation.names | Collect data for the named organisations only. A list of comma seperated organisation names should be given. Specifying no value will collect data for all organisations | "" |
| application.names | Collect data for the named applications only. A list of comma seperated application names should be given. Specifying no value will collect data for all applications | "" |

If both of these parameter are set then the organisation setting will be used and application setting will be ignored.

### Running get-metrics using Java

Examples of running the command from the command line is given here, command line options shown in [Get-metrics Command Line Options](Get-metrics-Command-Line-Options) may be added as required (add `--` in front of the parameter).

#### Get-metrics command line examples using Java on Linux/Mac machines

##### Fetch successmetrics data since 2021-07 on Mac/Linux

```bash
cd get-metrics
sh runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07
```

##### Fetch successmetrics data between 2021-07 and 2022-01 using Java on Mac/Linux

```bash
cd get-metrics
sh runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --last.timeperiod==2022-01
```

##### Fetch weekly successmetrics data between 2021-W07 and 2022-W01 using Java on Mac/Linux

```bash
cd get-metrics
sh runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --timeperiod.duration=week --first.timeperiod=2021-W07 --last.timeperiod==2022-W01
```

##### Fetch successmetrics data since 2021-07 for organisations org1 and org2 using Java on Mac/Linux

```bash
cd get-metrics
sh runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --organisation.names=org1,org2
```

##### Fetch successmetrics data since 2021-07 for applications app1 and app2 using Java on Mac/Linux

```bash
cd get-metrics
sh runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --application.names=app1,app2
```

#### Running get-metrics using Java

##### Fetch successmetrics data since 2021-07 using Java on Windows

```bash
cd get-metrics
runapp.bat --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07
```

##### Fetch successmetrics data between 2021-07 and 2022-01 using Java on Windows

```bash
cd get-metrics
runapp.bat --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --last.timeperiod==2022-01
```

##### Fetch weekly successmetrics data between 2021-W07 and 2022-W01 using Java on Windows

```bash
cd get-metrics
runapp.bat --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --timeperiod.duration=week --first.timeperiod=2021-W07 --last.timeperiod==2022-W01
```

##### Fetch successmetrics data since 2021-07 for organisations org1 and org2 using Java on Windows

```bash
cd get-metrics
runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --organisation.names=org1,org2
```

##### Fetch successmetrics data since 2021-07 for applications app1 and app2 on Mac/Linux

```bash
cd get-metrics
runapp.sh --iq.url=https://iq:8070 --iq.user=myuser --iq.passwd=mypassword --first.timeperiod=2021-07 --application.names=app1,app2
```

### Running get-metrics using Docker

&#9888; You must be in the get-metrics directory for the runapp-docker script to work.

Using the [examples above](#running-get-metrics-using-java) simply replace `runapp.sh` with `runapp-docker.sh` for Mac/Linux and `runapp.bat` with `runapp-docker.bat` for Windows.

## view-metrics

There are two modes to the application.

1. Web mode (interactive)
2. Data extract mode (non-interactive)

The view-metrics script processes metrics stored in the `./nexusiq` directory and presents them as detailed aggregated charts in a web view or in data files in the `./datafiles` directory.

&#9888; There must be a `successmetrics.csv` in the `./iqmetrics` directory.

&#9888; In order to aggregate and process metrics a minimum of three data points (weeks or months) are needed.

&#9888; Only fully completed months (or weeks) are included in the data extract.

### Web (interactive) mode

When running in this mode a web UI for the Success Metrics application is available on the localhost <http://localhost:4040>

### Data extract (non-interactive) mode

The Success Metrics application will perform calculations on the provided metrics, output files representing this and then close. Three output files can then be found in the `./datafiles` directory.

1. Metrics Summary PDF
2. Insights CSV
3. Data Extract CSV

### View-metrics Command Line Options

| Parameter | Description | Default |
| --------- | ----------- | ------- |
| spring.profiles.active | Specifies which mode to run in. Set to `web` for web (interactive) mode or `data` for data extract (non-interactive) mode | web |

## Running the view-metrics application

### Running view-metrics using Java

#### View-metrics command line examples - Mac/ Linux

##### Web (interactive mode) using Java on Linux/Mac

```bash
cd view-metrics
sh runapp.sh --spring.profiles.active="web"
```

##### Data (non-interactive mode) using Java on Linux/Mac

```bash
cd view-metrics
sh runapp.sh --spring.profiles.active="data"
```

#### View-metrics command line examples - Windows

##### Web (interactive mode) using Java on Windows

```dos
cd view-metrics
runapp.bat --spring.profiles.active="web"
```

##### Data (non-interactive mode) using Java on Windows

```dos
cd view-metrics
runapp.sh --spring.profiles.active="data"
```

### Running view-metrics using Docker

&#9888; You must be in the view-metrics directory for the runapp-docker script to work.

Using the [examples above](#running-view-metrics-using-java) simply replace `runapp.sh` with `runapp-docker.sh` for Mac/Linux and `runapp.bat` with `runapp-docker.bat` for Windows.


## The Fine Print

For large datasets we recommend running extracts for small periods of time and for sets of organisations instead of the full system.

This application is NOT SUPPORTED by Sonatype, and is a contribution of ours to the open source community (read: you!)

Don't worry, using this community item does not "void your warranty". In a worst case scenario, you may be asked by the Sonatype Support team to remove the community item in order to determine the root cause of any issues.

Please remember:

* Use this contribution at the risk tolerance that you have
* Do NOT file Sonatype support tickets related to iq-success-metrics
* DO file issues here on GitHub, so that the community can pitch in
* Phew, that was easier than I thought. Last but not least of all, have fun creating and using this application and the Nexus platform, we are glad to have you here!
