package ru.kackbip.chat.screens.chat.interactors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.testEntities.Message;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Владимир on 04.12.2016.
 */
public class SendMessageInteractorTest {

    private static final String ME = "Автобот";
    private static final String TEXT = "Трансформируюсь!";

    private IMessagesRepository repository;
    private SendMessageInteractor interactor;

    @Before
    public void setUp() throws Exception {
        repository = mock(IMessagesRepository.class);
        when(repository.create(eq(ME), eq(TEXT), any(Date.class))).thenReturn(new Message(ME, TEXT, new Date()));
        interactor = new SendMessageInteractor(repository, ME);
        when(repository.add(any())).thenReturn(Observable.just(null));
    }

    @Test
    public void doIt() throws Exception {
        TestSubscriber<Void> subscriber = new TestSubscriber<>();
        interactor.doIt(TEXT).subscribe(subscriber);
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        subscriber.assertValue(null);
        verify(repository).add(captor.capture());
        Message sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertEquals(TEXT, sentMessage.getText());
        assertTrue(System.currentTimeMillis()-sentMessage.getDate().getTime()<1000);
        assertEquals(ME, sentMessage.getAuthor());
    }

}