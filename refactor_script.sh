#!/bin/bash
# Find methods with "Verifying" at start and "verified successfully" at end pattern
grep -n "logger.info.*Verifying" /home/runner/work/qa-automation/qa-automation/src/test/java/stepdefinitions/LifeSteps.java | head -30
