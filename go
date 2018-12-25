#!/bin/bash
set -euo pipefail
exec mvn clean compile assembly:single
