package io.ugolkov.metric_mind.common.exception

import io.ugolkov.metric_mind.common.model.MmCommand

class NotValidCommand(command: MmCommand) : IllegalStateException("Unsupported command $command")
