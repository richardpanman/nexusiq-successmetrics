#!/bin/sh

workdir=$(pwd)
metricsdir=${workdir}/iqmetrics

docker run --rm -it -v "${workdir}":/config -v "${metricsdir}":/iqmetrics ghcr.io/sonatype-nexus-community/getmetrics:@APPVER@
