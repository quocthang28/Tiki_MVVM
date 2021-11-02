package com.example.tikimvvm.view.paging

//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.tikimvvm.db.dao.CategoryDAO
//import com.example.tikimvvm.models.DataX
//import com.example.tikimvvm.repository.TikiRepository
//
//class ProductPagingSource(private val dao: CategoryDAO) : PagingSource<Int, DataX>() {
//    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
//        return try {
//            val response = TikiRepository.getInstance(dao).getProductList(0, params.loadSize)
//            val productList = response.body()?.data?.data!!
//            LoadResult.Page(
//                    data = productList,
//                    prevKey =
//            )
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
//
//}