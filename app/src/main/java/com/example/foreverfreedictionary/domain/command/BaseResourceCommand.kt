package com.example.foreverfreedictionary.domain.command

import com.example.foreverfreedictionary.vo.Resource

abstract class BaseResourceCommand <T> : BaseCommand<Resource<T>>()