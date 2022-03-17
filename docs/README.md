# Success Metrics Installation and Usage

Using the Success Metrics application is a two step process:

1. Extract metrics from IQ server via REST API (get-metrics)
2. Run the Success Metrics application against the success metrics csv (view-metrics)

## Installation

1. Download the *nexusiq-successmetrics-[releasenumber].zip* file on the assets page to your machine
2. Unzip the contents into a directory of your choice

   ```bash
   unzip successmetrics-[releasenumber].zip
   ```

3. Navigate to the *successmetrics-[releasenumber]* directory (this will be the working directory for the rest of the commands given in this README)

   ```bash
   cd successmetrics-[releasenumber].zip
   ```

## Fetch metrics from the IQ Server (get-metrics)

The get-metrics script extract metrics from an IQ server and stores the result in the `./nexusiq` directory.

get-metrics can be executed using a [Java 1.8 jar](#running-get-metrics-using-java) or [Docker image](#running-get-metrics-using-docker).

:warning: For large installations/datasets, the extract should be limited to a shorter period (e.g. previous 6 months or weeks) or subset of organisations and/or applications.

### Get-Metrics Configuration

The configuration for get-metrics is stored in `./get-metrics/config/application.properties`.

#### Metrics

To fetch associated metrics from Nexus IQ the following properties may be set to true.

```text
metrics.successmetrics
metrics.applicationsevaluations
metrics.waivers
metrics.policyviolations
metrics.firewall
```

:warning: success-metrics should always be set to true.

#### Nexus IQ server details

These parameters should be updated with the details of the Nexus IQ instance.

```bash
iq.url
iq.user
iq.passwd
```

:warning: If you are using the get-metrics Docker image on the Nexus IQ machine then you cannot use `127.0.0.1` in the iq.url. You should instead use `host.docker.internal`.

#### Time period for which data should be fetched

Data may be collected in monthly or weekly periods by setting the `iq.api.sm.period` to either `month` or `week`.

The first period that we should collect data from is controlled by the `iq.api.sm.payload.timeperiod.first` parameter. *This is a mandatory parameter*. The format of this parameter is `2022-01` if `iq.api.sm.period` is set to month or `2022-W01` if `iq.api.sm.period` is set to `week`.

The last period that we should collect data from is controlled by the `iq.api.sm.payload.timeperiod.last` parameter. This is an optional parameter. The format of this parameter is `2022-04` if `iq.api.sm.period` is set to month or `2022-W04` if `iq.api.sm.period` is set to `week`.

#### Limit extracted data to given organisations or applications

To limit data extraction by organisation set  `iq.api.sm.payload.organisation.name` to a  list of comma separated organisation names. This is an optional parameter.

To limit data extraction by application set  `iq.api.sm.payload.application.name` to a  list of comma separated application names. This is an optional parameter.

If neither of these parameters are set then all organisations and applications will be fetched from Nexus IQ.

If both of these parameter are set then the organistaion setting will be used and application setting will be ignored.

### Running get-metrics using Java

#### Get-metrics using Java on Linux/Mac machines

```bash
cd get-metrics
sh runapp.bat
```

#### Get-metrics using Java on Windows machines

```dos
cd get-metrics
runapp.bat
```

### Running get-metrics using Docker

:warning: You must be in the get-metrics directory for the runapp-docker script to work.

#### Get-metrics using Docker on Linux/Mac machines

```bash
cd get-metrics
sh runapp-docker.sh
```

#### Get-metrics using Docker on Windows machines

```dos
cd get-metrics
runapp-docker.bat
```

## view-metrics

There are two modes to the application.

1. Web mode (interactive)
2. Data extract mode (non-interactive)

The view-metrics script processes metrics stored in the `./nexusiq` directory and presents them as detailed aggregated charts in a web view or in data files in the `./datafiles` directory.

:warning: There must be a `successmetrics.csv` in the `./iqmetrics` directory.

:warning: In order to aggregate and process metrics a minimum of three data points (weeks or months) are needed.

:warning: Only fully completed months (or weeks) are included in the data extract.

### View-metrics configuration

View-metrics configuration is stored in the `./successmetrics-[releasenumber]/view-metrics/application.properties` file.

To configure which mode the view-metrics application should run in set the `spring.profiles.active` parameter to `web` for web (interactive) mode or `data` for data extract (non-interactive) mode.

## Running the view-metrics application

### Running view-metrics using Java

#### View-metrics using Java on Linux/Mac machines

```bash
cd view-metrics
sh runapp.bat
```

#### View-metrics using Java on Windows machines

```dos
cd view-metrics
runapp.bat
```

### Running view-metrics using Docker

:warning: You must be in the get-metrics directory for the runapp-docker script to work.

#### View-metrics using Docker on Linux/Mac machines

```bash
cd view-metrics
sh runapp-docker.sh
```

#### View-metrics using Docker on Windows machines

```dos
cd view-metrics
runapp-docker.bat
```

### Web (interactive) mode

When running in this mode a web UI for the Success Metrics application is available on the localhost <http://localhost:4040>

### Data extract (non-interactive) mode

The Success Metrics application will perform calculations on the provided metrics, output files representing this and then close. Three output files can then be found in the `./successmetrics-[releasenumber]/output` directory.

1. Metrics Summary PDF
2. Insights CSV
3. Data Extract CSV
