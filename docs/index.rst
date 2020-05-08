.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright (C) 2019 IBM.
.. _master_index:

CCSDK APPS DOCUMENTATION REPOSITORY
-----------------------------------
.. toctree::
   :maxdepth: 1

Introduction:
=============
   APPS repository which contains all micro services for CCSDK module. Also these are application that are intended to run outside of OpenDaylight container.Modular feature implementation that is reusable across all controllers.

Microservices:
==============

.. toctree::
   :maxdepth: 1
   
   microservices/neng
   microservices/vlanTag


Installation:
=============

   To compile this code:

   1. Make sure your local Maven settings file ($HOME/.m2/settings.xml) contains references to the ONAP repositories and OpenDaylight repositories.

   2. To compile all of CCSDK apps code
      - git clone https://(LFID)@gerrit.onap.org/r/a/ccsdk/apps
      - cd apps ; mvn clean install ; cd ..


Logging:
========
CCSDK uses slf4j to log messages to the standard OpenDaylight karaf.log
log file.

Logs are found within the SDNC docker container, in the directory
/opt/opendaylight/current/data/logs

Release Notes:
==============
	release-notes
