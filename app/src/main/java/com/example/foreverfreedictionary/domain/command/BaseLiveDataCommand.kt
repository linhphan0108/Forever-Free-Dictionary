package com.example.foreverfreedictionary.domain.command

import androidx.lifecycle.LiveData
import com.example.foreverfreedictionary.vo.Resource

abstract class BaseLiveDataCommand<T> : BaseCommand<LiveData<Resource<T>>>()