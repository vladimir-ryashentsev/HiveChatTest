package ru.kackbip.chat.data.realm.injection;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.data.realm.RealmMessagesRepository;

/**
 * Created by Владимир on 05.12.2016.
 */
@Module
public class RealmMessagesRepositoryModule {

    @Provides
    @Singleton
    public Realm provideRealm(Application application) {
        Realm.init(application);
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public IMessagesRepository provideMessagesRepository(Realm realm) {
        return new RealmMessagesRepository(realm);
    }
}
