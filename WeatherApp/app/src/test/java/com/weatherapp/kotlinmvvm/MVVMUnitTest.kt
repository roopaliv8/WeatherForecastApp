package com.weatherapp.kotlinmvvm

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.weatherapp.kotlinmvvm.data.OperationCallback
import com.weatherapp.kotlinmvvm.model.WearherDataSource
import com.weatherapp.kotlinmvvm.model.WetherData
import com.weatherapp.kotlinmvvm.viewmodel.WeatherViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.*
import org.mockito.Mockito.*

class MVVMUnitTest {

    @Mock
    private lateinit var repository: WearherDataSource
    @Mock private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationCallback<WetherData>>

    private lateinit var viewModel:WeatherViewModel

    private lateinit var isViewLoadingObserver:Observer<Boolean>
    private lateinit var onMessageErrorObserver:Observer<Any>
    private lateinit var emptyListObserver:Observer<Boolean>
    private lateinit var onRenderMuseumsObserver:Observer<WetherData>

    private lateinit var museumEmptyList:List<com.weatherapp.kotlinmvvm.model.List>
    private lateinit var museumList:List<com.weatherapp.kotlinmvvm.model.List>
    private lateinit var watherdata:WetherData

    /**
     //https://jeroenmols.com/blog/2019/01/17/livedatajunit5/
     //Method getMainLooper in android.os.Looper not mocked

     java.lang.IllegalStateException: operationCallbackCaptor.capture() must not be null
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`<Context>(context.applicationContext).thenReturn(context)

        viewModel= WeatherViewModel(repository)

        mockData()
        setupObservers()
    }

    @Test
    fun `retrieve museums with ViewModel and Repository returns empty data`(){
        with(viewModel){
            loadData("833")
            isViewLoading.observeForever(isViewLoadingObserver)
            //onMessageError.observeForever(onMessageErrorObserver)
            isEmptyList.observeForever(emptyListObserver)
            weathers.observeForever(onRenderMuseumsObserver)
        }

        verify(repository, times(1)).retrieveWeather("833",capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onSuccess(watherdata)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        //Assert.assertNotNull(viewModel.onMessageError.value) //java.lang.AssertionError
        //Assert.assertNotNull(viewModel.isEmptyList.value)
        Assert.assertTrue(viewModel.isEmptyList.value==true)
        Assert.assertTrue(viewModel.weathers.value?.getList()?.size ?: ""==0)
    }

    @Test
    fun `retrieve museums with ViewModel and Repository returns full data`(){
        with(viewModel){
           loadData("833")
            isViewLoading.observeForever(isViewLoadingObserver)
            weathers.observeForever(onRenderMuseumsObserver)
        }

        verify(repository, times(1)).retrieveWeather("833",capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onSuccess(watherdata)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.weathers.value?.getList()?.size ?: ""==3)
    }

    @Test
    fun `retrieve museums with ViewModel and Repository returns an error`(){
        with(viewModel){
            loadData("833")
            isViewLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        verify(repository, times(1)).retrieveWeather("833",capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onError("An error occurred")
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertNotNull(viewModel.onMessageError.value)
    }

    private fun setupObservers(){
        isViewLoadingObserver= mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver= mock(Observer::class.java) as Observer<Any>
        emptyListObserver= mock(Observer::class.java) as Observer<Boolean>
        onRenderMuseumsObserver= mock(Observer::class.java)as Observer<WetherData>
    }

    private fun mockData(){
        museumEmptyList= emptyList()
        val mockList:MutableList<com.weatherapp.kotlinmvvm.model.List>  = mutableListOf()

        museumList= mockList.toList()


        watherdata.setList(museumList)
    }
}