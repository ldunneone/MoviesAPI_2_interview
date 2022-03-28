package com.luke.movies.base.output

class SimpleOutput<S, E> : BaseOutput<E>() {
    val complete by lazy { SingleLiveEvent<S>() }

}