package ru.kackbip.chat.screens.botSettings.presenter;

import org.junit.Before;
import org.junit.Test;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.screens.botSettings.interactors.GetBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.interactors.SwitchBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.view.IBotSettingsView;
import rx.Emitter;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Владимир on 07.12.2016.
 */
public class BotSettingsPresenterTest {

    private BotSettingsPresenter presenter;
    private IBotSettingsView view;
    private GetBotModeInteractor getBotModeInteractor;
    private SwitchBotModeInteractor switchBotModeInteractor;
    private Emitter<BotService.BotMode> modeEmitter;
    private Emitter<Void> stopEmitter;
    private ChatApplication application;
    private Emitter<Void> destroyEmitter;

    @Before
    public void setUp() throws Exception {
        view = mock(IBotSettingsView.class);
        when(view.observeModeSwitch()).thenReturn(Observable.fromEmitter(emitter -> modeEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeStop()).thenReturn(Observable.fromEmitter(emitter -> stopEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeDestroy()).thenReturn(Observable.fromEmitter(emitter -> destroyEmitter = emitter, Emitter.BackpressureMode.ERROR));

        getBotModeInteractor = mock(GetBotModeInteractor.class);
        when(getBotModeInteractor.doIt(null)).thenReturn(Observable.just(BotService.BotMode.WEAK));

        switchBotModeInteractor = mock(SwitchBotModeInteractor.class);
        when(switchBotModeInteractor.doIt(BotService.BotMode.MEGATRONE)).thenReturn(Observable.just(null));

        application = mock(ChatApplication.class);

        presenter = new BotSettingsPresenter(getBotModeInteractor, switchBotModeInteractor, application);
    }

    @Test
    public void setView() {
        presenter.setView(view);
        verify(view).showMode(BotService.BotMode.WEAK);
        verify(view).observeModeSwitch();
        verify(view).observeStop();
        verify(view).observeDestroy();
    }

    @Test
    public void switchMode() {
        presenter.setView(view);
        modeEmitter.onNext(BotService.BotMode.MEGATRONE);
        verify(switchBotModeInteractor).doIt(BotService.BotMode.MEGATRONE);
        verify(view).showMode(BotService.BotMode.MEGATRONE);
    }

    @Test
    public void stop() {
        presenter.setView(view);
        stopEmitter.onNext(null);

        modeEmitter.onNext(null);
        verify(switchBotModeInteractor, never()).doIt(any());
    }

    @Test
    public void destroy(){
        presenter.setView(view);
        destroyEmitter.onNext(null);
        verify(application).removeBotSettingsComponent();
    }

}