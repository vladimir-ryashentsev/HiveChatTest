package ru.kackbip.chat.data.realm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Владимир on 04.12.2016.
 */

public class RealmMessagesRepository implements IMessagesRepository {

    private Realm realm;
    private PublishSubject<IMessage> newMessagesSubject = PublishSubject.create();

    public RealmMessagesRepository(Realm realm) {
        this.realm = realm;
    }

    @Override
    public IMessage create(String author, String text, Date date) {
        return new RealmMessage(author, text, date);
    }

    @Override
    public Observable<Void> add(IMessage message) {
        if (!(message instanceof RealmMessage))
            return Observable.error(new RuntimeException("RealmMessagesRespository can work only with RealmMessage class"));
        return Observable
                .<Void>fromEmitter(
                        emitter -> {
                            realm.beginTransaction();
                            RealmMessage message1 = realm.copyToRealm((RealmMessage) message);
                            realm.commitTransaction();
                            newMessagesSubject.onNext(message1);
                            emitter.onNext(null);
                            emitter.onCompleted();

                        },
                        Emitter.BackpressureMode.ERROR
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<IMessage> observeNew() {
        return newMessagesSubject;
    }

    @Override
    public Observable<List<IMessage>> getAll() {
        return Observable.<List<IMessage>>fromEmitter(
                emitter -> {
                    RealmResults<RealmMessage> results = realm.where(RealmMessage.class).findAllSorted("date", Sort.ASCENDING);
                    ArrayList<IMessage> result = new ArrayList<>(results);
                    emitter.onNext(result);
                    emitter.onCompleted();
                },
                Emitter.BackpressureMode.ERROR)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
