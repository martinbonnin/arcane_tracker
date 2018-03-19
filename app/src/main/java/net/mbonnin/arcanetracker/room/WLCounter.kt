package net.mbonnin.arcanetracker.room

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

object WLCounter {
    fun watch(id: String): Flowable<RDeck> {
        return RDatabaseSingleton.instance.deckDao().findById(id)
                .map {
                    if (it.isEmpty()) {
                        RDeck(id, 0, 0)
                    } else {
                        it.first()
                    }
                }

    }

    fun increment(id: String, wins: Int, losses: Int) {
        watch(id)
                .firstOrError()
                .map {
                    val rDeck = it.copy(wins = it.wins + wins,
                            losses = it.losses + wins)

                    RDatabaseSingleton.instance.deckDao().insert(rDeck)
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun set(id: String, wins: Int, losses: Int) {
        Completable.fromAction {
            RDatabaseSingleton.instance.deckDao().insert(RDeck(id, wins, losses))
        }
                .subscribeOn(Schedulers.io())
                .subscribe()

    }
}