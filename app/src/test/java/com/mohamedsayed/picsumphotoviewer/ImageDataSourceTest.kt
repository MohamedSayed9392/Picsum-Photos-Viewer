package com.mohamedsayed.picsumphotoviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.mohamedsayed.picsumphotoviewer.helpers.FakeData
import com.mohamedsayed.picsumphotoviewer.model.data.ImagesDataSource
import com.mohamedsayed.picsumphotoviewer.model.database.dao.ImagesDao
import com.mohamedsayed.picsumphotoviewer.model.network.retrofit.ApiService
import com.mohamedsayed.picsumphotoviewer.model.objects.PicsumImage
import com.mohamedsayed.picsumphotoviewer.testinghelpers.CoroutineTestRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ExperimentalCoroutinesApi
class ImageDataSourceTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var api: ApiService

    @Mock
    lateinit var imagesDao: ImagesDao
    private lateinit var imagesDataSource: ImagesDataSource

    companion object {
        private var fakeDataFactory: FakeDataFactory = FakeDataFactory()
        val imagesApiResponse:ArrayList<PicsumImage> = ArrayList()
        val nextImagesApiResponse:ArrayList<PicsumImage> = ArrayList()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        imagesDataSource = ImagesDataSource(api, imagesDao)
        initFakeData()
    }

    private fun initFakeData(){
        for(i in 0..9){ //Page 1 Generate 10 Images
            imagesApiResponse.add(fakeDataFactory.generateImage("https://picsum.photos/id/1003/1181/1772"))
        }

        for(i in 0..7){ //Page 2 Generate 8 Images
            nextImagesApiResponse.add(fakeDataFactory.generateImage("https://picsum.photos/id/1004/5616/3744"))
        }
    }

    fun getPageResultWithAds(imagesFromApi:ArrayList<PicsumImage>):ArrayList<Any>{
        val dataList: ArrayList<Any> = ArrayList()
        dataList.addAll(imagesFromApi)
        var adPlaceHolderPage1Index = 5
        while (dataList.size >= adPlaceHolderPage1Index) {
            dataList.add(adPlaceHolderPage1Index, FakeData.getFakeAd(100))
            adPlaceHolderPage1Index += 6
        }
        return dataList
    }

    @Test
    fun `01 Images PagingSource - Refresh - success`() = runBlockingTest {
        given(api.getImagesList(any(), any())).willReturn(imagesApiResponse)
        val expectedResult = PagingSource.LoadResult.Page(
            data = getPageResultWithAds(imagesApiResponse),
            prevKey = null,
            nextKey = 2
        )

        assertEquals(
            expectedResult, imagesDataSource.load(
                PagingSource.LoadParams.Append(
                    key = 1,
                    loadSize = 12,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `02 Images PagingSource - Append - success`() = runBlockingTest {
        given(api.getImagesList(any(), any())).willReturn(nextImagesApiResponse)
        val expectedResult = PagingSource.LoadResult.Page(
            data = getPageResultWithAds(nextImagesApiResponse),
            prevKey = 1,
            nextKey = null
        )

        assertEquals(
            expectedResult, imagesDataSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 12,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `03 Images PagingSource - Failure - http error`() = runBlockingTest {
        val error = NullPointerException()
        given(api.getImagesList(any(), any())).willThrow(error)

        imagesDataSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ).let {
            assertTrue(it is PagingSource.LoadResult.Error)
            assertTrue((it as PagingSource.LoadResult.Error).throwable is NullPointerException)
        }
    }

    @Test
    fun `04 Images PagingSource - Failure - received null`() = runBlockingTest {
        given(api.getImagesList(any(), any())).willReturn(null)
        val expectedResult = PagingSource.LoadResult.Error<Int, Any>(NullPointerException())
        assertEquals(
            expectedResult.toString(), imagesDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }
}