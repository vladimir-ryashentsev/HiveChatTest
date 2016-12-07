package ru.kackbip.chat.screens.chat.interactors;

import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public interface IInteractor<ResultType, ParameterType> {
    Observable<ResultType> doIt(ParameterType parameter);
}
