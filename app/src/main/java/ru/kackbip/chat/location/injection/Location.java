package ru.kackbip.chat.location.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Владимир on 05.12.2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface Location {
}