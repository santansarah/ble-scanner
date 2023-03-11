package com.santansarah.scan.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.santansarah.scan.domain.interfaces.IBleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import timber.log.Timber

class DeleteNotSeenWorker(
    appContext: Context, workerParams: WorkerParameters,
    private val bleRepository: IBleRepository,
    private val dispatcher: CoroutineDispatcher
) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        return@withContext try {
            for (i in 0..14) {
                delay(60000)
                bleRepository.deleteNotSeen()
                Timber.d("ran not seen $i....")
            }
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
    }

}
