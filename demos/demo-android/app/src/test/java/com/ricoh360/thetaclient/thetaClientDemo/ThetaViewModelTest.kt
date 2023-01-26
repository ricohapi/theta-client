package com.ricoh360.thetaclient.thetaClientDemo

import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.thetaClientDemo.ThetaViewModel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ThetaViewModelTest {
    private lateinit var vm: ThetaViewModel

    @BeforeEach
    fun setUp() {
        vm = ThetaViewModel()
    }

    @AfterEach
    fun tearDown() {
        vm.stopPreview()
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun thetaInitTest() = runTest {
        val info = vm.thetaInfoState.drop(1).first()
        assertNotNull(info, "thetaInfoState")
        val files = vm.thetaFilesState.drop(1).first()
        assertNotNull(files, "thetaFilesState")
    }

   /*
    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
     fun previewTest() = runTest {
        vm.startPreview()
        val bitmap = vm.previewFlow.drop(1).first()
        assertNotNull(bitmap, "preview")
        vm.stopPreview()
    } */

    @Test
    fun shootingTest() = runTest {
        class TakenCallback : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(true, fileUrl)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, exception.toString())
            }
        }

        vm.takePhoto(TakenCallback())
    }

}
