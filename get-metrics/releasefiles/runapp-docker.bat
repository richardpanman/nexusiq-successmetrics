@echo off

set workdir=%cd%
metricsdir=%workdir%/iqmetrics

docker run --rm -it -v %workdir%:/config -v %metricsdir%:/iqmetrics ghcr.io/sonatype-nexus-community/getmetrics:@APPVER@
