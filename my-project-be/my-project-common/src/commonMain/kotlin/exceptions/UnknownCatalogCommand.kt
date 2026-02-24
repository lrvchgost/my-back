package ru.otus.otuskotlin.lrvch.common.exceptions

import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand

class UnknownCatalogCommand(command: CatalogCommand) : Throwable("Wrong command $command at mapping toTransport stage")