rootProject.name = "firefly"

includeBuild("plugin")

include(":processor")
include(":common")

include(":paper")
include(":game")

include(":config")
include(":config:json")
include(":config:hocon")
include(":config:yaml")
include(":config:toml")
include(":config:pkl")

include("test-plugin")
