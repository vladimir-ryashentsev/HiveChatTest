package ru.kackbip.chat.screens.chat.interactors;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.testEntities.Message;
import rx.Emitter;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Владимир on 04.12.2016.
 */
public class ObserveNewMessagesInteractorTest {

    private static final String ME = "Автобот";
    private static final IMessage MESSAGE = new Message(ME, "Трансформируюсь!", new Date(1));

    private IMessagesRepository api;
    private ObserveNewMessagesInteractor interactor;
    private Emitter<IMessage> messageEmiter;

    @Before
    public void setUp() throws Exception {
        api = mock(IMessagesRepository.class);
        interactor = new ObserveNewMessagesInteractor(api);
        when(api.observeNew()).thenReturn(Observable.fromEmitter(emitter -> messageEmiter = emitter, Emitter.BackpressureMode.ERROR));
    }

    @Test
    public void doIt() throws Exception {
        TestSubscriber<IMessage> subscriber = new TestSubscriber<>();
        interactor.doIt(null).subscribe(subscriber);
        subscriber.assertNoValues();
        messageEmiter.onNext(MESSAGE);
        subscriber.assertValue(MESSAGE);
    }

}