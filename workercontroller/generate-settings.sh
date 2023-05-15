#!/usr/bin/env bash

test -z "$MVN_MIRROR" && echo "No mirror, settings not generated" && exit 0

mkdir -p $HOME/.m2
cat > $HOME/.m2/settings.xml <<EOF
    <settings>
      <mirrors>
        <mirror>
          <id>ucbl-central-mirror</id>
          <name>UCBL Mirror Repository for Central</name>
          <url>$MVN_MIRROR</url>
          <mirrorOf>central</mirrorOf>
        </mirror>
      </mirrors>
    </settings>
EOF
