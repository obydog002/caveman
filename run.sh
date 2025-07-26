#!/bin/bash

java -cp '.:out' -XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=core -Dhotswapagent.config=./hotswap-agent.properties src.game.Main
