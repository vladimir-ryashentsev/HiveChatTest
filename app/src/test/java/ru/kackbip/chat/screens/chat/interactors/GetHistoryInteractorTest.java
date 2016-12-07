package ru.kackbip.chat.screens.chat.interactors;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.testEntities.Message;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Владимир on 04.12.2016.
 */
public class GetHistoryInteractorTest {

    private GetHistoryInteractor interactor;
    private IMessagesRepository api;

    private static final String ME = "Автобот";
    private static final String BOT = "Десептикон";
    private static final List<IMessage> HISTORY = Arrays.asList(
            new Message(ME, "Трансформируюсь!", new Date(1)),
            new Message(BOT, "Злобно трансформируюсь!", new Date(2))
    );

    @Before
    public void setUp() throws Exception {
        api = mock(IMessagesRepository.class);
        interactor = new GetHistoryInteractor(api);
        when(api.getAll()).thenReturn(Observable.just(HISTORY));
    }

    @Test
    public void doIt() throws Exception {
        TestSubscriber<List<IMessage>> subscriber = new TestSubscriber<>();
        interactor.doIt(null).subscribe(subscriber);
        verify(api).getAll();
        subscriber.assertValue(HISTORY);
    }
}